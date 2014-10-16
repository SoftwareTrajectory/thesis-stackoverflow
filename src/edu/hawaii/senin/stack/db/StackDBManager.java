package edu.hawaii.senin.stack.db;

import java.io.IOException;

/**
 * Manages StackDB instances preventing multiple objects instantiation.
 * 
 * @author psenin
 * 
 */
public class StackDBManager {

  private static StackDB productionInstance;

  /**
   * Get production DB instance.
   * 
   * @return production DB instance.
   * @throws IOException if error occurs.
   */
  public static StackDB getProductionInstance() throws IOException {
    if (null == productionInstance) {
      productionInstance = new StackDB(StackDB.PRODUCTION_INSTANCE);
    }
    return productionInstance;
  }

}
