package globaz.corvus.utils.pmt.mensuel;

/**
 * 
 * @author SCR
 * 
 */
public class REEcritureUtil {

    public long idOperation;
    // Chaque �criture est li�e � un RA. on stocke donc l'id de cette RA dans
    // l'�criture.
    // Utilis� pour le traitement des erreurs, le cas �ch�ants.
    public String idRA = "";
    public String idRubrique = "";
    public String libelle = "";

    public String montant = "";

}
