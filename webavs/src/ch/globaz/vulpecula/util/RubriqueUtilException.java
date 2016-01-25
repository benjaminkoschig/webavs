package ch.globaz.vulpecula.util;

/**
 * Exception g�n�r�e par le classe RubriqueUtil
 * 
 * @since WebBMS 1.0
 */
public class RubriqueUtilException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RubriqueUtilException(String message) {
        super(message);
    }

    public RubriqueUtilException(String message, final Throwable cause) {
        super(message, cause);
    }
}
