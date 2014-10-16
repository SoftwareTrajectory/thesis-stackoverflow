package edu.hawaii.senin.stack.quickloader;

import java.io.FileInputStream;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.hackystat.utilities.tstamp.Tstamp;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.PostSeries;

/**
 * Parse the challenge Posts XML and load it into database omitting large post bodies.
 * 
 * @author Pavel Senin
 * 
 */
public class LoadPostSeries {

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

    System.err.println("Loading simplified posts from StackOverflow posts table dump.");

    // set-up time tracking
    //
    int recordCounter = 0;
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

              PostSeries record = new PostSeries();

              int attributesCount = xmlStreamReader.getAttributeCount();
              for (int i = 0; i < attributesCount; i++) {
                record.setAttribute(xmlStreamReader.getAttributeLocalName(i),
                    xmlStreamReader.getAttributeValue(i));
              }
              db.savePostSeries(record);
              recordCounter++;

              if (recordCounter % 20000 == 0) {
                db.commit();

                XMLGregorianCalendar cTime = Tstamp.makeTimestamp();
                System.err.println("+20K post records loaded and saved, total records: "
                    + recordCounter);

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
