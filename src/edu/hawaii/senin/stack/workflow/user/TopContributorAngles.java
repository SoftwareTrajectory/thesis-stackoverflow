package edu.hawaii.senin.stack.workflow.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.jmotif.text.CosineDistanceMatrix;
import edu.hawaii.jmotif.text.TextUtils;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.senin.stack.db.StackDB;
import edu.hawaii.senin.stack.db.StackDBManager;

/**
 * Trying to extract
 * 
 * @author psenin
 * 
 */
public class TopContributorAngles {

  private static StackDB db;

  private static final int SAX_ALPHABET = 3;
  private static final int PAA_SIZE = 8;

  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(TopContributorAngles.class);
    consoleLogger.setLevel(LOGGING_LEVEL);
  }

  public static void main(String[] args) throws Exception {

    db = StackDBManager.getProductionInstance();

    List<WordBag> bagsA = new ArrayList<WordBag>();
    bagsA.add((new UserSeriesJob(22656, "DAILY", PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(23354, "DAILY", PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(893, "DAILY", PAA_SIZE, SAX_ALPHABET)).call());
    bagsA.add((new UserSeriesJob(29407, "DAILY", PAA_SIZE, SAX_ALPHABET)).call());

    for (WordBag b : bagsA) {
      System.out.println(b.getLabel() + "\n" + b.toColumn());
    }

    System.out.println(TextUtils.bagsToTable(bagsA));

    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bagsA);

    System.out.println(TextUtils.tfidfToTable(TextUtils.normalizeToUnitVectors(tfidf)));

    CosineDistanceMatrix m = new CosineDistanceMatrix(tfidf);
    System.out.println(m.toString());

  }

}
