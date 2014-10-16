package edu.hawaii.senin.stack.location;

import java.io.IOException;
import java.util.TimeZone;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;

public class Location00Normalizer {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    TimeZone tz = TimeZone.getTimeZone("UTC");

    TimeZone.setDefault(tz);

    LocationNormalizerHandler locationTimeHandler = new LocationNormalizerHandler();

    StackDB db = StackDBManager.getProductionInstance();

    db.getSession().select("NormalizeLocations", locationTimeHandler);

  }

}
