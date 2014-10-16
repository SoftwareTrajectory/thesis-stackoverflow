package edu.hawaii.senin.stack.dataloader;

import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;

/**
 * Parse the challenge change XML and load it into database.
 * 
 * @author Pavel Senin
 * 
 */
public class PrintFieldsVTDNav {

  /** The file location. */
  private static final String fileLocation2 = "/media/Stock/home/users/ponza/stack_overflow_201208/posthistory.xml";

  /**
   * Main runnable class.
   * 
   * @param args None used.
   * @throws Exception if error occurs.
   */
  public static void main(String[] args) throws Exception {

    // get an extended VTD instance
    //
    VTDGenHuge vtdGen = new VTDGenHuge();

    if (vtdGen.parseFile(fileLocation2, false, VTDGenHuge.MEM_MAPPED)) {
      System.out.println(fileLocation2 + " parsed.");
    }
    else {
      System.err.println("Unable to parse " + fileLocation2);
    }

    // get an auto-pilot
    //
    VTDNavHuge vn = vtdGen.getNav();
    AutoPilotHuge apm = new AutoPilotHuge(vn);

    // going over raws here
    //
    apm.selectElement("row");
    
    while (apm.iterate()) {
      // System.out.print("" + vn.getCurrentIndex() + " ");
      int count = vn.getAttrCount();
      for (int i = 0; i < count; i++) {
        System.out.println("Element name ==> " + vn.toNormalizedString(i));
      }
    }

  }
}
