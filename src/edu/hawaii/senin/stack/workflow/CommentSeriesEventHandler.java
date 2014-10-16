package edu.hawaii.senin.stack.workflow;

import java.io.IOException;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import edu.hawaii.senin.stack.persistence.CommentSeries;
import edu.hawaii.senin.stack.persistence.PreseriesEvent;
import edu.hawaii.senin.stack.util.StackTrace;

public class CommentSeriesEventHandler implements ResultHandler {

  private String tzname;
  private Integer preseriesId;
  private SqlSession session;

  public CommentSeriesEventHandler(SqlSession session, Integer preseriesId, String tzname)
      throws IOException {
    this.session = session;
    this.preseriesId = preseriesId;
    this.tzname = tzname.substring(0);
  }

  @Override
  public void handleResult(ResultContext context) {

    CommentSeries ps = (CommentSeries) context.getResultObject();

    PreseriesEvent event = new PreseriesEvent();
    event.setSeriesId(preseriesId);

    // get the UNIX UTC time out and use JodaTime to fix the tstamp
    //
    long unixTime = ps.getCreationDate().toGregorianCalendar().getTimeInMillis();
    LocalDateTime localDateTime = new LocalDateTime(unixTime, DateTimeZone.forID(tzname));

    event.setDate(localDateTime);
    event.setHour(localDateTime.getHourOfDay());

    event.setCounter(1);

    // The comment tag
    event.setTag('C');

    try {
      // check for the record existence
      //
      PreseriesEvent savedEvent = this.session.selectOne("getPreSeriesEvent", event);
      if (null == savedEvent) {
        this.session.insert("savePreseriesEvent", event);
      }
      else {
        event.setCounter(event.getCounter() + savedEvent.getCounter());
        this.session.update("updatePreseriesEvent", event);
      }

    }
    catch (Throwable t) {
      System.err.println(StackTrace.toString(t));
      session.close();
      System.exit(10);
    }
    finally {
      session.commit();
    }

  }
}
