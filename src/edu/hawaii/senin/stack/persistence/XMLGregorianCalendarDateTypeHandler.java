package edu.hawaii.senin.stack.persistence;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.hackystat.utilities.tstamp.Tstamp;

public class XMLGregorianCalendarDateTypeHandler implements TypeHandler<XMLGregorianCalendar> {

  @Override
  public void setParameter(PreparedStatement ps, int i, XMLGregorianCalendar parameter,
      JdbcType jdbcType) throws SQLException {
    if (null != parameter) {
      long time = parameter.toGregorianCalendar().getTimeInMillis();
      // System.out.println((XMLGregorianCalendar) parameter);
      Date date = new java.sql.Date(time);
      // System.out.println(date);
      ps.setDate(i, date);
    }
    else {
      ps.setTimestamp(i, null);
    }
  }

  @Override
  public XMLGregorianCalendar getResult(ResultSet rs, String columnName) throws SQLException {
    Date date = rs.getDate(columnName);
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
