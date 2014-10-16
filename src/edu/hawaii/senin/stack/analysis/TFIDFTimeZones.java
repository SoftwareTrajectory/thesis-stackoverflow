package edu.hawaii.senin.stack.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import edu.hawaii.jmotif.text.TextUtils;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.util.StackTrace;

public class TFIDFTimeZones {

  // logger business
  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(TFIDFTimeZones.class);
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

    // getting word bags

    List<WordBag> bags = new ArrayList<WordBag>();

    bags.add(getWordBag("Asia/Kolkata"));

    bags.add(getWordBag("America/New_York"));

    // bags.add(getWordBag("Europe/Paris"));

    bags.add(getWordBag("Europe/Moscow"));

    // bags.add(getWordBag("Asia/Tokyo"));

    // bags.add(getWordBag("Pacific/Honolulu"));

    // bags.add(getWordBag("America/Los_Angeles"));

    // bags.add(getWordBag("Europe/London"));

    // this creates the TFIDF data structure
    //
    HashMap<String, HashMap<String, Double>> tfidf = TextUtils.computeTFIDF(bags);
    // writePreClusterTable(tfidf, "newdat.csv");

    System.out.println(TextUtils.tfidfToTable(tfidf));

    // this creates the TFIDF data structure
    //
    // DistanceMatrix distance = new DistanceMatrix(tfidf);

    // Cluster c = HC.Hc(tfidf, LinkageCriterion.SINGLE);
    // System.out.println(c.toNewick());

    session.close();

  }

  private static WordBag getWordBag(String tzName) throws IOException {
    WordBag wb = new WordBag(tzName);
    SqlSession session = null;
    try {
      session = StackDBManager.getProductionInstance().getSession();

      List<Object> res = session.selectList("GetSaxWordsForTimezone", tzName);

      wb = new WordBag(tzName);

      for (Object row : res) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> r = (HashMap<String, Object>) row;
        Integer frequency = ((Long) r.get("count")).intValue();
        String word = (String) r.get("sax");
        wb.addWord(word, frequency);
      }

    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }
    finally {
      if (null != session) {
        session.close();
      }
    }
    return wb;
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
    StringBuilder sb = new StringBuilder();
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
          sb.append(data.get(w)).append(",");
        }
        else {
          sb.append("0.00,");
        }
      }
      bw.write(sb.delete(sb.length() - 1, sb.length()).append("\n").toString());
    }
    bw.close();
  }

}
