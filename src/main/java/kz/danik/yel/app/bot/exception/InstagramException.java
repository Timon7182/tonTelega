package kz.danik.yel.app.bot.exception;


import kz.danik.yel.app.bot.ErrorType;

import java.io.IOException;

public class InstagramException extends IOException {
	
    private ErrorType errorType;

    public InstagramException() {
    }

    public InstagramException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " : " + getErrorType();
    }
}
