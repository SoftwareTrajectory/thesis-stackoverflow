package edu.hawaii.senin.stack.workflow.user;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.jmotif.text.CosineDistanceMatrix;
import edu.hawaii.jmotif.text.TextUtils;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.jmotif.text.cluster.Cluster;
import edu.hawaii.jmotif.text.cluster.HC;
import edu.hawaii.jmotif.text.cluster.LinkageCriterion;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;

/**
 * Trying to extract
 * 
 * @author psenin
 * 
 */
public class Top24HcClustering {

  private static final int[] ids = { 22656, 29407, 23354, 157882, 17034, 34397, 6309, 893, 23283,
      14860, 95810, 115145, 61974, 13249, 13302, 65358, 12950, 20862, 5445, 70604, 18393, 1583,
      88656, 203907, 69083, 19068, };

  private static StackDB db;

  private static final int SAX_ALPHABET = 3;
  private static final int PAA_SIZE = 8;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(Top24HcClustering.class);
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

    System.out.println(TextUtils.bagsToTable(bagsA));

    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bagsA);

    System.out.println(TextUtils.tfidfToTable(tfidf));

    tfidf = TextUtils.normalizeToUnitVectors(tfidf);

    CosineDistanceMatrix m = new CosineDistanceMatrix(tfidf);
    System.out.println(m.toString());

    // launch KMeans with random centers
    Cluster clusters = HC.Hc(tfidf, LinkageCriterion.COMPLETE);

    System.out.println((new CosineDistanceMatrix(tfidf)).toString());

    BufferedWriter bw = new BufferedWriter(new FileWriter("test.newick"));
    bw.write("(" + clusters.toNewick() + ")");
    bw.close();

  }
}
