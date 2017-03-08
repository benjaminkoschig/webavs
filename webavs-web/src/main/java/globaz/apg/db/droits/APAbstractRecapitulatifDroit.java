/*
 * Créé le 30 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.prestation.db.demandes.PRDemande;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public abstract class APAbstractRecapitulatifDroit extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected static StringBuffer createFromBase(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);

        // jointure entre table des demandes et table des droits
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDEMANDE);

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer;
    }

    /** DOCUMENT ME! */
    protected String dateDebutDroit = "";

    /** DOCUMENT ME! */
    protected transient JAVector employeurs = null;

    /** DOCUMENT ME! */
    protected String etat = "";

    /** DOCUMENT ME! */
    protected transient String fromClause = null;

    /** DOCUMENT ME! */
    protected String genreService = "";

    /** DOCUMENT ME! */
    protected String idDroit = "";

    /** DOCUMENT ME! */
    protected String noAVS = "";

    /** DOCUMENT ME! */
    protected String noDroit = "";

    /** DOCUMENT ME! */
    protected String nom = "";

    /** DOCUMENT ME! */
    protected String idTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    protected String prenom = "";

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        // charge les employeurs
        APSitProJointEmployeurManager sMgr = new APSitProJointEmployeurManager();

        sMgr.setSession(getSession());
        sMgr.setForIdDroit(idDroit);
        sMgr.find(transaction);

        employeurs = sMgr.getContainer();
    }

    /**
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitLAPG.TABLE_NAME_LAPG;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        noDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_NODROIT);
        etat = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_ETAT);
        genreService = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_GENRESERVICE);
        dateDebutDroit = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_NOM);
        prenom = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_PRENOM);
        idTiers = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitLAPG.FIELDNAME_IDDROIT_LAPG,
                _dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * getter pour l'attribut employeurs
     * 
     * @return la valeur courante de l'attribut employeurs
     */
    public List getEmployeurs() {
        return employeurs;
    }

    /**
     * getter pour l'attribut etat
     * 
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut genre service
     * 
     * @return la valeur courante de l'attribut genre service
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut no droit
     * 
     * @return la valeur courante de l'attribut no droit
     */
    public String getNoDroit() {
        return noDroit;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * getter pour l'attribut modifiable
     * 
     * @return la valeur courante de l'attribut modifiable
     */
    public boolean isModifiable() {
        return IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(etat);
    }

    /**
     * setter pour l'attribut date debut droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroit(String string) {
        dateDebutDroit = string;
    }

    /**
     * setter pour l'attribut etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String string) {
        etat = string;
    }

    /**
     * setter pour l'attribut genre service
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String string) {
        genreService = string;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut no droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoDroit(String string) {
        noDroit = string;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut prenom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @param idTiers the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }
}
