package globaz.cygnus.application;

import globaz.cygnus.servlet.IRFActions;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.prestation.application.PRAbstractApplication;
import ch.globaz.jade.process.business.conf.JadeProcessConfManager;

/**
 * @author jje
 */
public class RFApplication extends PRAbstractApplication {

    private static final long serialVersionUID = 1L;
    public final static String APPLICATION_CYGNUS_REP = "cygnusRoot";
    public final static String APPLICATION_PREFIX = "RF";
    public static final String CYGNUS_RELATIVE_URL = "cygnus?userAction=";
    public final static String DEFAULT_APPLICATION_CYGNUS = "CYGNUS";
    public static final String MODELS_EXCELML_CYGNUS = "model/excelml";
    public final static String PROPERTY_AFFICHER_PERSONNE_REFERENCE = "genererDecision.afficher.libelle.personneDeReference";
    public static final String PROPERTY_AFFICHER_REMARQUE_FOURNISSEUR = "afficher.remarque.fournisseur";
    public final static String PROPERTY_AFFICHER_TELEPHONE_GESTIONNAIRE = "genererDecision.afficher.telephone.gestionnaire";
    public static final String PROPERTY_AFFIHCER_CASE_FORCER_PAIEMENT = "afficher.case.forcer.paiement";
    public static final String PROPERTY_AFFIHCER_CASE_RI = "afficher.case.ri";
    public static final String PROPERTY_AFFIHCER_TYPE_REMBOURSEMENT = "afficher.type.remboursement";
    public static final String PROPERTY_AJOUTER_DEMANDE_EN_COMPTA_SANS_TENIR_COMPTE_TYPE_HOME = "ajouter.demandes.en.comptabilite.sans.tenir.compte.type.home";
    public static final String PROPERTY_AJOUTER_QD_CANDIDATE = "adaptationJournaliere.ajout.qd.candidate";
    public static final String PROPERTY_AJOUTER_TEXTES_LIES_AU_DSAS = "decisions.textes.lies.au.dsas";
    public static final String PROPERTY_AVANCES_SAS_ID_TIERS = "avances.sas.id.tiers";
    public static final String PROPERTY_CS_GENRE_ADMINISTRATION_CMS = "cs.genre.administration.cms";
    public final static String PROPERTY_DECISION_RECTO_VESRO = "decisions.recto.verso";
    public static final String PROPERTY_DECOMPTE_SOUS_TYPES_DE_SOINS_A_CONTROLER = "decompte.sousTypesDeSoins.a.controller";
    public final static String PROPERTY_GED_TARGET_NAME = "ged.targetName";
    public static final String PROPERTY_GERER_AVANCES_SAS = "gerer.avances.sas";
    public static final String PROPERTY_GROUPE_CYGNUS_GESTIONNAIRE = "groupe.gestionnaire";
    public static final String PROPERTY_GROUPE_CYGNUS_NBJOURS_DOCUMENT = "date.defaut.generation.document.nombres.jours";
    public static final String PROPERTY_GROUPES_GESTIONNAIRES = "groupesGestionnaires";
    public static final String PROPERTY_IMPUTATION_PAR_PERIODE_DE_TRAITEMENT = "imputation.par.periodeDeTraitement";
    public final static String PROPERTY_INSERER_LIGNE_TECHNIQUE_DE_REGROUPEMENT = "inserer.ligne.technique.de.regroupement";
    public static final String PROPERTY_IS_PERIODE_DE_TRAITEMENT_REG_COTPAR_CUMULATIVE = "verificationDoublon.periodeTraitement.regimeAlimentaire.et.cotisationParitaire";
    public static final String PROPERTY_MNT_MAX_CANTONAL_STRUCTURES_ET_SEJOUR = "mnt.max.cantonal.structuresEtSejour";
    public final static String PROPERTY_MNT_MAX_REMBOUR_SOINS_JOURNALIER = "mnt.max.remboursement.frais.soins.journalier";
    public static final String PROPERTY_MNT_MAX_RI_COUPLE_FAMILLE = "mnt.max.ri.couple.famille";
    public static final String PROPERTY_MNT_MAX_RI_PERSONNE_SEULE = "mnt.max.ri.personne.seule";
    public static final String PROPERTY_NB_MOIS_RECHERCHE_DEVIS = "nb.mois.recherche.devis";
    public static final String PROPERTY_RETIRER_DECISIONS_REGIME_DANS_PDF = "retirer.decisions.regime.dans.pdf";
    public static final String PROPERTY_UTILISER_DOCUMENT_DECISION_REGIME_AVEC_EXCEDANT_REVENU = "utiliser.document.decision.regime.avec.excedant.revenu";
    public static final String PROPERTY_UTILISER_GESTIONNAIRE_ECRAN = "utiliser.gestionnaire.ecran";
    public static final String PROPERTY_UTILISER_LISTE_RECAPITULATION_COMPLETE = "utiliser.liste.recapitulation.complete";
    public static final String PROPERTY_UTILISER_MISE_EN_GED = "domaine.nomService.ged";
    public static final String PROPERTY_UTILISERS_GROUPE_GESTIONNAIRES = "utiliser.groupesGestionnaires";
    // public static final String PROPERTY_UTILISER_TYPE_BENEFICIAIRE_PC = "utiliser.type.beneficiaire.type.pc";
    public static final String PROPERTY_VERIFIER_ATTESTATION_AIDE_MENAGE_ORG_OSAD = "verificationAttestation.sousTypesSoins.attestation.ajouter.aide.menage.par.organisation.osad";
    public static final String PROPERTY_VERIFIER_ATTESTATION_CHAISES_PERCEES = "verificationAttestation.sousTypesSoins.attestation.ajouter.chaises.percees";
    public static final String PROPERTY_VERIFIER_ATTESTATION_CORSET_ORTHOPEDIQUE = "verificationAttestation.sousTypesSoins.attestation.ajouter.corset.orthopedique";
    public static final String PROPERTY_VERIFIER_ATTESTATION_FRAIS_ENDOPROTHESES = "verificationAttestation.sousTypesSoins.attestation.ajouter.frais.endoprotheses";
    public static final String PROPERTY_VERIFIER_ATTESTATION_LOMBOSTAT_ORTHOPEDIQUE = "verificationAttestation.sousTypesSoins.attestation.ajouter.lombostat.orthopedique";
    public static final String PROPERTY_VERIFIER_ATTESTATION_LUNETTES_VERRES_CONTACT = "verificationAttestation.sousTypesSoins.attestation.ajouter.lunettes.verres.contact";
    public static final String PROPERTY_VERIFIER_ATTESTATION_MINERVE = "verificationAttestation.sousTypesSoins.attestation.ajouter.minerve";
    public static final String PROPERTY_VERIFIER_ATTESTATION_TAXI = "verificationAttestation.sousTypesSoins.attestation.ajouter.taxi";
    public static final String PROPERTY_VERIFIER_ATTESTATION_LIT = "verificationAttestation.sousTypesSoins.attestation.ajouter.lit";
    public static final String PROPERTY_VERIFIER_ATTESTATION_MOYENS_AUX_REMIS_EN_PRET_AVEC_BON = "verificationAttestation.moyens.auxiliaires.remis.en.pret.avec.bon";
    public static final String PROPERTY_VERIFIER_CONVENTION_ACCOMPAGNEMENT_SOCIAL = "verificationConvention.sousTypesSoins.convention.ajouter.accompagnement.social";
    public static final String PROPERTY_VERIFIER_CONVENTION_ANIMATION = "verificationConvention.sousTypesSoins.convention.ajouter.animation";
    public static final String PROPERTY_VERIFIER_CONVENTION_BARRIERE = "verificationConvention.sousTypesSoins.convention.ajouter.barriere";
    public static final String PROPERTY_VERIFIER_CONVENTION_ENCADREMENT_SECURITAIRE = "verificationConvention.sousTypesSoins.convention.ajouter.encadrement.securitaire";
    public static final String PROPERTY_VERIFIER_CONVENTION_ENCADREMENT_SOCIO_EDUCATIF = "verificationConvention.sousTypesSoins.convention.ajouter.encadrement.socio.educatif";
    public static final String PROPERTY_VERIFIER_CONVENTION_LIT_ELECTRIQUE = "verificationConvention.sousTypesSoins.convention.ajouter.lit.electrique";
    public static final String PROPERTY_VERIFIER_CONVENTION_POTENCE = "verificationConvention.sousTypesSoins.convention.ajouter.potence";
    public static final String PROPERTY_VERIFIER_SI_CALCUL_CONCERNE_AUTRE_GESTIONNAIRE = "preparerDecision.verifierSiCalcul.concerne.autreGestionnaire";
    public static final String PROPERTY_VERIFIER_SI_DEVIS_RATTACHE_AUTRES_DEMANDES = "verification.siDevis.rattache.autres.demandes";
    public static final String PROPERTY_ANNULER_UNIQUEMENT_DECISIONS_LIEES_AU_QDS = "annuler.uniquement.decisions.liees.aux.qds";
    public static final String PROPERTY_MISE_EN_GED_DES_DECISIONS_SANS_REMBOURSEMENT = "decision.zero.ged";

    /**
     * Crée une nouvelle instance de la classe REApplication.
     * 
     * @throws Exception
     */
    public RFApplication() throws Exception {
        super(RFApplication.DEFAULT_APPLICATION_CYGNUS);
    }

    public RFApplication(String id) throws Exception {
        super(id);
    }

    @Override
    protected void _declareAPI() {
    }

    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("CYGNUSMenu.xml");
        JadeProcessConfManager.registerFile("RFProcess.xml");
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_DEMANDE + ".afficherBasDePage", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_DEMANDE + ".afficherDetail", FWSecureConstants.READ);
        // FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE +
        // ".majMembresFamilleDemande",FWSecureConstants.READ);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_QD + ".afficherChoixTypeQd", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_QD + ".afficherBasDePage", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_QD + ".afficherDetail", FWSecureConstants.READ);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE + ".majLimiteAnnuelleQdAssure",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE + ".majLimiteAnnuelleQdDroitPC",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION + ".ajouterLibelle",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION + ".ajouterMontantsConvention",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION + ".ajouterAssure",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION + ".supprimerAssure",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION
                + ".supprimerCoupleFournisseurSTS", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION + ".ajouterFournisseurType",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION + ".ajouterBDD",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION + ".modifierBDD",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_ATTESTATION_JOINT_TIERS + ".ajouterRegimeAlimentaire",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS + ".supprimerCoupleMotifRefusSTS",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS + ".ajouterSoinMotif",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IRFActions.ACTION_DECISION_JOINT_TIERS + ".supprimerCopie", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IRFActions.ACTION_DECISION_JOINT_TIERS + ".devalider", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS + ".actionCreerCorrection",
                FWSecureConstants.ADD);
    }
}
