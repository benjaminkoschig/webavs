package globaz.tucana.exception.fw;

/**
 * Exception levée lors d'un problème sur un BManager.getCount()
 * 
 * @author fgo
 * 
 */
public class TUFWCountException extends TUFWFindException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUFWCountException() {
    }

    public TUFWCountException(String _message) {
        super(_message);
    }

    public TUFWCountException(String _message, String _requeteSql, String _error) {
        super(_message, _requeteSql, _error);
    }

    public TUFWCountException(String _message, String _requeteSql, Throwable _throwable) {
        super(_message, _requeteSql, _throwable);
    }

}
