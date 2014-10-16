package edu.hawaii.senin.stack.persistence;

import org.joda.time.LocalDateTime;

public class PreseriesEvent {
  private Integer seriesId;
  private Character tag;
  private LocalDateTime date;
  private Integer hour;
  private Integer counter;

  public Integer getSeriesId() {
    return seriesId;
  }

  public void setSeriesId(Integer seriesId) {
    this.seriesId = seriesId;
  }

  public Character getTag() {
    return tag;
  }

  public void setTag(Character tag) {
    this.tag = tag;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public Integer getHour() {
    return hour;
  }

  public void setHour(Integer hour) {
    this.hour = hour;
  }

  public Integer getCounter() {
    return counter;
  }

  public void setCounter(Integer counter) {
    this.counter = counter;
  }

}
