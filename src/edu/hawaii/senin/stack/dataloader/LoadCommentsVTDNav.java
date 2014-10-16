package edu.hawaii.senin.stack.dataloader;

import org.hackystat.utilities.tstamp.Tstamp;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Comment;

/**
 * Parse the challenge change XML and load it into database.
 * 
 * @author Pavel Senin
 * 
 */
public class LoadCommentsVTDNav {

  /** The file location. */
  // private static final String fileLocation =
  // "/media/backup/XML/home/users/ponza/stack_overflow_201208/posts.xml";
  private static final String fileLocation = "/media/Stock/home/users/ponza/stack_overflow_201208/comments.xml";

  // private static final String ROOT_ELEMENT = "comments";

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

      Comment c = new Comment();

      // private Integer id;
      if (vn.hasAttr("Id")) {
        String id = vn.toString(vn.getAttrVal("Id"));
        if (!(null == id || id.isEmpty())) {
          c.setId(Integer.valueOf(id));
        }
      }

      // private Integer parentId;
      if (vn.hasAttr("UserId")) {
        String userId = vn.toString(vn.getAttrVal("UserId"));
        if (!(null == userId || userId.isEmpty())) {
          c.setUserId(Integer.valueOf(userId));
        }
      }

      if (vn.hasAttr("PostId")) {
        String postId = vn.toString(vn.getAttrVal("PostId"));
        if (!(null == postId || postId.isEmpty())) {
          c.setPostId(Integer.valueOf(postId));
        }
      }
      if (vn.hasAttr("CreationDate")) {
        String creationDate = vn.toString(vn.getAttrVal("CreationDate"));
        if (!(null == creationDate || creationDate.isEmpty())) {
          c.setCreationDate(Tstamp.makeTimestamp(creationDate));
        }
      }

      if (vn.hasAttr("Text")) {
        String text = vn.toString(vn.getAttrVal("Text"));
        if (!(null == text || text.isEmpty())) {
          c.setText(text.substring(0));
        }
      }

      if (vn.hasAttr("Score")) {
        String score = vn.toString(vn.getAttrVal("Score"));
        if (!(null == score || score.isEmpty())) {
          c.setScore(Integer.valueOf(score));
        }
      }

      db.saveComment(c);

    }

    db.shutDown();

  }
}
