package ch.globaz.common.sftp.exception;

public class SFtpPermissionException extends RuntimeException {
    public SFtpPermissionException() {
    }

    public SFtpPermissionException(String message) {
        super(message);
    }

    public SFtpPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SFtpPermissionException(Throwable cause) {
        super(cause);
    }

    protected SFtpPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
