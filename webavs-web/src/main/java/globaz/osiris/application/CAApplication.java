package globaz.osiris.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.api.APIAuxiliaire;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIEvenementContentieux;
import globaz.osiris.api.APIGestionBulletinNeutre;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIGestionRentesExterne;
import globaz.osiris.api.APIGestionSpecifiqueAFExterne;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationContentieux;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIOperationOrdreVersementAvance;
import globaz.osiris.api.APIPaiement;
import globaz.osiris.api.APIPaiementBVR;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APITauxRubriques;
import globaz.osiris.api.APITaxe;
import globaz.osiris.api.APITypeSection;
import globaz.osiris.api.APIUtil;
import globaz.osiris.api.APIVersement;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.api.helper.ICACompteCourantHelper;
import globaz.osiris.api.helper.ICARubriqueHelper;
import globaz.osiris.db.comptes.CAAuxiliaire;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationContentieux;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementAvance;
import globaz.osiris.db.comptes.CAPaiement;
import globaz.osiris.db.comptes.CAPaiementBVR;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CAVersement;
import globaz.osiris.db.contentieux.CAEtape;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAParametreEtape;
import globaz.osiris.db.contentieux.CATaxe;
import globaz.osiris.externe.CAGestionBulletinNeutre;
import globaz.osiris.externe.CAGestionComptabiliteExterne;
import globaz.osiris.externe.CAGestionSpecifiqueAFExterne;
import globaz.osiris.externe.rentes.CAGestionRentesExterne;
import globaz.osiris.services.CAServicesPropositionCompensation;
import globaz.osiris.utils.CAUtil;
import globaz.webavs.common.CommonProperties;
import java.io.Serializable;

/**
 * Application OSIRIS
 * 
 * @author: E.Fleury
 */
public class CAApplication extends globaz.globall.db.BApplication implements Serializable {
    private static final long serialVersionUID = -5123634731506588523L;
    private static BIApplication appPhenix = null;
    public static final String DEFAULT_APPLICATION_OSIRIS = "OSIRIS";
    public static final String DEFAULT_OSIRIS_NAME = "osiris";
    public static final String DEFAULT_OSIRIS_ROOT = "osirisRoot";
    public static final String PROPERTY_BULLETIN_SOLDE_MONTANT_MINIME = "bulletinSoldeMontantMinime";
    public static final String PROPERTY_IMPRESSION_BULLETIN_SOLDE_APRES_COMPTABILISATION = "impressionBulletinSoldeApresComptabilisation";
    public static final String PROPERTY_LIMITE_IMPRESSION_ECRITURES_JOURNAL = "limiteImpressionEcrituresJournal";

    public static final String PROPERTY_OSIRIS_ANCIEN_NO_ADHERENT_BVR = "osiris.class.CAReferenceBVRParserOLD.noAdherentBVR";
    public static final String PROPERTY_OSIRIS_APERCU_JOURNAUX_USER = "apercuJournauxUser";
    public static final String PROPERTY_OSIRIS_BULLETIN_NEUTRE = "bulletinNeutre";
    public static final String PROPERTY_OSIRIS_CHECK_MONTANT_A_REMBOURSER = "checkMontantARembourser";
    public static final String PROPERTY_OSIRIS_COMPTABILITE_AVS = "osiris.application.comptabiliteAvs";
    public static final String PROPERTY_OSIRIS_CONTENTIEUX_AQUILA = "contentieuxAquila";
    public static final String PROPERTY_OSIRIS_CTX_AVS_UNIQUEMENT = "contentieuxAvsUniquement";
    public static final String PROPERTY_OSIRIS_DATE_VALEUR_BVR = "dateValeurBVR";

    // propriétés eBill
    public static final String PROPERTY_OSIRIS_EBILL_ACTIVE = "eBill.activer";
    public static final String PROPERTY_OSIRIS_EBILL_BILLER_ID = "eBill.numero.BillerID";
    public static final String PROPERTY_OSIRIS_EBILL_EMAILS = "osiris.eBill.email.traitements";
    public static final String PROPERTY_OSIRIS_PLAGE_VALEURS_EBILL = "EBILLACNT";

    // propriétés de l'application
    public static final String PROPERTY_OSIRIS_EXTERNAL_APPLICATION = "osiris.application.externalApplication";
    public static final String PROPERTY_OSIRIS_FORMAT_ADMIN_NUM_AFFILIE = "formatAdminNumAffilie";
    public static final String PROPERTY_OSIRIS_IMPRESSIONCONFIDENTIEL = "impressionConfidentiel";
    public static final String PROPERTY_OSIRIS_INTERET_NOUVEAU_MODE_CALCUL = "interetNouveauModeCalcul";
    public static final String PROPERTY_OSIRIS_INTERET_REMUNERATOIRE_ACTIF = "interetRemuneratoireActif";
    public static final String PROPERTY_OSIRIS_INTERETSURCOTARRSURSECTIONSEPAREE = "interetSurCotArrSurSectionSeparee";
    public static final String PROPERTY_OSIRIS_RAPPEL_SUR_PLAN = "rappelSurPlan";
    public static final String PROPERTY_OSIRIS_INCREMENTER_NUM_SECTION_RECOUVREMENT = "incrementerNumSectionRecouvrement";

    public static final String PROPERTY_OSIRIS_REQUISITION_A_ADRESSE_DOMICILE = "requisitionAAdresseDomicile";

    public static final String PROPERTY_PHENIX_FACTURE_PAR_ANNEE = "factureParAnnee";

    public static final String PROPERTY_OSIRIS_MODE_TRAITEMENT_BULLETIN_NEUTRE = "modeBulletinNeutre";

    public static final String PROPERTY_OSIRIS_RECALCUL_SOLDES = "recalcul.soldes.compteAnnexe.section";

    /**
     * Renvoie une instance de l'application enregistrée dans le système
     * 
     * @return l'instance de l'application, év. null
     */
    public final static CAApplication getApplicationOsiris() {
        try {
            return (CAApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(
                    CAApplication.DEFAULT_APPLICATION_OSIRIS);
        } catch (Exception e) {
            throw new Error(e.toString());
        }
    }

    /**
     * @return
     */
    public static BIApplication getApplicationPhenix() {
        // Si application pas ouverte
        if (CAApplication.appPhenix == null) {
            try {
                CAApplication.appPhenix = GlobazSystem.getApplication("PHENIX");
            } catch (Exception e) {
                JadeLogger.error(CAApplication.appPhenix, e);
            }
        }
        return CAApplication.appPhenix;
    }

    private BIApplication appAquila = null;
    private BIApplication appMusca = null;

    private BIApplication appPyxis = null;
    private CAParametres caParametres = null;

    private BISession sessionAquila = null;

    private BISession sessionMusca = null;

    private BISession sessionPhenix = null;

    private BISession sessionPyxis = null;

    /**
     * Constructeur du type CAApplication.
     */
    public CAApplication() throws Exception {
        this(OsirisDef.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * Constructeur du type CAApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public CAApplication(String id) throws Exception {
        super(id);
        caParametres = new CAParametres(this);
    }

    /**
     * Déclare les APIs de l'application
     */
    @Override
    protected void _declareAPI() {
        _addAPI(APICompteAnnexe.class, CACompteAnnexe.class);
        _addAPI(APIEcriture.class, CAEcriture.class);
        _addAPI(APIEtape.class, CAEtape.class);
        _addAPI(APIEvenementContentieux.class, CAEvenementContentieux.class);
        _addAPI(APIJournal.class, CAJournal.class);
        _addAPI(APIOperation.class, CAOperation.class);
        _addAPI(APIOperationContentieux.class, CAOperationContentieux.class);
        _addAPI(APIOperationOrdreVersement.class, CAOperationOrdreVersement.class);
        _addAPI(APIPaiement.class, CAPaiement.class);
        _addAPI(APIPaiementBVR.class, CAPaiementBVR.class);
        _addAPI(APIParametreEtape.class, CAParametreEtape.class);
        _addAPI(APIGestionComptabiliteExterne.class, CAGestionComptabiliteExterne.class);
        _addAPI(APIGestionSpecifiqueAFExterne.class, CAGestionSpecifiqueAFExterne.class);
        _addAPI(APIGestionRentesExterne.class, CAGestionRentesExterne.class);
        _addAPI(APIGestionBulletinNeutre.class, CAGestionBulletinNeutre.class);
        _addAPI(APISection.class, CASection.class);
        _addAPI(APITauxRubriques.class, CATauxRubriques.class);
        _addAPI(APITaxe.class, CATaxe.class);
        _addAPI(APITypeSection.class, CATypeSection.class);
        _addAPI(APIVersement.class, CAVersement.class);
        _addAPI(APIRubrique.class, ICARubriqueHelper.class);
        _addAPI(APICompteCourant.class, ICACompteCourantHelper.class);
        _addAPI(APIAuxiliaire.class, CAAuxiliaire.class);
        _addAPI(APIPropositionCompensation.class, CAServicesPropositionCompensation.class);
        _addAPI(APIOperationOrdreVersementAvance.class, CAOperationOrdreVersementAvance.class);
        _addAPI(APIReferenceRubrique.class, CAReferenceRubrique.class);
        _addAPI(APIUtil.class, CAUtil.class);
    }

    /**
     * Initialise l'application
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    @Override
    protected void _initializeApplication() throws Exception {
        // this._setLocalPath(getProperty("applicationRootPath"));

        try {
            FWMenuCache.getInstance().addFile("OSIRISMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "OSIRISMenu.xml non résolu : " + e.toString());
        }

    }

    /**
     * @see BApplication#_initializeCustomActions()
     */
    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("osiris.comptes.extournerOperation.executerExtourne", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.extournerOperation.afficherOperation", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.comptes.extournerSection.executerExtourne", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.extournerSection.afficherSection", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.comptes.journalOperationPaiementEtranger.ajouterAvantCompensation",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.journalOperationPaiementEtranger.modifierAvantCompensation",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.journalOperationPaiementEtranger.chercherPaiements",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.comptes.journalOperationPaiementEtranger.supprimerPaiement",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.journalOperationOrdreVersement.rembourser",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.journalOperationLettrer.afficherLettrage",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.comptes.journalOperationCustom.afficherSpecial", FWSecureConstants.READ);

        FWAction.registerActionCustom("osiris.comptes.apercuSectionDetaille.rechercherContentieux",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.comptes.detailSection.rechercherContentieux", FWSecureConstants.READ);
        FWAction.registerActionCustom("aquila.process.processCreerARD.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.comptes.transfertSoldes.afficherTransfertSoldes",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.interets.interetMoratoire.nouvelleDecision", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.interets.interetMoratoire.afficherDecisionWithError",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.interets.apercuDecisionsInteretsMoratoires.nouvelleDecision",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.interets.apercuDecisionsInteretsMoratoires.afficherDecisionWithError",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.interets.gestionInterets.nouvelleDecision", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.interets.gestionInterets.afficherDecisionWithError",
                FWSecureConstants.READ);

        FWAction.registerActionCustom("osiris.message.apercuMessage.chercherOG", FWSecureConstants.READ);

        FWAction.registerActionCustom("osiris.print.listExtraitCompteAnnexe.afficherExtraitCA", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.print.bulletinsSoldes.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.print.listBulletinSolde.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.process.interetMoratoireManuel.simuler", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.interetMoratoireManuel.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.recouvrement.echeancePlan.calculSuivants", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.echeancePlan.calculer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.echeancePlan.calculerSave", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.echeancePlan.supprimerSuivants", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.sursis.supprimerSuivants", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.sursis.calculSuivants", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.processSuspendrePlanRecouvrement.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.recouvrement.planRecouvrement.chercher", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.print.rappelPlan.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.retours.retours.actionSupprimerLignesRetoursSurAdressePaiement",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.retours.actionAfficherPeriodeINV", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.retours.retours.actionSupprimerLignesRetoursSurSection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.retours.actionAjouterLignesRetoursSurSection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.retours.actionAjouterLignesRetoursSurAdressePaiement",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.retours.actionCreerRetourSplit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.actionListerLignesRetoursSurSection", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.actionListerLignesRetoursSurAdressePaiement",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.retours.retours.imprimerListePrestations", FWSecureConstants.READ);

        FWAction.registerActionCustom("osiris.process.contentieux.afficher", FWSecureConstants.UPDATE);

        // Ordres groupé
        FWAction.registerActionCustom("osiris.process.preparerOrdre.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.transmettreOrdreGroupe.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.annulerOrdre.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.process.annulerJournal.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.extournerJournal.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.rouvrirJournal.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.comptabiliserJournal.afficher", FWSecureConstants.UPDATE);

        // Traitement
        FWAction.registerActionCustom("osiris.process.bvr.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.paiementEtranger.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.annulerSoldeSection.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.processAvance.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.processImportOperations.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.genererQualiteDebiteur.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.process.remboursementAutomatique.afficher", FWSecureConstants.UPDATE);

        // GED
        FWAction.registerActionCustom("naos.affiliation.affiliation.gedafficherdossier", FWSecureConstants.READ);

        // Lettrage
        FWAction.registerActionCustom("osiris.lettrage.plages.definirPlages", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.lettrage.plages.listerPlages", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.lettrage.main.display", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.lettrage.main.query", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.lettrage.main.querySection", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.lettrage.main.infoSection", FWSecureConstants.READ);

        FWAction.registerActionCustom("osiris.lettrage.main.doLettrage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.lettrage.main.exlureSection", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.lettrage.main.inclureSection", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.lettrage.main.versementSection", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.lettrage.main.annulerVersementSection", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("osiris.lettrage.main.reportSection", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("osiris.ordres.ordresGroupes.valider", FWSecureConstants.UPDATE);

        // eBill
        FWAction.registerActionCustom("osiris.ebill.inscriptionEBill.optionAValider", FWSecureConstants.READ);
        FWAction.registerActionCustom("osiris.ebill.inscriptionEBill.optionATraiter", FWSecureConstants.READ);

    }

    @Override
    protected void _resetInternalCache() {
        PATraductionHelper.resetCache();

    }

    public IFormatData getAffileFormater() throws Exception {
        // le format du numéro d'affilié
        IFormatData affileFormater = null;
        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isBlank(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
                // affileFormater = new CFNumAffilie();
            }
        }
        return affileFormater;

    }

    /**
     * méthode permettant d'obtenir un objet application Aquila Date de création : (15.03.2006 12:12:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationAquila() {
        // Si application pas ouverte
        if (appAquila == null) {
            try {
                appAquila = GlobazSystem.getApplication("AQUILA");
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appAquila;
    }

    public BIApplication getApplicationMusca() {
        // Si application pas ouverte
        if (appMusca == null) {
            try {
                appMusca = GlobazSystem.getApplication("MUSCA");
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appMusca;
    }

    /**
     * ALD Ajout bulletins de soldes : 2004.07.28 méthode permettant d'obtenir un objet application pyxis Date de
     * création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationPyxis() {
        // Si application pas ouverte
        if (appPyxis == null) {
            try {
                appPyxis = GlobazSystem.getApplication("PYXIS");
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appPyxis;
    }

    /**
     * Renvoie les paramètres de l'instance d'application
     * 
     * @return les paramètres de l'instance d'application
     */
    public final CAParametres getCAParametres() {
        return caParametres;
    }

    /**
     * méthode permettant d'obetnir une session Aquila Date de création : (15.03.2006 12:15:58)
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionAquila(BISession session) {
        // Si session pas ouverte
        if (sessionAquila == null) {
            try {
                sessionAquila = getApplicationAquila().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionAquila;
    }

    public BISession getSessionMusca(BISession session) {
        // Si session pas ouverte
        if (sessionMusca == null) {
            try {
                sessionMusca = getApplicationMusca().newSession(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionMusca;
    }

    public BISession getSessionPhenix(BISession session) {
        // Si session pas ouverte
        if (sessionPhenix == null) {
            try {
                sessionPhenix = getApplicationMusca().newSession(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionPhenix;
    }

    /**
     * Return une session pyxis. Possibilité de forcer la création d'une nouvelle session.
     * 
     * @param session
     * @param createNewSession
     * @return
     */
    public BISession getSessionPyxis(BISession session, boolean createNewSession) {
        // Si session pas ouverte
        if ((sessionPyxis == null) || createNewSession) {
            try {
                sessionPyxis = getApplicationPyxis().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionPyxis;
    }

}
