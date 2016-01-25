/*
 * Créé le 07 janvier 2010
 */
package globaz.cygnus.db.demandes;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFTypesDemandeJointDossierJointTiers extends RFDemande {

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
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusqu'au décisions RFM
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
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        // jointure entre la table des demandes RFM et la table des dossiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des demandes maintien à dom. 13 et la table
        // des demandes RFM
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMai13.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMai13.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeMai13.FIELDNAME_ID_DEMANDE_MAINTIEN_DOM_13);

        // jointure entre la table des demandes moyen auxiliaire 5 et la table
        // des demandes RFM
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX);

        // jointure entre la table des demandes devis 19 et la table des
        // demandes RFM
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeDev19.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeDev19.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeDev19.FIELDNAME_ID_DEMANDE_DEVIS_19);

        // jointure entre la table des demandes frqp 17 fra 18 et la table des
        // demandes RFM
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeFrq17Fra18.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeFrq17Fra18.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeFrq17Fra18.FIELDNAME_ID_DEMANDE_1718);

        // jointure entre la table des demandes RFM et la table des sous-type de
        // soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);

        // jointure entre la table des sous-types de soin et la table des types
        // de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table des dossiers et la table des demandes PR
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

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des demandes et la table des gestionnaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_GESTIONNAIRE);

        // jointure entre la table des dossiers et la table ass. des décisions
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ASS_DOSSIER_DECISION);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_ASS_DOSSIER_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_DOSSIER_ASS_DOS_DEC);

        return fromClauseBuffer.toString();
    }

    private String alias_date_demande = "DATE_DEMANDE";
    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDebutTraitement = "";
    private String dateDeces = "";
    private String dateDecisionOAI = "";

    private String dateDecompte = "";
    private String dateDemande = "";
    private String dateEnvoiAcceptation = "";
    private String dateEnvoiMDC = "";
    private String dateFinTraitement = "";
    private String dateNaissance = "";
    private String dateReceptionPreavis = "";

    private transient String fromClause = null;
    private String idSousTypeDeSoin = "";

    private String idTiers = "";
    private String idTypeDeSoin = "";

    private String montantFacture44 = "";
    private String montantVerseOAI = "";

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
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement.dbReadString(FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(FIELDNAME_SEXE);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        visaGestionnaire = statement.dbReadString(FIELDNAME_VISA_GESTIONNAIRE);
        codeSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        codeTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        idSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        idTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        dateDebutTraitement = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        dateFinTraitement = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT);
        dateDecompte = statement.dbReadDateAMJ(RFDemandeFrq17Fra18.FIELDNAME_DATE_DECOMPTE);
        dateDecisionOAI = statement.dbReadDateAMJ(RFDemandeMoy5_6_7.FIELDNAME_DATE_DECISION_OAI);
        dateEnvoiMDC = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDC);
        dateReceptionPreavis = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_RECEPTION_PREAVIS);
        dateEnvoiAcceptation = statement.dbReadDateAMJ(RFDemandeDev19.FIELDNAME_DATE_ENVOI_ACCEPTATION);
        montantVerseOAI = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI, 2);
        montantFacture44 = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_FACTURE_44, 2);
        idTiers = statement.dbReadString(FIELDNAME_ID_TIERS_TI);

        dateDemande = statement.dbReadDateAMJ(alias_date_demande);

    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
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

    @Override
    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateDecisionOAI() {
        return dateDecisionOAI;
    }

    public String getDateDecompte() {
        return dateDecompte;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public String getDateEnvoiAcceptation() {
        return dateEnvoiAcceptation;
    }

    public String getDateEnvoiMDC() {
        return dateEnvoiMDC;
    }

    @Override
    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateReceptionPreavis() {
        return dateReceptionPreavis;
    }

    @Override
    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTypeDeSoin() {
        return idTypeDeSoin;
    }

    public String getMontantFacture44() {
        return montantFacture44;
    }

    public String getMontantVerseOAI() {
        return montantVerseOAI;
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
        return false;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
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

    @Override
    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateDecisionOAI(String dateDecisionOAI) {
        this.dateDecisionOAI = dateDecisionOAI;
    }

    public void setDateDecompte(String dateDecompte) {
        this.dateDecompte = dateDecompte;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }

    public void setDateEnvoiAcceptation(String dateEnvoiAcceptation) {
        this.dateEnvoiAcceptation = dateEnvoiAcceptation;
    }

    public void setDateEnvoiMDC(String dateEnvoiMDC) {
        this.dateEnvoiMDC = dateEnvoiMDC;
    }

    @Override
    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateReceptionPreavis(String dateReceptionPreavis) {
        this.dateReceptionPreavis = dateReceptionPreavis;
    }

    @Override
    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    public void setMontantFacture44(String montantFacture44) {
        this.montantFacture44 = montantFacture44;
    }

    public void setMontantVerseOAI(String montantVerseOAI) {
        this.montantVerseOAI = montantVerseOAI;
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
