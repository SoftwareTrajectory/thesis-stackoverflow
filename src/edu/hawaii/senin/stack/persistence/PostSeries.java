package edu.hawaii.senin.stack.persistence;

import java.util.HashSet;
import java.util.Set;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.utilities.tstamp.Tstamp;

public class PostSeries {
  private Integer id;
  private Integer parentId;
  private Integer postTypeId;
  private Integer ownerUserId;
  private Integer acceptedAnswerId;
  private XMLGregorianCalendar creationDate;
  private XMLGregorianCalendar closedDate;
  private XMLGregorianCalendar communityOwnedDate;
  private XMLGregorianCalendar lastEditDate;
  private Integer lastEditorUserId;
  private XMLGregorianCalendar lastActivityDate;
  private Integer answerCount;
  private Integer favoriteCount;
  private Integer viewCount;
  private Integer score;
  private Integer commentCount;
  private Set<String> tags;

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  public void setAttribute(String attributeName, String attributeValue) throws Exception {
    if ("id".equalsIgnoreCase(attributeName)) {
      this.id = parseIntegerAttribute(attributeValue);
    }
    else if ("parentId".equalsIgnoreCase(attributeName)) {
      this.parentId = parseIntegerAttribute(attributeValue);
    }
    else if ("postTypeId".equalsIgnoreCase(attributeName)) {
      this.postTypeId = parseIntegerAttribute(attributeValue);
    }
    else if ("ownerUserId".equalsIgnoreCase(attributeName)) {
      this.ownerUserId = parseIntegerAttribute(attributeValue);
    }
    else if ("acceptedAnswerId".equalsIgnoreCase(attributeName)) {
      this.acceptedAnswerId = parseIntegerAttribute(attributeValue);
    }
    else if ("creationDate".equalsIgnoreCase(attributeName)) {
      this.creationDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("closedDate".equalsIgnoreCase(attributeName)) {
      this.closedDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("communityOwnedDate".equalsIgnoreCase(attributeName)) {
      this.communityOwnedDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("lastEditDate".equalsIgnoreCase(attributeName)) {
      this.lastEditDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("lastEditorUserId".equalsIgnoreCase(attributeName)) {
      this.lastEditorUserId = parseIntegerAttribute(attributeValue);
    }
    else if ("lastEditorDisplayName".equalsIgnoreCase(attributeName)) {
      assert true;
    }
    else if ("lastActivityDate".equalsIgnoreCase(attributeName)) {
      this.lastActivityDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("tags".equalsIgnoreCase(attributeName)) {
      String tagLine = attributeValue.substring(0);
      Set<String> normalizedTags = new HashSet<String>();
      if (!((null == tagLine) || (tagLine.length() == 0))) {
        String[] split = tagLine.split(">\\s*<");
        for (String tag : split) {
          String trimmed = tag.trim().toLowerCase().replaceAll(">", "").replaceAll("<", "");
          normalizedTags.add(trimmed);
        }
      }
      this.tags = normalizedTags;
    }
    else if ("title".equalsIgnoreCase(attributeName)) {
      assert true;
    }
    else if ("body".equalsIgnoreCase(attributeName)) {
      assert true;
    }
    else if ("answerCount".equalsIgnoreCase(attributeName)) {
      this.answerCount = parseIntegerAttribute(attributeValue);
    }
    else if ("favoriteCount".equalsIgnoreCase(attributeName)) {
      this.favoriteCount = parseIntegerAttribute(attributeValue);
    }
    else if ("viewCount".equalsIgnoreCase(attributeName)) {
      this.viewCount = parseIntegerAttribute(attributeValue);
    }
    else if ("score".equalsIgnoreCase(attributeName)) {
      this.score = parseIntegerAttribute(attributeValue);
    }
    else if ("commentCount".equalsIgnoreCase(attributeName)) {
      this.commentCount = parseIntegerAttribute(attributeValue);
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

  public Integer getPostTypeId() {
    return postTypeId;
  }

  public void setPostTypeId(Integer postTypeId) {
    this.postTypeId = postTypeId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public Integer getOwnerUserId() {
    return ownerUserId;
  }

  public void setOwnerUserId(Integer ownerUserId) {
    this.ownerUserId = ownerUserId;
  }

  public Integer getAcceptedAnswerId() {
    return acceptedAnswerId;
  }

  public void setAcceptedAnswerId(Integer acceptedAnswerId) {
    this.acceptedAnswerId = acceptedAnswerId;
  }

  public XMLGregorianCalendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(XMLGregorianCalendar creationDate) {
    this.creationDate = creationDate;
  }

  public XMLGregorianCalendar getClosedDate() {
    return closedDate;
  }

  public void setClosedDate(XMLGregorianCalendar closedDate) {
    this.closedDate = closedDate;
  }

  public XMLGregorianCalendar getCommunityOwnedDate() {
    return communityOwnedDate;
  }

  public void setCommunityOwnedDate(XMLGregorianCalendar communityOwnedDate) {
    this.communityOwnedDate = communityOwnedDate;
  }

  public XMLGregorianCalendar getLastEditDate() {
    return lastEditDate;
  }

  public void setLastEditDate(XMLGregorianCalendar lastEditDate) {
    this.lastEditDate = lastEditDate;
  }

  public Integer getLastEditorUserId() {
    return lastEditorUserId;
  }

  public void setLastEditorUserId(Integer lastEditorUserId) {
    this.lastEditorUserId = lastEditorUserId;
  }

  public XMLGregorianCalendar getLastActivityDate() {
    return lastActivityDate;
  }

  public void setLastActivityDate(XMLGregorianCalendar lastActivityDate) {
    this.lastActivityDate = lastActivityDate;
  }

  public Integer getAnswerCount() {
    return answerCount;
  }

  public void setAnswerCount(Integer answerCount) {
    this.answerCount = answerCount;
  }

  public Integer getFavoriteCount() {
    return favoriteCount;
  }

  public void setFavoriteCount(Integer favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public Integer getViewCount() {
    return viewCount;
  }

  public void setViewCount(Integer viewCount) {
    this.viewCount = viewCount;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

}
