package globaz.tucana.exception.process;

/**
 * Problème lors de la création du bouclement suivant
 * 
 * @author fgo
 * 
 */
public class TUCreationBouclementSuivantException extends TUInitExportException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUCreationBouclementSuivantException() {
        super();
    }

    public TUCreationBouclementSuivantException(String _message) {
        super(_message);
    }

    public TUCreationBouclementSuivantException(String _message, String _error) {
        super(_message, _error);
    }

    public TUCreationBouclementSuivantException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }

}
