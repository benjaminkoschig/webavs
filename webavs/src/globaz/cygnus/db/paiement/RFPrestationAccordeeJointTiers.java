/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author fha
 * @revision JJE -> date échéance
 */
public class RFPrestationAccordeeJointTiers extends RFPrestationAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

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
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";

    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
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
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);

        /************* Faire aussi la jointure sur le gestionnaire *******/
        // jointure entre la table des prestations et la table des prestations des rentes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

        // jointure entre la table des prestations accordées et la table décision
        /*
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDecision.TABLE_NAME); fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDecision.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_DECISION);
         */

        // jointure entre la table des décisions et la table association
        // dossiers-décisions
        /*
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DECISION);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDecision.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);
         */

        // jointure entre la table association dossiers-décisions et la table
        // dossiers
        /*
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDossier.TABLE_NAME); fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDossier.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DOSSIER);
         */

        // jointure entre la table des dossiers et la table des demandes
        // prestation
        /*
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(PRDemande.TABLE_NAME); fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(PRDemande.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFDossier.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);
         */

        // jointure entre la table des demandes et la table des numeros AVS
        /*
         * fromClauseBuffer.append(innerJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_AVS); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(PRDemande.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_AVS); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);
         */

        // jointure entre la table des prestations des rentes et la table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des dossiers et la table des gestionnaires
        /*
         * fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_GESTIONNAIRES); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFDossier.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFDossier.FIELDNAME_ID_GESTIONNAIRE);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationAccordeeJointTiers.TABLE_GESTIONNAIRES); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFPrestationAccordeeJointTiers.FIELDNAME_ID_GESTIONNAIRE);
         */

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csEtatRE = "";
    private String csGenrePrestationAccordee = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDebutDroit = "";
    private String dateDeces = "";
    private String dateEcheance = "";
    private String dateFinDroit = "";
    private String dateNaissance = "";
    private String dateValidationDecision = "";
    private transient String fromClause = null;
    // private String idAdressePaiement = "";
    private String idTiers = "";
    private String montantPrestation = "";

    private String nom = "";
    // champs nécessaires description tiers
    private String nss = "";
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String referencePaiement = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationAccordeeJointTiers.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFPrestationAccordeeJointTiers.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(RFPrestationAccordeeJointTiers.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(RFPrestationAccordeeJointTiers.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFPrestationAccordeeJointTiers.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFPrestationAccordeeJointTiers.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFPrestationAccordeeJointTiers.FIELDNAME_PRENOM);
        idTiers = statement.dbReadString(RFPrestationAccordeeJointTiers.FIELDNAME_ID_TIERS_TI);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);

        // this.idAdressePaiement = statement.dbReadString(RFDecision.FIELDNAME_ID_ADRESSE_PAIEMENT);
        referencePaiement = statement.dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);
        csNationalite = statement.dbReadString(RFPrestationAccordeeJointTiers.FIELDNAME_NATIONALITE);
        csEtatRE = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CS_ETAT);

        csGenrePrestationAccordee = statement.dbReadString(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));

        dateEcheance = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));

    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsEtatRE() {
        return csEtatRE;
    }

    public String getCsGenrePrestationAccordee() {
        return csGenrePrestationAccordee;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    public String getFromClause() {
        return fromClause;
    }

    /*
     * public String getIdAdressePaiement() { return this.idAdressePaiement; }
     */

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantPrestation() {
        return montantPrestation;
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

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsEtatRE(String csEtatRE) {
        this.csEtatRE = csEtatRE;
    }

    public void setCsGenrePrestationAccordee(String csGenrePrestationAccordee) {
        this.csGenrePrestationAccordee = csGenrePrestationAccordee;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateValidationDecision(String dateValidationDecision) {
        this.dateValidationDecision = dateValidationDecision;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    /*
     * public void setIdAdressePaiement(String idAdressePaiement) { this.idAdressePaiement = idAdressePaiement; }
     */

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
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

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

}
