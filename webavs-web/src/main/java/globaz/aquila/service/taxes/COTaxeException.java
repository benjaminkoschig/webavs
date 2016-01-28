/**
 * 
 */
package globaz.aquila.service.taxes;

/**
 * @author sel
 * 
 */
public class COTaxeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public COTaxeException() {
        super();
    }

    /**
     * @param message
     */
    public COTaxeException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public COTaxeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public COTaxeException(Throwable cause) {
        super(cause);
    }

}
