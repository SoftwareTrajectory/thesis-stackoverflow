package edu.hawaii.senin.stack.location;

import java.io.IOException;
import java.util.TimeZone;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;

/**
 * This tries to set locations for users.
 * 
 * @author psenin
 * 
 */

public class Location02UserLocationSetter {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    TimeZone tz = TimeZone.getTimeZone("UTC");

    TimeZone.setDefault(tz);

    UserLocationHandler userLocationHandler = new UserLocationHandler();

    StackDB db = StackDBManager.getProductionInstance();

    db.getSession().select("getUsersForLocationUpdate", userLocationHandler);

  }

}
