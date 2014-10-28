package edu.hawaii.senin.stack.workflow.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.jmotif.text.TextUtils;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.senin.stack.analysis.UserSeriesJob;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.User;
import edu.hawaii.senin.stack.util.StackTrace;

/**
 * Trying to extract
 * 
 * @author psenin
 * 
 */
public class RegionsClustering {

  private static final String[] tzIds = { "Asia/Kolkata", "America/New_York", "Europe/Paris",
      "Europe/Moscow", "Asia/Tokyo", "Pacific/Honolulu", "America/Los_Angeles", "Europe/London",
      "Asia/Shanghai", "Pacific/Auckland" };

  private static StackDB db;

  private static final int SAX_ALPHABET = 3;
  private static final int PAA_SIZE = 8;

  private static final int MAX_THREADS = 5;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(RegionsClustering.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  public static void main(String[] args) {

    try {
      db = StackDBManager.getProductionInstance();
    }
    catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    List<WordBag> bags = new ArrayList<WordBag>();
    // iterating over timezones
    for (String tzName : tzIds) {
      consoleLogger.info("processing timezone " + tzName);
      List<User> users = db.getUsersForTimezone(tzName);
      WordBag bag = new WordBag(tzName);

      ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
      CompletionService<WordBag> completionService = new ExecutorCompletionService<WordBag>(
          executorService);
      int totalTaskCounter = 0;

      int uCounter = 0;
      for (User u : users) {
        consoleLogger.info("processing user " + uCounter + " out of " + users.size() + ", "
            + u.getDisplayName());
        final UserSeriesJob job = new UserSeriesJob(u.getId(), "DAILY", PAA_SIZE, SAX_ALPHABET);
        completionService.submit(job);
        totalTaskCounter++;
      }

      // waiting for completion, shutdown pool disabling new tasks from being submitted
      executorService.shutdown();

      try {

        while (totalTaskCounter > 0) {
          //
          // poll with a wait up to FOUR hours
          Future<WordBag> finished = completionService.poll(4, TimeUnit.HOURS);
          if (null == finished) {
            //
            // something went wrong - break from here
            System.err.println("Breaking POLL loop after 4 HOURS of waiting...");
            break;
          }
          else {
            bag.mergeWith(finished.get());
            // consoleLogger.info(finished.get() + "; jobs in queue: " + userTaskCounter);
          }
        }

        consoleLogger.info("All jobs for timezone " + tzName + " completed, In total "
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

      bags.add(bag);
    }

    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bags);
    tfidf = TextUtils.normalizeToUnitVectors(tfidf);
    System.out.println(TextUtils.tfidfToTable(tfidf));

    //
    // System.out.println(TextUtils.tfidfToTable(tfidf));
    //
    // tfidf = TextUtils.normalizeToUnitVectors(tfidf);

    //
    // List<WordBag> bagsA = new ArrayList<WordBag>();
    // for (int id : ids) {
    // WordBag bag = new UserSeriesJob(id, "DAILY", PAA_SIZE, SAX_ALPHABET).call();
    // if (null != bag) {
    // bagsA.add((new UserSeriesJob(id, "DAILY", PAA_SIZE, SAX_ALPHABET)).call());
    // }
    // }
    //
    // System.out.println(TextUtils.bagsToTable(bagsA));
    //
    // HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bagsA);
    //
    // System.out.println(TextUtils.tfidfToTable(tfidf));
    //
    // tfidf = TextUtils.normalizeToUnitVectors(tfidf);
    //
    // CosineDistanceMatrix m = new CosineDistanceMatrix(tfidf);
    // System.out.println(m.toString());
    //
    // // launch KMeans with random centers
    // Cluster clusters = HC.Hc(tfidf, LinkageCriterion.COMPLETE);
    //
    // System.out.println((new CosineDistanceMatrix(tfidf)).toString());
    //
    // BufferedWriter bw = new BufferedWriter(new FileWriter("test.newick"));
    // bw.write("(" + clusters.toNewick() + ")");
    // bw.close();

  }
}
