package globaz.tucana.exception.fw;

/**
 * Erreur levée par le framework en cas de problème lors d'un find()
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUFWFindException extends TUFWException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String requeteSql = null;

    /**
     * Construteur
     */
    public TUFWFindException() {
        super();
    }

    /**
     * Construteur
     * 
     * @param _message
     */
    public TUFWFindException(String _message) {
        super(_message);
    }

    /**
     * Construteur
     * 
     * @param _message
     * @param _requeteSql
     * @param _error
     */
    public TUFWFindException(String _message, String _requeteSql, String _error) {
        super(_message, _error);
        requeteSql = _requeteSql;
    }

    /**
     * Construteur
     * 
     * @param _message
     * @param _requeteSql
     * @param _throwable
     */
    public TUFWFindException(String _message, String _requeteSql, Throwable _throwable) {
        super(_message, _throwable);
        requeteSql = _requeteSql;
    }

    // /* (non-Javadoc)
    // * @see java.lang.Object#toString()
    // */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer(super.toString());
        str.append("\n Requête Sql :").append(requeteSql);
        str.append("\n ------------------------------------------------ \n");
        return str.toString();
    }
    // /* (non-Javadoc)
    // * @see java.lang.Object#toString()
    // */
    // public String toString() {
    // StringBuffer str = new
    // StringBuffer("\n ------------------------------------------------ \n");
    // str.append("\n ERREUR : Problème lors d'un find() sur un manager");
    // str.append("\n Erreur : ").append(str);
    // str.append("\n Requête Sql :").append(requeteSql);
    // str.append("\n Message : ").append(error);
    // str.append("\n ------------------------------------------------ \n");
    // return str.toString();
    // }
}
