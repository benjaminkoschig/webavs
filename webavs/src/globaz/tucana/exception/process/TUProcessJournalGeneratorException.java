package globaz.tucana.exception.process;

/**
 * Levé en cas de problème sur la génération du journal
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessJournalGeneratorException extends TUProcessException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public TUProcessJournalGeneratorException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param _message
     */
    public TUProcessJournalGeneratorException(String _message) {
        super(_message);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _error
     */
    public TUProcessJournalGeneratorException(String _message, String _error) {
        super(_message, _error);
    }

    /**
     * Constructeur
     * 
     * @param _message
     * @param _throwable
     */
    public TUProcessJournalGeneratorException(String _message, Throwable _throwable) {
        super(_message, _throwable);
    }
}
