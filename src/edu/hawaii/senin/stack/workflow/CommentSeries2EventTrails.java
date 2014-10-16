package edu.hawaii.senin.stack.workflow;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.Preseries;
import edu.hawaii.senin.stack.persistence.User;
import edu.hawaii.senin.stack.util.StackTrace;

public class CommentSeries2EventTrails {


  private static final Integer MAX_THREADS = 3;

  private static StackDB db;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(CommentSeries2EventTrails.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }
  /**
   * 
   * These iterates over the stack overflow users set, one by one, pulls hourly daily patterns and
   * dumps those into DB. It skips days with no activity.
   * 
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    db = StackDBManager.getProductionInstance();

    List<String> targetTimeZones = db.getSortedTimezones();
    consoleLogger.info("There are " + targetTimeZones.size() + " timezones to process");

    int totalTaskCounter = 0;

    for (String tzname : targetTimeZones) {

      // get the list of users
      //
      List<User> users = db.getUsersForTimezone(tzname);
      consoleLogger.info("Got " + users.size() + " users to process for timezone " + tzname);

      // create thread pool for processing these users
      //
      ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
      CompletionService<String> completionService = new ExecutorCompletionService<String>(
          executorService);

      // process user series
      //
      int userTaskCounter = 0;
      for (User u : users) {

        // make a series record
        //
        // consoleLogger.info("setting up a job for user id: " + u.getId() + ", display name: "
        // + u.getDisplayName() + ", currently " + totalTaskCounter + " jobs in queue...");

        Preseries ps = new Preseries();
        ps.setUserid(u.getId());
        ps.setTag("full");

        Preseries oldRecord = db.getPreseries(ps);

        if (null == oldRecord) {
          db.savePreseries(ps);
        }
        else {
          ps.setId(oldRecord.getId());
        }

        // create and submit the job
        //
        final CommentSeriesJob job = new CommentSeriesJob(u, ps, tzname);
        completionService.submit(job);

        totalTaskCounter++;
        userTaskCounter++;
      }

      // waiting for completion, shutdown pool disabling new tasks from being submitted
      executorService.shutdown();
      consoleLogger.info("Submitted " + userTaskCounter + " jobs for timezone " + tzname
          + " shutting down the current pool");

      try {

        while (userTaskCounter > 0) {
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
            userTaskCounter--;
            // consoleLogger.info(finished.get() + "; jobs in queue: " + userTaskCounter);
          }
        }

        consoleLogger.info("All jobs for timezone " + tzname + " completed, In total "
            + totalTaskCounter + " jobs completed, moving on.");

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
    }
    db.shutDown();
  }
}
