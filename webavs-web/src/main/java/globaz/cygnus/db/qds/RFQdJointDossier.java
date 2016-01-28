/*
 * Créé le 21 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.db.dossiers.RFDossier;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFQdJointDossier extends RFQd {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les Qds jusqu'au dossier RFM
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);

        // jointure entre la table des QdPrinicpale et la table Association
        // Qd,dossier

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_QD);

        // jointure entre la table Association Qd,dossier et la table des
        // dossier

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossier et la table des prDemande

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
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        return fromClauseBuffer.toString();
    }

    private String csEtatDossier = "";
    private String csTypeRelation = "";

    private String dateDebut = "";
    private String dateFin = "";
    private String etat = "";
    private transient String fromClause = null;
    private String idAssQdDossier = "";
    private String idDemande = "";
    private String idDossier = "";

    private String idPrDem = "";
    private String idQdDossier = "";
    private String idTiers = "";

    // champs nécessaires description gestionnaire
    // private String visaGestionnaire = "";

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
            fromClause = RFQdJointDossier.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idAssQdDossier = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER);
        idQdDossier = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
        idDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_DOSSIER);
        dateDebut = statement.dbReadDateAMJ(RFDossier.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFDossier.FIELDNAME_DATE_FIN);
        csEtatDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_CS_ETAT_DOSSIER);
        idPrDem = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_PRDEM);
        etat = statement.dbReadNumeric(PRDemande.FIELDNAME_ETAT);
        idDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE);
        idTiers = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        typeDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_TYPE_DEMANDE);
        csTypeRelation = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_TYPE_RELATION);

    }

    public String getCsEtatDossier() {
        return csEtatDossier;
    }

    public String getCsTypeRelation() {
        return csTypeRelation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdAssQdDossier() {
        return idAssQdDossier;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdPrDem() {
        return idPrDem;
    }

    public String getIdQdDossier() {
        return idQdDossier;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsEtatDossier(String csEtatDossier) {
        this.csEtatDossier = csEtatDossier;
    }

    public void setCsTypeRelation(String csTypeRelation) {
        this.csTypeRelation = csTypeRelation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdAssQdDossier(String idAssQdDossier) {
        this.idAssQdDossier = idAssQdDossier;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdPrDem(String idPrDem) {
        this.idPrDem = idPrDem;
    }

    public void setIdQdDossier(String idQdDossier) {
        this.idQdDossier = idQdDossier;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
