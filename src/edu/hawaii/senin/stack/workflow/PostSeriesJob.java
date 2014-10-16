package edu.hawaii.senin.stack.workflow;

import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.ibatis.session.SqlSession;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Preseries;
import edu.hawaii.senin.stack.persistence.User;
import edu.hawaii.senin.stack.util.StackTrace;

public class PostSeriesJob implements Callable<String> {

  private User user;
  private Integer preseriesId;
  private String targetTZName;

  public PostSeriesJob(User u, Preseries ps, String tzname) {
    this.user = u;
    this.preseriesId = ps.getId();
    this.targetTZName = tzname;
  }

  @Override
  public String call() throws Exception {
    SqlSession session = null;
    try {

      session = StackDBManager.getProductionInstance().getSession();

      PostSeriesEventHandler postsPreSeriesHandler = new PostSeriesEventHandler(session,
          this.preseriesId, this.targetTZName);

      // PreSeriesCommentEventHandler commentPreSeriesHandler = new PreSeriesCommentEventHandler(
      // session, this.preseriesId, this.targetTZName);

      session.select("selectPostSeriesForPreseries", user.getId(), postsPreSeriesHandler);

      // session.select("selectCommentSeriesForPreseries", user.getId(), commentPreSeriesHandler);

    }
    catch (IOException e) {
      return StackTrace.toString(e);
    }
    finally {
      session.close();
    }

    return "done for the user id: " + user.getId() + ", name: " + user.getDisplayName();

  }

}
