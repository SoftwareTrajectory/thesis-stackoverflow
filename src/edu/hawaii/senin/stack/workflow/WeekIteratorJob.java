package edu.hawaii.senin.stack.workflow;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import edu.hawaii.jmotif.sax.alphabet.NormalAlphabet;
import edu.hawaii.jmotif.timeseries.TSUtils;
import edu.hawaii.senin.stack.db.StackDBManager;
import edu.hawaii.senin.stack.persistence.PreseriesEvent;
import edu.hawaii.senin.stack.persistence.SaxEntry;
import edu.hawaii.senin.stack.util.StackTrace;

public class WeekIteratorJob implements Callable<String> {

  private static final int ALPHABET_SIZE = 4;
  private static final int PAA_SIZE = 7;

  private Integer userId;
  private NormalAlphabet a = new NormalAlphabet();
  private Integer seriesId;

  public WeekIteratorJob(Integer id, Integer seriesId) {
    this.userId = id;
    this.seriesId = id;
  }

  @Override
  public String call() throws Exception {
    SqlSession session = null;
    try {

      session = StackDBManager.getProductionInstance().getSession();

      // alter table preseries_event add key pre_event_date(date asc);
      PreseriesEvent start = session.selectOne("GetFirstUserEvent", this.userId);
      if (null == start) {
        return "User id " + this.userId + " has no associated series";
      }
      PreseriesEvent end = session.selectOne("GetLastUserEvent", this.userId);

      LocalDateTime startMonday = start.getDate().plusDays(7)
          .withDayOfWeek(DateTimeConstants.MONDAY);
      LocalDateTime endMonday = end.getDate().minusDays(7).withDayOfWeek(DateTimeConstants.MONDAY);

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

          double[] paa = TSUtils.paa(TSUtils.zNormalize(weekCounts), PAA_SIZE);
          char[] sax = TSUtils.ts2String(paa, a.getCuts(ALPHABET_SIZE));

          SaxEntry se = new SaxEntry();
          se.setSeriesId(this.seriesId);
          se.setSax_string(String.valueOf(sax));
          se.setDate(currentMonday);

          session.insert("SaveSaxEntry", se);

          // resSB.append(currentMonday.toString()).append(":").append(Arrays.toString(weekCounts))
          // .append(" ==> ").append(sax).append("\n");

          // move the start Monday
          //
          currentMonday = currentMonday.plusDays(7);
        }

        return resSB.toString();
      }
      else {
        return "User " + this.userId + ": endMonday " + endMonday + " is after Start Monday "
            + startMonday + "... skipping...";
      }

    }
    catch (IOException e) {
      return StackTrace.toString(e);
    }
    finally {
      session.close();
    }

  }
}
