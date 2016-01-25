package globaz.musca.application;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.format.IFormatData;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.naos.application.AFApplication;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.external.IntRole;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.api.helper.ITIPersonneAvsHelper;
import globaz.pyxis.db.tiers.TIRole;
import globaz.webavs.common.CommonProperties;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Application MUSCA
 * 
 * @author Emmanuel Fleury
 */
public class FAApplication extends globaz.globall.db.BApplication {
	private static final long serialVersionUID = 1L;	private static final int LENGTH_NSS = 14;    public final static String APPLICATION_MUSCA_REP = "muscaRoot";
    public final static String DEFAULT_APPLICATION_MUSCA = "MUSCA";
    public final static String ELEMENTSPARJOURNAL = "elementsParJournal";
    public final static String FLAG_EXECUTECLEANUP = "executeCleanUp";
    public final static String MONTANT_MINIMEMAX = "montantMinimalMax";
    public final static String MONTANT_MINIMEMAX_DEFVAL = "20";
    public final static String MONTANT_MINIMENEG = "montantMinimalNeg";
    public final static String MONTANT_MINIMENEG_DEFVAL = "-2";
    public final static String MONTANT_MINIMEPOS = "montantMinimalPos";
    public final static String MONTANT_MINIMEPOS_DEFVAL = "2";
    public final static String NOUVEAU_CONTROLE_EMPLOYEUR = "nouveautControleEmployeur";
    public final static String REPORTER_MONTANT_MIN = "reporterMontantMinimal";
    public final static String RUBRIQUE_MONTANT_MINIMAL = "rubriqueMontantMinimal";
    public final static String SEPARATION_IND_NA = "separationIndNonActif";
    public static final String PROPERTY_MISE_EN_GED = "mettreGed";

    // le format du numéro d'affilié
    private IFormatData affilieFormater = null;
    private String anneeDonneesComptablesCompletes = null;
    private globaz.globall.api.BIApplication appNaos = null;
    private globaz.globall.api.BIApplication appOsiris = null;
    private globaz.globall.api.BIApplication appPyxis = null;
    private java.util.Hashtable<String, APIRubrique> cacheRubriques = new java.util.Hashtable<String, APIRubrique>();
    // vide si langue du user doit être utiliser, sinon assigner cette varuable
    // une valeur de langue
    private String codeISOLangue = new String();
    private Class<?> cSectionDescriptor = null;
    private java.lang.String idDefaultModuleAfact = new String();
    private Boolean modeReporterMontantMinimal = new Boolean(false);
    private String montantMinimeMax = FAApplication.MONTANT_MINIMEMAX_DEFVAL;
    private String montantMinimeNeg = FAApplication.MONTANT_MINIMENEG_DEFVAL;
    private String montantMinimePos = FAApplication.MONTANT_MINIMEPOS_DEFVAL;

    private Boolean separationIndependantNonActif = new Boolean(false);
    private globaz.globall.api.BISession sessionNaos = null;

    private globaz.globall.api.BISession sessionOsiris = null;

    private globaz.globall.api.BISession sessionPyxis = null;

    /**
     * Constructeur du type FAApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public FAApplication() throws Exception {
        super(FAApplication.DEFAULT_APPLICATION_MUSCA);
    }

    /**
     * Constructeur du type FAApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public FAApplication(String id) throws Exception {
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
        montantMinimeNeg = this.getProperty(FAApplication.MONTANT_MINIMENEG);

        montantMinimePos = this.getProperty(FAApplication.MONTANT_MINIMEPOS);

        montantMinimeMax = this.getProperty(FAApplication.MONTANT_MINIMEMAX);

        modeReporterMontantMinimal = new Boolean(this.getProperty(FAApplication.REPORTER_MONTANT_MIN));

        separationIndependantNonActif = new Boolean(this.getProperty(FAApplication.SEPARATION_IND_NA));

        FWMenuCache cache = FWMenuCache.getInstance();
        try {
            cache.addFile("MUSCAMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("musca.facturation.passage.annuler", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passage.devalider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passage.valider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passage.confirmationValider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passage.confirmationDevalider", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passage.confirmationAnnuler", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("musca.facturation.passageFacturation.generer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.comptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.imprimerDecomptes",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listerAfactAQuittancer",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listes", FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.aQuittancer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.annuler", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.imprimer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listerAfacts", FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listerCompensations",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listerDecomptes", FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listerTaxation", FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.genererSoldeBVR", FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.genererIntMoratoire",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.genererLettreTaxeCo2",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.listerDecisionControle",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("musca.facturation.passageFacturation.imprimerDecisionIM", FWSecureConstants.READ);

        FWAction.registerActionCustom("musca.facturation.afact.accepterAfact", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.afact.refuserAfact", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("musca.facturation.afact.listerAQuittancer", FWSecureConstants.READ);

        FWAction.registerActionCustom("musca.process.processServices.browseDirectory", FWSecureConstants.READ);
        // PO 6686
        FWAction.registerActionCustom("musca.facturation.passageFacturation.imprimerLettreRentier",
                FWSecureConstants.READ);
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
     * Method getAffileFormater.
     * 
     * @return IFormatData
     * @throws Exception
     */
    public IFormatData getAffileFormater() throws Exception {

        if (affilieFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isBlank(className)) {
                affilieFormater = (IFormatData) Class.forName(className).newInstance();
            }
        }
        return affilieFormater;
    }

    /**
     * Cette méthode retourne l'année à partir de laquelle on dispose des données comptables comptètes.
     * 
     * @return anneeDonneesComptablesCompletes en cas d'erreur, retourne null
     */
    public String getAnneeDonneesComptablesCompletes(BTransaction transaction) {
        try {
            anneeDonneesComptablesCompletes = FWFindParameter.findParameter(transaction, "0", "FAANDOCACO", "0", "", 2);
            return anneeDonneesComptablesCompletes;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Retourne AFApplication (NAOS) ou null si inaccessible Date de création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationNaos() {
        // Si application pas ouverte
        if (appNaos == null) {
            try {
                appNaos = GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appNaos;
    }

    /**
     * Retourne CAApplication (OSIRIS) ou null si inaccessible Date de création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationOsiris() {
        // Si application pas ouverte
        if (appOsiris == null) {
            try {
                appOsiris = GlobazSystem.getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appOsiris;
    }

    /**
     * Retourne l'application Pyxis Date de création : (24.02.2003 18:41:34)
     * 
     * @return globaz.globall.api.BIApplication
     */
    public BIApplication getApplicationPyxis() {
        // Si application pas ouverte
        if (appPyxis == null) {
            try {
                appPyxis = GlobazSystem.getApplication("PYXIS");
            } catch (Exception e) {
                return null;
            }
        }
        return appPyxis;
    }

    public int getAutoDigitAffilie() {
        String temp = this.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
        return Integer.valueOf(temp).intValue();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 15:37:43)
     * 
     * @return java.lang.String
     */
    java.lang.String getCodeISOLangue() {
        return codeISOLangue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:22:12)
     * 
     * @return java.lang.String
     */
    public String getIdDefaultModuleAfact(BSession session) throws Exception {
        // Si id vide
        if (JadeStringUtil.isIntegerEmpty(idDefaultModuleAfact)) {
            FAModuleFacturationManager mgr = new FAModuleFacturationManager();
            mgr.setSession(session);
            mgr.setForIdTypeModule(FAModuleFacturation.CS_MODULE_AFACT);
            mgr.orderByNiveauAppel();
            mgr.find();
            if (mgr.size() > 1) {
                throw new Exception("Il y a plus d'un module de type Afact (getIdDefaultModuleAfact)");
            } else if (mgr.size() == 0) {
                throw new Exception("Aucun module de facturation trouvé (getIdDefaultModuleAfact)");
            } else {
                idDefaultModuleAfact = ((FAModuleFacturation) mgr.getEntity(0)).getIdModuleFacturation();
            }
        }
        return idDefaultModuleAfact;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:55:52)
     * 
     * @return java.lang.String
     */
    public String getLibelleRubrique(BSession session, String idRubrique) {

        // Les rubriques d'Osiris
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(session);
        rubrique.setId(idRubrique);

        String libelleRubrique = "";
        // Si le libellé de la rubrique su l'afact est vide, retourner le
        // libellé de la rubrique
        try {
            rubrique.retrieve();
            if (!rubrique.isNew()) {
                libelleRubrique = rubrique.getDescription();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally { // unused
        }
        return libelleRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:55:52)
     * 
     * @return java.lang.String
     */
    public String getLibelleRubrique(BSession session, String idRubrique, String langue) {

        // Les rubriques d'Osiris
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(session);
        rubrique.setId(idRubrique);

        String libelleRubrique = "";
        // Si le libellé de la rubrique su l'afact est vide, retourner le
        // libellé de la rubrique
        try {
            rubrique.retrieve();
            if (!rubrique.isNew()) {
                libelleRubrique = rubrique.getDescription(langue);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally { // unused
        }
        return libelleRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:55:52)
     * 
     * @return java.lang.String
     */
    public String getLibelleRubrique(BTransaction transaction, String idRubrique) {

        // Avec utilisation de la rubrique provenant du cache
        try {
            return getRubriqueFromCache(transaction, idRubrique, 0).getDescription();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 16:55:52)
     * 
     * @return java.lang.String
     */
    public String getLibelleRubrique(BTransaction transaction, String idRubrique, String codeISOLangue) {

        // Avec utilisation de la rubrique provenant du cache
        try {
            return getRubriqueFromCache(transaction, idRubrique, 0).getDescription(codeISOLangue); // TBD
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    public String getMontantMinimeMax() {
        return montantMinimeMax;
    }

    /**
     * Returns the montantMinimeNeg.
     * 
     * @return String
     */
    public String getMontantMinimeNeg() {
        return montantMinimeNeg;
    }

    /**
     * Returns the montantMinimePos.
     * 
     * @return String
     */
    public String getMontantMinimePos() {
        return montantMinimePos;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 18:53:56)
     * 
     * @return globaz.pyxis.api.ITIPersonneAvs
     * @param transaction
     *            globaz.globall.db.BTransaction
     * @param alternateKey
     *            globaz.osiris.external.IntAdresseCourrier
     * @param id
     *            java.lang.String
     */
    public ITIPersonneAvs getPersonneAvs(BTransaction transaction, int alternateKey, String id, String[] methodsToLoad)
            throws Exception {
        // Instancier une personne
        ITIPersonneAvs personne = (ITIPersonneAvs) getSessionPyxis(transaction.getSession()).getAPIFor(
                ITIPersonneAvs.class);
        // Id en fonction de la clé alternée
        personne.setAlternateKey(new Integer(alternateKey));
        if (alternateKey == 0) {
            personne.setIdTiers(id);
        } else if (alternateKey == 2) {
            // personne.setNumAffilieActuel(id);
            Hashtable<String, String> cr = new Hashtable<String, String>();
            cr.put(ITIPersonneAvs.FIND_FOR_NUM_AFFILIE_ACTUEL, id);
            try {
                ITIPersonneAvs result = null;
                Object[] objResult = personne.find(cr);
                if (objResult != null) {
                    if (objResult.length > 0) {
                        GlobazValueObject obj = (GlobazValueObject) objResult[0];
                        result = new ITIPersonneAvsHelper(obj);
                        result.setISession(transaction.getISession());
                        personne = result;
                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        } else if (alternateKey == ITIPersonneAvs.ALT_KEY_NUMERO_AVS) {
            personne.setNumAvsActuel(id);
        } else {
            personne.setIdTiers(id);
        }
        personne.retrieve(transaction);
        if (methodsToLoad != null) {
            personne.setMethodsToLoad(methodsToLoad);
        }
        // Renvoyer la personne
        return personne;
    }

    /**
     * Récupère une rubrique comptable à partir du cache Date de création : (12.12.2002 14:41:23)
     * 
     * @return globaz.osiris.api.APIRubrique
     * @param transaction
     *            globaz.globall.db.BTransaction la
     * @param id
     *            java.lang.String
     * @param alternateKey
     *            globaz.osiris.external.IntAdresseCourrier
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public synchronized APIRubrique getRubriqueFromCache(BTransaction transaction, String id, int alternateKey)
            throws java.lang.Exception {
        // Vérifier l'id
        if (JadeStringUtil.isBlank(id)) {
            throw new Exception("getRuriqueFromCache(): l'identifiant de la rubrique doit être renseigné");
        }
        // Vérifier la transaction
        if (transaction == null) {
            throw new Exception("getRubriqueFromCache(): la transaction doit être fourni");
        }
        // Définir
        APIRubrique rub = null;
        // Vérifier si la rubrique existe par clé primaire
        if (alternateKey == 0) {
            rub = cacheRubriques.get(id);
            // Récupérer par id Externe
        } else {
            Enumeration<APIRubrique> e = cacheRubriques.elements();
            boolean found = false;
            while (e.hasMoreElements()) {
                rub = e.nextElement();

                if (rub.getIdExterne().equals(id)) {
                    found = true;
                    break;
                }
            }
            // Si pas trouvé
            if (!found) {
                rub = null;
            }
        }
        // Tenter une lecture si pas trouvé
        if (rub == null) {
            // Récupérer la rubrique à partir de la comptabilité auxiliaire
            BIApplication remoteApp = GlobazSystem.getApplication("OSIRIS");
            BISession remoteSession = remoteApp.newSession(transaction.getSession());
            rub = (APIRubrique) remoteSession.getAPIFor(APIRubrique.class);
            // Récupérer à partir de l'id externe
            if (alternateKey == APIRubrique.AK_IDEXTERNE) {
                rub.setIdExterne(id);
                rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
                // Sinon, on récupère à partir de l'id primaire
            } else {
                rub.setIdRubrique(id);
            }

            // Recherche de la rubrique
            rub.retrieve(transaction);
            // Stocker la rubrique si elle existe
            if (!rub.isNew()) {
                cacheRubriques.put(rub.getIdRubrique(), rub);
            }
        }

        // Retourne la rubrique
        return rub;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:38:08)
     * 
     * @return globaz.osiris.api.APISectionDescriptor
     */
    public APISectionDescriptor getSectionDescriptor(BISession session) throws Exception {
        // Charger le descripteur
        if (cSectionDescriptor == null) {
            cSectionDescriptor = Class.forName(this.getProperty("defaultSectionDescritor",
                    "globaz.osiris.db.comptes.impl.CASectionAVS"));
        }
        APISectionDescriptor d = (APISectionDescriptor) cSectionDescriptor.newInstance();
        d.setISession(session);
        return d;
    }

    /**
     * Retourne une session NAOS ou null Date de création : (24.02.2003 18:44:58)
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionNaos(BISession session) {
        // Si session pas ouverte
        if (sessionNaos == null) {
            try {
                sessionNaos = getApplicationNaos().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionNaos;
    }

    /**
     * Retourne une session OSIRIS ou null Date de création : (24.02.2003 18:44:58)
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionOsiris(BISession session) {
        // Si session pas ouverte
        if (sessionOsiris == null) {
            try {
                sessionOsiris = getApplicationOsiris().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionOsiris;
    }

    /**
     * Retourne une session PYXIS ou null Date de création : (24.02.2003 18:44:58)
     * 
     * @return globaz.globall.api.BISession
     */
    public BISession getSessionPyxis(BISession session) {
        // Si session pas ouverte
        if (sessionPyxis == null) {
            try {
                sessionPyxis = getApplicationPyxis().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionPyxis;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:14:23)
     * 
     * @return globaz.pyxis.api.ITITiers
     * @param session
     *            globaz.globall.api.BISession
     * @param idRole
     *            java.lang.String
     * @param idExterne
     *            java.lang.String
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public ITITiers getTiersByRole(BISession session, String idRole, String idExterne, String idTiers,
            String[] methodsToLoad) throws java.lang.Exception {
        // S'il s'agit d'une personne AVS
        ITIPersonneAvs personne = (ITIPersonneAvs) getSessionPyxis(session).getAPIFor(ITIPersonneAvs.class);

        if (idRole.equals(TIRole.CS_AFFILIE) || idRole.equals(TIRole.CS_ASSURE)
                || idRole.equals(CaisseHelperFactory.CS_RENTIER)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            if (!idRole.equals(TIRole.CS_ASSURE) && !idRole.equals(CaisseHelperFactory.CS_RENTIER)) {
                if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                    personne = givePersonneForIdExterne(session, idExterne);
                } else {
                    personne.setIdTiers(idTiers);
                }
            } else {
                personne.setAlternateKey(new Integer(ITIPersonneAvs.ALT_KEY_NUMERO_AVS));
                personne.setNumAvsActuel(idExterne);
            }

        } else if (idRole.equals(IntRole.ROLE_APG) || idRole.equals(IntRole.ROLE_IJAI)
                || idRole.equals(IntRole.ROLE_AF)) {
            if (idExterne.length() > 10) {
                personne.setAlternateKey(new Integer(ITIPersonneAvs.ALT_KEY_NUMERO_AVS));
                personne.setNumAvsActuel(idExterne);
            } else {
                if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                    personne = givePersonneForIdExterne(session, idExterne);
                } else {
                    personne.setIdTiers(idTiers);
                }
            }

        } else if (IntRole.ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES.equals(idRole)) {
            String numeroUnformat = unformat(idExterne);
            if (numeroUnformat.length() > 11) {
                personne.setAlternateKey(new Integer(ITIPersonneAvs.ALT_KEY_NUMERO_AVS));
                personne.setNumAvsActuel(idExterne);
            } else {
                if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                    personne = givePersonneForIdExterne(session, idExterne);
                } else {
                    personne.setIdTiers(idTiers);
                }
            }
        } else {
            throw new Exception("Le type de tiers " + idRole + " n'est pas géré");
        }

        if (methodsToLoad != null) {
            personne.setMethodsToLoad(methodsToLoad);
        }

        personne.retrieve(null);
        return personne;
    }

    /**
     * @param session
     * @param idExterne
     * @param personne
     * @return
     * @throws Exception
     */
    private ITIPersonneAvs givePersonneForIdExterne(BISession session, String idExterne) throws Exception {
        ITIPersonneAvs personne = (ITIPersonneAvs) getSessionPyxis(session).getAPIFor(ITIPersonneAvs.class);
        Hashtable<String, String> cr = new Hashtable<String, String>();
        cr.put(ITIPersonneAvs.FIND_FOR_NUM_AFFILIE_ACTUEL, idExterne);
        try {
            ITIPersonneAvs result = null;
            Object[] objResult = personne.find(cr);
            if (objResult != null) {
                if (objResult.length > 0) {
                    GlobazValueObject obj = (GlobazValueObject) objResult[0];
                    result = new ITIPersonneAvsHelper(obj);
                    result.setISession(session);
                    personne = result;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return personne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 19:53:54)
     * 
     * @return globaz.pyxis.api.ITITiers
     * @param transaction
     *            globaz.globall.db.BTransaction
     * @param idRole
     *            java.lang.String
     * @param idExterne
     *            numéro formaté
     */
    public ITITiers getTiersByRole(BTransaction transaction, String idRole, String idExterne, String idTiers,
            String[] methodsToLoad) throws Exception {
        // S'il s'agit d'une personne AVS
        ITIPersonneAvs personne = (ITIPersonneAvs) getSessionPyxis(transaction.getSession()).getAPIFor(
                ITIPersonneAvs.class);
        if (idRole.equals(TIRole.CS_AFFILIE) || idRole.equals(TIRole.CS_ASSURE)
                || idRole.equals(CaisseHelperFactory.CS_RENTIER)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PARITAIRE)
                || idRole.equals(CaisseHelperFactory.CS_AFFILIE_PERSONNEL)) {
            if (!idRole.equals(TIRole.CS_ASSURE) && !idRole.equals(CaisseHelperFactory.CS_RENTIER)) {
                if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                    personne = givePersonneForIdExterne(transaction.getSession(), idExterne);
                } else {
                    personne.setIdTiers(idTiers);
                }
            } else {
                personne.setAlternateKey(new Integer(ITIPersonneAvs.ALT_KEY_NUMERO_AVS));
                personne.setNumAvsActuel(idExterne);
            }
        } else if (idRole.equals(IntRole.ROLE_APG) || idRole.equals(IntRole.ROLE_IJAI)
                || idRole.equals(IntRole.ROLE_AF)) {
            if (idExterne.length() > 10) {
                personne.setAlternateKey(new Integer(ITIPersonneAvs.ALT_KEY_NUMERO_AVS));
                personne.setNumAvsActuel(idExterne);
            } else {
                if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                    personne = givePersonneForIdExterne(transaction.getSession(), idExterne);
                } else {
                    personne.setIdTiers(idTiers);
                }
            }
        } else if (IntRole.ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES.equals(idRole)) {
            String numeroUnformat = unformat(idExterne);
            if (numeroUnformat.length() > 11) {
                personne.setAlternateKey(new Integer(ITIPersonneAvs.ALT_KEY_NUMERO_AVS));
                personne.setNumAvsActuel(idExterne);
            } else {
                if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                    personne = givePersonneForIdExterne(transaction.getSession(), idExterne);
                } else {
                    personne.setIdTiers(idTiers);
                }
            }
        } else {
            throw new Exception("Le type de tiers " + idRole + " n'est pas géré");
        }

        if (methodsToLoad != null) {
            personne.setMethodsToLoad(methodsToLoad);
        }
        personne.retrieve(null);
        return personne;
    }

    /**
     * @param idExterne
     * @return
     */
    private String unformat(String idExterne) {
        String numeroUnformat = idExterne.replace(".", "").replace("-", "").trim();
        return numeroUnformat;
    }

    /**
     * @return
     */
    public boolean isModeReporterMontantMinimal() {
        return modeReporterMontantMinimal.booleanValue();
    }

    public boolean isNouveauControleEmployeur() {
        return Boolean.valueOf(this.getProperty(FAApplication.NOUVEAU_CONTROLE_EMPLOYEUR, "false").trim())
                .booleanValue();
    }

    public boolean isSeparationIndependantNonActif() {
        return separationIndependantNonActif.booleanValue();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 15:37:43)
     * 
     * @param newCodeISOLangue
     *            java.lang.String
     */
    public void setCodeISOLangue(java.lang.String newCodeISOLangue) {
        codeISOLangue = newCodeISOLangue;
    }

    public boolean wantOldProcessFacturation() {
        return Boolean.valueOf(this.getProperty("wantOldProcessFacturation", "false").trim()).booleanValue();
    }
}
