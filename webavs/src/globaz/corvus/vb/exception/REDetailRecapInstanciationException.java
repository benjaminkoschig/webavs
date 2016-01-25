package globaz.corvus.vb.exception;

/**
 * Erreur levée en cas de problème d'instanciation dans le détail récap
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
