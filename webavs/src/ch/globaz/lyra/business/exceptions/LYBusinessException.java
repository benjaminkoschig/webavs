package ch.globaz.lyra.business.exceptions;

public class LYBusinessException extends LYException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LYBusinessException() {
        this("");
    }

    public LYBusinessException(String message) {
        super(message);
    }

    public LYBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
