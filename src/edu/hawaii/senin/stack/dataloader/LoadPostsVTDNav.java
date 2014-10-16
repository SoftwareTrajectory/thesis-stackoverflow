package edu.hawaii.senin.stack.dataloader;

import org.hackystat.utilities.tstamp.Tstamp;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Post;

/**
 * Parse the challenge change XML and load it into database.
 * 
 * @author Pavel Senin
 * 
 */
public class LoadPostsVTDNav {

  /** The file location. */
  // private static final String fileLocation =
  // "/media/backup/XML/home/users/ponza/stack_overflow_201208/posts.xml";
  // private static final String fileLocation =
  // "/media/Stock/home/users/ponza/stack_overflow_201208/posts.xml";
  private static final String fileLocation = "test/data/posts.xml";

  // private static final String ROOT_ELEMENT = "posts";

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

      Post p = new Post();

      // private Integer id;
      if (vn.hasAttr("Id")) {
        String id = vn.toString(vn.getAttrVal("Id"));
        if (!(null == id || id.isEmpty())) {
          p.setId(Integer.valueOf(id));
        }
      }

      // private Integer parentId;
      if (vn.hasAttr("ParentId")) {
        String parentId = vn.toString(vn.getAttrVal("ParentId"));
        if (!(null == parentId || parentId.isEmpty())) {
          p.setParentId(Integer.valueOf(parentId));
        }
      }

      if (vn.hasAttr("PostTypeId")) {
        String postTypeId = vn.toString(vn.getAttrVal("PostTypeId"));
        if (!(null == postTypeId || postTypeId.isEmpty())) {
          p.setPostTypeId(Integer.valueOf(postTypeId));
        }
      }

      if (vn.hasAttr("AcceptedAnswerId")) {
        String acceptedAnswerId = vn.toString(vn.getAttrVal("AcceptedAnswerId"));
        if (!(null == acceptedAnswerId || acceptedAnswerId.isEmpty())) {
          p.setAcceptedAnswerId(Integer.valueOf(acceptedAnswerId));
        }
      }

      if (vn.hasAttr("CreationDate")) {
        String creationDate = vn.toString(vn.getAttrVal("CreationDate"));
        if (!(null == creationDate || creationDate.isEmpty())) {
          p.setCreationDate(Tstamp.makeTimestamp(creationDate));
        }
      }

      if (vn.hasAttr("ClosedDate")) {
        String closedDate = vn.toString(vn.getAttrVal("ClosedDate"));
        if (!(null == closedDate || closedDate.isEmpty())) {
          p.setClosedDate(Tstamp.makeTimestamp(closedDate));
        }
      }

      if (vn.hasAttr("CommunityOwnedDate")) {
        String communityOwnedDate = vn.toString(vn.getAttrVal("CommunityOwnedDate"));
        if (!(null == communityOwnedDate || communityOwnedDate.isEmpty())) {
          p.setCommunityOwnedDate(Tstamp.makeTimestamp(communityOwnedDate));
        }
      }

      if (vn.hasAttr("Score")) {
        String score = vn.toString(vn.getAttrVal("Score"));
        if (!(null == score || score.isEmpty())) {
          p.setScore(Integer.valueOf(score));
        }
      }

      if (vn.hasAttr("ViewCount")) {
        String viewCount = vn.toString(vn.getAttrVal("ViewCount"));
        if (!(null == viewCount || viewCount.isEmpty())) {
          p.setViewCount(Integer.valueOf(viewCount));
        }
      }

      if (vn.hasAttr("Body")) {
        String body = vn.toString(vn.getAttrVal("Body"));
        if (!(null == body || body.isEmpty())) {
          p.setBody(body.substring(0));
        }
      }

      if (vn.hasAttr("OwnerUserId")) {
        String ownerUserId = vn.toString(vn.getAttrVal("OwnerUserId"));
        if (!(null == ownerUserId || ownerUserId.isEmpty())) {
          p.setOwnerUserId(Integer.valueOf(ownerUserId));
        }
      }

      if (vn.hasAttr("LastEditorUserId")) {
        String lastEditorUserId = vn.toString(vn.getAttrVal("LastEditorUserId"));
        if (!(null == lastEditorUserId || lastEditorUserId.isEmpty())) {
          p.setLastEditorUserId(Integer.valueOf(lastEditorUserId));
        }
      }

      if (vn.hasAttr("LastEditorUserId")) {
        String lastEditorDisplayName = vn.toString(vn.getAttrVal("LastEditorDisplayName"));
        if (!(null == lastEditorDisplayName || lastEditorDisplayName.isEmpty())) {
          p.setLastEditorDisplayName(lastEditorDisplayName.substring(0));
        }
      }

      if (vn.hasAttr("LastEditDate")) {
        String lastEditDate = vn.toString(vn.getAttrVal("LastEditDate"));
        if (!(null == lastEditDate || lastEditDate.isEmpty())) {
          p.setLastEditDate(Tstamp.makeTimestamp(lastEditDate));
        }
      }

      if (vn.hasAttr("LastActivityDate")) {
        String lastActivityDate = vn.toString(vn.getAttrVal("LastActivityDate"));
        if (!(null == lastActivityDate || lastActivityDate.isEmpty())) {
          p.setLastActivityDate(Tstamp.makeTimestamp(lastActivityDate));
        }
      }

      if (vn.hasAttr("Title")) {
        String title = vn.toString(vn.getAttrVal("Title"));
        if (!(null == title || title.isEmpty())) {
          p.setTitle(title.substring(0));
        }
      }

      if (vn.hasAttr("AnswerCount")) {
        String answerCount = vn.toString(vn.getAttrVal("AnswerCount"));
        if (!(null == answerCount || answerCount.isEmpty())) {
          p.setAnswerCount(Integer.valueOf(answerCount));
        }
      }

      if (vn.hasAttr("CommentCount")) {
        String commentCount = vn.toString(vn.getAttrVal("CommentCount"));
        if (!(null == commentCount || commentCount.isEmpty())) {
          p.setCommentCount(Integer.valueOf(commentCount));
        }
      }

      if (vn.hasAttr("FavoriteCount")) {
        String favoriteCount = vn.toString(vn.getAttrVal("FavoriteCount"));
        if (!(null == favoriteCount || favoriteCount.isEmpty())) {
          p.setFavoriteCount(Integer.valueOf(favoriteCount));
        }
      }
      db.savePost(p);

    }

    db.shutDown();

  }
}
