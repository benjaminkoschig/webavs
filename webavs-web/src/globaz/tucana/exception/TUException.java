package globaz.tucana.exception;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Leve en cas de problème quelconque sur l'application TUCANA
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public abstract class TUException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String error = null;

    private String message = null;

    /**
     * Constructeur sans paramètre
     */
    protected TUException() {
        super();
    }

    /**
     * Constructeur en passant l'erreur exeception
     * 
     * @param _message
     */
    public TUException(String _message) {
        this();
        message = _message;
    }

    /**
     * Constructeur en passant l'erreur et le message
     * 
     * @param _message
     * @param _error
     */
    public TUException(String _message, String _error) {
        this(_message);
        error = _error;
    }

    /**
     * Constructeur en passant l'erreur et le throw
     * 
     * @param _message
     * @param _error
     */
    public TUException(String _message, Throwable _throwable) {
        this(_message);
        error = _throwable.toString();
    }

    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (!JadeStringUtil.isEmpty(getMessage())) {
            str = new StringBuffer("\n ------------------------------------------------ \n");
            str.append("\n ERREUR : Problème générale sur application Tucana : " + this.getClass().getName());
            str.append("\n Message : ").append(getMessage());
            str.append("\n ------------------------------------------------ \n");
        }
        if (!JadeStringUtil.isEmpty(getError())) {
            str.append("\n Erreur : ").append(getError());
        }
        return str.toString();
    }

}
