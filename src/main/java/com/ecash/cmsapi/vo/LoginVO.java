package com.ecash.cmsapi.vo;

public class LoginVO {

  private String username;
  private String password;

  public LoginVO(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }

  public LoginVO() {
    super();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
