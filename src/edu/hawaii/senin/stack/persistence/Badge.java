package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.utilities.tstamp.Tstamp;

public class Badge {
  private Integer id;
  private Integer userId;
  private String name;
  private XMLGregorianCalendar date;

  public void setAttribute(String attributeName, String attributeValue) throws Exception {
    if ("id".equalsIgnoreCase(attributeName)) {
      this.id = Integer.valueOf(attributeValue);
    }
    else if ("userId".equalsIgnoreCase(attributeName)) {
      this.userId = Integer.valueOf(attributeValue);
    }
    else if ("name".equalsIgnoreCase(attributeName)) {
      this.name = String.valueOf(attributeValue);
    }
    else if ("date".equalsIgnoreCase(attributeName)) {
      this.date = Tstamp.makeTimestamp(attributeValue);
    }
    else {
      throw new RuntimeException("Unable to process the attribute name " + attributeName);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public XMLGregorianCalendar getDate() {
    return date;
  }

  public void setDate(XMLGregorianCalendar date) {
    this.date = date;
  }

}
