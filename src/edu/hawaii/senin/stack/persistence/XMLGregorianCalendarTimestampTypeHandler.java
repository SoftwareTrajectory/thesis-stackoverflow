package edu.hawaii.senin.stack.persistence;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.hackystat.utilities.tstamp.Tstamp;

public class XMLGregorianCalendarTimestampTypeHandler implements TypeHandler<XMLGregorianCalendar> {

  @Override
  public void setParameter(PreparedStatement ps, int i, XMLGregorianCalendar parameter,
      JdbcType jdbcType) throws SQLException {
    if (null != parameter) {
      long time = parameter.toGregorianCalendar().getTimeInMillis();
      // System.out.println((XMLGregorianCalendar) parameter);
      Timestamp date = new java.sql.Timestamp(time);
      // System.out.println(date);
      ps.setTimestamp(i, date);
    }
    else {
      ps.setTimestamp(i, null);
    }
  }

  @Override
  public XMLGregorianCalendar getResult(ResultSet rs, String columnName) throws SQLException {
    Date date = rs.getTimestamp(columnName);
    if (null == date) {
      return null;
    }
    return Tstamp.makeTimestamp(date.getTime());
  }

  @Override
  public XMLGregorianCalendar getResult(CallableStatement cs, int columnIndex) throws SQLException {
    Date date = cs.getDate(columnIndex);
    return Tstamp.makeTimestamp(date.getTime());
  }

  @Override
  public XMLGregorianCalendar getResult(ResultSet rs, int columnIndex) throws SQLException {
    Date date = rs.getDate(columnIndex);
    return Tstamp.makeTimestamp(date.getTime());
  }

}
