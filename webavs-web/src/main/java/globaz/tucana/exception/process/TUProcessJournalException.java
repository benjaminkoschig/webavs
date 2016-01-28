package globaz.tucana.exception.process;

/**
 * Levée en cas de problème sur la classe TUJournal
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessJournalException extends TUProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public TUProcessJournalException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param _message
     */
    public TUProcessJournalException(String _message) {
        super(_message);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _error
     */
    public TUProcessJournalException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _throwable
     */
    public TUProcessJournalException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }
}
