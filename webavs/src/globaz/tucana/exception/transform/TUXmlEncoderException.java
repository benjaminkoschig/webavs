package globaz.tucana.exception.transform;

import globaz.tucana.exception.TUException;

public class TUXmlEncoderException extends TUException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String error = null;
    protected String message = null;

    public TUXmlEncoderException() {
        super();
    }

    public TUXmlEncoderException(String _message) {
        super(_message);
    }

    public TUXmlEncoderException(String _message, String _error) {
        super(_message, _error);
    }

    public TUXmlEncoderException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n ERREUR : Problème lors de l'encodage XML");
        str.append("\n Erreur : ").append(str);
        str.append("\n Message : ").append(error);
        str.append("\n ------------------------------------------------ \n");
        return str.toString();
    }
}
