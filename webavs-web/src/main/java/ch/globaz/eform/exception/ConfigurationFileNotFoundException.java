package ch.globaz.eform.exception;

public class ConfigurationFileNotFoundException extends RuntimeException {
    public ConfigurationFileNotFoundException() {
    }

    public ConfigurationFileNotFoundException(String message) {
        super(message);
    }

    public ConfigurationFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationFileNotFoundException(Throwable cause) {
        super(cause);
    }

    public ConfigurationFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
