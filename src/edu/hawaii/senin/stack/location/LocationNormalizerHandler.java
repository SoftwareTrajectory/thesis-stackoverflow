package edu.hawaii.senin.stack.location;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.xml.sax.SAXException;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.util.StackTrace;

public class LocationNormalizerHandler implements ResultHandler {

  // private static final Object SPACE = " ";
  // private static final Object CR = "\n";
  private StackDB db;

  public LocationNormalizerHandler() throws IOException {
    super();
    db = StackDBManager.getProductionInstance();
  }

  @Override
  public void handleResult(ResultContext context) {

    @SuppressWarnings("unchecked")
    Map<String, Object> locationFreqMap = (HashMap<String, Object>) context.getResultObject();

    String loc = (String) locationFreqMap.get("loc");

    try {

      String normalLocation = GeocodingFactory.tokenizeNormalize(loc);

      if (null != normalLocation && normalLocation.length() > 0) {

        if (db.locationIsParsed(normalLocation.toString())) {
          return;
        }

        String location = null;
        try {
          location = GeocodingFactory.getLocationForString(normalLocation.toString());
          if (location.toUpperCase().contains("OVER_QUERY_LIMIT")) {
            System.err.println("Limit reached: " + location);
            System.exit(-10);
          }
          if (location.length() > 18000) {
            location = location.substring(0, 17999);
          }
        }
        catch (XPathExpressionException e) {
          System.err.println("Exception was thrown: " + StackTrace.toString(e));
        }
        catch (SAXException e) {
          System.err.println("Exception was thrown: " + StackTrace.toString(e));
        }
        catch (ParserConfigurationException e) {
          System.err.println("Exception was thrown: " + StackTrace.toString(e));
        }

        db.saveLocations(loc, normalLocation.toString(), location);

        // Pause for 3 seconds
        Thread.sleep(3000);
      }

    }
    catch (IOException ex) {
      System.err.println("Exception was thrown: " + StackTrace.toString(ex));
    }
    catch (InterruptedException e1) {
      System.err.println("Exception was thrown: " + StackTrace.toString(e1));
    }

  }
}
