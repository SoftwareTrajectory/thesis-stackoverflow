package edu.hawaii.senin.stack.workflow.user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.jmotif.text.TextUtils;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.jmotif.text.cluster.FurthestFirstStrategy;
import edu.hawaii.jmotif.text.cluster.TextKMeans;
import edu.hawaii.senin.stack.analysis.UserSeriesJob;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;

/**
 * Trying to extract
 * 
 * @author psenin
 * 
 */
public class Top24KMeansClustering {

  private static final int[] ids = { 22656, 29407, 23354, 157882, 17034, 34397, 6309, 893, 23283,
      14860, 95810, 115145, 61974, 13249, 13302, 65358, 12950, 20862, 5445, 70604, 18393, 1583,
      88656, 203907, 69083, 19068, };

  private static StackDB db;

  private static final DecimalFormat df = new DecimalFormat("#0.0000000000");

  private static final int SAX_ALPHABET = 3;
  private static final int PAA_SIZE = 4;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(Top24KMeansClustering.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  public static void main(String[] args) throws Exception {

    db = StackDBManager.getProductionInstance();

    List<WordBag> bagsA = new ArrayList<WordBag>();
    for (int id : ids) {
      WordBag bag = new UserSeriesJob(id, "DAILY", PAA_SIZE, SAX_ALPHABET).call();
      if (null != bag) {
        bagsA.add((new UserSeriesJob(id, "DAILY", PAA_SIZE, SAX_ALPHABET)).call());
      }
    }

    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bagsA);
    tfidf = TextUtils.normalizeToUnitVectors(tfidf);

    // launch KMeans with random centers
    @SuppressWarnings("unused")
    HashMap<String, List<String>> clusters = TextKMeans.cluster(tfidf, 4,
        new FurthestFirstStrategy());

    // write down tf*idf vectors for each class
    writePreClusterTable(tfidf, "top24kmeans.csv");

  }

  private static void writePreClusterTable(HashMap<String, HashMap<String, Double>> tfidf,
      String fname) throws IOException {

    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fname)));
    // melt together sets of keys
    //
    TreeSet<String> words = new TreeSet<String>();
    for (HashMap<String, Double> t : tfidf.values()) {
      words.addAll(t.keySet());
    }

    // print keys - the dictionaries names
    //
    StringBuilder sb = new StringBuilder("\"\",");
    for (String key : tfidf.keySet()) {
      sb.append("\"").append(key).append("\",");
    }
    bw.write(sb.delete(sb.length() - 1, sb.length()).append("\n").toString());

    // print rows, one by one
    //
    for (String w : words) {
      sb = new StringBuilder();
      sb.append("\"").append(w).append("\",");
      for (String key : tfidf.keySet()) {
        HashMap<String, Double> data = tfidf.get(key);
        if (data.keySet().contains(w)) {
          sb.append(df.format(data.get(w))).append(",");
        }
        else {
          sb.append(df.format(0.0d)).append(",");
        }
      }
      bw.write(sb.delete(sb.length() - 1, sb.length()).append("\n").toString());
    }
    bw.close();
  }

}
