package edu.hawaii.senin.stack.dataloader;

import org.hackystat.utilities.tstamp.Tstamp;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Badge;

/**
 * Parse the challenge change XML and load it into database.
 * 
 * @author Pavel Senin
 * 
 */
public class LoadBadgesVTDNav {

  /** The file location. */
  private static final String fileLocation = "/media/Stock/home/users/ponza/stack_overflow_201208/badges.xml";

  //  private static final String ROOT_ELEMENT = "badges";

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

    // get an extended VTD instance
    //
    VTDGenHuge vtdGen = new VTDGenHuge();

    if (vtdGen.parseFile(fileLocation, false, VTDGenHuge.MEM_MAPPED)) {
      System.out.println(fileLocation + " parsed.");
    }
    else {
      System.err.println("Unable to parse " + fileLocation);
      System.exit(10);
    }

    // get an auto-pilot
    //
    VTDNavHuge vn = vtdGen.getNav();
    AutoPilotHuge apm = new AutoPilotHuge(vn);

    // going over raws here
    //
    apm.selectElement(ROW_ELEMENT);

    while (apm.iterate()) {

      Badge b = new Badge();

      String id = vn.toString(vn.getAttrVal("Id"));
      if (!(null == id || id.isEmpty())) {
        b.setId(Integer.valueOf(id));
      }

      String userId = vn.toString(vn.getAttrVal("UserId"));
      if (!(null == userId || userId.isEmpty())) {
        b.setUserId(Integer.valueOf(userId));
      }

      String name = vn.toString(vn.getAttrVal("Name"));
      if (!(null == userId || userId.isEmpty())) {
        b.setName(name.substring(0));
      }

      String date = vn.toString(vn.getAttrVal("Date"));
      if (!(null == userId || userId.isEmpty())) {
        b.setDate(Tstamp.makeTimestamp(date));
      }

      db.saveBadge(b);

    }

    db.shutDown();

  }
}
