package edu.hawaii.senin.stack.analysis;

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

/**
 * Trying to extract
 * 
 * @author psenin
 * 
 */
public class Experiment01TopContributorDailyAngles {

  // private static StackDB db;

  private static final int SAX_ALPHABET = 3;
  private static final int PAA_SIZE = 8;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(Experiment01TopContributorDailyAngles.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  public static void main(String[] args) throws Exception {

    // db = StackDBManager.getProductionInstance();

    List<WordBag> bagsA = new ArrayList<WordBag>();
    bagsA.add((new UserSeriesJob(22656, UserSeriesJob.DAILY, PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(29407, UserSeriesJob.DAILY, PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(23354, UserSeriesJob.DAILY, PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(157882, UserSeriesJob.DAILY, PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(17034, UserSeriesJob.DAILY, PAA_SIZE, SAX_ALPHABET)).call());

    for (WordBag b : bagsA) {
      System.out.println("# " + b.getLabel() + "\n" + b.toColumn());
    }

    System.out.println(TextUtils.bagsToTable(bagsA));

    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bagsA);

    System.out.println(TextUtils.tfidfToTable(TextUtils.normalizeToUnitVectors(tfidf)));

    CosineDistanceMatrix m = new CosineDistanceMatrix(tfidf);
    System.out.println(m.toString());
    
    // launch HC
    Cluster clusters = HC.Hc(tfidf, LinkageCriterion.COMPLETE);

    BufferedWriter bw = new BufferedWriter(new FileWriter("clusters_DAILY.newick"));
    bw.write("(" + clusters.toNewick() + ")");

    bw.close();

  }

}
