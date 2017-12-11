package com.ecash.cmsapi.vo;

public class LoginVO {

  private String username;
  private String password;
  private String language;

  public LoginVO(String username, String password, String language) {
    super();
    this.username = username;
    this.password = password;
    this.language = language;
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

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
