package ch.globaz.amal.business.exceptions.models;

public class AmalRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 772865193507598344L;

    public AmalRuntimeException() {
        super();
    }

    public AmalRuntimeException(String message) {
        super(message);
    }

    public AmalRuntimeException(Throwable cause) {
        super(cause);
    }

    public AmalRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
