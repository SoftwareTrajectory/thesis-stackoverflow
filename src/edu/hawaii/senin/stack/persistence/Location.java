package edu.hawaii.senin.stack.persistence;

public class Location {
  private Integer id;
  private String location;
  private String normalized_location;
  private String known_location;
  private String tzname;
  private String gmtoffset;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getNormalized_location() {
    return normalized_location;
  }

  public void setNormalized_location(String normalized_location) {
    this.normalized_location = normalized_location;
  }

  public String getKnown_location() {
    return known_location;
  }

  public void setKnown_location(String known_location) {
    this.known_location = known_location;
  }

  public String getTzname() {
    return tzname;
  }

  public void setTzname(String tzname) {
    this.tzname = tzname;
  }

  public String getGmtoffset() {
    return gmtoffset;
  }

  public void setGmtoffset(String gmtoffset) {
    this.gmtoffset = gmtoffset;
  }

}
