package globaz.corvus.utils.pmt.mensuel;

/**
 * 
 * @author SCR
 * 
 */
public class REEcritureUtil {

    public long idOperation;
    // Chaque écriture est liée à un RA. on stocke donc l'id de cette RA dans
    // l'écriture.
    // Utilisé pour le traitement des erreurs, le cas échéants.
    public String idRA = "";
    public String idRubrique = "";
    public String libelle = "";

    public String montant = "";

}
