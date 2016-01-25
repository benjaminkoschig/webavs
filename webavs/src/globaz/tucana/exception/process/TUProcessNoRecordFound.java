package globaz.tucana.exception.process;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Permet de lever une exception si aucun enregistrement n'est trouvé
 * 
 * @author fgo date de création : 6 juil. 06
 * @version : version 1.0
 * 
 */
public class TUProcessNoRecordFound extends TUProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construteur
     */
    public TUProcessNoRecordFound() {
        super();
    }

    /**
     * Construteur
     * 
     * @param _message
     */
    public TUProcessNoRecordFound(String _message) {
        super(_message);
    }

    /**
     * Construteur
     * 
     * @param _message
     * @param _error
     */
    public TUProcessNoRecordFound(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * Construteur
     * 
     * @param _message
     * @param _throwable
     */
    public TUProcessNoRecordFound(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (!JadeStringUtil.isEmpty(getMessage())) {
            str = new StringBuffer("\n ------------------------------------------------ \n");
            str.append("\n Aucun enregistrement trouvé");
            str.append("\n Message : ").append(getMessage());
            str.append("\n ------------------------------------------------ \n");
        }
        if (!JadeStringUtil.isEmpty(getError())) {
            str.append("\n Erreur : ").append(getError());
        }
        return str.toString();
    }
}
