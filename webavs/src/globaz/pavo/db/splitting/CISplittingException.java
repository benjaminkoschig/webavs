package globaz.pavo.db.splitting;

/**
 * Exception utilisée pour la partie splitting de l'application, notamment pour indiquer les opérations non permises sur
 * l'objet métier. Date de création : (22.10.2002 08:01:18)
 * 
 * @author: dgi
 */
public class CISplittingException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception.
     */
    public CISplittingException() {
        super();
    }

    /**
     * Créé une exception avec un message d'erreur spécifique.
     * 
     * @param s
     *            le message de l'exception
     */
    public CISplittingException(String s) {
        super(s);
    }
}
