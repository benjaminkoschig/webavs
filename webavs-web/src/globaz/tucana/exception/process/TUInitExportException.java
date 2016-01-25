package globaz.tucana.exception.process;

import globaz.tucana.exception.TUException;

/**
 * Erreur levée lors de problème du process d'exportation
 * 
 * @author fgo
 * 
 */
public class TUInitExportException extends TUException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUInitExportException() {
        super();
    }

    public TUInitExportException(String _message) {
        super(_message);
    }

    public TUInitExportException(String _message, String _error) {
        super(_message, _error);
    }

    public TUInitExportException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

}
