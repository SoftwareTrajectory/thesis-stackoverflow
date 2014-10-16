package edu.hawaii.senin.stack.workflow;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.hackystat.utilities.tstamp.Tstamp;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

public class TestConversion {

  private final static String[] sample = { "2008-08-20T19:55:51", "2008-08-20T19:59:57",
      "2008-08-20T20:02:48", "2008-08-20T20:08:37", "2008-08-20T20:18:08", "2008-08-21T03:20:05",
      "2008-08-21T14:04:55", "2008-08-21T18:33:28", "2008-08-22T15:33:39", "2008-08-22T17:10:59",
      "2008-08-23T04:12:29", "2008-08-23T04:27:12", "2008-08-23T04:55:28", "2008-08-24T05:10:57",
      "2008-08-24T05:48:22" };

  @Test
  public void TestConversion() throws Exception {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    for (String s : sample) {
      LocalDateTime cd = LocalDateTime.fromCalendarFields(Tstamp.makeTimestamp(s)
          .toGregorianCalendar());

      Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
      cal.clear();
      cal.set(cd.getYear(), cd.getMonthOfYear() - 1, cd.getDayOfMonth());

      System.out.println(s
          + " => "
          + cd
          + " => "
          + cal.getTime()
          + " => "
          + new LocalDateTime(Tstamp.makeTimestamp(s).toGregorianCalendar().getTimeInMillis(),
              DateTimeZone.forID("Asia/Kolkata")));
    }
  }
}
