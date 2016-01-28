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
public class RFQdPrincipaleJointDossier extends RFQdPrincipale {

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

        // jointure entre la table des Qd et la table des QdPrinicpale

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des QdPrinicpale et la table Association
        // Qd,dossier

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssQdDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
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

        // jointure entre la table des demandes PR et la table des numeros AVS
        // fromClauseBuffer.append(innerJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_AVS);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(PRDemande.TABLE_NAME);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_AVS);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.FIELDNAME_ID_TIERS_TI);
        //
        // jointure entre la table des numeros AVS et la table des personnes
        // fromClauseBuffer.append(innerJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_PERSONNE);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_AVS);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.FIELDNAME_ID_TIERS_TI);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_PERSONNE);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.FIELDNAME_ID_TIERS_TI);
        //
        // jointure entre la table des personnes et la table des tiers
        // fromClauseBuffer.append(innerJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_TIERS);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_PERSONNE);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.FIELDNAME_ID_TIERS_TI);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.TABLE_TIERS);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(RFQdPrincipaleJointDossier.FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    private String csEtatDossier = "";
    private String csSource = "";
    private String csTypeRelation = "";

    private String dateDebut = "";
    private String dateFin = "";
    private String etat = "";
    private transient String fromClause = null;
    private String idAssQdPrincipaleDossier = "";
    private String idDemande = "";
    private String idDossier = "";

    private String idGestionnaire = "";
    private String idPrDem = "";
    private String idQdDossier = "";
    private String idQdPrincipale = "";
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
            fromClause = RFQdPrincipaleJointDossier.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idAssQdPrincipaleDossier = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER);
        idQdPrincipale = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_QD);
        idQdDossier = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_DOSSIER);

        idDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_DOSSIER);
        dateDebut = statement.dbReadDateAMJ(RFDossier.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFDossier.FIELDNAME_DATE_FIN);
        csSource = statement.dbReadNumeric(RFDossier.FIELDNAME_CS_SOURCE);
        csEtatDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_CS_ETAT_DOSSIER);
        idGestionnaire = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_GESTIONNAIRE);
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

    @Override
    public String getCsSource() {
        return csSource;
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

    public String getIdAssQdPrincipaleDossier() {
        return idAssQdPrincipaleDossier;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPrDem() {
        return idPrDem;
    }

    public String getIdQdDossier() {
        return idQdDossier;
    }

    @Override
    public String getIdQdPrincipale() {
        return idQdPrincipale;
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

    @Override
    public void setCsSource(String csSource) {
        this.csSource = csSource;
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

    public void setIdAssQdPrincipaleDossier(String idAssQdPrincipaleDossier) {
        this.idAssQdPrincipaleDossier = idAssQdPrincipaleDossier;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    @Override
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPrDem(String idPrDem) {
        this.idPrDem = idPrDem;
    }

    public void setIdQdDossier(String idQdDossier) {
        this.idQdDossier = idQdDossier;
    }

    @Override
    public void setIdQdPrincipale(String idQdPrincipale) {
        this.idQdPrincipale = idQdPrincipale;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
