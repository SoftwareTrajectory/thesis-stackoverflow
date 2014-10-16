package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.utilities.tstamp.Tstamp;

public class PostHistory {
  private Integer id;
  private Integer userId;
  private Integer postId;
  private XMLGregorianCalendar creationDate;
  private String revisionGUID;
  private Integer postHistoryTypeId;
  private Integer closeReasonId;
  private String userDisplayName;
  private String text;
  private String comment;

  public void setAttribute(String attributeName, String attributeValue) throws Exception {
    if ("id".equalsIgnoreCase(attributeName)) {
      this.id = Integer.valueOf(attributeValue);
    }
    else if ("userId".equalsIgnoreCase(attributeName)) {
      this.userId = Integer.valueOf(attributeValue);
    }
    else if ("postId".equalsIgnoreCase(attributeName)) {
      this.postId = Integer.valueOf(attributeValue);
    }
    else if ("creationDate".equalsIgnoreCase(attributeName)) {
      this.creationDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("revisionGUID".equalsIgnoreCase(attributeName)) {
      this.revisionGUID = attributeValue.substring(0);
    }
    else if ("postHistoryTypeId".equalsIgnoreCase(attributeName)) {
      this.postHistoryTypeId = Integer.valueOf(attributeValue);
    }
    else if ("closeReasonId".equalsIgnoreCase(attributeName)) {
      this.closeReasonId = Integer.valueOf(attributeValue);
    }
    else if ("userDisplayName".equalsIgnoreCase(attributeName)) {
      this.userDisplayName = attributeValue.substring(0);
    }
    else if ("text".equalsIgnoreCase(attributeName)) {
      this.text = attributeValue.substring(0);
    }
    else if ("comment".equalsIgnoreCase(attributeName)) {
      this.comment = attributeValue.substring(0);
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

  public Integer getPostId() {
    return postId;
  }

  public void setPostId(Integer postId) {
    this.postId = postId;
  }

  public XMLGregorianCalendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(XMLGregorianCalendar creationDate) {
    this.creationDate = creationDate;
  }

  public String getRevisionGUID() {
    return revisionGUID;
  }

  public void setRevisionGUID(String revisionGUID) {
    this.revisionGUID = revisionGUID;
  }

  public Integer getPostHistoryTypeId() {
    return postHistoryTypeId;
  }

  public void setPostHistoryTypeId(Integer postHistoryTypeId) {
    this.postHistoryTypeId = postHistoryTypeId;
  }

  public Integer getCloseReasonId() {
    return closeReasonId;
  }

  public void setCloseReasonId(Integer closeReasonId) {
    this.closeReasonId = closeReasonId;
  }

  public String getUserDisplayName() {
    return userDisplayName;
  }

  public void setUserDisplayName(String userDisplayName) {
    this.userDisplayName = userDisplayName;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

}
