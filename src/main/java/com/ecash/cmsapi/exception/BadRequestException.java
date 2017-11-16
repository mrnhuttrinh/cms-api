package com.ecash.cmsapi.exception;

public class BadRequestException extends CmsException {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -9155977100946281298L;

  public BadRequestException() {
    super();
  }

  public BadRequestException(String message) {
    super(message);
  }
}
