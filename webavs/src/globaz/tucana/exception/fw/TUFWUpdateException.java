package globaz.tucana.exception.fw;

/**
 * Exception levée lors de problèmes dans une instruction sql UPDATE
 * 
 * @author fgo
 * 
 */
public class TUFWUpdateException extends TUFWException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUFWUpdateException() {
    }

    public TUFWUpdateException(String _message) {
        super(_message);
    }

    public TUFWUpdateException(String _message, String _error) {
        super(_message, _error);
    }

    public TUFWUpdateException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

}
