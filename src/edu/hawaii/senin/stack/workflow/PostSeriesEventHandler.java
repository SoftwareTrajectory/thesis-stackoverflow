package edu.hawaii.senin.stack.workflow;

import java.io.IOException;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import edu.hawaii.senin.stack.persistence.PostSeries;
import edu.hawaii.senin.stack.persistence.PreseriesEvent;
import edu.hawaii.senin.stack.util.StackTrace;

public class PostSeriesEventHandler implements ResultHandler {

  private String tzname;
  private Integer preseriesId;
  private SqlSession session;

  public PostSeriesEventHandler(SqlSession session, Integer preseriesId, String tzname)
      throws IOException {
    this.session = session;
    this.preseriesId = preseriesId;
    this.tzname = tzname.substring(0);
  }

  @Override
  public void handleResult(ResultContext context) {

    PostSeries ps = (PostSeries) context.getResultObject();

    PreseriesEvent event = new PreseriesEvent();
    event.setSeriesId(preseriesId);

    // get the UNIX UTC time out and use JodaTime to fix the tstamp
    //
    long unixTime = ps.getCreationDate().toGregorianCalendar().getTimeInMillis();
    LocalDateTime localDateTime = new LocalDateTime(unixTime, DateTimeZone.forID(tzname));

    event.setDate(localDateTime);
    event.setHour(localDateTime.getHourOfDay());

    event.setCounter(1);

    // 1 Q Question
    // 2 A Answer
    // 3 O Orphaned tag wiki
    // 4 E Tag wiki excerpt
    // 5 W Tag wiki
    // 6 M Moderator nomination
    // 7 P "Wiki placeholder" (seems to only be the election description)
    // 8 Z Privilege wiki

    switch (ps.getPostTypeId()) {
    case 1:
      event.setTag('Q');
      break;
    case 2:
      event.setTag('A');
      break;
    case 3:
      event.setTag('O');
      break;
    case 4:
      event.setTag('E');
      break;
    case 5:
      event.setTag('W');
      break;
    case 6:
      event.setTag('M');
      break;
    case 7:
      event.setTag('P');
      break;
    case 8:
      event.setTag('Z');
      break;
    default:
      event.setTag('?');
    }

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
