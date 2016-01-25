package globaz.tucana.exception.process;

import globaz.tucana.exception.TUException;

/**
 * Erreur levée lors de problème de configuration pour statistique
 * 
 * @author fgo
 * 
 */
public class TUInitStatistiquesConfigException extends TUException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUInitStatistiquesConfigException() {
        super();
    }

    public TUInitStatistiquesConfigException(String _message) {
        super(_message);
    }

    public TUInitStatistiquesConfigException(String _message, String _error) {
        super(_message, _error);
    }

    public TUInitStatistiquesConfigException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

}
