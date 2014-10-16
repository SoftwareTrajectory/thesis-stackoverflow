package edu.hawaii.senin.stack.dataloader;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import edu.hawaii.senin.stack.util.StackTrace;

/**
 * Parse the challenge change XML and load it into database.
 * 
 * @author Pavel Senin
 * 
 */
public class PrintFields {

  /** The file location. */
  private static String fileLocation = "";

  private static String baseElement = "";

  private static HashMap<String, Integer> attributesCollection = new HashMap<String, Integer>();

  /**
   * Main runnable class.
   * 
   * @param args None used.
   * @throws Exception if error occurs.
   */
  public static void main(String[] args) throws Exception {

    fileLocation = args[0].substring(0);
    baseElement = args[1].substring(0);

    // get the XML file handler
    //
    FileInputStream fileInputStream = new FileInputStream(fileLocation);
    XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(
        fileInputStream);

    // parse it
    //
    try {
      while (xmlStreamReader.hasNext()) {

        int eventCode = xmlStreamReader.next();
        // String name = xmlStreamReader.getLocalName();

        if ((XMLStreamConstants.START_ELEMENT == eventCode)
            && xmlStreamReader.getLocalName().equalsIgnoreCase(baseElement)) {

          // parse the stream looking for the start of something within change
          //
          while (xmlStreamReader.hasNext()) {

            eventCode = xmlStreamReader.next();

            if ((XMLStreamConstants.END_ELEMENT == eventCode)
                && xmlStreamReader.getLocalName().equalsIgnoreCase(baseElement)) {
              break;
            }
            else {
              if ((XMLStreamConstants.START_ELEMENT == eventCode)) {
                // System.out.println(xmlStreamReader.getLocalName());

                int attributesCount = xmlStreamReader.getAttributeCount();
                for (int i = 0; i < attributesCount; i++) {

                  String attr = xmlStreamReader.getAttributeLocalName(i);
                  int newLen = xmlStreamReader.getAttributeValue(i).length();

                  Integer oldLen = attributesCollection.get(attr);
                  if ((null == oldLen) || (oldLen < newLen)) {
                    attributesCollection.put(attr, newLen);
                  }

                }

              }
            }

          }

        }
      }
    }
    catch (javax.xml.stream.XMLStreamException ex) {
      System.err.println(StackTrace.toString(ex));
    }

    for (Entry<String, Integer> entry : attributesCollection.entrySet()) {
      System.out.println(entry.getKey() + " " + entry.getValue());
    }

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