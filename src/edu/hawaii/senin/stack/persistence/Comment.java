package edu.hawaii.senin.stack.persistence;

import javax.xml.datatype.XMLGregorianCalendar;

public class Comment {
  private Integer id;
  private Integer userId;
  private Integer postId;
  private XMLGregorianCalendar creationDate;
  private String text;
  private Integer Score;

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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Integer getScore() {
    return Score;
  }

  public void setScore(Integer score) {
    Score = score;
  }

}
