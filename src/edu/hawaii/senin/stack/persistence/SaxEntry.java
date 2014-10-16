package edu.hawaii.senin.stack.persistence;

import org.joda.time.LocalDateTime;

public class SaxEntry {
  private Integer seriesId;
  private LocalDateTime date;
  private String sax_string;

  public Integer getSeriesId() {
    return seriesId;
  }

  public void setSeriesId(Integer seriesId) {
    this.seriesId = seriesId;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getSax_string() {
    return sax_string;
  }

  public void setSax_string(String sax_string) {
    this.sax_string = sax_string;
  }

}
