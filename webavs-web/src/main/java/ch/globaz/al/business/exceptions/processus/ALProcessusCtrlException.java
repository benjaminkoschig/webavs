package ch.globaz.al.business.exceptions.processus;

/**
 * 
 * Exception gérant les erreurs inhérentes au lancement des traitements (Ex. le traitement ne peut se lancer si son
 * prédécesseur n'est pas terminé)
 * 
 * @author GMO
 * 
 */
public class ALProcessusCtrlException extends ALProcessusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALProcessusException#ALProcessusException()
     */
    public ALProcessusCtrlException() {
        super();

    }

    /**
     * @see ALProcessusException#ALProcessusException(String )
     */
    public ALProcessusCtrlException(String m) {
        super(m);
    }

    /**
     * @see ALProcessusException#ALProcessusException(String, Throwable)
     */

    public ALProcessusCtrlException(String m, Throwable t) {
        super(m, t);

    }

}
