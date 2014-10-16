package edu.hawaii.senin.stack.workflow;

import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.User;
import edu.hawaii.senin.stack.util.StackTrace;

//CREATE TABLE `preseries_sax` (
//    `seriesId` int(11) NOT NULL,
//    `date` date NOT NULL,
//    `sax_string` character(5) NOT NULL,
//    UNIQUE KEY `preseries_sax_entry` (`seriesId`, `date`)
//    ) ENGINE=MyISAM DEFAULT CHARSET=latin1;

public class WeekIterator {

  private static final Integer MAX_THREADS = 5;

  private static StackDB db;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(WeekIterator.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  /**
   * 
   * These iterates over the stack overflow users set, one by one, pulls hourly daily patterns and
   * dumps those into DB. It skips days with no activity.
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    SqlSession session = StackDBManager.getProductionInstance().getSession();

    List<User> users = session.selectList("GetPreseriesUsers");
    consoleLogger.info("There are " + users.size() + " users to iterate");

    // create thread pool for processing these users
    //
    ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
    CompletionService<String> completionService = new ExecutorCompletionService<String>(
        executorService);

    int totalTaskCounter = 0;
    for (User u : users) {

      Integer seriesId = (Integer) session.selectOne("GetFullUserSeriesId", u.getId());
      final WeekIteratorJob job = new WeekIteratorJob(u.getId(), seriesId);
      completionService.submit(job);

      totalTaskCounter++;

    }

    session.close();

    // waiting for completion, shutdown pool disabling new tasks from being submitted
    executorService.shutdown();
    consoleLogger.info("Submitted " + totalTaskCounter + " jobs");

    try {

      while (totalTaskCounter > 0) {
        //
        // poll with a wait up to FOUR hours
        Future<String> finished = completionService.poll(4, TimeUnit.HOURS);
        if (null == finished) {
          //
          // something went wrong - break from here
          System.err.println("Breaking POLL loop after 4 HOURS of waiting...");
          break;
        }
        else {
          totalTaskCounter--;
          consoleLogger.info(finished.get() + "; jobs in queue: " + totalTaskCounter);
        }
      }

      consoleLogger.info("All jobs completed.");

    }
    catch (Exception e) {
      System.err.println("Error while waiting results: " + StackTrace.toString(e));
    }
    finally {
      // wait at least 1 more hour before terminate and fail
      try {
        if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
          executorService.shutdownNow(); // Cancel currently executing tasks
          if (!executorService.awaitTermination(30, TimeUnit.MINUTES))
            System.err.println("Pool did not terminate... FATAL ERROR");
        }
      }
      catch (InterruptedException ie) {
        System.err.println("Error while waiting interrupting: " + StackTrace.toString(ie));
        // (Re-)Cancel if current thread also interrupted
        executorService.shutdownNow();
        // Preserve interrupt status
        Thread.currentThread().interrupt();
      }

    }

    session.close();

  }
}
