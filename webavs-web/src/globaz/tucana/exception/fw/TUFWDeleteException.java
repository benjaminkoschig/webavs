package globaz.tucana.exception.fw;

/**
 * Erreur lev�e en cas de probl�me lors de la suppression d'un enregistrement
 * 
 * @author fgo date de cr�ation : 28 juin 06
 * @version : version 1.0
 * 
 */
public class TUFWDeleteException extends TUFWException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cosntructeur
     */
    public TUFWDeleteException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param _message
     */
    public TUFWDeleteException(String _message) {
        super(_message);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _error
     */
    public TUFWDeleteException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _throwable
     */
    public TUFWDeleteException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    // public String toString() {
    // StringBuffer str = new
    // StringBuffer("\n ------------------------------------------------ \n");
    // str.append("\n ERREUR : Probl�me lors de la suppression d'un enregistrement");
    // str.append("\n Erreur : ").append(str);
    // str.append("\n Message : ").append(error);
    // str.append("\n ------------------------------------------------ \n");
    // return str.toString();
    // }

}
