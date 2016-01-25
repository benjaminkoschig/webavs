package globaz.tucana.exception.transform;

import globaz.tucana.exception.TUException;

public class TUDomReaderException extends TUException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String error = null;
    protected String message = null;

    public TUDomReaderException() {
        super();
    }

    public TUDomReaderException(String _message) {
        message = _message;

    }

    public TUDomReaderException(String _message, String _error) {
        message = _message;
        error = _error;

    }

    public TUDomReaderException(String _message, Throwable _throwable) {
        message = _message;
        error = _throwable.toString();
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n ERREUR : Problème lors d'une lecture d'un fichier xml en DOM");
        str.append("\n Erreur : ").append(str);
        str.append("\n Message : ").append(error);
        str.append("\n ------------------------------------------------ \n");
        return str.toString();
    }
}
