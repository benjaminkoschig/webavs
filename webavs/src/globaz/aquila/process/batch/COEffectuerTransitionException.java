/**
 * 
 */
package globaz.aquila.process.batch;

/**
 * @author sel
 * 
 */
public class COEffectuerTransitionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public COEffectuerTransitionException() {
        super();
    }

    /**
     * @param message
     */
    public COEffectuerTransitionException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public COEffectuerTransitionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public COEffectuerTransitionException(Throwable cause) {
        super(cause);
    }

}
