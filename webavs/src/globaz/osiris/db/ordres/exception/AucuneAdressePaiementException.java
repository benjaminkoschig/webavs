package globaz.osiris.db.ordres.exception;

/**
 * Exception lev�e si aucune adresse de paiement n'est trouv�e � la g�n�ration d'un fichier DTA
 * 
 * @since WebBMS 1.0
 */
public class AucuneAdressePaiementException extends Exception {
    private static final long serialVersionUID = 2946469480683889680L;

    public AucuneAdressePaiementException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public AucuneAdressePaiementException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public AucuneAdressePaiementException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public AucuneAdressePaiementException(Throwable cause) {
        super(cause);
    }

}
