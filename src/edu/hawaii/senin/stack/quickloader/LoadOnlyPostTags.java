package edu.hawaii.senin.stack.quickloader;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.hackystat.utilities.tstamp.Tstamp;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Tag;

/**
 * Parse the challenge change XML and load it into database.
 * 
 * @author Pavel Senin
 * 
 */
public class LoadOnlyPostTags {

  /** The file location. */
  private static final String fileLocation = "/media/Stock/stack/xml/posts.xml";

  private static final String ROOT_ELEMENT = "posts";

  private static final String ROW_ELEMENT = "row";

  private static StackDB db;

  /**
   * Main runnable class.
   * 
   * @param args None used.
   * @throws Exception if error occurs.
   */
  public static void main(String[] args) throws Exception {

    db = StackDBManager.getProductionInstance();

    // get the XML file handler
    //
    FileInputStream fileInputStream = new FileInputStream(fileLocation);
    XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
        fileInputStream);

    System.err.println("Loading only tags (i.e. normalizing tags) from "
        + "StackOverflow posts table dump.");

    // set-up time tracking
    //
    int tagRecordCounter = 0;
    int postRecordCounter = 0;
    XMLGregorianCalendar startTstamp = Tstamp.makeTimestamp();

    // run begins
    //
    while (xmlStreamReader.hasNext()) {

      int eventCode = xmlStreamReader.next();

      if ((XMLStreamConstants.START_ELEMENT == eventCode)
          && xmlStreamReader.getLocalName().equalsIgnoreCase(ROOT_ELEMENT)) {

        // parse the stream looking for the start of something within change
        //
        while (xmlStreamReader.hasNext()) {

          eventCode = xmlStreamReader.next();

          if ((XMLStreamConstants.END_ELEMENT == eventCode)
              && xmlStreamReader.getLocalName().equalsIgnoreCase(ROOT_ELEMENT)) {
            break;
          }
          else {

            if ((XMLStreamConstants.START_ELEMENT == eventCode)
                && xmlStreamReader.getLocalName().equalsIgnoreCase(ROW_ELEMENT)) {

              String tagLine = xmlStreamReader.getAttributeValue(null, "Tags");
              String dateLine = xmlStreamReader.getAttributeValue(null, "CreationDate");

              Set<String> normalizedTags = new HashSet<String>();
              if (!((null == tagLine) || (tagLine.length() == 0))) {
                String[] split = tagLine.split(">\\s*<");
                for (String tag : split) {
                  String trimmed = tag.trim().toLowerCase().replaceAll(">", "").replaceAll("<", "");
                  normalizedTags.add(trimmed);
                }
              }

              if (!(normalizedTags.isEmpty())) {
                XMLGregorianCalendar date = Tstamp.makeTimestamp(dateLine);
                for (String tagStr : normalizedTags) {
                  Tag dbRecord = db.getTag(tagStr);
                  if (null == dbRecord) {
                    Tag tag = new Tag();
                    tag.setTag(tagStr);
                    tag.setCreationDate(date);
                    db.saveTag(tag);
                    tagRecordCounter++;
                  }
                  else {
                    if (Tstamp.greaterThan(dbRecord.getCreationDate(), date)) {
                      dbRecord.setCreationDate(date);
                      db.saveTag(dbRecord);
                    }
                  }
                }
              }

              postRecordCounter++;

              if (postRecordCounter % 20000 == 0) {
                db.commit();

                XMLGregorianCalendar cTime = Tstamp.makeTimestamp();
                System.err.println("+20K of post lines processed. total tag records: "
                    + tagRecordCounter);

                System.err.println("  .. . time from start: " + timeToString(startTstamp, cTime));
              }
            }
          }
        }
      }
    }

    db.shutDown();

  }

  @SuppressWarnings("unused")
  private static String timeToString(XMLGregorianCalendar start, XMLGregorianCalendar finish) {
    long l1 = start.toGregorianCalendar().getTimeInMillis();
    long l2 = finish.toGregorianCalendar().getTimeInMillis();
    long diff = l2 - l1;

    long secondInMillis = 1000;
    long minuteInMillis = secondInMillis * 60;
    long hourInMillis = minuteInMillis * 60;
    long dayInMillis = hourInMillis * 24;
    long yearInMillis = dayInMillis * 365;

    long elapsedYears = diff / yearInMillis;
    diff = diff % yearInMillis;
    long elapsedDays = diff / dayInMillis;
    diff = diff % dayInMillis;
    long elapsedHours = diff / hourInMillis;
    diff = diff % hourInMillis;
    long elapsedMinutes = diff / minuteInMillis;
    diff = diff % minuteInMillis;
    long elapsedSeconds = diff / secondInMillis;

    return elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + "s";
  }
}
