package com.visiting.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class VisitorEntry {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private String hashNRIC;
  private Timestamp timestamp;

  public String getHashNRIC() {
    return hashNRIC;
  }

  public void setHashNRIC(String hashNRIC) {
    this.hashNRIC = hashNRIC;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}
