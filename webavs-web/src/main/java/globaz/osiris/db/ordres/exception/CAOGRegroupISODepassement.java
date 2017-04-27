package globaz.osiris.db.ordres.exception;

/**
 * Exception typée utilisé par le processus de regroupement des OV à la création des OG (AttacherOrdre) pour remonter le
 * fait que le dépassement est atteint et déclencher la création de l'OG suivant.
 * 
 * @author cel
 * 
 */
public class CAOGRegroupISODepassement extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 6650500904392575518L;

}
