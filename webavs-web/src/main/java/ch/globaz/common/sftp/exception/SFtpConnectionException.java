package ch.globaz.common.sftp.exception;

public class SFtpConnectionException extends RuntimeException{
    public SFtpConnectionException() {
        super();
    }

    public SFtpConnectionException(String message) {
        super(message);
    }

    public SFtpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SFtpConnectionException(Throwable cause) {
        super(cause);
    }

    protected SFtpConnectionException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
