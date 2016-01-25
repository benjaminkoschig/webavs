package globaz.tucana.exception.process;

import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.exception.TUException;

/**
 * Levé en cas d'exception sur process
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessException extends TUException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur sans paramètre
     */
    protected TUProcessException() {
        super();
    }

    /**
     * Constructeur en passant l'erreur exeception
     * 
     * @param _message
     */
    public TUProcessException(String _message) {
        super(_message);
    }

    /**
     * Constructeur en passant l'erreur et le message
     * 
     * @param _message
     * @param _error
     */
    public TUProcessException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * Constructeur en passant l'erreur et le throw
     * 
     * @param _message
     * @param _error
     */
    public TUProcessException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.exception.TUException#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (!JadeStringUtil.isEmpty(getMessage())) {
            str = new StringBuffer("\n ------------------------------------------------ \n");
            str.append("\n Problème générale sur un process");
            str.append("\n Exception : " + this.getClass().getName());
            str.append("\n Message : ").append(getMessage());
            str.append("\n ------------------------------------------------ \n");
        }
        if (!JadeStringUtil.isEmpty(getError())) {
            str.append("\n Erreur : ").append(getError());
        }
        return str.toString();
    }

}
