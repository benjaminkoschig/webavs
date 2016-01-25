/**
 * Exception mère des exceptions levées dans le package vb
 */
package globaz.corvus.vb.exception;

/**
 * @author FGO
 * 
 */
public class REViewBeanException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REViewBeanException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REViewBeanException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REViewBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REViewBeanException(Throwable cause) {
        super(cause);
    }
}
