package com.ecash.cmsapi.exception;

public class NotAuthenticatedException extends CmsException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6835144709329077012L;
    
    public NotAuthenticatedException() {
        super("Authentication Error");
    }
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
