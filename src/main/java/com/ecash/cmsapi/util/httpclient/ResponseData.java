package com.ecash.cmsapi.util.httpclient;

public class ResponseData {

  private int responseCode;
  
  private String responseMessage;
  
  private String responseBody;

  public ResponseData() {
    super();
  }

  public ResponseData(int responseCode, String responseMessage, String responseBody) {
    super();
    this.responseCode = responseCode;
    this.responseMessage = responseMessage;
    this.responseBody = responseBody;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public String getResponseMessage() {
    return responseMessage;
  }

  public void setResponseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }
}
