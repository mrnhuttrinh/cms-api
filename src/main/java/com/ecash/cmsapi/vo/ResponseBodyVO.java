package com.ecash.cmsapi.vo;

import java.io.Serializable;

public class ResponseBodyVO implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -4433563809343271505L;

  private int code;
  private String status;
  private String message;
  private Object data;

  public ResponseBodyVO() {
    super();
  }

  public ResponseBodyVO(int code, String status, String message, Object data) {
    super();
    this.code = code;
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

}
