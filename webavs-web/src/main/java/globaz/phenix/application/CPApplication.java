package globaz.phenix.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.OsirisDef;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.webavs.common.CommonProperties;
import java.math.BigDecimal;
import ch.globaz.common.properties.PropertiesException;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Application PHENIX
 * 
 * @author Emmanuel Fleury
 */
public class CPApplication extends globaz.globall.db.BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String ACOMPTE_DETAIL_CALCUL = "acompteDetailCalcul";

    public final static String ADD_JOURDATE_FACTURATION = "addJourDateFacturation";
    public final static String AFFICHAGE_DATE_FACTURATION = "affichageDateFacturation";
    public final static String AFFICHAGE_DECISION_FLUX_PDF = "affichageDecisionFluxPDF";
    public final static String AFFICHAGE_MISEENCOMPTE = "affichageMiseEnCompte";
    public final static String AFFICHAGE_SPECIFICATION = "affichageSpecification";
    public final static String ANNEE_CHANGEMENT = "anneeChangement";
    public final static String APPLICATION_PHENIX_REP = "phenixRoot";
    public final static String CALCUL_IMMOBILIER = "calculImmobilier";
    public static final String CANTON_CAISSE = "cantonCaisse";
    public static final String CANTON_DEFAUT = "cantonDefaut";
    public static final String CANTON_FRANCAIS = "cantonFrancais";
    public static final String CANTON_ITALIEN = "cantonItalien";
    public final static String CHAMP_RECHERCHE_TIERSEXISTANT_RETOUR = "champRechercheTiersExistantPourRetour";
    public final static String COMPTABILISATION_JOURNALCI = "comptabilisationJournalCi";
    public final static String CREATIONPASSAGE_AUTOMATIQUE = "creationPassageAutomatique";
    public final static String DATE_EXERCICE = "dateExercice";
    public final static String DEFAULT_APPLICATION_PHENIX = "PHENIX";
    public final static String DIFFERENCE_MONTANTCI = "differenceMontantCI";
    public final static String FACTURE_PAR_ANNEE = "factureParAnnee";
    public final static String GED_SERVICE = "GED_Service";
    public final static String GED_TYPEDOSSIER = "GED_TypeDossier";
    public final static String GENRE_DECISION_DEFAUT = "genreDecisionDefaut";
    public final static String HISTORIQUE_COMMUNICATION = "historiqueCommunication";
    public final static String ID_CAISSE = "idCaisse";
    public final static String IMPRESSION_COMFISC_AUTO = "impressionComFiscAuto";
    public final static String IMPRESSION_MONTANT_IDENTIQUE = "impressionMontantIdentique";
    public final static String IMPRIMER_RECAPITULATION = "imprimerRecapitulation";
    public final static String LETTRE_COUPLE = "lettreCouple";
    public final static String LETTRE_SIGNATURE = "lettreSignature";
    public static final String LIBELLE_JOURNAL = "libelleJournal";
    public final static String LIBELLEPASSAGE_AUTOMATIQUE = "libellePassageCotPersAuto";
    public final static String LIBELLEPASSAGE_AUTOMATIQUE_DEFAULT = "Facturation PHENIX";
    public final static String LIBELLEPASSAGE_AUTOMATIQUE_IND = "libellePassageCotPersAutoInd";
    public final static String LIBELLEPASSAGE_AUTOMATIQUE_NAC = "libellePassageCotPersAutoNac";
    public final static String LIBELLEPASSAGE_AUTOMATIQUE_PORTAIL = "libellePassageCotPersAutoPortail";
    public final static String MAJ_NUMCONTRIBUABLE = "majNumContribuable";
    public final static String NB_COMMUNICATIONS_MAX_PAR_FICHIER = "NbComMax";
    public final static String NB_COMMUNICATIONS_MAX_PAR_FICHIER_DEFVAL = "3000";
    public final static String NBRE_MAX_COM_SEDEX = "nombreMaxCommunicationsParJournalSedex";
    private final static int NBRE_MAX_COM_SEDEX_DEFAUT = 100;
    public final static String NBRE_MAX_ENVOI_SEDEX = "nombreMaxEnvoiSedex";
    public final static int NBRE_MAX_ENVOI_SEDEX_DEFAUT = 1000;
    public final static String PROCEDURE_EXTRAORDINAIRE = "procedureExtraordinaire";
    public final static String REMB_FRAIS_ADMIN = "rembFraisImputation";
    public static final String REP_COM_FISC = "repComFisc";
    public static final String REP_COM_FISC_RECEPT = "repComFiscReceptionnees";
    public final static String REVENU_AF = "revenuAF";
    public final static String REVENU_AGRICOLE = "revenuAgricole";
    public final static String TRANSFERT_CHANGE_NUMAFFILIE = "transfertChangeNumAffilie";
    // Adresse proffessionnelle
    public final static String TYPE_ADRESSE_PROFFESSIONNELLE = "19120045";
    public final static String TYPE_REVENU = "typeRevenu";
    public static final String TYPES_GENERATION = "typeGeneration";
    public static final String TYPES_VALIDATION = "typeValidation";
    public static final String USE_AGENCE = "useAgence";
    public final static String USE_SESSION_USER_FOR_HEADER = "useSessionUserForHeader";
    public final static String VALIDATION_DECISION = "validationDecision";
    public static final String MISE_EN_GED_COM_FISC_RETOUR = "phenix.com.fiscale.retour.mettreGed";
    public static final String NOM_USER_PORTAIL = "phenix.nomUserPortail";

    /**
     * Renvoie une instance de l'application enregistrée dans le système
     * 
     * @return l'instance de l'application, év. null
     * @param applicationId
     *            l'id de l'instance de l'application
     */
    public final static CPApplication getCPApplication(String applicationId) {
        try {
            return (CPApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(applicationId);
        } catch (Exception e) {
            return null;
        }
    }

    // le format du numéro d'affilié
    private IFormatData affilieFormater = null;

    private globaz.globall.api.BIApplication appMusca = null;

    private globaz.globall.api.BIApplication appOsiris = null;
    private globaz.globall.api.BIApplication appPyxis = null;
    private String cantonCaisse = "";
    private String nbComMax = CPApplication.NB_COMMUNICATIONS_MAX_PAR_FICHIER_DEFVAL;

    // Ajout manuel
    private globaz.globall.api.BISession sessionPyxis = null;

    /**
     * Constructeur du type TIApplication.
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public CPApplication() throws Exception {
        super(CPApplication.DEFAULT_APPLICATION_PHENIX);
    }

    /**
     * Constructeur du type TIApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public CPApplication(String id) throws Exception {
        super(id);
    }

    /**
     * Déclare les APIs de l'application
     */
    @Override
    protected void _declareAPI() {
    }

    /**
     * Initialise l'application
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    @Override
    protected void _initializeApplication() throws Exception {
        try {
            FWMenuCache.getInstance().addFile("PHENIXMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "PHENIXMenu.xml non résolu : " + e.toString());
        }
        nbComMax = this.getProperty(CPApplication.NB_COMMUNICATIONS_MAX_PAR_FICHIER);
        cantonCaisse = this.getProperty(CPApplication.CANTON_CAISSE);
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.abandonner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.abandonner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decision.dernierDossier", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.journalRetour.receptionner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.traitementAnomalies.receptionner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.traitementAnomalies.executerReceptionner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.decision.reporter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.listes.reinjectionConcordanceCICotPersListeExcel.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.acompte.creation", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.acompte.suppression", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decision.imprimerLot", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.reglePlausibilite.rechercher", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.receptionReader.rechercher", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.principale.decision.calculer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decision.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.principale.decision.duplicata", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.principale.decision.devalider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.reglePlausibilite.configurer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.receptionner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.abandonner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.generer",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.imprimer",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.executerImprimer",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.reinitialiser",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.abandonnerEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.enqueterEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.reinitialiserEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.genererEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherAbandonnerEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherReinitialiserEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherGenererEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.generer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.fermer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.genererEnMasse", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.journalRetour.abandonner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.abandonnerEnMasse", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.reinitialiserEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.retourner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.supprimerCommunication",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.reinitialiser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.modifierDecision",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.abandonner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.devalider",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.decision.recomptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.processAbandonnerSelection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.communicationFiscaleAffichage.imprimer",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerImprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.processDevaliderSelection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerAbandonner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerAbandonnerEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerReinitialiser",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerReinitialiserEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerRetourner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerReceptionner",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerSupprimer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerGenerer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerGenererEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerEnqueterEnMasse",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerEnqueter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.processGenerer",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.journalRetour.executerValider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.traitementAnomalies.executerGenerer",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.processValiderSelection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.acompte.executerCreation", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.acompte.executerSuppression", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.decision.executerRecomptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.process.decision.executerReporter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decision.initCreer", FWSecureConstants.ADD);
        FWAction.registerActionCustom("phenix.principale.decision.initialiser", FWSecureConstants.ADD);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.validerSelection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.devaliderSelection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.abandonnerSelection",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.valider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.executerSuppression",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decisionValider.modifierProvenanceJournalRetour",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.autreDossier.modifier", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.miseAjourComFisc.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.communicationImprimer.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.communicationEnvoyer.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.validationJournalRetour.chercher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.miseAjourMontantsAffiliation.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.miseAjourCodeActifDecision.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.devalidationJournalRetour.chercher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decisionValider.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.principale.decision.imprimer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.communicationFiscaleImpression.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherOriginale",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherOriginaleBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherContribuable",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherContribuableBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesBase",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesBaseBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesPrivees",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesPriveesBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommerciales",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommercialesBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherConjoint",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherConjointBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommunication",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(
                "phenix.communications.apercuCommunicationFiscaleRetour.afficherDonneesCommunicationBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.retournerOriginale",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.modifierCustom",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.lister",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherRenteAVSWIRR",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.apercuCommunicationFiscaleRetour.afficherRenteAVSWIRRBCK",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.suivis.NouvelleAffiliation.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.suivis.NouvelleAffiliation.executer", FWSecureConstants.UPDATE);
        // même s'il s'agit d'une action afficher, il n'y a pas de raison d'aller sur cet écran sans droit de maj...
        FWAction.registerActionCustom("phenix.suivis.RadiationAffiliation.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.suivis.RadiationAffiliation.executer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.rejets.afficherImpression", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.rejets.changerStatus", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.rejets.abandonner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("phenix.communications.rejets.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("phenix.communications.envoiIndividuelSedex.afficher", FWSecureConstants.UPDATE);

    }

    /**
     * Insert the method's description here. Creation date: (22.07.2003 08:32:50)
     * 
     * @param path
     *            java.lang.String
     */
    public void _setLocalPath(String path) {
        Jade.getInstance().setHomeDir(path);
    }

    /**
     * Retourne le nombre de jour à ajouter lors de la création automatique du passage de facturation Date de création :
     * (20.04.2006 10:46:24)
     * 
     * @return int nombre de jour à ajouter
     */
    public int getAddJourDateFacturation() {
        return Integer.parseInt(this.getProperty(CPApplication.ADD_JOURDATE_FACTURATION));
    }

    /**
     * Method getAffileFormater.
     * 
     * @return IFormatData
     * @throws Exception
     */
    public IFormatData getAffileFormater() throws Exception {
        if (affilieFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isEmpty(className)) {
                affilieFormater = (IFormatData) Class.forName(className).newInstance();
                // affileFormater = new CFNumAffilie();
            }
        }
        return affilieFormater;
    }

    /**
     * Retourne l'id de la caisse Date de création : (24.01.2003 07:46:24)
     * 
     * @return l'id de la caisse
     */
    public int getAnneeChangement() {
        return Integer.parseInt(this.getProperty(CPApplication.ANNEE_CHANGEMENT));
    }

    public BIApplication getApplicationMusca() throws Exception {
        if (appMusca == null) {
            appMusca = GlobazSystem.getApplication("MUSCA");
        }
        return appMusca;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationOsiris() throws Exception {
        // Si application pas ouverte
        if (appOsiris == null) {
            appOsiris = GlobazSystem.getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS);
        }
        return appOsiris;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationPyxis() throws Exception {
        // Si application pas ouverte
        if (appPyxis == null) {
            appPyxis = GlobazSystem.getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS);
        }
        return appPyxis;
    }

    public int getAutoDigitAffilie() {
        try {
            String temp = this.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
            return Integer.valueOf(temp).intValue();
        } catch (Exception e) {
            return -1;
        }
        // return getProperty(AUTODIGITAFF);
    }

    /**
     * @return
     */
    public String getCantonCaisse() {
        return cantonCaisse;
    }

    /**
     * Retourne le genre de décision par défaut de la caisse Date de création : (24.01.2003 07:46:24)
     * 
     * @return String genre de décision
     */
    public String getCantonDefaut() {
        return this.getProperty(CPApplication.CANTON_DEFAUT);
    }

    /**
     * Retourne le nom du champ utilisé pour la recherche du tiers lors de la réception des communications fiscales (EX:
     * numAffilie, numContribuable, numAVS
     * 
     * @return String
     */
    public String getChampRechercheTiersExistantRetour() {
        return this.getProperty(CPApplication.CHAMP_RECHERCHE_TIERSEXISTANT_RETOUR);
    }

    /**
     * Retourne la date d'introduction du NNSS Properties défini dans common
     * 
     * @return string
     */
    public String getDateNNSS() {
        return this.getProperty("nnss.dateProduction");
    }

    /**
     * Recherche la différence tolérée antre le total du CI et le calcul CI des cot pers. Si pas trouvé, prendre CHF.3.-
     * par défaut Date de création : (27.07.2005 13:27:00)
     * 
     * @return boolean
     */
    public BigDecimal getDifferenceMontantCI() throws Exception {
        return new BigDecimal(this.getProperty(CPApplication.DIFFERENCE_MONTANTCI, "3"));
    }

    public String getGedService() {
        return this.getProperty(CPApplication.GED_SERVICE);
    }

    public String getGedTypeDossier() {
        return this.getProperty(CPApplication.GED_TYPEDOSSIER);
    }

    /**
     * Retourne le genre de décision par défaut de la caisse Date de création : (24.01.2003 07:46:24)
     * 
     * @return String genre de décision
     */
    public String getGenreDecisionDefaut() {
        return this.getProperty(CPApplication.GENRE_DECISION_DEFAUT);
    }

    /**
     * Retourne l'id de la caisse Date de création : (24.01.2003 07:46:24)
     * 
     * @return l'id de la caisse
     */
    public String getIdCaisse() {
        return this.getProperty(CPApplication.ID_CAISSE);
    }

    /**
     * @param langue
     *            langue en code système
     * @return le code ISO de langue: FR, DE ou IT
     */
    public String getLangue2ISO(String langue) {
        if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(langue)) {
            return "FR";
        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(langue)) {
            return "DE";
        } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(langue)) {
            return "IT";
        } else {
            return "FR";
        }
    }

    public String getLangueCantonISO(String canton) {
        // On essaye en français
        String francophone = this.getProperty(CPApplication.CANTON_FRANCAIS);
        if (JadeStringUtil.contains(francophone, canton, true)) {
            return IConstantes.CS_TIERS_LANGUE_FRANCAIS;
        }
        // On essaye l'italien
        String italien = this.getProperty(CPApplication.CANTON_ITALIEN);
        if (JadeStringUtil.contains(italien, canton, true)) {
            return IConstantes.CS_TIERS_LANGUE_ITALIEN;
        } else {
            return IConstantes.CS_TIERS_LANGUE_ALLEMAND;
        }
    }

    /**
     * Retourne le nom du libelle du journal pour les com. fisc. automatique
     * 
     * @return String le libelle du journal
     */
    public String getLibelleJournal() {
        return this.getProperty(CPApplication.LIBELLE_JOURNAL);
    }

    /**
     * Recherche le libellé automatique par défaut si le mode de création de passage est automatique après qu'un passage
     * soit facturé Method getLibellePassageAutomatique.
     * 
     * @return String
     */
    public String getLibellePassageAutomatique(String module) {
        String libelle = "";
        if (FAModuleFacturation.CS_MODULE_COT_PERS_IND.equalsIgnoreCase(module)) {
            libelle = this.getProperty(CPApplication.LIBELLEPASSAGE_AUTOMATIQUE_IND);
        } else if (FAModuleFacturation.CS_MODULE_COT_PERS_NAC.equalsIgnoreCase(module)) {
            libelle = this.getProperty(CPApplication.LIBELLEPASSAGE_AUTOMATIQUE_NAC);
        } else if (FAModuleFacturation.CS_MODULE_COT_PERS_PORTAIL.equalsIgnoreCase(module)) {
            libelle = this.getProperty(CPApplication.LIBELLEPASSAGE_AUTOMATIQUE_PORTAIL);
        }
        if (JadeStringUtil.isBlankOrZero(libelle)) {
            libelle = this.getProperty(CPApplication.LIBELLEPASSAGE_AUTOMATIQUE);
        }
        if (!"".equals(libelle)) {
            return libelle;
        } else {
            return CPApplication.LIBELLEPASSAGE_AUTOMATIQUE_DEFAULT;
        }
    }

    /**
     * Test si la caisse désire mettre à jour le n° de contribuable lors de la réception du fisc Date de création :
     * (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public String getMajNumContribuable() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.MAJ_NUMCONTRIBUABLE))) {
                return "YES";
            } else {
                return "NO";
            }
        } catch (Exception e) {
            return "NO";
        }
    }

    /**
     * @return
     */
    public String getNbComMax() {
        return nbComMax;
    }

    /**
     * Retourne le répertoire où sont stockées les fichiers de réception des communications Fiscales
     * 
     * @return String repertoire des Com. Fisc.
     */
    public String getRepertoireCommunicationsFiscales() {
        return this.getProperty(CPApplication.REP_COM_FISC);
    }

    /**
     * Retourne le répertoire où sont stockées les fichiers de réception des communications Fiscales qui sont déjà
     * réceptionnées dans l'application
     * 
     * @return String repertoire des Com. Fisc.
     */
    public String getRepertoireCommunicationsFiscalesReceptionnees() {
        return this.getProperty(CPApplication.REP_COM_FISC_RECEPT);
    }

    public BISession getSessionOsiris(BISession session) throws Exception {
        return getApplicationOsiris().newSession(session);
    }

    public BISession getSessionPyxis(BISession session) throws Exception {
        if (sessionPyxis == null) {
            sessionPyxis = getApplicationPyxis().newSession(session);
        }
        return sessionPyxis;
    }

    /**
     * Retourne l'id de la caisse Date de création : (24.01.2003 07:46:24)
     * 
     * @return l'id de la caisse
     */
    public String getTypeRevenu() {
        try {
            return this.getProperty(CPApplication.TYPE_REVENU);
        } catch (Exception e) {
            return "Annuel";
        }
    }

    /**
     * Retourne les différents type de communications fiscales à générer
     */
    public String getTypesGeneration() {
        return this.getProperty(CPApplication.TYPES_GENERATION);
    }

    /**
     * Retourne les différents type de communications fiscales à valider
     */
    public String getTypesValidation() {
        return this.getProperty(CPApplication.TYPES_VALIDATION);
    }

    /**
     * Renvoie la version de l'application
     * 
     * @return la version de l'application
     */
    @Override
    public String getVersion() {
        return "PHENIX_1-4-11";
    }

    public boolean hasSQLUCase() throws Exception {
        return new Boolean(this.getProperty("use_sql_ucase")).booleanValue();
    }

    /**
     * Test si la caisse veut le détail des revenus pour la création des acomptes Date de création : (30.09.2002
     * 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAcompteDetailCalcul() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.ACOMPTE_DETAIL_CALCUL))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAffichageDateFacturation() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.AFFICHAGE_DATE_FACTURATION))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Renvoie si on désire afficher les décisions par flux Method isAffichageDecisionFluxPDF.
     * 
     * @return boolean
     */
    public Boolean isAffichageDecisionFluxPDF() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.AFFICHAGE_DECISION_FLUX_PDF))) {
                return new Boolean(true);
            } else {
                return new Boolean(false);
            }
        } catch (Exception e) {
            return new Boolean(true);
        }
    }

    /**
     * Indique si l'on affiche l'information mise en compte (Ex: CSC=false) Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAffichageMiseEnCompte() {
        try {
            if ("false".equalsIgnoreCase(this.getProperty(CPApplication.AFFICHAGE_MISEENCOMPTE))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Indique si l'on affiche l'information spécialité (Ex: CSC) Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isAffichageSpecification() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.AFFICHAGE_SPECIFICATION))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indique si la caisse comptabilise directement le journal ci Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public Boolean isComptabiliseJournalCi() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.COMPTABILISATION_JOURNALCI))) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isCPCALCIMMO() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.CALCUL_IMMOBILIER))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Renvoie si la création du passage est automatique Method isCreationPassageAutomatique.
     * 
     * @return boolean
     */
    public boolean isCreationPassageAutomatique() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.CREATIONPASSAGE_AUTOMATIQUE))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isDateExercice() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.DATE_EXERCICE))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return true si la caisse regroupe ces factures par année de décision et non par année de facturation Date de
     * création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isFactureParAnnee() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.FACTURE_PAR_ANNEE))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isHistoriqueCommunication() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.HISTORIQUE_COMMUNICATION))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test si validation direct des décisions générées par les communications fiscales Date de création : (30.09.2002
     * 15:15:15)
     * 
     * @return boolean
     */
    public boolean isImpressionComFiscAuto() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.IMPRESSION_COMFISC_AUTO))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Défini si il faut imprimer et facturer les décisions qui sont identiques aux communications fiscales
     * 
     * @return impressionMontantIdentique
     */
    public Boolean isImpressionMontantIdentique() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.IMPRESSION_MONTANT_IDENTIQUE))) {
                return new Boolean(true);
            } else {
                return new Boolean(false);
            }
        } catch (Exception e) {
            return new Boolean(false);
        }
    }

    /**
     * Défini si il faut imprimer la récapitulation pourla liste des décisions
     * 
     * @return imprimerRecapitulation
     */
    public Boolean isImprimerRecap() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.IMPRIMER_RECAPITULATION))) {
                return new Boolean(true);
            } else {
                return new Boolean(false);
            }
        } catch (Exception e) {
            return new Boolean(false);
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isLettreCouple() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.LETTRE_COUPLE))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public Boolean isLettreSignature() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.LETTRE_SIGNATURE))) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isProcedureExtraordinaire() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.PROCEDURE_EXTRAORDINAIRE))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indique si la caisse rembourse les frais lors d'une imputation Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isRemboursementFraisAdmin() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.REMB_FRAIS_ADMIN))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return true si le 2ème source de revenu est pour le revenu AF (utile pour cas spéciaux AF si revenu sur 2
     * cantons) Date de création : (14.12.2006 15:15:15)
     * 
     * @return boolean
     */
    public boolean isRevenuAf() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.REVENU_AF))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Return true si le 2ème source de revenu est le revenu agricole (utile pour AFI) Date de création : (14.12.2006
     * 15:15:15)
     * 
     * @return boolean
     */
    public boolean isRevenuAgricole() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.REVENU_AGRICOLE))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Test s'il faut transférer les décisions cot. pers. si c'est le même numéro d'affilié (pour un même tiers et même
     * genre) Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isTransfertChangeNumAffilie() {
        try {
            if ("false".equalsIgnoreCase(this.getProperty(CPApplication.TRANSFERT_CHANGE_NUMAFFILIE))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Utilise les agences communales (caisses cantonales) Date de création : (30.09.2002 15:15:15)
     * 
     * @return boolean
     */
    public boolean isUseAgence() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.USE_AGENCE))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isUseSessionUserForHeader() {
        try {
            if ("true".equalsIgnoreCase(this.getProperty(CPApplication.USE_SESSION_USER_FOR_HEADER))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Test si validation direct des décisions générées par les communications fiscales Date de création : (30.09.2002
     * 15:15:15)
     * 
     * @return boolean
     */
    public boolean isValidationDecision() throws Exception {
        if ("true".equalsIgnoreCase(this.getProperty(CPApplication.VALIDATION_DECISION))) {
            return true;
        } else {
            return false;
        }
    }

    public int nombreMaxCommunicationsParJournalSedex() {
        try {
            return Integer.valueOf(this.getProperty((CPApplication.NBRE_MAX_COM_SEDEX))).intValue();
        } catch (Exception e) {
            return CPApplication.NBRE_MAX_COM_SEDEX_DEFAUT;
        }

    }

    public int nombreMaxEnvoiSedex() {
        try {
            return Integer.valueOf(this.getProperty((CPApplication.NBRE_MAX_ENVOI_SEDEX))).intValue();
        } catch (Exception e) {
            return CPApplication.NBRE_MAX_ENVOI_SEDEX_DEFAUT;
        }
    }

    public static String getNomUserPortail() throws PropertiesException {
        String value = JadePropertiesService.getInstance().getProperty(NOM_USER_PORTAIL);
        // on s'assure que la propriété ne soit pas null
        if (null == value) {
            throw new PropertiesException("The properties [" + NOM_USER_PORTAIL + "] doesn't exist.");
        }
        return value;
    }

    /**
     * Propriété en base de données permettant de savoir si on veut mettre en GED
     * les communications fiscales en retour.
     * 
     * @return Boolean True si on envoi en GED, false sinon
     * @throws PropertiesException Si la propriété n'est pas présente ou pas dans le bon format
     */
    public static boolean isComFisEnvoiGed() throws PropertiesException {

        String value = JadePropertiesService.getInstance().getProperty(MISE_EN_GED_COM_FISC_RETOUR);

        // on s'assure que la propriété ne soit pas null
        if (null == value) {
            throw new PropertiesException("The properties [" + MISE_EN_GED_COM_FISC_RETOUR + "] doesn't exist.");
        }

        // dans le cas d'un booléen, on s'assure que cela en soit un
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        } else {
            throw new PropertiesException("The value (" + value + ") for the properties " + MISE_EN_GED_COM_FISC_RETOUR
                    + " is not a boolean value");
        }

    }

    /**
     * Permet de savoir si la caisse en question est la CCCVS
     * DDS : S141124_011
     * 
     * @return true si la caisse est la CCCVS, false sinon
     */
    public boolean isCaisseCCCVS() throws Exception {
        if ("23".equalsIgnoreCase(this.getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE))) {
            return true;
        } else {
            return false;
        }
    }
}