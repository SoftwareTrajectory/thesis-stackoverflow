package edu.hawaii.senin.stack.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Experiment01Vectors2WebLogo {

  // logger business
  private static Logger consoleLogger;
  private static Level LOGGING_LEVEL = Level.INFO;

  static {
    consoleLogger = (Logger) LoggerFactory.getLogger(Experiment01Vectors2WebLogo.class);
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

    ArrayList<String[]> dat = readCSV("results/WEEKLY/vectors.txt");
    System.out.println(Arrays.toString(dat.get(0)));
    for (int i = 1; i < dat.size(); i++) {
      String[] arr = dat.get(i);
      Double freq = Double.valueOf(arr[5]);
      if (freq > 0.0) {
        long count = Math.round(freq * 100);
        // long count = 1;
        for (int j = 0; j < count; j++) {
          System.out.println(arr[0]);
        }
      }
    }

  }

  private static ArrayList<String[]> readCSV(String fname) throws IOException {
    ArrayList<String[]> res = new ArrayList<String[]>();
    String line = null;
    BufferedReader br = new BufferedReader(new FileReader(new File(fname)));
    while ((line = br.readLine()) != null) {
      String[] r = line.split("\\s*,\\s*");
      for (int i = 0; i < r.length; i++) {
        String s = r[i];
        if (s.startsWith("\"") && s.endsWith("\"")) {
          s = s.replace("\"", "");
          r[i] = s;
        }
      }
      res.add(r);
    }
    br.close();
    return res;
  }

}
