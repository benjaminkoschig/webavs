/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.decisions;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeMoy5_6_7;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * 
 * @author fha
 */
public class RFDecisionJointDemandeJointMotifRefusJointTiersValidation extends RFDecision {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_DOSSIER_DEM = "dossierDemande";
    public static final String ALIAS_PR_DEMANDE_DEM = "prDemandeDemande";

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
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String as = " AS ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);

        /************* Faire aussi la jointure sur le gestionnaire *******/

        // jointure entre la table des décisions et la table des qds
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_QD_PRINICIPALE);

        // jointure entre la table des décisions et la table des
        // RFM accordées
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        // jointure entre la table des RFM accordées et REPRACC (rentes)
        fromClauseBuffer.append(leftJoin);
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

        // jointure entre la table des décisions et la table des demandes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        // jointure entre la table des demandes et la table des demandes de moyens auxiliaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeMoy5_6_7.FIELDNAME_ID_DEMANDE_MOYENS_AUX);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);

        // jointure entre la table des sous types de soin et la table des demandes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);

        // jointure entre la table des sous types de soin et la table des types de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table des demandes et la table association demandes - motifs de refus
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);

        // jointure entre la table association motifs de refus et motifs de refus
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);

        // jointure entre la table des décisions et la table des copies
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFCopieDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFCopieDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFCopieDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        // MODIFICATIONS STARTS HERE

        // jointure entre la table des décisions et la table association dossiers-décisions
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        // jointure entre la table association dossiers-décisions et la table dossiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des demandes prestation
        fromClauseBuffer.append(leftJoin);
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
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des personnes et la table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des dossiers et la table des gestionnaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_GESTIONNAIRE);

        // jointure entre la table des dossiers*bis et la table des demandes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.ALIAS_DOSSIER_DEM);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.ALIAS_DOSSIER_DEM);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des PRDemandes et la table des dossiers*bis
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.ALIAS_PR_DEMANDE_DEM);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.ALIAS_PR_DEMANDE_DEM);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.ALIAS_DOSSIER_DEM);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        return fromClauseBuffer.toString();
    }

    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDebutTraitementDemande = "";
    private String dateDeces = "";
    private String dateFacture = "";
    private String dateFinTraitementDemande = "";
    private String dateNaissance = "";
    private String dateReceptionDemande = "";

    private String descriptionMotifRefusDe = "";
    private String descriptionMotifRefusFr = "";
    private String descriptionMotifRefusIt = "";
    private transient String fromClause = null;
    private String genrePrestation = "";
    private Boolean hasAnnexes = Boolean.FALSE;

    private Boolean hasCopies = Boolean.FALSE;

    private Boolean hasDecompte = Boolean.FALSE;
    private Boolean hasMoyensDroit = Boolean.FALSE;
    private Boolean hasPageGarde = Boolean.FALSE;
    private Boolean hasRemarques = Boolean.FALSE;
    private Boolean hasSignature = Boolean.FALSE;
    private Boolean hasVersement = Boolean.FALSE;

    private String idCopie = "";
    private String idDemande = "";
    private String idDemandeParent = "";
    private String idFournisseur = "";
    private String idMotifRefus = "";
    private String idRubriqueAI = "";
    private String idRubriqueAVS = "";
    private String idRubriqueInvalidite = "";
    private String idSousTypeSoin = "";
    private String idTiers = "";
    private String idTiersAssureConcerne = "";
    private String idTiersCopie = "";
    private String idTypeSoin = "";
    private Boolean isForcerPayement = Boolean.FALSE;
    private Boolean isPP = Boolean.FALSE;
    private Boolean isTexteRedirection = Boolean.FALSE;
    private String montantAccepte = "";
    // private String montantAcceptePeriodeDeTraitement = "";
    private String montantFacture = "";
    private String montantMensuel = "";
    private String montantMotifRefus = "";
    private String montantMotifRefusOAI = "";
    // private String montantPeriodeDeTraitement = "";
    private String motifRefusSysteme = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    private String referencePaiement = "";
    private String remarqueFournisseur = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDecisionJointDemandeJointMotifRefusJointTiersValidation.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        nss = NSUtil.formatAVSUnknown(statement
                .dbReadString(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_NUM_AVS));
        dateNaissance = statement
                .dbReadDateAMJ(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_DATENAISSANCE);
        dateDeces = statement
                .dbReadDateAMJ(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_SEXE);
        nom = statement.dbReadString(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_NOM);
        prenom = statement.dbReadString(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_PRENOM);

        idTiers = statement
                .dbReadString(RFDecisionJointDemandeJointMotifRefusJointTiersValidation.FIELDNAME_ID_TIERS_TI);
        idDemande = statement.dbReadString(RFDemande.FIELDNAME_ID_DEMANDE);
        idDemandeParent = statement.dbReadNumeric(RFDemande.FIELDNAME_ID_DEMANDE_PARENT);
        idMotifRefus = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
        motifRefusSysteme = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS_SYSTEME);
        descriptionMotifRefusFr = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_FR);
        descriptionMotifRefusIt = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_LONGUE_IT);
        descriptionMotifRefusDe = statement.dbReadString(RFMotifsDeRefus.FIELDNAME_DESCRIPTION_DE);
        dateReceptionDemande = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_RECEPTION);
        dateDebutTraitementDemande = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        dateFinTraitementDemande = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT);
        dateFacture = statement.dbReadDateAMJ(RFDemande.FIELDNAME_DATE_FACTURE);
        idSousTypeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        idTypeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        idFournisseur = statement.dbReadString(RFDemande.FIELDNAME_ID_FOURNISSEUR);
        montantMotifRefus = statement.dbReadString(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS);
        montantMotifRefusOAI = statement.dbReadNumeric(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI);

        idTiersAssureConcerne = statement
                .dbReadString(RFDecisionJointDemandeJointMotifRefusJointTiersValidationManager.ALIAS_CHAMP_TIERS_DEMANDE);

        idCopie = statement.dbReadString(RFCopieDecision.FIELDNAME_ID_COPIE);
        idTiersCopie = statement.dbReadString(RFCopieDecision.FIELDNAME_ID_TIERS);
        hasPageGarde = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_PAGE_GARDE);
        hasVersement = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_VERSEMENT);
        hasDecompte = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_DECOMPTE);
        hasRemarques = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_REMARQUE);
        hasMoyensDroit = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_MOYEN_DROIT);
        hasSignature = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_SIGNATURE);
        hasAnnexes = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_ANNEXES);
        hasCopies = statement.dbReadBoolean(RFCopieDecision.FIELDNAME_HAS_COPIES);

        idRubriqueAI = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AI);
        idRubriqueAVS = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AVS);
        idRubriqueInvalidite = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_INVALIDITE);

        isPP = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_PP);
        isForcerPayement = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT);
        isTexteRedirection = statement.dbReadBoolean(RFDemande.FIELDNAME_IS_TEXTE_REDIRECTION);

        montantFacture = statement.dbReadString(RFDemande.FIELDNAME_MONTANT_FACTURE);
        montantAccepte = statement.dbReadString(RFDemande.FIELDNAME_MONTANT_A_PAYER);
        montantMensuel = statement.dbReadString(RFDemande.FIELDNAME_MONTANT_MENSUEL);

        genrePrestation = statement.dbReadNumeric(RFQdPrincipale.FIELDNAME_CS_TYPE_PC_ACCORDEE);
        referencePaiement = statement.dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);
        remarqueFournisseur = statement.dbReadString(RFDemande.FIELDNAME_REMARQUE_FOURNISSEUR);
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

    public String getDateDebutTraitementDemande() {
        return dateDebutTraitementDemande;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateFinTraitementDemande() {
        return dateFinTraitementDemande;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateReceptionDemande() {
        return dateReceptionDemande;
    }

    public String getDescriptionMotifRefusDe() {
        return descriptionMotifRefusDe;
    }

    public String getDescriptionMotifRefusFr() {
        return descriptionMotifRefusFr;
    }

    public String getDescriptionMotifRefusIt() {
        return descriptionMotifRefusIt;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public Boolean getHasAnnexes() {
        return hasAnnexes;
    }

    public Boolean getHasCopies() {
        return hasCopies;
    }

    public Boolean getHasDecompte() {
        return hasDecompte;
    }

    public Boolean getHasMoyensDroit() {
        return hasMoyensDroit;
    }

    public Boolean getHasPageGarde() {
        return hasPageGarde;
    }

    public Boolean getHasRemarques() {
        return hasRemarques;
    }

    public Boolean getHasSignature() {
        return hasSignature;
    }

    public Boolean getHasVersement() {
        return hasVersement;
    }

    public String getIdCopie() {
        return idCopie;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDemandeParent() {
        return idDemandeParent;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdMotifRefus() {
        return idMotifRefus;
    }

    public String getIdRubriqueAI() {
        return idRubriqueAI;
    }

    public String getIdRubriqueAVS() {
        return idRubriqueAVS;
    }

    public String getIdRubriqueInvalidite() {
        return idRubriqueInvalidite;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAssureConcerne() {
        return idTiersAssureConcerne;
    }

    public String getIdTiersCopie() {
        return idTiersCopie;
    }

    public String getIdTypeSoin() {
        return idTypeSoin;
    }

    public Boolean getIsForcerPayement() {
        return isForcerPayement;
    }

    public Boolean getIsPP() {
        return isPP;
    }

    public String getMontantAccepte() {
        return montantAccepte;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getMontantMotifRefus() {
        return montantMotifRefus;
    }

    public String getMontantMotifRefusOAI() {
        return montantMotifRefusOAI;
    }

    public String getMotifRefusSysteme() {
        return motifRefusSysteme;
    }

    // public String getMontantAcceptePeriodeDeTraitement() {
    // return this.montantAcceptePeriodeDeTraitement;
    // }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    // public String getMontantPeriodeDeTraitement() {
    // return this.montantPeriodeDeTraitement;
    // }

    public String getPrenom() {
        return prenom;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public String getRemarqueFournisseur() {
        return remarqueFournisseur;
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

    public void setDateDebutTraitementDemande(String dateDebutTraitementDemande) {
        this.dateDebutTraitementDemande = dateDebutTraitementDemande;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateFinTraitementDemande(String dateFinTraitementDemande) {
        this.dateFinTraitementDemande = dateFinTraitementDemande;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateReceptionDemande(String dateReceptionDemande) {
        this.dateReceptionDemande = dateReceptionDemande;
    }

    public void setDescriptionMotifRefusDe(String descriptionMotifRefusDe) {
        this.descriptionMotifRefusDe = descriptionMotifRefusDe;
    }

    public void setDescriptionMotifRefusFr(String descriptionMotifRefusFr) {
        this.descriptionMotifRefusFr = descriptionMotifRefusFr;
    }

    public void setDescriptionMotifRefusIt(String descriptionMotifRefusIt) {
        this.descriptionMotifRefusIt = descriptionMotifRefusIt;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setHasAnnexes(Boolean hasAnnexes) {
        this.hasAnnexes = hasAnnexes;
    }

    public void setHasCopies(Boolean hasCopies) {
        this.hasCopies = hasCopies;
    }

    public void setHasDecompte(Boolean hasDecompte) {
        this.hasDecompte = hasDecompte;
    }

    public void setHasMoyensDroit(Boolean hasMoyensDroit) {
        this.hasMoyensDroit = hasMoyensDroit;
    }

    public void setHasPageGarde(Boolean hasPageGarde) {
        this.hasPageGarde = hasPageGarde;
    }

    public void setHasRemarques(Boolean hasRemarques) {
        this.hasRemarques = hasRemarques;
    }

    public void setHasSignature(Boolean hasSignature) {
        this.hasSignature = hasSignature;
    }

    public void setHasVersement(Boolean hasVersement) {
        this.hasVersement = hasVersement;
    }

    public void setIdCopie(String idCopie) {
        this.idCopie = idCopie;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDemandeParent(String idDemandeParent) {
        this.idDemandeParent = idDemandeParent;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdMotifRefus(String idMotifRefus) {
        this.idMotifRefus = idMotifRefus;
    }

    public void setIdRubriqueAI(String idRubriqueAI) {
        this.idRubriqueAI = idRubriqueAI;
    }

    public void setIdRubriqueAVS(String idRubriqueAVS) {
        this.idRubriqueAVS = idRubriqueAVS;
    }

    public void setIdRubriqueInvalidite(String idRubriqueInvalidite) {
        this.idRubriqueInvalidite = idRubriqueInvalidite;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAssureConcerne(String idTiersAssureConcerne) {
        this.idTiersAssureConcerne = idTiersAssureConcerne;
    }

    public void setIdTiersCopie(String idTiersCopie) {
        this.idTiersCopie = idTiersCopie;
    }

    public void setIdTypeSoin(String idTypeSoin) {
        this.idTypeSoin = idTypeSoin;
    }

    public void setIsForcerPayement(Boolean isForcerPayement) {
        this.isForcerPayement = isForcerPayement;
    }

    public void setIsPP(Boolean isPP) {
        this.isPP = isPP;
    }

    // public void setMontantAcceptePeriodeDeTraitement(String montantAcceptePeriodeDeTraitement) {
    // this.montantAcceptePeriodeDeTraitement = montantAcceptePeriodeDeTraitement;
    // }

    public void setMontantAccepte(String montantAccepte) {
        this.montantAccepte = montantAccepte;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    // public void setMontantPeriodeDeTraitement(String montantPeriodeDeTraitement) {
    // this.montantPeriodeDeTraitement = montantPeriodeDeTraitement;
    // }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setMontantMotifRefus(String montantMotifRefus) {
        this.montantMotifRefus = montantMotifRefus;
    }

    public void setMontantMotifRefusOAI(String montantMotifRefusOAI) {
        this.montantMotifRefusOAI = montantMotifRefusOAI;
    }

    public void setMotifRefusSysteme(String motifRefusSysteme) {
        this.motifRefusSysteme = motifRefusSysteme;
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

    public void setRemarqueFournisseur(String remarqueFournisseur) {
        this.remarqueFournisseur = remarqueFournisseur;
    }

    public Boolean getIsTexteRedirection() {
        return isTexteRedirection;
    }

    public void setIsTexteRedirection(Boolean isTexteRedirection) {
        this.isTexteRedirection = isTexteRedirection;
    }

}
