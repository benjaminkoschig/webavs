package ch.globaz.common.business.exceptions;

/**
 * Exception racine des exceptions techniques
 * 
 * Les Exceptions techniques de chaque module doivent étendre cette classe
 * 
 * @author MMO
 */
public class CommonTechnicalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CommonTechnicalException(String message) {
        super(message);
    }

    public CommonTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonTechnicalException(Throwable nestedException) {
        super(nestedException);
    }
}
