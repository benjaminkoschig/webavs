package globaz.tucana.exception.fw;

/**
 * Erreur levée en cas de problème sur un retrieve de BEntity
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUFWRetrieveException extends TUFWException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUFWRetrieveException() {
        super();
    }

    /**
     * @param _message
     */
    public TUFWRetrieveException(String _message) {
        super(_message);
    }

    /**
     * @param _message
     * @param _error
     */
    public TUFWRetrieveException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * @param _message
     * @param _throwable
     */
    public TUFWRetrieveException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }
    // /* (non-Javadoc)
    // * @see java.lang.Object#toString()
    // */
    // public String toString() {
    // StringBuffer str = new
    // StringBuffer("\n ------------------------------------------------ \n");
    // str.append("\n ERREUR : Problème lors de la recherche d'un enregistrement");
    // str.append("\n Erreur : ").append(str);
    // str.append("\n Message : ").append(error);
    // str.append("\n ------------------------------------------------ \n");
    // return str.toString();
    // }
}
