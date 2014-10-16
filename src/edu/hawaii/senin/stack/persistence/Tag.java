package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;

public class Tag {

  private Integer id;
  private String tag;
  private XMLGregorianCalendar creationDate;

  public XMLGregorianCalendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(XMLGregorianCalendar date) {
    this.creationDate = date;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

}
