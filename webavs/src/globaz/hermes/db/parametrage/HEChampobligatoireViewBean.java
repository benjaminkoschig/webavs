package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (29.11.2002 13:43:21)
 * 
 * @author: ado
 */
public class HEChampobligatoireViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (RDICHA) */
    private String idChampAnnonce = new String();
    /** Fichier HECHOBP */
    /** (RCICHO) */
    private String idChampObligatoire = new String();
    /** (RBICRM) */
    private String idCritereMotif = new String();

    // code systeme
    /**
     * Commentaire relatif au constructeur HEChampobligatoireViewBean
     */
    public HEChampobligatoireViewBean() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entit� dans la BD
     * <p>
     * L'ex�cution de l'ajout n'est pas effectu�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        setIdChampObligatoire(_incCounter(transaction, ""));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HECHOBP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idChampObligatoire = statement.dbReadNumeric("RCICHO", 0);
        idCritereMotif = statement.dbReadNumeric("RBICRM", 0);
        idChampAnnonce = statement.dbReadNumeric("RDICHA", 0);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RCICHO",
                _dbWriteNumeric(statement.getTransaction(), getIdChampObligatoire(), "idChampObligatoire"));
        statement.writeField("RBICRM",
                _dbWriteNumeric(statement.getTransaction(), getIdCritereMotif(), "idCritereMotif"));
        statement.writeField("RDICHA",
                _dbWriteNumeric(statement.getTransaction(), getIdChampAnnonce(), "idChampAnnonce"));
    }

    public String getIdChampAnnonce() {
        return idChampAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdChampObligatoire() {
        return idChampObligatoire;
    }

    public String getIdCritereMotif() {
        return idCritereMotif;
    }

    public void setIdChampAnnonce(String newIdChampAnnonce) {
        idChampAnnonce = newIdChampAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdChampObligatoire(String newIdChampObligatoire) {
        idChampObligatoire = newIdChampObligatoire;
    }

    public void setIdCritereMotif(String newIdCritereMotif) {
        idCritereMotif = newIdCritereMotif;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toMyString() {
        return idChampObligatoire + " - " + idCritereMotif + " - " + idChampAnnonce;
    }
}
