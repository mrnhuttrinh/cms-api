package com.ecash.cmsapi.exception;

import com.ecash.ecashcore.exception.EcashException;

public class CmsException extends EcashException {
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 4364327459838519857L;

  public CmsException() {
    super();
  }

  public CmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public CmsException(String message, Throwable cause) {
    super(message, cause);
  }

  public CmsException(String message) {
    super(message);
  }

  public CmsException(Throwable cause) {
    super(cause);
  }

}
