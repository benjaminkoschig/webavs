package globaz.tucana.exception.fw;

import globaz.tucana.exception.TUException;

/**
 * Exception lev�e en cas de probl�me g�n�ral sur le framework. Classe m�re de toutes les erruers framework
 * 
 * @author fgo date de cr�ation : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUFWException extends TUException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur sans param�tre
     */
    protected TUFWException() {
        super();
    }

    public TUFWException(String _message) {
        super(_message);
    }

    public TUFWException(String _message, String _error) {
        super(_message, _error);
    }

    public TUFWException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n ------------------------------------------------ \n");
        str.append("\n ERREUR : Probl�me g�n�rale du framework");
        str.append("\n Exception : ").append(this.getClass().getName());
        str.append("\n Erreur : ").append(getError());
        str.append("\n Message : ").append(getMessage());
        str.append("\n ------------------------------------------------ \n");
        return str.toString();
    }
}
