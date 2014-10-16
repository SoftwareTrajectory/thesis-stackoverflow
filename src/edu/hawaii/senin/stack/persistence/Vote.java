package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.utilities.tstamp.Tstamp;

public class Vote {

  private Integer id;
  private Integer userId;
  private Integer postId;
  private XMLGregorianCalendar creationDate;
  private Integer voteTypeId;
  private Integer bountyAmount;

  public void setAttribute(String attributeName, String attributeValue) throws Exception {
    if ("id".equalsIgnoreCase(attributeName)) {
      this.id = parseIntegerAttribute(attributeValue);
    }
    else if ("userId".equalsIgnoreCase(attributeName)) {
      this.userId = parseIntegerAttribute(attributeValue);
    }
    else if ("postId".equalsIgnoreCase(attributeName)) {
      this.postId = parseIntegerAttribute(attributeValue);
    }
    else if ("voteTypeId".equalsIgnoreCase(attributeName)) {
      this.voteTypeId = parseIntegerAttribute(attributeValue);
    }
    else if ("bountyAmount".equalsIgnoreCase(attributeName)) {
      this.bountyAmount = parseIntegerAttribute(attributeValue);
    }
    else if ("creationDate".equalsIgnoreCase(attributeName)) {
      this.creationDate = Tstamp.makeTimestamp(attributeValue);
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

  public Integer getVoteTypeId() {
    return voteTypeId;
  }

  public void setVoteTypeId(Integer voteTypeId) {
    this.voteTypeId = voteTypeId;
  }

  public Integer getBountyAmount() {
    return bountyAmount;
  }

  public void setBountyAmount(Integer bountyAmount) {
    this.bountyAmount = bountyAmount;
  }

}
