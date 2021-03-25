package com.visiting.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Visitor {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private String name;
  private String hashNric;
  private String maskedNric;
  private String phoneNo;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHashNric() {
    return hashNric;
  }

  public void setHashNric(String hashNric) {
    this.hashNric = hashNric;
  }

  public String getMaskedNric() {
    return maskedNric;
  }

  public void setMaskedNric(String maskedNric) {
    this.maskedNric = maskedNric;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Visitor that = (Visitor) o;
    return hashNric.equalsIgnoreCase( that.hashNric) &&
      name.equals( that.name ) &&
      maskedNric.equals( that.maskedNric);
  }

  @Override
  public String toString() {
    return "Name = " + name + "\n" +
      "Hash Nric = " + hashNric + "\n" +
      "Masked Nric = " + maskedNric + "\n" +
      "Phone No: " + getPhoneNo();
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }
}
