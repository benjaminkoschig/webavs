package ch.globaz.eavs.exception;

public class EAVSNoVersionNumberException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EAVSNoVersionNumberException() {
        super();
    }

    public EAVSNoVersionNumberException(String message) {
        super(message);
    }

    public EAVSNoVersionNumberException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EAVSNoVersionNumberException(Throwable cause) {
        super(cause);
    }

}
