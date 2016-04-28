package globaz.naos.exceptions;

/**
 * erreur lors du traitement des annonces entrantes, si l'annonce concerne un numéro ide inexistant parmi nos affilié
 * 
 * @since D0181 (1.16.0)
 * @author cel
 * 
 */
public class AFIdeNumberNoMatchException extends AFBusinessException {

    private static final long serialVersionUID = 1L;

    public AFIdeNumberNoMatchException() {
        super();
    }

    public AFIdeNumberNoMatchException(String message) {
        super(message);
    }

    public AFIdeNumberNoMatchException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

}
