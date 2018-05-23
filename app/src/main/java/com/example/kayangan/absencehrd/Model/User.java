package com.example.kayangan.absencehrd.Model;


/**
 * Created by KAYANGAN on 2/27/2018.
 */

public class User {
  private String Name;
  private String Password;
  private String Zone;

  public User() {
  }

  public User(String name, String password, String zone) {
    Name = name;
    Password = password;
    Zone = zone;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getPassword() {
    return Password;
  }

  public void setPassword(String password) {
    Password = password;
  }

  public String getZone() {
    return Zone;
  }

  public void setZone(String zone) {
    Zone = zone;
  }
}
