package ch.globaz.common.file.exception;

public class FSOperationException extends RuntimeException {
    public FSOperationException() {
    }

    public FSOperationException(String message) {
        super(message);
    }

    public FSOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FSOperationException(Throwable cause) {
        super(cause);
    }

    protected FSOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
