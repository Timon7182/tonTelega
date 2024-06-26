package kz.danik.yel.app.bot.exception;


import kz.danik.yel.app.bot.ErrorType;

public class InstagramAuthException extends InstagramException {

    public InstagramAuthException() {
    }

    public InstagramAuthException(String message) {
        super(message, ErrorType.UNKNOWN_ERROR);
    }

    public InstagramAuthException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
