package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.utilities.tstamp.Tstamp;

public class CommentSeries {
  private Integer id;
  private Integer postId;
  private Integer userId;
  private Integer score;
  private XMLGregorianCalendar creationDate;

  public void setAttribute(String attributeName, String attributeValue) throws Exception {
    if ("id".equalsIgnoreCase(attributeName)) {
      this.id = parseIntegerAttribute(attributeValue);
    }
    else if ("postId".equalsIgnoreCase(attributeName)) {
      this.postId = parseIntegerAttribute(attributeValue);
    }
    else if ("userId".equalsIgnoreCase(attributeName)) {
      this.userId = parseIntegerAttribute(attributeValue);
    }
    else if ("score".equalsIgnoreCase(attributeName)) {
      this.score = parseIntegerAttribute(attributeValue);
    }
    else if ("creationDate".equalsIgnoreCase(attributeName)) {
      this.creationDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("text".equalsIgnoreCase(attributeName)) {
      assert true;
    }
    else {
      throw new RuntimeException("Unable to process the attribute name " + attributeName);
    }
  }

  private Integer parseIntegerAttribute(String attributeValue) {
    if (null != attributeValue && !(attributeValue.isEmpty()) && attributeValue.length() > 0) {
      return Integer.valueOf(attributeValue);
    }
    return 0;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getPostId() {
    return postId;
  }

  public void setPostId(Integer postId) {
    this.postId = postId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public XMLGregorianCalendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(XMLGregorianCalendar creationDate) {
    this.creationDate = creationDate;
  }

}
