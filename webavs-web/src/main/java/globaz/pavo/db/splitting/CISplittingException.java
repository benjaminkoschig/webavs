package globaz.pavo.db.splitting;

/**
 * Exception utilis�e pour la partie splitting de l'application, notamment pour indiquer les op�rations non permises sur
 * l'objet m�tier. Date de cr�ation : (22.10.2002 08:01:18)
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
     * Cr�� une exception avec un message d'erreur sp�cifique.
     * 
     * @param s
     *            le message de l'exception
     */
    public CISplittingException(String s) {
        super(s);
    }
}
