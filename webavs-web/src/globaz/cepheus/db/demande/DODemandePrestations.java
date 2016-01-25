/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.db.demande;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DODemandePrestations extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DATE_NAISSANCE = "HPDNAI";

    /** L'id d'un tiers */
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    /** Le nom d'un tiers */
    public static final String FIELDNAME_NOM = "HTLDE1";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";

    /** Le numero AVS */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    /** Le pernom d'un tiers */
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    /** DOCUMENT ME! */
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    /** Table des personnes AVS */
    public static final String TABLE_AVS = "TIPAVSP";

    /** Table des tiers */
    public static final String TABLE_TIERS = "TITIERP";

    public static final String TABLE_TIERS_DETAIL = "TIPERSP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des tiers et table détail des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csNationalite = "";
    private String csSexe = "";
    private String dateNaissance = "";
    private String etatDemande = "";
    private transient String fromClause = null;
    private String idDemande = "";
    private String idTiers = "";
    private String noAvs = "";
    private String nom = "";
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String typeDemande = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return PRDemande.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE);
        typeDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_TYPE_DEMANDE);
        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        etatDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_ETAT);
        noAvs = NSUtil.formatAVSUnknown(statement.dbReadString(FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(FIELDNAME_SEXE);
        csNationalite = statement.dbReadNumeric(FIELDNAME_NATIONALITE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(PRDemande.FIELDNAME_IDDEMANDE,
                _dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(PRDemande.FIELDNAME_IDDEMANDE,
                _dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(PRDemande.FIELDNAME_TYPE_DEMANDE,
                _dbWriteNumeric(statement.getTransaction(), typeDemande, "typeDemande"));
        statement.writeField(PRDemande.FIELDNAME_IDTIERS,
                _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(PRDemande.FIELDNAME_ETAT,
                _dbWriteNumeric(statement.getTransaction(), etatDemande, "etatDemande"));
        statement.writeField(FIELDNAME_NUM_AVS, _dbWriteAVS(statement.getTransaction(), noAvs, "noAvs"));
        statement.writeField(FIELDNAME_NOM, _dbWriteString(statement.getTransaction(), nom, "nom"));
        statement.writeField(FIELDNAME_PRENOM, _dbWriteString(statement.getTransaction(), prenom, "prenom"));
    }

    /**
     * @return
     */
    public String getCsNationalite() {
        return csNationalite;
    }

    /**
     * @return
     */
    public String getCsSexe() {
        return csSexe;
    }

    /**
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return
     */
    public String getEtatDemande() {
        return etatDemande;
    }

    /**
     * @return
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getNoAvs() {
        return noAvs;
    }

    /**
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * @param string
     */
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    /**
     * @param string
     */
    public void setCsSexe(String string) {
        csSexe = string;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param string
     */
    public void setEtatDemande(String string) {
        etatDemande = string;
    }

    /**
     * @param string
     */
    public void setIdDemande(String string) {
        idDemande = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setNoAvs(String string) {
        noAvs = string;
    }

    /**
     * @param string
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * @param string
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    /**
     * @param string
     */
    public void setTypeDemande(String string) {
        typeDemande = string;
    }

}
