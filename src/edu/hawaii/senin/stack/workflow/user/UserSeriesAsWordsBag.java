package edu.hawaii.senin.stack.workflow.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.jmotif.text.TextUtils;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.jmotif.util.StdRandom;
import edu.hawaii.senin.stack.analysis.UserSeriesJob;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.User;

/**
 * Trying to extract
 * 
 * @author psenin
 * 
 */
public class UserSeriesAsWordsBag {
  private static StackDB db;

  private static final int SAX_ALPHABET = 3;
  private static final int PAA_SIZE = 6;
  private static final int SAMPLE_SIZE = 10;
  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(UserSeriesAsWordsBag.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  public static void main(String[] args) throws Exception {

    db = StackDBManager.getProductionInstance();

    // List<User> subset20K = db.getUsersWithReputation(200000, 1000000);
    List<User> subset20K = new ArrayList<User>();
    subset20K.add(db.getUser(22656));
    List<WordBag> bags20K = getUserTracksAsWordBags(SAMPLE_SIZE, subset20K);

    // List<User> subset10K = db.getUsersWithReputation(5000, 10000);
    List<User> subset10K = new ArrayList<User>();
    subset10K.add(db.getUser(29407));
    List<WordBag> bags10K = getUserTracksAsWordBags(SAMPLE_SIZE, subset10K);

    WordBag w10K = new WordBag("10K");
    WordBag w20K = new WordBag("20K");

    for (int i = 0; i < 1; i++) {
      w10K.mergeWith(bags10K.get(i));
      w20K.mergeWith(bags20K.get(i));
    }

    System.out.println(TextUtils.wordBagToTable(w20K));

    System.out.println(TextUtils.wordBagToTable(w10K));

    List<WordBag> bags = new ArrayList<WordBag>();

    bags.add(w10K);
    bags.add(w20K);

    System.out.println(TextUtils.bagsToTable(bags));

    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bags);
    // writePreClusterTable(tfidf, "newdat.csv");

    System.out.println(TextUtils.tfidfToTable(tfidf));
  }

  private static List<WordBag> getUserTracksAsWordBags(int num, List<User> subset20k)
      throws Exception {

    int count = subset20k.size();
    TreeSet<Integer> seen = new TreeSet<Integer>();
    List<WordBag> res = new ArrayList<WordBag>();

    while (res.size() < num && seen.size() < count) {

      int idx = StdRandom.uniform(count);
      while (seen.contains(idx)) {
        idx = StdRandom.uniform(count);
      }

      seen.add(idx);

      WordBag wb = (new UserSeriesJob(subset20k.get(idx).getId(), UserSeriesJob.DAILY, PAA_SIZE,
          SAX_ALPHABET)).call();
      if (null == wb) {
        continue;
      }
      else {
        res.add(wb);
      }

    }
    return res;
  }
}
