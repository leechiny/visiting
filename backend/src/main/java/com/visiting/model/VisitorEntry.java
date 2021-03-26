package com.visiting.model;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class VisitorEntry {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private Timestamp timestamp;

  @ManyToOne(cascade = CascadeType.ALL)
  private Visitor visitor;

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public Visitor getVisitor() {
    return visitor;
  }

  public void setVisitor(Visitor visitor) {
    this.visitor = visitor;
  }
}
