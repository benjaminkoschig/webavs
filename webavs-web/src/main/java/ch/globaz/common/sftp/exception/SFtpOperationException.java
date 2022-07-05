package ch.globaz.common.sftp.exception;

public class SFtpOperationException extends RuntimeException {
    public SFtpOperationException() {
        super();
    }

    public SFtpOperationException(String message) {
        super(message);
    }

    public SFtpOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SFtpOperationException(Throwable cause) {
        super(cause);
    }

    protected SFtpOperationException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
