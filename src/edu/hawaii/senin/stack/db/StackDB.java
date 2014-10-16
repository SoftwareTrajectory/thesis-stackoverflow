package edu.hawaii.senin.stack.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.senin.stack.persistence.Badge;
import edu.hawaii.senin.stack.persistence.Comment;
import edu.hawaii.senin.stack.persistence.CommentSeries;
import edu.hawaii.senin.stack.persistence.Location;
import edu.hawaii.senin.stack.persistence.Post;
import edu.hawaii.senin.stack.persistence.PostHistory;
import edu.hawaii.senin.stack.persistence.PostSeries;
import edu.hawaii.senin.stack.persistence.Preseries;
import edu.hawaii.senin.stack.persistence.PreseriesEvent;
import edu.hawaii.senin.stack.persistence.Tag;
import edu.hawaii.senin.stack.persistence.User;
import edu.hawaii.senin.stack.persistence.Vote;

/**
 * Implements Android database IO, by providing both test and production methods.
 * 
 * @author psenin
 * 
 */
public class StackDB {

  /** The mapper instance key. */
  public static final String PRODUCTION_INSTANCE = "production";

  /** The db configuration constants. */
  private static final String STACK_DB_CONFIGNAME = "mybatis-stackDB.xml";
  private static final String STACK_DB_ENVIRONMENT = "production_pooled";

  /** Test database SQL factory. */
  private SqlSessionFactory sessionFactory;

  @SuppressWarnings("unused")
  private String instanceType;

  /** Mapper config file location. */
  private String dbConfigFileName;
  private String dbEnvironmentKey;

  // the session
  private SqlSession session;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(StackDB.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  /**
   * Constructor.
   * 
   * @param isTestInstance The test instance semaphore.
   * @throws IOException if error occurs.
   */
  protected StackDB(String instanceType) throws IOException {
    this.instanceType = instanceType;
    initialize();
  }

  /**
   * Lazy initialization, takes care about set-up.
   * 
   * @throws IOException if error occurs.
   */
  private void initialize() throws IOException {

    this.dbConfigFileName = STACK_DB_CONFIGNAME;
    this.dbEnvironmentKey = STACK_DB_ENVIRONMENT;

    consoleLogger.info("Getting connected to the database, myBATIS config: " + dbConfigFileName
        + ", environment key: " + dbEnvironmentKey);

    // do check for the file existence
    //

    InputStream in = this.getClass().getClassLoader().getResourceAsStream(this.dbConfigFileName);
    if (null == in) {
      throw new RuntimeException("Unable to locate " + this.dbConfigFileName);

    }

    // proceed with configuration
    //
    this.sessionFactory = new SqlSessionFactoryBuilder().build(in, this.dbEnvironmentKey);

    this.session = this.sessionFactory.openSession(ExecutorType.REUSE);

    consoleLogger.info("Connected to database.");

  }

  /**
   * Get the session factory used.
   * 
   * @return The session factory used in this instance.
   */
  public synchronized SqlSessionFactory getSessionFactory() {
    return this.sessionFactory;
  }

  /**
   * Get the session.
   * 
   * @return The active SQL session.
   */
  public synchronized SqlSession getSession() {
    return this.sessionFactory.openSession();
  }

  /**
   * Commits and closes the open session.
   */
  public synchronized void shutDown() {
    this.session.commit(true);
    this.session.close();
  }

  /**
   * Commit pending transactions.
   */
  public synchronized void commit() {
    this.session.commit(true);
  }

  /**
   * Save the user record.
   * 
   * @param user The user record to save.
   */
  public synchronized void saveUser(User user) {
    if (null != user) {
      this.session.insert("saveUser", user);
    }
  }

  /**
   * Save the vote record.
   * 
   * @param vote The vote record to save.
   */
  public synchronized void saveVote(Vote vote) {
    if (null != vote) {
      this.session.insert("saveVote", vote);
    }
  }

  public synchronized void saveBadge(Badge badge) {
    if (null != badge) {
      this.session.insert("saveBadge", badge);
    }
  }

  public synchronized void savePost(Post post) {
    if (null != post) {
      this.session.insert("savePost", post);
    }
  }

  public synchronized Integer getPostsNumber() {
    return this.session.selectOne("countDistinctPosts");
  }

  public synchronized void saveComment(Comment c) {
    if (null != c) {
      this.session.insert("saveComment", c);
    }
  }

  public synchronized void savePostHistory(PostHistory p) {
    if (null != p) {
      this.session.insert("savePostHistory", p);
    }
  }

  public synchronized Tag getTag(String tag) {
    return this.session.selectOne("selectTag", tag);
  }

  public synchronized void saveTag(Tag tag) {
    if (null != tag) {
      this.session.insert("saveTag", tag);
    }
  }

  public synchronized void savePostSeries(PostSeries record) {
    this.session.insert("savePostSeries", record);
    if (!(null == record.getTags())) {
      for (String tag : record.getTags()) {
        Tag tagRec = getTag(tag);
        HashMap<String, Integer> param = new HashMap<String, Integer>();
        param.put("tag_id", tagRec.getId());
        param.put("post_id", record.getId());
        this.session.insert("savePostTags", param);
      }
    }

  }

  public synchronized void saveLocations(String loc, String string, String location) {
    HashMap<String, String> param = new HashMap<String, String>();
    param.put("location", loc);
    param.put("tokens", string);
    param.put("address", location);
    this.session.insert("saveLocation", param);
  }

  public synchronized void updateTZInfo(Integer id, String timezoneId, String gmtOffset) {
    HashMap<String, Object> param = new HashMap<String, Object>();
    param.put("id", id);
    param.put("timezoneId", timezoneId);
    param.put("gmtOffset", gmtOffset);
    this.session.update("updateTimezone", param);
  }

  public synchronized boolean locationIsParsed(String normalizedLocation) {
    return !(null == this.session.selectOne("checkLocationIsParsed", normalizedLocation));
  }

  public synchronized Location getLocationForNormalString(String normalLocation) {
    return this.session.selectOne("selectLocationForNormalString", normalLocation);
  }

  public synchronized void updateUserLocation(Integer userId, Integer locationId) {
    HashMap<String, Object> param = new HashMap<String, Object>();
    param.put("userId", userId);
    param.put("locationId", locationId);
    this.session.update("updateUserLocation", param);
  }

  public synchronized List<String> getSortedTimezones() {
    return this.session.selectList("selectDistinctTimezones");
  }

  public synchronized List<User> getUsersForTimezone(String tzname) {
    return this.session.selectList("selectUsersForTimezone", tzname);
  }

  public synchronized void savePreseries(Preseries ps) {
    this.session.insert("savePreseries", ps);

  }

  public synchronized void savePreseriesEvent(PreseriesEvent event) {
    this.session.insert("savePreseriesEvent", event);
  }

  public void saveCommentSeries(CommentSeries record) {
    this.session.insert("saveCommentSeries", record);
  }

  public Preseries getPreseries(Preseries record) {
    return this.session.selectOne("getPreseriesRecord", record);
  }

  public List<User> getUsersWithReputation(int lowLimit, int highLimit) {
    HashMap<String, Object> param = new HashMap<String, Object>();
    param.put("high", highLimit);
    param.put("low", lowLimit);
    return this.session.selectList("getUsersWithReputation", param);
  }

  public User getUser(int userId) {
    return this.session.selectOne("getUser", userId);
  }

}
