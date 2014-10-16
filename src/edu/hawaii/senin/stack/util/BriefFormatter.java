package edu.hawaii.senin.stack.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Implements log formatter.
 * 
 * @author psenin
 * 
 */
public class BriefFormatter extends Formatter {

  private static final DateFormat timeFormat = new SimpleDateFormat("h:mm:ss");
  private static final String CR = System.getProperty("line.separator");

  /**
   * A Custom format implementation.
   */
  public String format(LogRecord record) {
    String loggerName = record.getSourceMethodName();
    if (loggerName == null) {
      loggerName = "root";
    }
    StringBuilder output = new StringBuilder().append(loggerName).append(" [")
        .append(record.getLevel()).append('|').append(Thread.currentThread().getName()).append('|')
        .append(timeFormat.format(new Date(record.getMillis()))).append("] ")
        .append(record.getMessage()).append(' ').append(CR);
    return output.toString();
  }

}
