package globaz.tucana.exception.fw;

/**
 * @author fgo
 * 
 *         Cette classe surcharge l'exception du framework lors d'un delete sur une entity
 */
public class TUDeleteException extends TUFWException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construteur
     */
    public TUDeleteException() {
        super();
    }

    /**
     * Construteur
     * 
     * @param _message
     */
    public TUDeleteException(String _message) {
        super(_message);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _error
     */
    public TUDeleteException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * @param _message
     * @param _throwable
     */
    public TUDeleteException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    // /* (non-Javadoc)
    // * @see java.lang.Object#toString()
    // */
    // public String toString() {
    // StringBuffer str = new
    // StringBuffer("\n ------------------------------------------------ \n");
    // str.append("\n ERREUR : Problème lors de la suppression d'un enregistrement");
    // str.append("\n Erreur : ").append(str);
    // str.append("\n Message : ").append(error);
    // str.append("\n ------------------------------------------------ \n");
    // return str.toString();
    // }
}
