package edu.hawaii.senin.stack.location;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.util.StackTrace;

public class LocationTZHandler implements ResultHandler {

  // private static final Object SPACE = " ";
  // private static final Object CR = "\n";
  private StackDB db;

  public LocationTZHandler() throws IOException {
    super();
    db = StackDBManager.getProductionInstance();
  }

  @Override
  public void handleResult(ResultContext context) {

    @SuppressWarnings("unchecked")
    Map<String, Object> locationFreqMap = (HashMap<String, Object>) context.getResultObject();

    String knownLocation = (String) locationFreqMap.get("known_location");

    // Metz, France | | lat/lng=49.119667,6.176905 | lat/lng=NaN,NaN
    //
    // | lat=47.01&lng=10.2

    Pattern pattern = Pattern.compile("\\| lat/lng=(\\-*\\d+\\.\\d+\\,\\-*\\d+\\.\\d+)");
    Matcher matcher = pattern.matcher(knownLocation);
    if (matcher.find()) {
      String str = matcher.group(1);
      String lat = str.substring(0, str.indexOf(","));
      String lng = str.substring(str.indexOf(",") + 1);

      String url = "http://api.geonames.org/timezone?lat=" + lat + "&lng=" + lng
          + "&username=seninp";

      // String url = "http://api.askgeo.com/v1/516/"
      // + "fe0cfb47448b39e7a0c2c8382d887f6e0c7dad286c1f0a3605eae7230eba199e"
      // + "/query.xml?points=" + lat + "%2C" + lng + "&databases=TimeZone";

      try {
        Map<String, String> tsData = GeocodingFactory.getTZforURL(url);
        if (null != tsData && !tsData.isEmpty()) {
          // res.put("timezoneId", temezoneId);
          // res.put("gmtOffset", gmtOffset);
          db.updateTZInfo((Integer) locationFreqMap.get("id"), tsData.get("timezoneId"),
              tsData.get("gmtOffset"));
          System.out.println(knownLocation + " ==> " + tsData.get("timezoneId"));
        }

        // Pause for 2 seconds
        Thread.sleep(2000);

      }
      catch (Exception e) {
        System.err.println("Exception was thrown: " + StackTrace.toString(e));
      }

    }
  }
}
