package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.utilities.tstamp.Tstamp;

public class User {

  private Integer id;
  private String displayName;
  private String emailHash;
  private Integer age;
  private String location;
  private Integer locationId;
  private String websiteUrl;
  private String aboutMe;
  private XMLGregorianCalendar creationDate;
  private XMLGregorianCalendar lastAccessDate;
  private Integer reputation;
  private Integer views;
  private Integer upVotes;
  private Integer downVotes;

  public void setAttribute(String attributeName, String attributeValue) throws Exception {
    if ("id".equalsIgnoreCase(attributeName)) {
      this.id = Integer.valueOf(attributeValue);
    }
    else if ("displayName".equalsIgnoreCase(attributeName)) {
      this.displayName = String.valueOf(attributeValue);
    }
    else if ("emailHash".equalsIgnoreCase(attributeName)) {
      this.emailHash = String.valueOf(attributeValue);
    }
    else if ("age".equalsIgnoreCase(attributeName)) {
      this.age = Integer.valueOf(attributeValue);
    }
    else if ("location".equalsIgnoreCase(attributeName)) {
      this.location = String.valueOf(attributeValue);
    }
    else if ("websiteUrl".equalsIgnoreCase(attributeName)) {
      this.websiteUrl = String.valueOf(attributeValue);
    }
    else if ("aboutMe".equalsIgnoreCase(attributeName)) {
      this.aboutMe = String.valueOf(attributeValue);
    }
    else if ("creationDate".equalsIgnoreCase(attributeName)) {
      this.creationDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("lastAccessDate".equalsIgnoreCase(attributeName)) {
      this.lastAccessDate = Tstamp.makeTimestamp(attributeValue);
    }
    else if ("reputation".equalsIgnoreCase(attributeName)) {
      this.reputation = Integer.valueOf(attributeValue);
    }
    else if ("views".equalsIgnoreCase(attributeName)) {
      this.views = Integer.valueOf(attributeValue);
    }
    else if ("views".equalsIgnoreCase(attributeName)) {
      this.upVotes = Integer.valueOf(attributeValue);
    }
    else if ("upVotes".equalsIgnoreCase(attributeName)) {
      this.upVotes = Integer.valueOf(attributeValue);
    }
    else if ("downVotes".equalsIgnoreCase(attributeName)) {
      this.downVotes = Integer.valueOf(attributeValue);
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

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getEmailHash() {
    return emailHash;
  }

  public void setEmailHash(String emailHash) {
    this.emailHash = emailHash;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public XMLGregorianCalendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(XMLGregorianCalendar creationDate) {
    this.creationDate = creationDate;
  }

  public XMLGregorianCalendar getLastAccessDate() {
    return lastAccessDate;
  }

  public void setLastAccessDate(XMLGregorianCalendar lastAccessDate) {
    this.lastAccessDate = lastAccessDate;
  }

  public Integer getReputation() {
    return reputation;
  }

  public void setReputation(Integer reputation) {
    this.reputation = reputation;
  }

  public Integer getViews() {
    return views;
  }

  public void setViews(Integer views) {
    this.views = views;
  }

  public Integer getUpVotes() {
    return upVotes;
  }

  public void setUpVotes(Integer upVotes) {
    this.upVotes = upVotes;
  }

  public Integer getDownVotes() {
    return downVotes;
  }

  public void setDownVotes(Integer downVotes) {
    this.downVotes = downVotes;
  }
}
