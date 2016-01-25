package globaz.corvus.db.decisions;

import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.math.BigDecimal;
import ch.globaz.corvus.business.services.models.decisions.DecisionService;
import ch.globaz.corvus.domaine.constantes.EtatDecisionRente;
import ch.globaz.corvus.domaine.constantes.TypeDecisionRente;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.corvus.domaine.constantes.TypeTraitementDecisionRente;
import ch.globaz.prestation.domaine.CodePrestation;
import ch.globaz.prestation.domaine.constantes.EtatPrestationAccordee;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

/**
 * Entité utilisée pour le service {@link DecisionService}
 */
public class REDecisionJointOrdresVersements extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_CID_OV_COMPENSE = "cid_ov_comp";
    public static final String ALIAS_CID_OV_PONCTIONNE = "cid_ov_ponc";
    public static final String ALIAS_CODE_PRESTATION_PRESTATION_ACCORDEE_DECISION = "prac_dec_codeprest";
    public static final String ALIAS_CODE_PRESTATION_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "prac_ov_codeprest";
    public static final String ALIAS_CS_ETAT_PRESTATION_ACCORDEE_DECISION = "prac_dec_csetat";
    public static final String ALIAS_CS_ETAT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "prac_ov_csetat";
    public static final String ALIAS_ID_CID_COMPENSE = "id_cid_comp";
    public static final String ALIAS_ID_CID_PONCTIONNE = "id_cid_ponc";
    public static final String ALIAS_ID_COMPTE_ANNEXE_PRESTATION_ACCORDEE_DECISION = "id_ca_prac_dec";
    public static final String ALIAS_ID_COMPTE_ANNEXE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "id_ca_prac_ov";
    public static final String ALIAS_ID_OV_COMPENSE = "id_ov_cid_comp";
    public static final String ALIAS_ID_OV_PONCTIONNE = "id_ov_cid_ponc";
    public static final String ALIAS_ID_PRESTATION_ACCORDEE_DECISION = "prac_dec_id";
    public static final String ALIAS_ID_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "prac_ov_id";
    public static final String ALIAS_ID_TIERS_ADRESSE_PAIEMENT_PRESTATION_ACCORDEE_DECISION = "id_tadpmt_prac_dec";
    public static final String ALIAS_ID_TIERS_ADRESSE_PAIEMENT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "id_tadpmt_prac_ov";
    public static final String ALIAS_ID_TIERS_BENEFICIAIRE_DECISION = "ti_dec_id";
    public static final String ALIAS_ID_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "ti_prac_dec_id";
    public static final String ALIAS_ID_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "ti_prac_ov_id";
    public static final String ALIAS_MOIS_DEBUT_PRESTATION_ACCORDEE_DECISION = "prac_dec_moisdeb";
    public static final String ALIAS_MOIS_DEBUT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "prac_ov_moisdeb";
    public static final String ALIAS_MOIS_FIN_PRESTATION_ACCORDEE_DECISION = "prac_dec_moisfin";
    public static final String ALIAS_MOIS_FIN_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "prac_ov_moisfin";
    public static final String ALIAS_MONTANT_CID_COMPENSE = "mont_cid_comp";
    public static final String ALIAS_MONTANT_CID_PONCTIONNE = "mont_cid_ponc";
    public static final String ALIAS_MONTANT_PRESTATION_ACCORDEE_DECISION = "prac_dec_montant";
    public static final String ALIAS_MONTANT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "prac_ov_montant";
    public static final String ALIAS_NOM_TIERS_BENEFICIAIRE_DECISION = "ti_dec_nom";
    public static final String ALIAS_NOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "ti_prac_dec_nom";
    public static final String ALIAS_NOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "ti_prac_ov_nom";
    public static final String ALIAS_NSS_TIERS_BENEFICIAIRE_DECISION = "pavs_dec_nss";
    public static final String ALIAS_NSS_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "pavs_pra_nss";
    public static final String ALIAS_NSS_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "pavs_prac_ov_nss";
    public static final String ALIAS_PRENOM_TIERS_BENEFICIAIRE_DECISION = "ti_dec_prenom";
    public static final String ALIAS_PRENOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "ti_prac_dec_prenom";
    public static final String ALIAS_PRENOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "ti_prac_ov_prenom";
    public static final String ALIAS_REFERENCE_PAIEMENT_PRESTATION_ACCORDEE_DECISION = "ref_pmt_prac_dec";
    public static final String ALIAS_REFERENCE_PAIEMENT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "ref_pmt_prac_ov";
    public static final String ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_DECISION = "inf_com_prac_dec";
    public static final String ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "inf_com_prac_ov";
    public static final String ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_DECISION = "pavs_dec";
    public static final String ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "pavs_pra";
    public static final String ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "pavs_prac_ov";
    public static final String ALIAS_TABLE_PERSONNE_BENEFICIAIRE_DECISION = "pers_dec";
    public static final String ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "pers_pra";
    public static final String ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "pers_prac_ov";
    public static final String ALIAS_TABLE_PRESTATION_ACCORDE_DECISION = "prac_dec";
    public static final String ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT = "prac_ov";
    public static final String ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION = "ti_dec";
    public static final String ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION = "ti_prac_dec";
    public static final String ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT = "ti_prac_ov";

    private String adresseEmailDecision;
    private boolean afficherTexteAnnulerDecision;
    private boolean afficherTexteAvecBonneFoi;
    private boolean afficherTexteAvecIncarceration;
    private boolean afficherTexteInteretMoratoire;
    private boolean afficherTexteObligationPayerCotisation;
    private boolean afficherTexteReductionPourPlafonnement;
    private boolean afficherTexteRemariageRenteDeSurvivant;
    private boolean afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    private boolean afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    private boolean afficherTexteRenteDeVeufLimitee;
    private boolean afficherTexteRenteDeVeuveLimitee;
    private boolean afficherTexteRentePourEnfant;
    private boolean afficherTexteRenteReduitePourSurassurance;
    private boolean afficherTexteSansBonneFoi;
    private boolean afficherTexteSupplementVeuve;
    private CodePrestation codePrestationPrestationAccordeeDecision;
    private CodePrestation codePrestationPrestationAccordeeOrdreVersement;
    private String dateDecision;
    private String datePreparationDecision;
    private String dateValidationDecision;
    private EtatDecisionRente etatDecision;
    private EtatPrestationAccordee etatPrestationAccordeeDecision;
    private EtatPrestationAccordee etatPrestationAccordeeOrdreVersement;
    private String gestionnairePreprationDecision;
    private String gestionnaireTraitementDecision;
    private String gestionnaireValidationDecision;
    private Long idAutreOrdreVersementCIDCompensant;
    private Long idAutreOrdreVersementCIDPonctionnant;
    private Long idCIDCompensant;
    private Long idCIDPonctionant;
    private Long idCompteAnnexePrestationAccordeeDecision;
    private Long idCompteAnnexePrestationAccordeeOrdreVersement;
    private Long idDecision;
    private Long idDemandeRente;
    private Long idOrdreVersement;
    private Long idPrestation;
    private Long idPrestationAccordeeDecision;
    private Long idPrestationAccordeeOrdreVersement;
    private Long idPrestationVerseeDue;
    private Long idRenteVerseeATort;
    private Long idTiersAdressePaiementPrestationAccordeeDecision;
    private Long idTiersAdressePaiementPrestationAccordeeOrdreVersement;
    private Long idTiersBeneficiairePrestationAccordeeDecision;
    private Long idTiersBeneficiairePrestationAccordeeOrdreVersement;
    private Long idTiersBeneficiairePrincipalDecision;
    private boolean isCompensationInterDecision;
    private boolean isCompense;
    private String moisDebutPrestationAccordeeDecision;
    private String moisDebutPrestationAccordeeOrdreVersement;
    private String moisDebutRetroDecision;
    private String moisDecisionDepuis;
    private String moisFinPrestationAccordeeDecision;
    private String moisFinPrestationAccordeeOrdreVersement;
    private String moisFinRetroDecision;
    private BigDecimal montantCompense;
    private BigDecimal montantCompenseCID;
    private BigDecimal montantDette;
    private BigDecimal montantPonctionneCID;
    private BigDecimal montantPrestation;
    private BigDecimal montantPrestationAccordeeDecision;
    private BigDecimal montantPrestationAccordeeOrdreVersement;
    private String nomBeneficiairePrestationAccordeeDecision;
    private String nomBeneficiairePrestationAccordeeOrdreVersement;
    private String nomBeneficiairePrincipalDecision;
    private NumeroSecuriteSociale nssBeneficiairePrestationAccordeeDecision;
    private NumeroSecuriteSociale nssBeneficiairePrestationAccordeeOrdreVersement;
    private NumeroSecuriteSociale nssBeneficiairePrincipalDecision;
    private String prenomBeneficiairePrestationAccordeeDecision;
    private String prenomBeneficiairePrestationAccordeeOrdreVersement;
    private String prenomBeneficiairePrincipalDecision;
    private String referencePourLePaiementPrestationAccordeeDecision;
    private String referencePourLePaiementPrestationAccordeeOrdreVersement;
    private String remarqueDecision;
    private TypeDecisionRente typeDecision;
    private TypeOrdreVersement typeOrdreVersement;
    private TypeRenteVerseeATort typeRenteVerseeATort;
    private TypeTraitementDecisionRente typeTraitementDecision;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(" DISTINCT ");

        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;

        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_DEBUT_RETRO).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_DECISION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_FIN_RETRO).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_PREPARATION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_VALIDATION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DECISION_DEPUIS).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ETAT).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_EMAIL_ADRESSE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_GENRE_DECISION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DECISION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_AVEC_BONNE_FOI).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_INTERET_MORATOIRE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_ANNULE_DECISION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_RED_PLAFOND).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_REMARIAGE_RENTE_SURVIVANT)
                .append(",");
        sql.append(tableDecision).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE).append(",");
        sql.append(tableDecision).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_RENTE_ENFANT).append(",");
        sql.append(tableDecision).append(".")
                .append(REDecisionEntity.FIELDNAME_IS_REM_RENTE_REDUITE_POUR_SURASSURANCEE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUF_LIMITEE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUVE_LIMITEE).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_REM_SUPP_VEUF).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_SANS_BONNE_FOI).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_IS_TEXTE_OBLIG_PAYER_COTI).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_PREPARE_PAR).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_REMARQUE_DECISION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_TRAITER_PAR).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_TYPE_DECISION).append(",");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_VALIDE_PAR).append(",");

        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_PRESTATION).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_MONTANT_PRESTATION).append(",");

        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT)
                .append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_IS_COMPENSE).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_IS_COMPENSATION_INTER_DECISION)
                .append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_MONTANT).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_MONTANT_DETTE).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_TYPE).append(",");

        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT).append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_CID_OV_COMPENSE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_MONTANT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MONTANT_CID_COMPENSE).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_CID_OV_PONCTIONNE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_MONTANT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MONTANT_CID_PONCTIONNE).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_CID_OV_COMPENSE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_ID_COMP_INTER_DEC).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_CID_COMPENSE).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_CID_OV_PONCTIONNE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_ID_COMP_INTER_DEC).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_CID_PONCTIONNE).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_CID_OV_COMPENSE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_ID_ORDRE_VERSEMENT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_OV_PONCTIONNE).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_CID_OV_PONCTIONNE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_ID_OV_COMPENSATION).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_OV_COMPENSE).append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION).append(".")
                .append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_BENEFICIAIRE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_NOM_TIERS_BENEFICIAIRE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION).append(".")
                .append(ITITiersDefTable.DESIGNATION_2).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_PRENOM_TIERS_BENEFICIAIRE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_DECISION).append(".")
                .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_NSS_TIERS_BENEFICIAIRE_DECISION).append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_PRESTATION_ACCORDEE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_CODE_PRESTATION_PRESTATION_ACCORDEE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MONTANT_PRESTATION_ACCORDEE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MOIS_DEBUT_PRESTATION_ACCORDEE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MOIS_FIN_PRESTATION_ACCORDEE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_CS_ETAT_PRESTATION_ACCORDEE_DECISION).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_REFERENCE_PAIEMENT_PRESTATION_ACCORDEE_DECISION)
                .append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_COMPTE_ANNEXE_PRESTATION_ACCORDEE_DECISION)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_ADRESSE_PAIEMENT_PRESTATION_ACCORDEE_DECISION)
                .append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_PRESTATION_ACCORDEE_ORDRE_VERSEMENT).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_CODE_PRESTATION_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MONTANT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MOIS_DEBUT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_MOIS_FIN_PRESTATION_ACCORDEE_ORDRE_VERSEMENT).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_CS_ETAT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT).append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_REFERENCE_PAIEMENT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");

        sql.append(
                REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_COMPTE_ANNEXE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");
        sql.append(
                REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_ADRESSE_PAIEMENT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITITiersDefTable.DESIGNATION_1).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_NOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITITiersDefTable.DESIGNATION_2).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_PRENOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_NSS_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(",");

        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(ITITiersDefTable.ID_TIERS)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(ITITiersDefTable.DESIGNATION_1)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_NOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");
        sql.append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(ITITiersDefTable.DESIGNATION_2)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_PRENOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(",");
        sql.append(
                REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_NSS_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
        String tableCompensationInterDecision = _getCollection()
                + RECompensationInterDecisions.TABLE_NAME_COMP_INTER_DECISION;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableValidationDecision = _getCollection() + REValidationDecisions.TABLE_NAME_VALIDATION_DECISION;
        String tablePrestationVerseeDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;
        String tableInformationComptabilite = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        sql.append(tableDecision);

        sql.append(" INNER JOIN ").append(tablePrestation);
        sql.append(" ON ").append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DECISION).append("=")
                .append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_DECISION);

        sql.append(" INNER JOIN ").append(tableOrdreVersement);
        sql.append(" ON ").append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_PRESTATION)
                .append("=").append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_PRESTATION);

        sql.append(" LEFT OUTER JOIN ").append(tableRenteVerseeATort);
        sql.append(" ON ").append(tableOrdreVersement).append(".")
                .append(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT).append("=").append(tableRenteVerseeATort)
                .append(".").append(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT);

        sql.append(" LEFT OUTER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT);
        sql.append(" ON ").append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT)
                .append("=").append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" LEFT OUTER JOIN ")
                .append(tableInformationComptabilite)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT)
                .append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA)
                .append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".").append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        sql.append(" LEFT OUTER JOIN ")
                .append(tableTiers)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_ORDRE_VERSEMENT)
                .append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE)
                .append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ")
                .append(tablePersonne)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(ITITiersDefTable.ID_TIERS)
                .append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ")
                .append(tablePersonneAvs)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".")
                .append(ITIPersonneDefTable.ID_TIERS)
                .append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)
                .append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableCompensationInterDecision).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_CID_OV_COMPENSE);
        sql.append(" ON ").append(tableOrdreVersement).append(".")
                .append(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_CID_OV_COMPENSE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_ID_OV_COMPENSATION);

        sql.append(" LEFT OUTER JOIN ").append(tableCompensationInterDecision).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_CID_OV_PONCTIONNE);
        sql.append(" ON ").append(tableOrdreVersement).append(".")
                .append(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_CID_OV_PONCTIONNE).append(".")
                .append(RECompensationInterDecisions.FIELDNAME_ID_ORDRE_VERSEMENT);

        sql.append(" INNER JOIN ").append(tableTiers).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION);
        sql.append(" ON ").append(tableDecision).append(".")
                .append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION).append(".")
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_DECISION);
        sql.append(" ON ").append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_DECISION).append(".")
                .append(ITITiersDefTable.ID_TIERS).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_DECISION).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_DECISION);
        sql.append(" ON ").append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_DECISION)
                .append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_DECISION).append(".")
                .append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tableValidationDecision);
        sql.append(" ON ").append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DECISION).append("=")
                .append(tableValidationDecision).append(".").append(REValidationDecisions.FIELDNAME_ID_DECISION);

        sql.append(" INNER JOIN ").append(tablePrestationVerseeDue);
        sql.append(" ON ").append(tableValidationDecision).append(".")
                .append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE).append("=").append(tablePrestationVerseeDue)
                .append(".").append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);

        sql.append(" INNER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION);
        sql.append(" ON ").append(tablePrestationVerseeDue).append(".")
                .append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" INNER JOIN ")
                .append(tableInformationComptabilite)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_DECISION);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION)
                .append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA)
                .append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_INFORMATION_COMPTABILITE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        sql.append(" INNER JOIN ").append(tableTiers).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION);
        sql.append(" ON ").append(REDecisionJointOrdresVersements.ALIAS_TABLE_PRESTATION_ACCORDE_DECISION).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne).append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ")
                .append(tablePersonneAvs)
                .append(" AS ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION);
        sql.append(" ON ")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".")
                .append(ITIPersonneDefTable.ID_TIERS)
                .append("=")
                .append(REDecisionJointOrdresVersements.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION)
                .append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REDecisionEntity.TABLE_NAME_DECISIONS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDecision = Long.parseLong(statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_DECISION));
        idDemandeRente = Long.parseLong(statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE));
        adresseEmailDecision = statement.dbReadString(REDecisionEntity.FIELDNAME_EMAIL_ADRESSE);
        afficherTexteAnnulerDecision = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_ANNULE_DECISION);
        afficherTexteAvecBonneFoi = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_AVEC_BONNE_FOI);
        afficherTexteAvecIncarceration = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REMARQUE_INCARCERATION);
        afficherTexteInteretMoratoire = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_INTERET_MORATOIRE);
        afficherTexteObligationPayerCotisation = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_TEXTE_OBLIG_PAYER_COTI);
        afficherTexteReductionPourPlafonnement = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RED_PLAFOND);
        afficherTexteRemariageRenteDeSurvivant = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_REMARIAGE_RENTE_SURVIVANT);
        afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_DEBUT_DROIT_5ANS_AVANT_DEPOT_DEMANDE);
        afficherTexteRenteAvecMontantMinimumMajoreInvalidite = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_AVEC_MONTANT_MINIMUM_MAJORE_INVALIDITE);
        afficherTexteRenteDeVeufLimitee = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUF_LIMITEE);
        afficherTexteRenteDeVeuveLimitee = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_VEUVE_LIMITEE);
        afficherTexteRentePourEnfant = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_ENFANT);
        afficherTexteRenteReduitePourSurassurance = statement
                .dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_RENTE_REDUITE_POUR_SURASSURANCEE);
        afficherTexteSansBonneFoi = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_SANS_BONNE_FOI);
        afficherTexteSupplementVeuve = statement.dbReadBoolean(REDecisionEntity.FIELDNAME_IS_REM_SUPP_VEUF);
        dateDecision = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_DECISION));
        datePreparationDecision = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_PREPARATION));
        dateValidationDecision = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_VALIDATION));
        gestionnairePreprationDecision = statement.dbReadString(REDecisionEntity.FIELDNAME_PREPARE_PAR);
        gestionnaireTraitementDecision = statement.dbReadString(REDecisionEntity.FIELDNAME_TRAITER_PAR);
        gestionnaireValidationDecision = statement.dbReadString(REDecisionEntity.FIELDNAME_VALIDE_PAR);
        etatDecision = EtatDecisionRente.parse(statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ETAT));
        moisDebutRetroDecision = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_DEBUT_RETRO));
        moisDecisionDepuis = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DECISION_DEPUIS));
        moisFinRetroDecision = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_FIN_RETRO));
        remarqueDecision = statement.dbReadString(REDecisionEntity.FIELDNAME_REMARQUE_DECISION);
        typeDecision = TypeDecisionRente.parse(statement.dbReadNumeric(REDecisionEntity.FIELDNAME_GENRE_DECISION));
        typeTraitementDecision = TypeTraitementDecisionRente.parse(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_TYPE_DECISION));

        idPrestation = Long.parseLong(statement.dbReadNumeric(REPrestations.FIELDNAME_ID_PRESTATION));
        montantPrestation = new BigDecimal(statement.dbReadNumeric(REPrestations.FIELDNAME_MONTANT_PRESTATION));

        idOrdreVersement = Long.parseLong(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT));
        typeOrdreVersement = TypeOrdreVersement.parse(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_TYPE));
        isCompensationInterDecision = statement
                .dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSATION_INTER_DECISION);
        isCompense = statement.dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSE);
        montantCompense = new BigDecimal(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT));
        montantDette = new BigDecimal(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT_DETTE));

        String idRenteVerseeATort = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT);
        if (!JadeStringUtil.isBlankOrZero(idRenteVerseeATort)) {
            this.idRenteVerseeATort = Long.parseLong(idRenteVerseeATort);
            typeRenteVerseeATort = TypeRenteVerseeATort.parse(statement
                    .dbReadNumeric(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT));
        }

        String idCIDCompensant = statement.dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_CID_COMPENSE);
        if (!JadeStringUtil.isBlankOrZero(idCIDCompensant)) {
            this.idCIDCompensant = Long.parseLong(idCIDCompensant);
        }
        String montantCompenseCID = statement.dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_MONTANT_CID_COMPENSE);
        if (!JadeStringUtil.isBlankOrZero(montantCompenseCID)) {
            this.montantCompenseCID = new BigDecimal(montantCompenseCID);
        } else {
            this.montantCompenseCID = BigDecimal.ZERO;
        }

        String idAutreOrdreVersementCIDCompensant = statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_OV_PONCTIONNE);
        if (!JadeStringUtil.isBlankOrZero(idAutreOrdreVersementCIDCompensant)) {
            this.idAutreOrdreVersementCIDCompensant = Long.parseLong(idAutreOrdreVersementCIDCompensant);
        }
        String idCIDPonctionnant = statement.dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_CID_PONCTIONNE);
        if (!JadeStringUtil.isBlankOrZero(idCIDPonctionnant)) {
            idCIDPonctionant = Long.parseLong(idCIDPonctionnant);
        }
        String montantPonctionneCID = statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_MONTANT_CID_PONCTIONNE);
        if (!JadeStringUtil.isBlankOrZero(montantPonctionneCID)) {
            this.montantPonctionneCID = new BigDecimal(montantPonctionneCID);
        } else {
            this.montantPonctionneCID = BigDecimal.ZERO;
        }
        String idAutreOrdreVersementCIDPonctionnant = statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_OV_COMPENSE);
        if (!JadeStringUtil.isBlankOrZero(idAutreOrdreVersementCIDPonctionnant)) {
            this.idAutreOrdreVersementCIDPonctionnant = Long.parseLong(idAutreOrdreVersementCIDPonctionnant);
        }

        idTiersBeneficiairePrincipalDecision = Long.parseLong(statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_BENEFICIAIRE_DECISION));
        nomBeneficiairePrincipalDecision = statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_NOM_TIERS_BENEFICIAIRE_DECISION);
        nssBeneficiairePrincipalDecision = new NumeroSecuriteSociale(
                statement.dbReadString(REDecisionJointOrdresVersements.ALIAS_NSS_TIERS_BENEFICIAIRE_DECISION));
        prenomBeneficiairePrincipalDecision = statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_PRENOM_TIERS_BENEFICIAIRE_DECISION);

        idTiersBeneficiairePrestationAccordeeDecision = Long
                .parseLong(statement
                        .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION));
        nomBeneficiairePrestationAccordeeDecision = statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_NOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION);
        nssBeneficiairePrestationAccordeeDecision = new NumeroSecuriteSociale(
                statement
                        .dbReadString(REDecisionJointOrdresVersements.ALIAS_NSS_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION));
        prenomBeneficiairePrestationAccordeeDecision = statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_PRENOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_DECISION);

        String idTiersBeneficiairePrestationAccordeeOrdreVersement = statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);
        if (!JadeStringUtil.isBlankOrZero(idTiersBeneficiairePrestationAccordeeOrdreVersement)) {
            this.idTiersBeneficiairePrestationAccordeeOrdreVersement = Long
                    .parseLong(idTiersBeneficiairePrestationAccordeeOrdreVersement);
            nomBeneficiairePrestationAccordeeOrdreVersement = statement
                    .dbReadString(REDecisionJointOrdresVersements.ALIAS_NOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);
            nssBeneficiairePrestationAccordeeOrdreVersement = new NumeroSecuriteSociale(
                    statement
                            .dbReadString(REDecisionJointOrdresVersements.ALIAS_NSS_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            prenomBeneficiairePrestationAccordeeOrdreVersement = statement
                    .dbReadString(REDecisionJointOrdresVersements.ALIAS_PRENOM_TIERS_BENEFICIAIRE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);

            idPrestationAccordeeOrdreVersement = Long.parseLong(statement
                    .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            codePrestationPrestationAccordeeOrdreVersement = CodePrestation
                    .getCodePrestation(Integer.parseInt(statement
                            .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_CODE_PRESTATION_PRESTATION_ACCORDEE_ORDRE_VERSEMENT)));
            montantPrestationAccordeeOrdreVersement = new BigDecimal(
                    statement
                            .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_MONTANT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            moisDebutPrestationAccordeeOrdreVersement = PRDateFormater
                    .convertDate_AAAAMM_to_MMxAAAA(statement
                            .dbReadString(REDecisionJointOrdresVersements.ALIAS_MOIS_DEBUT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            moisFinPrestationAccordeeOrdreVersement = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(REDecisionJointOrdresVersements.ALIAS_MOIS_FIN_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            etatPrestationAccordeeOrdreVersement = EtatPrestationAccordee.parse(statement
                    .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_CS_ETAT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            referencePourLePaiementPrestationAccordeeOrdreVersement = statement
                    .dbReadString(REDecisionJointOrdresVersements.ALIAS_REFERENCE_PAIEMENT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT);

            idCompteAnnexePrestationAccordeeOrdreVersement = Long
                    .parseLong(statement
                            .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_COMPTE_ANNEXE_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
            idTiersAdressePaiementPrestationAccordeeOrdreVersement = Long
                    .parseLong(statement
                            .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_ADRESSE_PAIEMENT_PRESTATION_ACCORDEE_ORDRE_VERSEMENT));
        }

        idPrestationAccordeeDecision = Long.parseLong(statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_PRESTATION_ACCORDEE_DECISION));
        codePrestationPrestationAccordeeDecision = CodePrestation.getCodePrestation(Integer.parseInt(statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_CODE_PRESTATION_PRESTATION_ACCORDEE_DECISION)));
        montantPrestationAccordeeDecision = new BigDecimal(
                statement.dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_MONTANT_PRESTATION_ACCORDEE_DECISION));
        moisDebutPrestationAccordeeDecision = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_MOIS_DEBUT_PRESTATION_ACCORDEE_DECISION));
        moisFinPrestationAccordeeDecision = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_MOIS_FIN_PRESTATION_ACCORDEE_DECISION));
        etatPrestationAccordeeDecision = EtatPrestationAccordee.parse(statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_CS_ETAT_PRESTATION_ACCORDEE_DECISION));
        referencePourLePaiementPrestationAccordeeDecision = statement
                .dbReadString(REDecisionJointOrdresVersements.ALIAS_REFERENCE_PAIEMENT_PRESTATION_ACCORDEE_DECISION);

        idCompteAnnexePrestationAccordeeDecision = Long.parseLong(statement
                .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_COMPTE_ANNEXE_PRESTATION_ACCORDEE_DECISION));
        idTiersAdressePaiementPrestationAccordeeDecision = Long
                .parseLong(statement
                        .dbReadNumeric(REDecisionJointOrdresVersements.ALIAS_ID_TIERS_ADRESSE_PAIEMENT_PRESTATION_ACCORDEE_DECISION));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REDecisionEntity.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision.toString(), "idDecision"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAdresseEmailDecision() {
        return adresseEmailDecision;
    }

    public CodePrestation getCodePrestationPrestationAccordeeDecision() {
        return codePrestationPrestationAccordeeDecision;
    }

    public CodePrestation getCodePrestationPrestationAccordeeOrdreVersement() {
        return codePrestationPrestationAccordeeOrdreVersement;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDatePreparationDecision() {
        return datePreparationDecision;
    }

    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    public EtatDecisionRente getEtatDecision() {
        return etatDecision;
    }

    public EtatPrestationAccordee getEtatPrestationAccordeeDecision() {
        return etatPrestationAccordeeDecision;
    }

    public EtatPrestationAccordee getEtatPrestationAccordeeOrdreVersement() {
        return etatPrestationAccordeeOrdreVersement;
    }

    public String getGestionnairePreprationDecision() {
        return gestionnairePreprationDecision;
    }

    public String getGestionnaireTraitementDecision() {
        return gestionnaireTraitementDecision;
    }

    public String getGestionnaireValidationDecision() {
        return gestionnaireValidationDecision;
    }

    public Long getIdAutreOrdreVersementCIDCompensant() {
        return idAutreOrdreVersementCIDCompensant;
    }

    public Long getIdAutreOrdreVersementCIDPonctionnant() {
        return idAutreOrdreVersementCIDPonctionnant;
    }

    public Long getIdCIDCompensant() {
        return idCIDCompensant;
    }

    public Long getIdCIDPonctionant() {
        return idCIDPonctionant;
    }

    public Long getIdCompteAnnexePrestationAccordeeDecision() {
        return idCompteAnnexePrestationAccordeeDecision;
    }

    public Long getIdCompteAnnexePrestationAccordeeOrdreVersement() {
        return idCompteAnnexePrestationAccordeeOrdreVersement;
    }

    public Long getIdDecision() {
        return idDecision;
    }

    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    public Long getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public Long getIdPrestation() {
        return idPrestation;
    }

    public Long getIdPrestationAccordeeDecision() {
        return idPrestationAccordeeDecision;
    }

    public Long getIdPrestationAccordeeOrdreVersement() {
        return idPrestationAccordeeOrdreVersement;
    }

    public Long getIdPrestationVerseeDue() {
        return idPrestationVerseeDue;
    }

    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public Long getIdTiersAdressePaiementPrestationAccordeeDecision() {
        return idTiersAdressePaiementPrestationAccordeeDecision;
    }

    public Long getIdTiersAdressePaiementPrestationAccordeeOrdreVersement() {
        return idTiersAdressePaiementPrestationAccordeeOrdreVersement;
    }

    public Long getIdTiersBeneficiairePrestationAccordeeDecision() {
        return idTiersBeneficiairePrestationAccordeeDecision;
    }

    public Long getIdTiersBeneficiairePrestationAccordeeOrdreVersement() {
        return idTiersBeneficiairePrestationAccordeeOrdreVersement;
    }

    public Long getIdTiersBeneficiairePrincipalDecision() {
        return idTiersBeneficiairePrincipalDecision;
    }

    public String getMoisDebutPrestationAccordeeDecision() {
        return moisDebutPrestationAccordeeDecision;
    }

    public String getMoisDebutPrestationAccordeeOrdreVersement() {
        return moisDebutPrestationAccordeeOrdreVersement;
    }

    public String getMoisDebutRetroDecision() {
        return moisDebutRetroDecision;
    }

    public String getMoisDecisionDepuis() {
        return moisDecisionDepuis;
    }

    public String getMoisFinPrestationAccordeeDecision() {
        return moisFinPrestationAccordeeDecision;
    }

    public String getMoisFinPrestationAccordeeOrdreVersement() {
        return moisFinPrestationAccordeeOrdreVersement;
    }

    public String getMoisFinRetroDecision() {
        return moisFinRetroDecision;
    }

    public BigDecimal getMontantCompense() {
        return montantCompense;
    }

    public BigDecimal getMontantCompenseCID() {
        return montantCompenseCID;
    }

    public BigDecimal getMontantDette() {
        return montantDette;
    }

    public BigDecimal getMontantPonctionneCID() {
        return montantPonctionneCID;
    }

    public BigDecimal getMontantPrestation() {
        return montantPrestation;
    }

    public BigDecimal getMontantPrestationAccordeeDecision() {
        return montantPrestationAccordeeDecision;
    }

    public BigDecimal getMontantPrestationAccordeeOrdreVersement() {
        return montantPrestationAccordeeOrdreVersement;
    }

    public String getNomBeneficiairePrestationAccordeeDecision() {
        return nomBeneficiairePrestationAccordeeDecision;
    }

    public String getNomBeneficiairePrestationAccordeeOrdreVersement() {
        return nomBeneficiairePrestationAccordeeOrdreVersement;
    }

    public String getNomBeneficiairePrincipalDecision() {
        return nomBeneficiairePrincipalDecision;
    }

    public NumeroSecuriteSociale getNssBeneficiairePrestationAccordeeDecision() {
        return nssBeneficiairePrestationAccordeeDecision;
    }

    public NumeroSecuriteSociale getNssBeneficiairePrestationAccordeeOrdreVersement() {
        return nssBeneficiairePrestationAccordeeOrdreVersement;
    }

    public NumeroSecuriteSociale getNssBeneficiairePrincipalDecision() {
        return nssBeneficiairePrincipalDecision;
    }

    public String getPrenomBeneficiairePrestationAccordeeDecision() {
        return prenomBeneficiairePrestationAccordeeDecision;
    }

    public String getPrenomBeneficiairePrestationAccordeeOrdreVersement() {
        return prenomBeneficiairePrestationAccordeeOrdreVersement;
    }

    public String getPrenomBeneficiairePrincipalDecision() {
        return prenomBeneficiairePrincipalDecision;
    }

    public String getReferencePourLePaiementPrestationAccordeeDecision() {
        return referencePourLePaiementPrestationAccordeeDecision;
    }

    public String getReferencePourLePaiementPrestationAccordeeOrdreVersement() {
        return referencePourLePaiementPrestationAccordeeOrdreVersement;
    }

    public String getRemarqueDecision() {
        return remarqueDecision;
    }

    public TypeDecisionRente getTypeDecision() {
        return typeDecision;
    }

    public TypeOrdreVersement getTypeOrdreVersement() {
        return typeOrdreVersement;
    }

    public TypeRenteVerseeATort getTypeRenteVerseeATort() {
        return typeRenteVerseeATort;
    }

    public TypeTraitementDecisionRente getTypeTraitementDecision() {
        return typeTraitementDecision;
    }

    public boolean isAfficherTexteAnnulerDecision() {
        return afficherTexteAnnulerDecision;
    }

    public boolean isAfficherTexteAvecBonneFoi() {
        return afficherTexteAvecBonneFoi;
    }

    public boolean isAfficherTexteAvecIncarceration() {
        return afficherTexteAvecIncarceration;
    }

    public boolean isAfficherTexteInteretMoratoire() {
        return afficherTexteInteretMoratoire;
    }

    public boolean isAfficherTexteObligationPayerCotisation() {
        return afficherTexteObligationPayerCotisation;
    }

    public boolean isAfficherTexteReductionPourPlafonnement() {
        return afficherTexteReductionPourPlafonnement;
    }

    public boolean isAfficherTexteRemariageRenteDeSurvivant() {
        return afficherTexteRemariageRenteDeSurvivant;
    }

    public boolean isAfficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande() {
        return afficherTexteRenteAvecDebutDroit5AnsAvantDepotDemande;
    }

    public boolean isAfficherTexteRenteAvecMontantMinimumMajoreInvalidite() {
        return afficherTexteRenteAvecMontantMinimumMajoreInvalidite;
    }

    public boolean isAfficherTexteRenteDeVeufLimitee() {
        return afficherTexteRenteDeVeufLimitee;
    }

    public boolean isAfficherTexteRenteDeVeuveLimitee() {
        return afficherTexteRenteDeVeuveLimitee;
    }

    public boolean isAfficherTexteRentePourEnfant() {
        return afficherTexteRentePourEnfant;
    }

    public boolean isAfficherTexteRenteReduitePourSurassurance() {
        return afficherTexteRenteReduitePourSurassurance;
    }

    public boolean isAfficherTexteSansBonneFoi() {
        return afficherTexteSansBonneFoi;
    }

    public boolean isAfficherTexteSupplementVeuve() {
        return afficherTexteSupplementVeuve;
    }

    public boolean isCompensationInterDecision() {
        return isCompensationInterDecision;
    }

    public boolean isCompense() {
        return isCompense;
    }
}
