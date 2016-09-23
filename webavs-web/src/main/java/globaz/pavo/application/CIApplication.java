package globaz.pavo.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.utils.DateUtils;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.gsc.bean.JadeRoleGroup;
import globaz.jade.admin.gsc.service.JadeRoleGroupService;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserGroupService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.helper.ITIAdministrationHelper;
import globaz.pyxis.api.helper.ITIPersonneAvsHelper;
import globaz.webavs.common.CommonProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Application PAVO
 * 
 * @author Emmanuel Fleury
 */
public class CIApplication extends globaz.globall.db.BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ADRESSE_APPLICATION_PYXIS = "adresseApplication";
    public final static String AFFICHE_JOURNAL_ASSURANCE_FAC = "afficheJournalAssuranceFac";
    public final static String ANCIENNES_ANNONCES = "anciennesAnnonces";
    public final static String ANNONCE_APP = "annonce";
    public final static String ANNONCES_A_BLOQUER = "annoncesABloquer";
    public final static String ANNONCES_WA = "annoncesWA";
    public final static String APPLICATION_PAVO_REP = "pavoRoot";
    public final static String CAISSE_DIFFERENTE = "isCaisseDifferente";
    public final static String CC_FOR_EXTRAIT = "caisseCompExtraitCi";
    public final static String CHAMP_AFFICH = "affichage";
    public final static String CI_ADD_KEY = "ciAddAuto";
    public final static String CI_ADD_NON_CONFORME = "isCIAdditionnelNonConforme";
    public final static String CODE_AGENCE = "agence";
    public final static String CODE_APPLICATION_ARC = "11";
    public final static String CODE_CAISSE = "caisse";
    public final static String CODE_IRRECOUVRABLE = "330001";
    public final static String CODESPECIALINDIV = "isCodeSpecialInscIndividuel";
    public final static String COLONNESAMASQUER = "colonnesAMasquer";
    public final static String CS_DEFAULT_FORMAT_DE = "defaultFormatDS";
    public final static String CS_DOMAINE_ADRESSE_CI_ARC = "519001";
    public final static String DEFAULT_APPLICATION_PAVO = "PAVO";
    public final static int DEFAULT_SPECIALIST_LEVEL = 5;
    public final static String DEFAULT_USER_EMAIL_KEY = "EMail";
    public final static String EMAIL_ADMIN = "mailAdmin";
    public final static String FORMAT_COMPARAISON_EBCDIC = "formatEBCDIC";
    public final static String GENRE_ZERO_POSSIBLE = "isCodeGenreZeroPossible";
    public final static String ID_RUBRIQUE = "idRubrique";
    public final static String IS_CAISSE_FUSION = "isCaisseFusion";
    public final static String KEY_SESSION_CAISSE = "sessionCaisse";
    public final static String KEY_SESSION_HERMES = "sessionHermes";
    public final static String KEY_SESSION_PYXIS = "sessionPyxis";
    public final static String NB_ASSURE_NNSS = "nbAssuresNNSS";
    public final static String NUMERO_TICKET_ETUDIANT = "ticketEtudiant";
    public final static String ROLE_CI = "responsableCI";
    public final static String SPECIALIST_LEVEL_KEY = "specialistLevel";
    public final static String TIERS_APP = "tiers";
    public final static String USER_EMAIL_KEY = "emailKey";
    public final static String WANT_BLOCAGE_SECURITE_CI = "wantBlocageSecuriteCi";
    public final static String WANT_SYNCOR_NRA = "wantSyncroNra";
    public final static String WANT_UPI_DAILY = "wantUpiDaily";
    public final static String WANTED_TO_COMPARE = "anomalieCompare";
    public final static String WANTED_TO_CORRECT = "anomalieCorrige";
    public static final String PROPERTY_ACTIVER_PARSING_PUCS_4 = "activerParsingPUCS4";

    // le format du numéro d'affilié
    private IFormatData affileFormater = null;
    private ArrayList<String> annoncesABloquer = null;
    private ArrayList<String> champsAffiche = null;
    private ArrayList<String> codesComparaisonToCorrect = null;
    private ArrayList<String> codesCompraisonToCompare = null;
    private ArrayList<String> colonnesAMasquer = null;
    // les pays
    private FWParametersSystemCodeManager csPaysListe = null;
    // les sexes
    private FWParametersSystemCodeManager csSexeListe = null;
    public HashMap<?, ?> resultBuffer;

    /**
     * Insérez la description de la méthode ici. Date de création : (07.11.2002 10:39:22)
     */
    public CIApplication() throws Exception {
        super(CIApplication.DEFAULT_APPLICATION_PAVO);
    }

    /**
     * Constructeur du type CIApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public CIApplication(String id) throws Exception {
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
            FWMenuCache.getInstance().addFile("PAVOMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "PAVOMenu.xml non résolu : " + e.toString());
        }
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.extournerAfficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.extourner", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.chercherEcritureAnnonce", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.chercherDetailAnnonce", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.compareMasseSalariale", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.compareMasseSalarialeListe", FWSecureConstants.READ);

        FWAction.registerActionCustom("pavo.compte.compteIndividuel.reAfficherEchec", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.certificatAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.compteIndividuel.attestationAfficher", FWSecureConstants.READ);

        FWAction.registerActionCustom("pavo.compte.ecriture.listerSurPage", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.ecriture.afficherSurPage", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.ecriture.chercherEcriture", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.ecriture.supprimerSurPage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.modifierSurPage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.chercherSurPage", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.ecriture.ajouterSurPage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.supprimerSuspens", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.comptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.extourne", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.copie", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.eclate", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.eclaterExecuter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.suivantPerso", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.precedantPerso", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.ajouterSucces", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.reAfficherDetail", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.reAfficherEclate", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.compte.ecrituresNonRA.modifierDepuisEcriture", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecrituresNonRA.imprimer", FWSecureConstants.READ);

        FWAction.registerActionCustom("pavo.compte.historique.listerSurPage", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.historique.afficherSurPage", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.historique.chercherEcriture", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.historique.supprimerSurPage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.historique.modifierSurPage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.historique.chercherSurPage", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.historique.ajouterSurPage", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.historique.supprimerSuspens", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.historique.extourne", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.historique.copie", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.compte.transfertAffilie.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.inscriptions.journal.comptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.inscriptions.journal.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.inscriptions.journal.afficherDepuisEcriture", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.inscriptions.declaration.importer", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.splitting.revenuSplitting.chercherRevenu", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.domicileSplitting.chercherDomicile", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.apercuRCI", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.afficherDepuisMandat", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.imprimerAnalyse.afficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.annulerDossier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.rouvrir", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.rouvrirDossier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.revoquer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.revoquerDossier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.apercuRCI", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.mandatSplitting.chercherMandat", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.ouvrir", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.imprimerAccuseInvitation",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.executerSplitting", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.apercuRCI", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.executerSplitting", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.mandatSplitting.revoquer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.mandatSplitting.revoquer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.mandatSplitting.apercuRCI", FWSecureConstants.READ);

        FWAction.registerActionCustom("pavo.compte.periodeSplitting.chercherPeriodeSplitting", FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.rassemblementOuverture.chercherRassemblementOuverture",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.rassemblementOuverture.chercherEcrituresRassemblement",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("pavo.compte.rassemblementOuverture.envoiCI", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.rassemblementOuverture.envoiCIConjoint", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.bta.inscriptionBta.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.bta.inscriptionRetroBta.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.comparaison.comparaisonCorrigeAnomaliesProcess.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.comparaison.comparaisonEnteteParcoursProcess.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.comparaison.comparaisonEnteteProcess.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.eclaterExecuter.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.compte.ecriture.eclaterExecuter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.inscriptions.declaration.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.bta.repetitionBta.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.process.conversionGenre6.exectuer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.inscriptions.journalComptabiliser.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.compte.ecritureGenre6.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.annulerDossierExecuter.afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("pavo.splitting.dossierSplitting.annulerDossierExecuter",
                FWSecureConstants.UPDATE);

        FWAction.registerLinkedAction("pavo.splitting.dossierSplitting.apercuRCI",
                "hermes.parametrage.attenteReception.chercher");
        FWAction.registerLinkedAction("pavo.splitting.mandatSplitting.apercuRCI",
                "hermes.parametrage.attenteReception.chercher");
        FWAction.registerLinkedAction("pavo.compte.compteIndividuel.chercherDetailAnnonce",
                "hermes.parametrage.attenteReception.afficher");
    }

    /**
     * Effectue une annonce ARC. Date de création : (11.12.2002 09:17:50)
     * 
     * @param transaction
     *            la transaction utilisée lors de l'annonce.
     * @param attributs
     *            les attributs de l'annonce.
     * @return l'id de l'annonce ou null si un problème est survenu.
     * @deprecated
     */
    @Deprecated
    public String annonceARC(BTransaction transaction, BITransaction remoteTransaction, HashMap<?, ?> attributs) {
        String idAnnonce = null;
        if ((transaction != null) && !transaction.hasErrors()) {
            try {
                // BSession local = transaction.getSession();
                // BISession remoteSession = getSessionAnnonce(local);
                // remoteTransaction = remoteSession.newTransaction();
                // remoteTransaction.openTransaction();
                // création de l'API
                IHEInputAnnonce remoteEcritureAnnonce = (IHEInputAnnonce) remoteTransaction.getISession().getAPIFor(
                        IHEInputAnnonce.class);
                // attributs standard ARC
                remoteEcritureAnnonce.setIdProgramme(CIApplication.DEFAULT_APPLICATION_PAVO);
                remoteEcritureAnnonce.setUtilisateur(remoteTransaction.getISession().getUserId());
                remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
                remoteEcritureAnnonce.put(
                        IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        "PAVO//"
                                + DateUtils.convertDate(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ,
                                        DateUtils.JJMM_DOTS));
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, CIApplication.CODE_APPLICATION_ARC);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                        this.getProperty(CIApplication.CODE_CAISSE));
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                        this.getProperty(CIApplication.CODE_AGENCE));
                // ajout de attributs spécifique à l'annonce
                remoteEcritureAnnonce.putAll(attributs);
                remoteEcritureAnnonce.add(remoteTransaction);
                if (!remoteTransaction.hasErrors()) {
                    idAnnonce = remoteEcritureAnnonce.getRefUnique();
                }
            } catch (Exception ex) {
                // annonce invalide
                transaction.addErrors(transaction.getSession().getLabel("MSG_ANNONCE_INPUT_INVALID"));
                System.out.println(ex);
            }
        }
        return idAnnonce;
    }

    public String annonceARC(BTransaction transaction, HashMap<?, ?> attributs, boolean withErrors) {
        return this.annonceARC(transaction, attributs, withErrors, true);
    }

    public String annonceARC(BTransaction transaction, HashMap<?, ?> attributs, boolean withErrors,
            boolean wantPlausiCIOuvert) {
        return this.annonceARC(transaction, attributs, withErrors, wantPlausiCIOuvert, "");
    }

    /**
     * Effectue une annonce ARC. Date de création : (11.12.2002 09:17:50)
     * 
     * @param transaction
     *            la transaction utilisée lors de l'annonce.
     * @param attributs
     *            les attributs de l'annonce.
     * @param withErrors
     *            met la transaction en erreur si l'annonce n'a pas pu être créée.
     * @return l'id de l'annonce ou null si un problème est survenu.
     */
    public String annonceARC(BTransaction transaction, HashMap<?, ?> attributs, boolean withErrors,
            boolean wantPlausiCIOuvert, String user) {
        String idAnnonce = null;
        if ((transaction != null) && !transaction.hasErrors()) {
            BITransaction remoteTransaction = null;
            BSession local = transaction.getSession();
            try {
                BISession remoteSession = getSessionAnnonce(local);
                remoteTransaction = ((BSession) remoteSession).newTransaction();
                remoteTransaction.openTransaction();
                // création de l'API
                IHEInputAnnonce remoteEcritureAnnonce = (IHEInputAnnonce) remoteSession
                        .getAPIFor(IHEInputAnnonce.class);
                // attributs standard ARC
                if (wantPlausiCIOuvert) {
                    remoteEcritureAnnonce.wantCheckCiOuvert(IHEAnnoncesViewBean.WANT_CHECK_CI_OUVERT_TRUE);
                } else {
                    remoteEcritureAnnonce.wantCheckCiOuvert(IHEAnnoncesViewBean.WANT_CHECK_CI_OUVERT_FALSE);
                }
                remoteEcritureAnnonce.setIdProgramme(CIApplication.DEFAULT_APPLICATION_PAVO);
                if (JadeStringUtil.isBlank(user) == false) {
                    remoteEcritureAnnonce.setUtilisateur(user);
                } else {
                    remoteEcritureAnnonce.setUtilisateur(local.getUserId());
                }
                remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, CIApplication.CODE_APPLICATION_ARC);
                remoteEcritureAnnonce.put(
                        IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        "PAVO//"
                                + DateUtils.convertDate(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ,
                                        DateUtils.JJMM_DOTS));
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                        this.getProperty(CIApplication.CODE_CAISSE));
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                        this.getProperty(CIApplication.CODE_AGENCE));
                // ajout de attributs spécifique à l'annonce
                remoteEcritureAnnonce.putAll(attributs);
                // todo: même transaction mais le rollback ne fonctionne pas ->
                // fw
                remoteEcritureAnnonce.add(remoteTransaction);
                // remoteEcritureAnnonce.add(transaction);
                if (remoteTransaction.hasErrors()) {
                    if (withErrors) {
                        transaction.addErrors(local.getLabel("MSG_ANNONCE_INPUT_INVALID"));
                    }
                    JadeLogger.warn(remoteEcritureAnnonce, local.getLabel("MSG_ANNONCE_INPUT_INVALID") + " ("
                            + attributs.get(IHEAnnoncesViewBean.NUMERO_ASSURE) + "): " + remoteTransaction.getErrors());
                    remoteTransaction.rollback();
                } else {
                    // annonce ok
                    idAnnonce = remoteEcritureAnnonce.getRefUnique();
                    remoteTransaction.commit();
                }
            } catch (Exception ex) {
                // annonce invalide
                if (withErrors) {
                    transaction.addErrors(local.getLabel("MSG_ANNONCE_INPUT_INVALID"));
                }
                JadeLogger.warn(
                        this,
                        local.getLabel("MSG_ANNONCE_INPUT_INVALID") + " ("
                                + attributs.get(IHEAnnoncesViewBean.NUMERO_ASSURE) + ")");
                // FWDebug.println(FWDebug.ERROR, ex);
            } finally {
                if (remoteTransaction != null) {
                    try {
                        remoteTransaction.closeTransaction();
                    } catch (Exception ec) {
                    }
                }
            }
        }
        return idAnnonce;
    }

    /**
     * Recherche l'administration (caisse/agence). Date de création : (11.12.2002 09:17:50)
     * 
     * @param session
     *            la session de l'utilisateur.
     * @param caisse
     *            le numéro de la caisse.
     * @param agence
     *            le numéro de l'agence. Peut être null (pas d'agence).
     * @param methodsToLoad
     *            les méthodes à charger par l'API
     * @return l'administration. Peut être vide si une erreur est survenue.
     */
    public ITIAdministration getAdministration(BSession session, String caisse, String agence, String[] methodsToLoad) {
        caisse = CIUtil.unPadAdmin(caisse);
        agence = CIUtil.unPadAdmin(agence);
        ITIAdministration admin = new ITIAdministrationHelper();
        try {
            BISession remoteSession = session;
            // création de l'API
            ITIAdministration remoteAdmin = (ITIAdministration) remoteSession.getAPIFor(ITIAdministration.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITIAdministration.FIND_FOR_GENRE_ADMINISTRATION, "509001");

            if ((agence == null) || JadeStringUtil.isBlank(agence)) {
                // remoteAdmin.setCodeAdministration(caisse);
                params.put(ITIAdministration.FIND_FOR_CODE_ADMINISTRATION, caisse);
            } else {
                // remoteAdmin.setCodeAdministration(caisse + "." + agence);
                params.put(ITIAdministration.FIND_FOR_CODE_ADMINISTRATION, caisse + "." + agence);
            }
            // remoteAdmin.setAlternateKey(new Integer(1));
            if (methodsToLoad != null) {
                remoteAdmin.setMethodsToLoad(methodsToLoad);
            }
            // todo: new remoteTransaction ou pas?
            // remoteAdmin.retrieve(remoteSession.newTransaction());

            Object[] res = remoteAdmin.find(params);
            if (!remoteSession.hasErrors()) {

                if (res.length > 0) {
                    // tiers trouvé
                    if (res[0] instanceof GlobazValueObject) {

                        GlobazValueObject globazObj = (GlobazValueObject) res[0];

                        admin = (ITIAdministration) session.getAPIFor(ITIAdministration.class.getName(), globazObj);
                    }
                }
            }
        } catch (Exception ex) {
            // requête invalide
            // System.out.println("help");
        }
        return admin;
    }

    /**
     * Recherche l'administration (caisse/agence). Date de création : (11.12.2002 09:17:50)
     * 
     * @param session
     *            la session de l'utilisateur.
     * @param idTiers
     *            l'id tiers de l'administration
     * @param methodsToLoad
     *            les méthodes à charger par l'API
     * @return l'administration. Peut être vide si une erreur est survenue.
     */
    public ITIAdministration getAdministration(BSession session, String idTiers, String[] methodsToLoad) {
        ITIAdministration admin = new ITIAdministrationHelper();
        try {
            BISession remoteSession = getSessionTiers(session);
            // création de l'API
            // BISession remoteSession = session;
            ITIAdministration remoteAdmin = (ITIAdministration) remoteSession.getAPIFor(ITIAdministration.class);
            remoteAdmin.setIdTiersAdministration(idTiers);
            if (methodsToLoad != null) {
                remoteAdmin.setMethodsToLoad(methodsToLoad);
            }
            // todo: new remoteTransaction ou pas?
            remoteAdmin.retrieve(((BSession) remoteSession).newTransaction());
            if (!remoteSession.hasErrors()) {
                // tiers trouvé
                admin = remoteAdmin;
            }
        } catch (Exception ex) {
            // requête invalide
            ex.printStackTrace();
        }
        return admin;
    }

    /**
     * Retourne l'administration la caisse actuelle. Date de création : (13.12.2002 07:35:24)
     * 
     * @param local
     *            la session locale
     * @return l'administration locale. Peut être vide si une erreur est survenue.
     */
    public ITIAdministration getAdministrationLocale(BSession local) {
        ITIAdministration admin = (ITIAdministration) local.getAttribute(CIApplication.KEY_SESSION_CAISSE);
        if (admin == null) {
            // pas encore chargé
            admin = this.getAdministration(local, this.getProperty(CIApplication.CODE_CAISSE),
                    this.getProperty(CIApplication.CODE_AGENCE), null);
            local.setAttribute(CIApplication.KEY_SESSION_CAISSE, admin);
        }
        return admin;
    }

    /**
     * Retourne l'application pour la recherche de l'adresse du tiers. Date de création : (24.01.2003 07:46:24)
     * 
     * @return l'application pour la recherche de l'adresse du tiers.
     */
    public String getAdresseApplication() {
        return this.getProperty(CIApplication.ADRESSE_APPLICATION_PYXIS);
    }

    /**
     * Method getAffileFormater.
     * 
     * @return IFormatData
     * @throws Exception
     */
    public IFormatData getAffileFormater() throws Exception {

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
     * Recherche le tiers en fonction de sa clé primaire. Date de création : (11.12.2002 09:17:50)
     * 
     * @param session
     *            la session de l'utilisateur.
     * @param l
     *            'id de l'affilié.
     * @param methodsToLoad
     *            les méthodes à charger par l'API
     * @return le tiers. Peut être vide si une erreur est survenue.
     */
    public AFAffiliation getAffilie(BSession session, String id, String[] methodsToLoad) {
        /*
         * ITIPersonneAvs personne = new ITIPersonneAvsHelper(); try { AFAffiliationManager affMgr = new
         * AFAffiliationManager(); affMgr.setSession(session); affMgr.setForAffiliationId(id); affMgr.find(); if
         * (!affMgr.isEmpty()) { BISession remoteSession = getSessionTiers(session); // création de l'API ITIPersonneAvs
         * remotePersonne = (ITIPersonneAvs) remoteSession.getAPIFor(ITIPersonneAvs.class);
         * remotePersonne.setIdTiers(((AFAffiliation) affMgr.getFirstEntity()).getTiersId()); if (methodsToLoad != null)
         * { remotePersonne.setMethodsToLoad(methodsToLoad); } // todo: new remoteTransaction ou pas?
         * remotePersonne.retrieve(remoteSession.newTransaction()); //if (!remoteSession.hasErrors()) { // tiers trouvé
         * personne = remotePersonne; //} } } catch (Exception ex) { // requête invalide } return personne;
         */
        AFAffiliationManager affMgr = new AFAffiliationManager();
        AFAffiliation aff = new AFAffiliation();
        try {
            affMgr.setSession(session);
            affMgr.setForAffiliationId(id);
            affMgr.find();
            if (!affMgr.isEmpty()) {
                aff = (AFAffiliation) affMgr.getFirstEntity();
            }
        } catch (Exception ex) {
        }
        return aff;
    }

    /**
     * Recherche le tiers en fonction de sa clé primaire. Date de création : (11.12.2002 09:17:50)
     * 
     * @param transaction
     *            la transaction courante.
     * @param l
     *            'id de l'affilié.
     * @param methodsToLoad
     *            les méthodes à charger par l'API
     * @return le tiers. Peut être vide si une erreur est survenue.
     */
    public AFAffiliation getAffilie(BTransaction transaction, String id, String[] methodsToLoad) {
        /*
         * ITIPersonneAvs personne = new ITIPersonneAvsHelper(); try { AFAffiliationManager affMgr = new
         * AFAffiliationManager(); affMgr.setSession(transaction.getSession()); affMgr.setForAffiliationId(id);
         * affMgr.find(transaction); if (!affMgr.isEmpty()) { BISession remoteSession =
         * getSessionTiers(transaction.getSession()); // création de l'API ITIPersonneAvs remotePersonne =
         * (ITIPersonneAvs) remoteSession.getAPIFor(ITIPersonneAvs.class); remotePersonne.setIdTiers(((AFAffiliation)
         * affMgr.getFirstEntity()).getTiersId()); if (methodsToLoad != null) {
         * remotePersonne.setMethodsToLoad(methodsToLoad); } remotePersonne.retrieve(transaction); //if
         * (!remoteSession.hasErrors()) { // tiers trouvé personne = remotePersonne; //} } } catch (Exception ex) { //
         * requête invalide } return personne;
         */
        AFAffiliationManager affMgr = new AFAffiliationManager();
        AFAffiliation aff = new AFAffiliation();
        try {
            affMgr.setSession(transaction.getSession());
            affMgr.setForAffiliationId(id);
            affMgr.find(transaction);
            if (!affMgr.isEmpty()) {
                aff = (AFAffiliation) affMgr.getFirstEntity();
            }
        } catch (Exception ex) {
        }
        return aff;
    }

    /**
     * retourne l'affilié en fonction du numéro d'affilié. si forParitaire et forPersonnel sont à false aucun filtre
     * n'est effectué
     * 
     * @param session
     * @param numeroAffilie
     * @param forParitaire
     *            true pour filtrer sur les affiliés paritaires
     * @param forPersonnel
     *            true pour filtrer sur les affiliés personnels
     * @param moisD
     * @param moisF
     * @param annee
     * @param jourDebut
     * @param jourFin
     * @return
     */
    public AFAffiliation getAffilieByNo(BSession session, String numeroAffilie, boolean forParitaire,
            boolean forPersonnel, String moisD, String moisF, String annee, String jourDebut, String jourFin) {

        if (JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            return null;
        }

        try {
            // Recherche des affiliations selon critères
            AFAffiliationManager affMgr = new AFAffiliationManager();
            affMgr.setSession(session);
            affMgr.setForAffilieNumero(numeroAffilie);

            // Pour les paritaires
            if (forParitaire) {
                affMgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
                affMgr.setForTypesAffParitaires();
            }

            // Pour les personnelles
            if (forPersonnel) {
                affMgr.setForTypesAffPersonelles();
            }

            affMgr.find(BManager.SIZE_NOLIMIT);

            // Si année non rempli, nous donnons la première affiliation
            if (JadeStringUtil.isEmpty(annee) || "0".equals(annee)) {
                return (AFAffiliation) affMgr.getFirstEntity();
            }

            AFAffiliation aff = null;
            // Boucle sur chaque affiliation afin de rechercher le bon selon les dates données
            for (int i = 0; i < affMgr.size(); i++) {
                aff = (AFAffiliation) affMgr.getEntity(i);

                if (isInAffiliation(session, aff.getDateDebut(), aff.getDateFin(), jourDebut, jourFin, moisD, moisF,
                        annee)) {
                    return aff;
                }
            }
        } catch (Exception e) {
            JadeLogger.info(e, e.getMessage());
        }

        return null;
    }

    public boolean isInAffiliation(BSession session, final String debutAffiliation, final String finAffiliation,
            final String jourDebutATester, final String jourFinATester, final String moisDebutATester,
            final String moisFinATester, final String anneeATester) throws Exception {

        if (debutAffiliation == null || finAffiliation == null) {
            return false;
        }

        // Si nous avons l'année, mais pas le mois de début
        if (JadeStringUtil.isIntegerEmpty(moisDebutATester) || "66".equals(moisDebutATester)
                || "77".equals(moisDebutATester) || "99".equals(moisDebutATester)) {

            int anneeInt = Integer.parseInt(anneeATester);
            int anneeDebutAff = Integer.parseInt(debutAffiliation.substring(6));

            // Si année début donnée plus petite que l'année de début de l'affiliation, on passe au suivant
            if (anneeInt < anneeDebutAff) {
                return false;
            }

            // Si une date de fin à l'affiliation
            if (!JAUtil.isDateEmpty(finAffiliation)) {
                int anneeFinAff = Integer.parseInt(finAffiliation.substring(6));
                // Si année fin donnée plus grande que l'année de fin de l'affiliation, on passe au suivant
                if (anneeInt > anneeFinAff) {
                    return false;
                }
            }

            return true;

            // Si nous avons l'année et le mois de début, mais pas le jour de début
        } else if (!JadeStringUtil.isIntegerEmpty(moisDebutATester) && JadeStringUtil.isBlankOrZero(jourDebutATester)) {

            final String moisAnneeDebutAff = debutAffiliation.substring(3);
            final String moisAnneeDebutDonnee = JadeStringUtil.rightJustifyInteger(moisDebutATester, 2) + "."
                    + anneeATester;

            // Vrai si la date début (mois.annee) de l'affiliation est plus petite ou égale avec la date de
            // début (mois.année) donnée
            final boolean resultDebut = BSessionUtil.compareDateFirstLowerOrEqual(session, moisAnneeDebutAff,
                    CIUtil.padDate(moisAnneeDebutDonnee));

            // Si Faux, on passe au suivant
            if (!resultDebut) {
                return false;
            }

            // Si une date de fin à l'affiliation
            if (!JAUtil.isDateEmpty(finAffiliation)) {

                String moisFinATesterClone = moisFinATester;
                if (JadeStringUtil.isIntegerEmpty(moisFinATesterClone)) {
                    moisFinATesterClone = "12";
                }

                final String moisAnneeFinAff = finAffiliation.substring(3);
                final String moisAnneeFinDonnee = JadeStringUtil.rightJustifyInteger(moisFinATesterClone, 2) + "."
                        + anneeATester;

                // Vrai si la date de fin (mois.année) de l'affiliation est plus grande ou égale avec la date de
                // fin (mois.année) donnée
                final boolean resultFin = BSessionUtil.compareDateFirstGreaterOrEqual(session, moisAnneeFinAff,
                        CIUtil.padDate(moisAnneeFinDonnee));

                // Si Faux, on passe au suivant
                if (!resultFin) {
                    return false;
                }
            }

            return true;

            // Si nous avons l'année, le mois de début et le jour de début
        } else {

            String jourDebutATesterClone = jourDebutATester;
            if (JadeStringUtil.isBlankOrZero(jourDebutATesterClone)) {
                jourDebutATesterClone = "01";
            }

            final String jourMoisAnneeDebutAff = debutAffiliation;
            final String jourMoisAnneeDebutDonnee = JadeStringUtil.rightJustifyInteger(jourDebutATesterClone, 2) + "."
                    + JadeStringUtil.rightJustifyInteger(moisDebutATester, 2) + "." + anneeATester;

            // Vrai si la date de début (jour.mois.année) de l'affiliation est plus petite ou égale avec la date
            // de début (jour.mois.année) donnée
            final boolean resultDebut = BSessionUtil.compareDateFirstLowerOrEqual(session, jourMoisAnneeDebutAff,
                    CIUtil.padDate(jourMoisAnneeDebutDonnee));

            // Si Faux, on passe au suivant
            if (!resultDebut) {
                return false;
            }

            if (!JAUtil.isDateEmpty(finAffiliation)) {

                String moisFinATesterClone = moisFinATester;
                if (JadeStringUtil.isIntegerEmpty(moisFinATesterClone)) {
                    moisFinATesterClone = "12";
                }

                String jourFinATesterClone = jourFinATester;
                if (JadeStringUtil.isBlankOrZero(jourFinATesterClone)) {
                    jourFinATesterClone = returnJourFinSelonMois(moisFinATesterClone);
                }

                final String jourMoisAnneeFinAff = finAffiliation;
                final String jourMoisAnneeFinDonnee = JadeStringUtil.rightJustifyInteger(jourFinATesterClone, 2) + "."
                        + JadeStringUtil.rightJustifyInteger(moisFinATesterClone, 2) + "." + anneeATester;

                // Vrai si la date de fin (jour.mois.année) de l'affiliation est plus grande ou égale avec la
                // date de fin (jour.mois.année) donnée
                final boolean resultFin = BSessionUtil.compareDateFirstGreaterOrEqual(session, jourMoisAnneeFinAff,
                        CIUtil.padDate(jourMoisAnneeFinDonnee));

                if (!resultFin) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * retourne l'affilié en fonction du numéro d'affilié. si forParitaire et forPersonnel sont à false aucun filtre
     * n'est effectué. Cette méthode prend aussi les affiliés qui sont radiés. Créée pour des besoins suite au mandat
     * inforom 451 et bz 8594
     * 
     * @param session
     * @param numeroAffilie
     * @param forParitaire
     *            true pour filtrer sur les affiliés paritaires
     * @param forPersonnel
     *            true pour filtrer sur les affiliés personnels
     * @param annee
     * @return AFAffiliation
     */
    public AFAffiliation getAffilieByNoIncludeRadie(BSession session, String numeroAffilie, boolean forParitaire,
            boolean forPersonnel, String annee) {
        if (session == null) {
            throw new NullPointerException("la session ne doit pas être nulle");
        }
        if (numeroAffilie == null) {
            throw new NullPointerException("le numéro d'affilié ne doit pas être null");
        }
        AFAffiliationManager affMgr = new AFAffiliationManager();
        AFAffiliation afftoreturn = null;
        try {
            affMgr.setSession(session);
            affMgr.setForAffilieNumero(numeroAffilie);
            if (forParitaire) {
                affMgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
                affMgr.setForTypesAffParitaires();
            }
            if (forPersonnel) {
                affMgr.setForTypesAffPersonelles();
            }
            affMgr.setOrder("MADFIN ASC");
            affMgr.find();
            if (JadeStringUtil.isBlankOrZero(annee)) {
                return (AFAffiliation) affMgr.getFirstEntity();
            }
            int mgrSize = affMgr.getSize();
            if (mgrSize >= 1) {
                afftoreturn = (AFAffiliation) affMgr.getFirstEntity();
                if (!JadeStringUtil.isBlankOrZero(afftoreturn.getDateFin())) {
                    afftoreturn = (AFAffiliation) affMgr.getEntity(mgrSize - 1);
                }
            }
            return afftoreturn;
        } catch (Exception ex) {
            JadeLogger.error(this, ex);
        }
        return null;
    }

    public String getAffiliesByNo(BSession session, String numeroAffilie) {
        AFAffiliationManager affMgr = new AFAffiliationManager();
        AFAffiliation aff = null;
        StringBuffer buf = new StringBuffer();
        try {
            affMgr.setSession(session);
            affMgr.setForAffilieNumero(numeroAffilie);
            affMgr.find();
            for (int i = 0; i < affMgr.size(); i++) {
                aff = (AFAffiliation) affMgr.getEntity(i);
                if (i != 0) {
                    buf.append(",");
                }
                buf.append(aff.getAffiliationId());
            }
        } catch (Exception ex) {
        }
        return buf.toString();
    }

    public ArrayList<String> getAnnoncesABloquer() {
        if (annoncesABloquer == null) {
            annoncesABloquer = new ArrayList<String>();
            StringTokenizer token = new StringTokenizer(this.getProperty(CIApplication.ANNONCES_A_BLOQUER), ",");
            while (token.hasMoreElements()) {
                annoncesABloquer.add(token.nextToken());
            }
        }
        return annoncesABloquer;
    }

    /**
     * Retourne l'API des annonces ARC (hermes). Date de création : (04.12.2002 11:25:47)
     * 
     * @param session
     *            la session de l'utilisateur (client)
     * @return un object du type {@link globaz.hermes.api.ITIPersonneAvs}
     * @deprecated
     */
    @Deprecated
    public IHEInputAnnonce getAPIAnnonce(BSession session) throws Exception {
        BIApplication application = getApplication(CIApplication.ANNONCE_APP);
        if (application != null) {
            return (IHEInputAnnonce) application.newSession(session).getAPIFor(IHEInputAnnonce.class);
        }
        return null;
    }

    /**
     * Retourne l'API du tiers (pyxis). Date de création : (04.12.2002 11:25:47)
     * 
     * @param session
     *            la session de l'utilisateur (client)
     * @return un object du type {@link globaz.pyxis.api.ITIPersonneAvs}
     * @deprecated
     */
    @Deprecated
    public ITIPersonneAvs getAPITiers(BSession session) throws Exception {
        BIApplication application = getApplication(CIApplication.TIERS_APP);
        if (application != null) {
            return (ITIPersonneAvs) application.newSession(session).getAPIFor(ITIPersonneAvs.class);
        }
        return null;
    }

    /**
     * Retourne l'application selon le nom donné. Ce nom doit se trouver dans le fichier de configuration.<br>
     * <tt>applicationExterne.tiers=PYXIS</tt><br>
     * où tiers est le nom à donner en paramètre. Date de création : (04.12.2002 11:25:47)
     * 
     * @param application
     *            le nom de l'application référencé dans le fichier properties
     * @return un object du type {@link globaz.globall.api.BIApplication}
     * @exception si
     *                une exception survient.
     */
    public BIApplication getApplication(String application) throws Exception {
        return GlobazSystem.getApplication(this.getProperty("applicationExterne." + application));
    }

    public int getAutoDigitAffilie() {

        String temp = this.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
        return Integer.valueOf(temp).intValue();

        // return getProperty(AUTODIGITAFF);
    }

    public String getCaisseCompForExtrait() {
        return this.getProperty(CIApplication.CC_FOR_EXTRAIT);
    }

    /**
     * Retourne l'application pour la recherche du numéro de caisse Date de création : (24.01.2003 07:46:24)
     * 
     * @return l'application pour la recherche de l'adresse du tiers.
     */
    public String getCaisseInterne() {
        return this.getProperty(CIApplication.CODE_CAISSE);
    }

    /**
     * Retourne la valeur de la propriété indiquant si le parsing pucs 4 est activé
     * 
     * @return la valeur de la propriété PROPERTY_ACTIVER_PARSING_PUCS_4 (false par défaut)
     */
    public String getPropertyActiverParsingPUCS4() {
        return this.getProperty(CIApplication.PROPERTY_ACTIVER_PARSING_PUCS_4, "false");
    }

    public ArrayList<String> getChampsAffiche() {

        if (champsAffiche == null) {
            champsAffiche = new ArrayList<String>();
            StringTokenizer token = new StringTokenizer(this.getProperty(CIApplication.CHAMP_AFFICH), ",");
            while (token.hasMoreElements()) {
                champsAffiche.add(token.nextToken());
            }
        }
        return champsAffiche;
    }

    public ArrayList<String> getCodesComparaisonAComparer() {
        if (codesCompraisonToCompare == null) {
            codesCompraisonToCompare = new ArrayList<String>();
            StringTokenizer token = new StringTokenizer(this.getProperty(CIApplication.WANTED_TO_COMPARE).trim(), ",");
            while (token.hasMoreElements()) {
                codesCompraisonToCompare.add(token.nextToken());
            }
        }
        return codesCompraisonToCompare;
    }

    public ArrayList<String> getCodesComparaisonAcorriger() {
        if (codesComparaisonToCorrect == null) {
            codesComparaisonToCorrect = new ArrayList<String>();
            StringTokenizer token = new StringTokenizer(this.getProperty(CIApplication.WANTED_TO_CORRECT).trim(), ",");
            while (token.hasMoreElements()) {
                codesComparaisonToCorrect.add(token.nextToken());
            }
        }
        return codesComparaisonToCorrect;
    }

    public ArrayList<String> getColonneAMasquer() {
        if (colonnesAMasquer == null) {
            colonnesAMasquer = new ArrayList<String>();
            StringTokenizer token = new StringTokenizer(this.getProperty(CIApplication.COLONNESAMASQUER), ",");
            while (token.hasMoreElements()) {
                colonnesAMasquer.add(token.nextToken());
            }
        }
        return colonnesAMasquer;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsPaysListe(BSession session) throws Exception {
        if ((csPaysListe == null) || csPaysListe.isEmpty()
                || !csPaysListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csPaysListe = new FWParametersSystemCodeManager();
            csPaysListe.setForIdGroupe("CIPAYORI");
            csPaysListe.setForIdTypeCode("10300015");
            csPaysListe.setSession(session);
            csPaysListe.find(BManager.SIZE_NOLIMIT);
        }
        return csPaysListe;
    }

    /**
     * Method getCsSexeListe.
     * 
     * @param session
     * @return FWParametersSystemCodeManager
     * @throws Exception
     */
    public FWParametersSystemCodeManager getCsSexeListe(BSession session) throws Exception {
        if ((csSexeListe == null) || csSexeListe.isEmpty()
                || !csSexeListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csSexeListe = new FWParametersSystemCodeManager();
            csSexeListe.setForIdGroupe("CISEXE");
            csSexeListe.setForIdTypeCode("10300016");
            csSexeListe.setSession(session);
            csSexeListe.find();
        }
        return csSexeListe;
    }

    public String getDefaultCSForDS() {
        // Si pas renseigné, par défaut PUCS
        return this.getProperty(CIApplication.CS_DEFAULT_FORMAT_DE, "327007");
    }

    /**
     * Retourne l'email admin. Date de création : (24.01.2003 07:46:24)
     * 
     * @return le nom du rôle responsable CI.
     */
    public String getEmailAdmin() {
        return this.getProperty(CIApplication.EMAIL_ADMIN, "dgi@globaz.ch");
    }

    /**
     * Retourne l'email admin. Date de création : (24.01.2003 07:46:24)
     * 
     * @return le nom du rôle responsable CI.
     */
    public String getEmailKey() {
        return this.getProperty(CIApplication.USER_EMAIL_KEY, CIApplication.DEFAULT_USER_EMAIL_KEY);
    }

    /**
     * Retourne l'adresse email du responsable des ci. Date de création : (26.11.2002 08:29:01)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return l'adresse email ou null si non trouvé.
     */
    public ArrayList<String> getEMailResponsableCI(BTransaction transaction) throws Exception {
        // recherche de l'email des responsable des ci (non implémenté
        // actuellement au FW)
        ArrayList<String> email = new ArrayList<String>();
        if (JadeStringUtil.isBlank(getRoleResponsableCI())) {
            // aucune information dans le fichier properties
            return null;
        }

        // recherche des groupes ayant ce rôle
        if (!JadeStringUtil.isBlank(getRoleResponsableCI())) {
            JadeRoleGroupService roleGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getRoleGroupService();
            JadeUserGroupService userGroupService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserGroupService();
            JadeUserService userService = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator()
                    .getUserService();
            Map<String, String> criteres = new HashMap<String, String>();
            criteres.put(JadeRoleGroupService.CR_FOR_IDROLE, getRoleResponsableCI());
            JadeRoleGroup[] groups = roleGroupService.findForCriteres(criteres);
            if (groups != null) {
                for (int i = 0; i < groups.length; i++) {
                    String[] userids = userGroupService.findAllIdUserForIdGroup(groups[i].getIdGroup());
                    if (userids != null) {
                        for (int j = 0; j < userids.length; j++) {
                            JadeUser user = userService.load(userids[j]);
                            if ((user != null) && (!JadeStringUtil.isBlank(user.getEmail()))) {
                                email.add(user.getEmail());
                            }
                        }
                    }
                }
            }
        }
        if (email.size() == 0) {
            email.add(transaction.getSession().getApplication().getProperty(CIApplication.EMAIL_ADMIN));
        }
        return email;
    }

    public String getIdRubrique() {
        return this.getProperty(CIApplication.ID_RUBRIQUE).trim();
    }

    public int getNbAssuresNNSS() {

        String temp = this.getProperty(CIApplication.NB_ASSURE_NNSS, "20").trim();
        return Integer.valueOf(temp).intValue();

        // return getProperty(AUTODIGITAFF);
    }

    public String getNumeroTicketsEtuditants() {
        return this.getProperty(CIApplication.NUMERO_TICKET_ETUDIANT).trim();

    }

    /**
     * Retourne une session remote en fonction de la clé donnée. Date de création : (13.12.2002 07:35:24)
     * 
     * @param local
     *            la session locale
     * @param sessionKey
     *            <tt>KEY_SESSION_PYXIS</tt> ou <tt>KEY_SESSION_HERMES</tt>
     * @return la session remote de HERMES ou PYXIS
     * @exception si
     *                un erreur survient.
     * @deprecated
     */
    @Deprecated
    public BISession getRemoteSession(BSession local, String sessionKey) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(sessionKey);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(CIApplication.ANNONCE_APP).newSession(local);
            local.setAttribute(sessionKey, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Retourne le nom du rôle responsable CI. Date de création : (24.01.2003 07:46:24)
     * 
     * @return le nom du rôle responsable CI.
     */
    public String getRoleResponsableCI() {
        return this.getProperty(CIApplication.ROLE_CI);
    }

    /**
     * Retourne la session remote de HERMES. Date de création : (13.12.2002 07:35:24)
     * 
     * @param local
     *            la session locale
     * @return la session remote de HERMES
     * @exception si
     *                un erreur survient.
     */
    public BISession getSessionAnnonce(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(CIApplication.KEY_SESSION_HERMES);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(CIApplication.ANNONCE_APP).newSession(local);
            local.setAttribute(CIApplication.KEY_SESSION_HERMES, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Retourne la session remote de PYXIS. Date de création : (13.12.2002 07:35:24)
     * 
     * @param local
     *            la session locale
     * @return la session remote de PYXIS
     * @exception si
     *                un erreur survient.
     */
    public BISession getSessionTiers(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(CIApplication.KEY_SESSION_PYXIS);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication(CIApplication.TIERS_APP).newSession(local);
            local.setAttribute(CIApplication.KEY_SESSION_PYXIS, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    public int getSpecialistLevel() {
        String level = this.getProperty(CIApplication.SPECIALIST_LEVEL_KEY,
                String.valueOf(CIApplication.DEFAULT_SPECIALIST_LEVEL));
        try {
            return Integer.parseInt(level);
        } catch (Exception ex) {
            return CIApplication.DEFAULT_SPECIALIST_LEVEL;
        }
    }

    public int getTailleChampsAffilie() {
        String temp = this.getProperty(CommonProperties.KEY_TAILLE_CHAMPS_AFF);
        return Integer.valueOf(temp).intValue();
    }

    /**
     * Recherche le tiers en fonction d'un no avs. Date de création : (11.12.2002 09:17:50)
     * 
     * @param session
     *            la session de l'utilisateur.
     * @param avs
     *            le numéro avs.
     * @param methodsToLoad
     *            les méthodes à charger par l'API
     * @return le tiers. Peut être vide si une erreur est survenue.
     */
    public ITIPersonneAvs getTiersByAvs(BSession session, String avs, String[] methodsToLoad) {
        ITIPersonneAvs personne = new ITIPersonneAvsHelper();
        try {
            BISession remoteSession = getSessionTiers(session);
            // création de l'API
            ITIPersonneAvs remotePersonne = (ITIPersonneAvs) remoteSession.getAPIFor(ITIPersonneAvs.class);
            remotePersonne.setNumAvsActuel(avs);
            remotePersonne.setAlternateKey(new Integer(1));
            if (methodsToLoad != null) {
                remotePersonne.setMethodsToLoad(methodsToLoad);
            }
            remotePersonne.retrieve(((BSession) remoteSession).newTransaction());
            // if (!remoteSession.hasErrors()) {
            // tiers trouvé
            personne = remotePersonne;
            // }
        } catch (Exception ex) {
            // requête invalide
        }
        return personne;
    }

    /**
     * Recherche le tiers en fonction d'un no avs. Date de création : (11.12.2002 09:17:50)
     * 
     * @param session
     *            la session de l'utilisateur.
     * @param avs
     *            le numéro avs.
     * @param methodsToLoad
     *            les méthodes à charger par l'API
     * @return le tiers. Peut être vide si une erreur est survenue.
     */
    public ITIPersonneAvs getTiersByAvs(BTransaction transaction, String avs, String[] methodsToLoad) {
        ITIPersonneAvs personne = new ITIPersonneAvsHelper();
        try {
            BISession remoteSession = getSessionTiers(transaction.getSession());
            // création de l'API
            ITIPersonneAvs remotePersonne = (ITIPersonneAvs) remoteSession.getAPIFor(ITIPersonneAvs.class);
            remotePersonne.setNumAvsActuel(avs);
            remotePersonne.setAlternateKey(new Integer(1));
            if (methodsToLoad != null) {
                remotePersonne.setMethodsToLoad(methodsToLoad);
            }
            remotePersonne.retrieve(transaction);
            // if (!remoteSession.hasErrors()) {
            // tiers trouvé
            personne = remotePersonne;
            // }
        } catch (Exception ex) {
            // requête invalide
        }
        return personne;
    }

    /**
     * Renvoie true si les journaux d'assurance facultative doivent être affichés
     * 
     * @return true si les journaux d'assurance facultative doivent être affichés
     */
    public boolean isAfficheJournalAssuranceFac() {
        boolean test = Boolean.valueOf(this.getProperty(CIApplication.AFFICHE_JOURNAL_ASSURANCE_FAC, "true").trim())
                .booleanValue();
        return test;
    }

    public boolean isAnciennesAnnonces() {
        return Boolean.valueOf(this.getProperty(CIApplication.ANCIENNES_ANNONCES, "false").trim()).booleanValue();
    }

    /**
     * Renvoie true si la base de donnée ne contient pas d'informations sur la caisse tenant le CI (information encodée
     * dans le type de l'annonce). Date de création : (24.01.2003 07:46:24)
     * 
     * @return le nom du rôle responsable CI.
     */
    public boolean isAnnoncesWA() {
        return Boolean.valueOf(this.getProperty(CIApplication.ANNONCES_WA, "false").trim()).booleanValue();
    }

    public boolean isCaisseDifferente() {
        return Boolean.valueOf(this.getProperty(CIApplication.CAISSE_DIFFERENTE, "false").trim()).booleanValue();
    }

    /**
     * Renvoie true si l'envoi automatique de ci additionnel est requis lors du traitement des ouvertures de CI
     * 
     * @return true si l'envoi automatique de ci additionnel est requis
     */
    public boolean isCaisseFusion() {
        return Boolean.valueOf(this.getProperty(CIApplication.IS_CAISSE_FUSION, "false").trim()).booleanValue();
    }

    /**
     * Renvoie true si l'envoi automatique de ci additionnel est requis lors du traitement des ouvertures de CI
     * 
     * @return true si l'envoi automatique de ci additionnel est requis
     */
    public boolean isCIAddAutomatique() {
        return Boolean.valueOf(this.getProperty(CIApplication.CI_ADD_KEY, "false").trim()).booleanValue();
    }

    public boolean isCIAddNonConforme() {
        return Boolean.valueOf(this.getProperty(CIApplication.CI_ADD_NON_CONFORME, "false").trim()).booleanValue();
    }

    public boolean isCodeSpecialIndiv() {
        return Boolean.valueOf(this.getProperty(CIApplication.CODESPECIALINDIV, "false").trim()).booleanValue();
    }

    public boolean isComparaisonEBCDIC() {
        return Boolean.valueOf(this.getProperty(CIApplication.FORMAT_COMPARAISON_EBCDIC, "false").trim())
                .booleanValue();
    }

    public boolean isGenreZeroPossible() {
        return Boolean.valueOf(this.getProperty(CIApplication.GENRE_ZERO_POSSIBLE, "true").trim()).booleanValue();
    }

    public String returnJourFinSelonMois(String mois) {
        if ("1".equals(mois) || "01".equals(mois)) {
            return "31";
        }
        if ("2".equals(mois) || "02".equals(mois)) {
            return "28";
        }
        if ("3".equals(mois) || "03".equals(mois)) {
            return "31";
        }
        if ("4".equals(mois) || "04".equals(mois)) {
            return "30";
        }
        if ("5".equals(mois) || "05".equals(mois)) {
            return "31";
        }
        if ("6".equals(mois) || "06".equals(mois)) {
            return "30";
        }
        if ("7".equals(mois) || "07".equals(mois)) {
            return "31";
        }
        if ("8".equals(mois) || "08".equals(mois)) {
            return "31";
        }
        if ("9".equals(mois) || "09".equals(mois)) {
            return "30";
        }
        if ("10".equals(mois) || "10".equals(mois)) {
            return "31";
        }
        if ("11".equals(mois) || "11".equals(mois)) {
            return "30";
        }
        if ("12".equals(mois) || "12".equals(mois)) {
            return "31";
        }
        return "31";
    }

    /**
     * Envoie un message à l'administration GLOBAZ. (propriété 'mailAdmin' du fichier <tt>.properties</tt>) Date de
     * création : (03.12.2002 07:46:05)
     * 
     * @param sujet
     *            le sujet du mail.
     * @param message
     *            le message à envoyer.
     * @param provenance
     *            la classe qui envoie ce message.
     */
    public void sendEmailToAdmin(String sujet, String message, Object provenance) throws Exception {
        if (provenance != null) {
            message += "\n\n trace:\n" + provenance;
        }
        JadeSmtpClient.getInstance().sendMail(getEmailAdmin(), sujet, message, null);
    }

    public boolean wantBlocageSecuriteCi() {
        return Boolean.valueOf(this.getProperty(CIApplication.WANT_BLOCAGE_SECURITE_CI, "false").trim()).booleanValue();
    }

    public boolean wantSyncroNra() {
        return Boolean.valueOf(this.getProperty(CIApplication.WANT_SYNCOR_NRA, "false").trim()).booleanValue();
    }

    public boolean wantUpiDaily() {
        return Boolean.valueOf(this.getProperty(CIApplication.WANT_UPI_DAILY, "false").trim()).booleanValue();
    }
}
