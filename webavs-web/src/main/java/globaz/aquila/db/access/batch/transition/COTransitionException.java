package globaz.aquila.db.access.batch.transition;

/**
 * <h1>Description</h1>
 * <p>
 * Exception � utiliser en cas de probl�me dans une transition, elle peut contenir l'id du message � afficher �
 * l'utilisateur et la cause de l'ereur.
 * </p>
 * 
 * @author Pascal Lovy, 03-nov-2004
 */
public class COTransitionException extends Exception {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Object[] args;
    /** La cause de l'erreur. */
    private Throwable cause = null;

    /** L'id du message � afficher. */
    private String messageId = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialise l'exception avec la cause de l'erreur.
     * 
     * @param newCause
     *            La cause
     */
    public COTransitionException(Exception newCause) {
        setCause(newCause);
    }

    /**
     * Initialise l'exception avec l'id du message � afficher.
     * 
     * @param newMessageId
     *            L'id du message
     */
    public COTransitionException(String newMessageId, String message) {
        super(message);
        setMessageId(newMessageId);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return le tableau des arguments � remplacer dans le label ou null s'il n'y en a pas.
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    @Override
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return super.getMessage() + (cause != null ? " / " + cause.getMessage() : "");
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param throwable
     *            La nouvelle valeur de la propri�t�
     */
    public void setCause(Throwable throwable) {
        cause = throwable;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setMessageId(String string) {
        messageId = string;
    }

}
