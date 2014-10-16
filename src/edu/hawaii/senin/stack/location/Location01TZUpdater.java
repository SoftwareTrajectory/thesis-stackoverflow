package edu.hawaii.senin.stack.location;

import java.io.IOException;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;

public class Location01TZUpdater {

  /**
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {

    StackDB db = StackDBManager.getProductionInstance();

    LocationTZHandler locationTZHandler = new LocationTZHandler();

    db.getSession().select("UpdateLocations", locationTZHandler);

  }

}
