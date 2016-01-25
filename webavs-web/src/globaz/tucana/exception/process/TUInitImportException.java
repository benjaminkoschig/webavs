package globaz.tucana.exception.process;

import globaz.tucana.exception.TUException;

/**
 * Erreur levée lors de problème sur le process d'importation
 * 
 * @author fgo
 * 
 */
public class TUInitImportException extends TUException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUInitImportException() {
        super();
    }

    public TUInitImportException(String _message) {
        super(_message);
    }

    public TUInitImportException(String _message, String _error) {
        super(_message, _error);
    }

    public TUInitImportException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

}
