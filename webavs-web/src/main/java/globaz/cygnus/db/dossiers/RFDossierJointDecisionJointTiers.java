/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.db.dossiers;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFDossierJointDecisionJointTiers extends RFDossier {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_DECISION_ASS_DOS_DEC = "ECIDOS";

    public static final String FIELDNAME_ID_DOSSIER_ASS_DOS_DEC = "ECIDEC";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";

    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";

    public static final String TABLE_ASS_DOSSIER_DECISION = "RFADODE";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";

    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers, demandes jusqu'au tiers
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);

        // jointure entre la table des dossiers et la table des demandes
        // prestation
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

        // jointure entre la table des demandes et la table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des dossiers et la table des gestionnaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_GESTIONNAIRE);

        // jointure entre la table des dossiers et la table ass. des décisions
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_ASS_DOSSIER_DECISION);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.TABLE_ASS_DOSSIER_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossierJointDecisionJointTiers.FIELDNAME_ID_DOSSIER_ASS_DOS_DEC);

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private transient String fromClause = null;
    private String idTiers = "";
    private String nom = "";
    // champs nécessaires description tiers
    private String nss = "";

    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // champs nécessaires description gestionnaire
    private String visaGestionnaire = "";

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
            fromClause = RFDossierJointDecisionJointTiers.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * Lecture des propriétés dans les champs de la requête
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFDossierJointDecisionJointTiers.FIELDNAME_NUM_AVS));
        idTiers = statement.dbReadNumeric(RFDossierJointDecisionJointTiers.FIELDNAME_ID_TIERS_TI);
        dateNaissance = statement.dbReadDateAMJ(RFDossierJointDecisionJointTiers.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(RFDossierJointDecisionJointTiers.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFDossierJointDecisionJointTiers.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFDossierJointDecisionJointTiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFDossierJointDecisionJointTiers.FIELDNAME_PRENOM);
        visaGestionnaire = statement.dbReadString(RFDossierJointDecisionJointTiers.FIELDNAME_VISA_GESTIONNAIRE);
        csNationalite = statement.dbReadString(RFDossierJointDecisionJointTiers.FIELDNAME_NATIONALITE);
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }

}
