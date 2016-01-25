package globaz.corvus.vb.exception;

/**
 * Erreur lev�e en cas de probl�me d'instanciation dans le d�tail r�cap
 * 
 * @author FGO
 * 
 */
public class REDetailRecapInstanciationException extends REViewBeanException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REDetailRecapInstanciationException() {
    }

    public REDetailRecapInstanciationException(String message) {
        super(message);
    }

    public REDetailRecapInstanciationException(String message, Throwable cause) {
        super(message, cause);
    }

    public REDetailRecapInstanciationException(Throwable cause) {
        super(cause);
    }

}
