/*
 * Créé le 1 février 2010
 */
package globaz.cygnus.db.qds;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFQdJointDossierJointTiersJointDemande extends RFQdPrincipale {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_DECISION_ASS_DOS_DEC = "ECIDOS";

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "WAVDEV";
    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > des Qds jusqu'au tiers
     * 
     * @param schema
     * @return la clause from
     */
    public static final String createFromClause(String schema, boolean hasIdDemande) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String or = " OR ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);

        // jointure entre la table des Qds et la table des Augmentation de Qd
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdAugmentation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdAugmentation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdAugmentation.FIELDNAME_ID_QD);

        // jointure entre la table des Qds et la table des soldes de charge de Qd
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdSoldeCharge.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdSoldeCharge.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdSoldeCharge.FIELDNAME_ID_QD);

        // jointure entre la table des Qds et la table des QdAssures

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdAssure.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQd.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQd.FIELDNAME_ID_QD);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdAssure.FIELDNAME_ID_QD_ASSURE);

        // jointure entre la table table des QdAssures et la table des sousTypes

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdAssure.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);

        // jointure entre la table des types de soin et la table des sous type
        // de soin

        fromClauseBuffer.append(leftJoin);
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

        // jointure entre la table des Qds et la table des QdPrinicpales

        fromClauseBuffer.append(leftJoin);
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

        // jointure entre la table des QdPrincipales et la table des périodes de
        // validités

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);

        // jointure entre la table des QdBase et la table Association Qd,dossier

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

        // jointure entre la table Association Qd, dossier et la table des
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

        // jointure entre la table des dossiers et la table des prDemande

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
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);

        if (hasIdDemande) {
            // jointure entre la table des des QdPrincipales et des demandes
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFDemande.TABLE_NAME);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFDemande.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);

            // jointure entre la table des des QdAssures et des demandes
            fromClauseBuffer.append(or);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFQdAssure.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFQdAssure.FIELDNAME_ID_QD_ASSURE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFDemande.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFDemande.FIELDNAME_ID_QD_ASSURE);

        }

        // jointure entre la table des dossiers et la table assosiation dossier
        // decision

        /*
         * fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DOSSIER);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDossier.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
         */

        // jointure entre la table association dossier decision et la table des
        // decisions

        /*
         * fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDecision.TABLE_NAME); fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDecision.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION); fromClauseBuffer.append(egal);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DECISION);
         */

        return fromClauseBuffer.toString();
    }

    private String augmentationDeQd = "";
    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String csTypeModificationAugmentationQd = "";
    private String csTypeModificationPeriode = "";
    private String csTypeModificationSoldeCharge = "";
    private String dateDebutGrandeQd = "";
    private String dateDebutPetiteQd = "";
    private String dateDeces = "";
    private String dateFinGrandeQd = "";
    private String dateFinPetiteQd = "";
    private String dateNaissance = "";
    private String idAugmentationQd = "";
    private String idDemande = "";
    private String idDossier = "";
    private String idFamilleAugmentationDeQd = "";
    private String idFamillePeriodeGrandeQd = "";
    private String idFamilleSoldeDeCharge = "";
    private String idPeriodeGrandeQd = "";
    private String idSoldeCharge = "";
    private String idTiers = "";
    private String nom = "";
    private String nss = "";
    private String numeroDecision = "";
    private String prenom = "";

    private String qdAssureCodeSousTypeDeSoin = "";
    private String qdAssureCodeTypeDeSoin = "";
    private String qdAssureIdSousTypeDeSoin = "";
    private String qdAssureIdTypeDeSoin = "";

    private String soldeDeCharge = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
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
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_DOSSIER);
        idDemande = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE);
        numeroDecision = statement.dbReadNumeric(RFDecision.FIELDNAME_NUMERO_DECISION);
        qdAssureCodeSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        qdAssureCodeTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        qdAssureIdSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        qdAssureIdTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        idTiers = statement.dbReadNumeric(RFQdJointDossierJointTiersJointDemande.FIELDNAME_ID_TIERS_TI);
        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFQdJointDossierJointTiersJointDemande.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(RFQdJointDossierJointTiersJointDemande.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(RFQdJointDossierJointTiersJointDemande.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFQdJointDossierJointTiersJointDemande.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFQdJointDossierJointTiersJointDemande.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFQdJointDossierJointTiersJointDemande.FIELDNAME_PRENOM);
        csNationalite = statement.dbReadString(RFQdJointDossierJointTiersJointDemande.FIELDNAME_NATIONALITE);
        dateDebutGrandeQd = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
        dateDebutPetiteQd = statement.dbReadDateAMJ(RFQdAssure.FIELDNAME_DATE_DEBUT);
        dateFinGrandeQd = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
        dateFinPetiteQd = statement.dbReadDateAMJ(RFQdAssure.FIELDNAME_DATE_FIN);
        augmentationDeQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_MONTANT_AUGMENTATION_QD);
        soldeDeCharge = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_MONTANT_SOLDE);
        idPeriodeGrandeQd = statement.dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        idAugmentationQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD);
        idSoldeCharge = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE);
        csTypeModificationPeriode = statement.dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION);
        csTypeModificationSoldeCharge = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_TYPE_MODIF);
        csTypeModificationAugmentationQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_TYPE_MODIF);
        idFamilleAugmentationDeQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION);
        idFamillePeriodeGrandeQd = statement
                .dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION);
        idFamilleSoldeDeCharge = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION);

    }

    public String getAugmentationDeQd() {
        return augmentationDeQd;
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

    public String getCsTypeModificationAugmentationQd() {
        return csTypeModificationAugmentationQd;
    }

    public String getCsTypeModificationPeriode() {
        return csTypeModificationPeriode;
    }

    public String getCsTypeModificationSoldeCharge() {
        return csTypeModificationSoldeCharge;
    }

    public String getDateDebutGrandeQd() {
        return dateDebutGrandeQd;
    }

    public String getDateDebutPetiteQd() {
        return dateDebutPetiteQd;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFinGrandeQd() {
        return dateFinGrandeQd;
    }

    public String getDateFinPetiteQd() {
        return dateFinPetiteQd;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getIdAugmentationQd() {
        return idAugmentationQd;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdFamilleAugmentationDeQd() {
        return idFamilleAugmentationDeQd;
    }

    public String getIdFamillePeriodeGrandeQd() {
        return idFamillePeriodeGrandeQd;
    }

    public String getIdFamilleSoldeDeCharge() {
        return idFamilleSoldeDeCharge;
    }

    public String getIdPeriodeGrandeQd() {
        return idPeriodeGrandeQd;
    }

    public String getIdSoldeCharge() {
        return idSoldeCharge;
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

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getQdAssureCodeSousTypeDeSoin() {
        return qdAssureCodeSousTypeDeSoin;
    }

    public String getQdAssureCodeTypeDeSoin() {
        return qdAssureCodeTypeDeSoin;
    }

    public String getQdAssureIdSousTypeDeSoin() {
        return qdAssureIdSousTypeDeSoin;
    }

    public String getQdAssureIdTypeDeSoin() {
        return qdAssureIdTypeDeSoin;
    }

    public String getSoldeDeCharge() {
        return soldeDeCharge;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setAugmentationDeQd(String augmentationDeQd) {
        this.augmentationDeQd = augmentationDeQd;
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

    public void setCsTypeModificationAugmentationQd(String csTypeModificationAugmentationQd) {
        this.csTypeModificationAugmentationQd = csTypeModificationAugmentationQd;
    }

    public void setCsTypeModificationPeriode(String csTypeModificationPeriode) {
        this.csTypeModificationPeriode = csTypeModificationPeriode;
    }

    public void setCsTypeModificationSoldeCharge(String csTypeModificationSoldeCharge) {
        this.csTypeModificationSoldeCharge = csTypeModificationSoldeCharge;
    }

    public void setDateDebutGrandeQd(String dateDebutGrandeQd) {
        this.dateDebutGrandeQd = dateDebutGrandeQd;
    }

    public void setDateDebutPetiteQd(String dateDebutPetiteQd) {
        this.dateDebutPetiteQd = dateDebutPetiteQd;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFinGrandeQd(String dateFinGrandeQd) {
        this.dateFinGrandeQd = dateFinGrandeQd;
    }

    public void setDateFinPetiteQd(String dateFinPetiteQd) {
        this.dateFinPetiteQd = dateFinPetiteQd;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdAugmentationQd(String idAugmentationQd) {
        this.idAugmentationQd = idAugmentationQd;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdFamilleAugmentationDeQd(String idFamilleAugmentationDeQd) {
        this.idFamilleAugmentationDeQd = idFamilleAugmentationDeQd;
    }

    public void setIdFamillePeriodeGrandeQd(String idFamillePeriodeQd) {
        idFamillePeriodeGrandeQd = idFamillePeriodeQd;
    }

    public void setIdFamilleSoldeDeCharge(String idFamilleSoldeDeCharge) {
        this.idFamilleSoldeDeCharge = idFamilleSoldeDeCharge;
    }

    public void setIdPeriodeGrandeQd(String idPeriodeGrandeQd) {
        this.idPeriodeGrandeQd = idPeriodeGrandeQd;
    }

    public void setIdSoldeCharge(String idSoldeCharge) {
        this.idSoldeCharge = idSoldeCharge;
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

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setQdAssureCodeSousTypeDeSoin(String qdAssureCodeSousTypeDeSoin) {
        this.qdAssureCodeSousTypeDeSoin = qdAssureCodeSousTypeDeSoin;
    }

    public void setQdAssureCodeTypeDeSoin(String qdAssureCodeTypeDeSoin) {
        this.qdAssureCodeTypeDeSoin = qdAssureCodeTypeDeSoin;
    }

    public void setQdAssureIdSousTypeDeSoin(String qdAssureIdSousTypeDeSoin) {
        this.qdAssureIdSousTypeDeSoin = qdAssureIdSousTypeDeSoin;
    }

    public void setQdAssureIdTypeDeSoin(String qdAssureIdTypeDeSoin) {
        this.qdAssureIdTypeDeSoin = qdAssureIdTypeDeSoin;
    }

    public void setSoldeDeCharge(String soldeDeCharge) {
        this.soldeDeCharge = soldeDeCharge;
    }

}