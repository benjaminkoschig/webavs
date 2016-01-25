package globaz.tucana.exception.fw;

/**
 * Erreur levée en cas de problème lors de l'ajout d'un enregistrement en base de données
 * 
 * @author fgo date de création : 28 juin 06
 * @version : version 1.0
 * 
 */
public class TUFWAddException extends TUFWException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construteur
     */
    public TUFWAddException() {
        super();
    }

    /**
     * Construteur
     * 
     * @param _message
     */
    public TUFWAddException(String _message) {
        super(_message);
    }

    /**
     * Construteur
     * 
     * @param _message
     * @param _error
     */
    public TUFWAddException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * Construteur
     * 
     * @param _message
     * @param _throwable
     */
    public TUFWAddException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }
    // /* (non-Javadoc)
    // * @see java.lang.Object#toString()
    // */
    // public String toString(){
    // StringBuffer str = new
    // StringBuffer("\n ------------------------------------------------ \n");
    // str.append("\n ERREUR : Problème lors de l'ajout d'un enregistrement");
    // str.append("\n Erreur : ").append(str);
    // str.append("\n Message : ").append(error);
    // str.append("\n ------------------------------------------------ \n");
    // return str.toString();
    // }
}
