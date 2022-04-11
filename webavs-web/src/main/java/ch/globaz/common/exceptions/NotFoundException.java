package ch.globaz.common.exceptions;

public class NotFoundException extends RuntimeException {
   public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
