package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (04.11.2002 11:49:35)
 * 
 * @author: Administrator
 */
public class HECriteremotif extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (09.01.2003 14:33:42)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
    }

    private String codeApplication = "";
    protected HEMotifcodeapplicationManager codeAppManager = null;
    /** (CRI_IDCRITEREMOTIF - RBICRP) */
    private String cri_idcriteremotif = new String();
    private String fatherId = "";
    /** (IDCRITERE) - RBTCRI */
    private String idcritere = new String();
    /** Fichier HECRMOP */
    /** (IDCRITEREMOTIF) */
    private String idcriteremotif = new String();
    // code systeme
    // private FWParametersSystemCode csIdcritere = null;
    // le niveau
    private String level;
    // pour obtenir des informations dans l'�cran _rc
    private String libelle = "";

    private String motif = "";

    /**
     * Commentaire relatif au constructeur HECriteremotif
     */
    public HECriteremotif() {
        super();
    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la lecture de l'entit� dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        /*
         * if (codeAppManager == null) { codeAppManager = new HEMotifcodeapplicationManager();
         * codeAppManager.setSession(getSession()); } codeAppManager.setForIdCritereMotif(getIdcriteremotif());
         * codeAppManager.find(); int i = codeAppManager.size();
         */
        // le libell� courant
    }

    /**
     * Effectue des traitements apr�s une mise � jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la mise � jour de l'entit� dans la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        /*
         * if (codeAppManager == null) { codeAppManager = new HEMotifcodeapplicationManager();
         * codeAppManager.setSession(getSession()); } codeAppManager.setForIdCritereMotif(getFatherId());
         * codeAppManager.find(); int i = codeAppManager.size();
         */

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HECRMOP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idcriteremotif = statement.dbReadNumeric("RBICRM");
        cri_idcriteremotif = statement.dbReadNumeric("RBICRP");
        idcritere = statement.dbReadNumeric("RBTCRI");
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
        statement.writeKey("RBICRM", _dbWriteNumeric(statement.getTransaction(), getIdcriteremotif(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RBICRM",
                _dbWriteNumeric(statement.getTransaction(), getIdcriteremotif(), "idcriteremotif"));
        statement.writeField("RBICRP",
                _dbWriteNumeric(statement.getTransaction(), getCri_idcriteremotif(), "cri_idcriteremotif"));
        statement.writeField("RBTCRI", _dbWriteNumeric(statement.getTransaction(), getIdcritere(), "idcritere"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 15:20:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCodeApplication() {
        return codeApplication;
    }

    public String getCri_idcriteremotif() {
        return cri_idcriteremotif;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 16:05:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFatherId() {
        return fatherId;
    }

    public String getIdcritere() {
        return idcritere;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdcriteremotif() {
        return idcriteremotif;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.11.2002 14:41:45)
     * 
     * @return int
     */
    public String getLevel() {
        return level;
    }

    public String getLibelle() {
        return libelle;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 15:20:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotif() {
        return motif;
    }

    public HEMotifcodeapplication getMotifcodeapplicationViewBean() {
        HEMotifcodeapplication eb = (HEMotifcodeapplication) codeAppManager.getEntity(0);
        if (eb == null) {
            codeAppManager.setForIdCritereMotif(getFatherId());
            try {
                codeAppManager.find();
                eb = (HEMotifcodeapplication) codeAppManager.getEntity(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return eb;
    }

    public String getParentIdcritere() {
        HECriteremotif parent = new HECriteremotif();
        parent.setIdcriteremotif(getCri_idcriteremotif());
        parent.setSession(getSession());
        try {
            parent.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return parent.getIdcritere();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 15:20:25)
     * 
     * @param newCodeApplication
     *            java.lang.String
     */
    public void setCodeApplication(java.lang.String newCodeApplication) {
        codeApplication = newCodeApplication;
    }

    public void setCri_idcriteremotif(String newCri_idcriteremotif) {
        cri_idcriteremotif = newCri_idcriteremotif;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 16:05:44)
     * 
     * @param newFatherId
     *            java.lang.String
     */
    public void setFatherId(java.lang.String newFatherId) {
        fatherId = newFatherId;
    }

    public void setIdcritere(String newIdcritere) {
        idcritere = newIdcritere;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdcriteremotif(String newIdcriteremotif) {
        idcriteremotif = newIdcriteremotif;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.11.2002 14:41:45)
     * 
     * @param newLevel
     *            int
     */
    public void setLevel(String newLevel) {
        level = newLevel;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.11.2002 15:20:25)
     * 
     * @param newMotif
     *            java.lang.String
     */
    public void setMotif(java.lang.String newMotif) {
        motif = newMotif;
    }
}
