package edu.hawaii.senin.stack.workflow.user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import edu.hawaii.jmotif.sax.alphabet.NormalAlphabet;
import edu.hawaii.jmotif.text.WordBag;
import edu.hawaii.jmotif.timeseries.TSUtils;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.PreseriesEvent;
import edu.hawaii.senin.stack.persistence.User;
import edu.hawaii.senin.stack.util.StackTrace;

/**
 * this suppose to take a lot of parameters and return a series ID for further analyses.
 * 
 * @author psenin
 * 
 */
public class UserSeriesJob implements Callable<WordBag> {

  public static final String WEEKLY = "weekly";
  public static final String DAILY = "daily";

  private int userId;
  private String type;
  private int paaSize;
  private int alphabetSize;
  private NormalAlphabet a = new NormalAlphabet();

  public UserSeriesJob(int userId, String type, int paaSize, int saxAlphabet) {
    this.userId = userId;
    this.type = type;
    this.paaSize = paaSize;
    this.alphabetSize = saxAlphabet;
  }

  @Override
  public WordBag call() throws Exception {

    WordBag resBag = new WordBag(String.valueOf(this.userId) + " " + this.type);

    SqlSession session = null;

    try {

      session = StackDBManager.getProductionInstance().getSession();

      User user = session.selectOne("getUser", this.userId);
      resBag.setLabel(user.getDisplayName());

      BufferedWriter bw = new BufferedWriter(new FileWriter(new File(user.getDisplayName().replace(
          " ", "_")
          + this.type + "_" + this.paaSize + "_" + this.alphabetSize + ".csv")));

      // get the first and the last events
      //
      PreseriesEvent start = session.selectOne("GetFirstUserEvent", this.userId);
      if (null == start) {
        return null;
      }
      PreseriesEvent end = session.selectOne("GetLastUserEvent", this.userId);

      if (WEEKLY.equalsIgnoreCase(type)) {
        // fix the first and the last mondays for weekly tests
        //
        LocalDateTime startMonday = start.getDate().plusDays(7)
            .withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDateTime endMonday = end.getDate().minusDays(7)
            .withDayOfWeek(DateTimeConstants.MONDAY);

        //
        //
        if (endMonday.isAfter(startMonday)) {

          StringBuffer resSB = new StringBuffer();

          LocalDateTime currentMonday = new LocalDateTime(startMonday);

          while (currentMonday.isBefore(endMonday)) {

            double[] weekCounts = new double[7];

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", this.userId);
            params.put("start", currentMonday);
            params.put("end", currentMonday.plusDays(6));
            List<Object> res = session.selectList("GetFullUserEventSeries", params);

            if (res.isEmpty()) {
              currentMonday = currentMonday.plusDays(7);
              continue;
            }

            for (Object row : res) {
              @SuppressWarnings("unchecked")
              HashMap<String, Object> r = (HashMap<String, Object>) row;
              int idx = ((Integer) r.get("weekday")).intValue();
              double count = ((BigDecimal) r.get("count")).doubleValue();
              if (1 == idx) {
                weekCounts[6] = count;
              }
              else {
                weekCounts[idx - 2] = count;
              }
            }

            // sax business
            //

            double[] paa = TSUtils.paa(TSUtils.zNormalize(weekCounts), this.paaSize);
            char[] sax = TSUtils.ts2String(paa, a.getCuts(this.alphabetSize));

            resBag.addWord(String.valueOf(sax));

            // move the start Monday
            //
            currentMonday = currentMonday.plusDays(7);
          }

          return resBag;
        }
      }
      else if (DAILY.equalsIgnoreCase(type)) {

        if (end.getDate().isAfter(start.getDate())) {

          LocalDateTime currentDate = new LocalDateTime(start.getDate());

          // need to collect data for the last day too
          while (currentDate.isBefore(end.getDate().plusDays(1))) {

            double[] dayCounts = new double[24];

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("userId", this.userId);
            params.put("start", currentDate);
            params.put("end", currentDate.plusDays(1));
            List<Object> res = session.selectList("GetFullUserDaySeries", params);

            if (res.isEmpty()) {
              currentDate = currentDate.plusDays(1);
              continue;
            }

            // --
            for (Object row : res) {
              @SuppressWarnings("unchecked")
              HashMap<String, Object> r = (HashMap<String, Object>) row;
              int idx = ((Integer) r.get("hour")).intValue();
              double count = ((BigDecimal) r.get("counter")).doubleValue();
              dayCounts[idx] = count;
            }

            // sax business
            //

            double[] paa = TSUtils.optimizedPaa(TSUtils.optimizedZNorm(dayCounts, 0.5D),
                this.paaSize);
            char[] sax = TSUtils.ts2String(paa, a.getCuts(this.alphabetSize));

            resBag.addWord(String.valueOf(sax));

            if ("bbedbc".equalsIgnoreCase(String.valueOf(sax))) {
              System.out.println(this.userId + " | " + Arrays.toString(dayCounts) + " | "
                  + Arrays.toString(paa) + " | " + Arrays.toString(sax));
            }

            bw.write(currentDate + ","
                + Arrays.toString(dayCounts).replace("[", "").replace("]", "").replace(", ", ",")
                + "," + String.valueOf(sax) + "\n");

            // move the day counter
            //
            currentDate = currentDate.plusDays(1);

          }

          bw.close();
          return resBag;
        }

      }
      return null;
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
      return null;
    }
    finally {
      session.close();
    }
  }

}
