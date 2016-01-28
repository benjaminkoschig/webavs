package globaz.hermes.application;

// import globaz.framework.messaging.FWSmtpClient;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.api.ICICompteIndividuel;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonProperties;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Application HERMES
 * 
 * @author Emmanuel Fleury
 */
public class HEApplication extends BApplication {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_DOMAINE_ADRESSE_CI_ARC = "519001";
    public final static String DEFAULT_APPLICATION_HERMES = "HERMES";
    public final static String DEFAULT_APPLICATION_ROOT = "hermesRoot";
    // session PAVO
    public final static String KEY_SESSION_PAVO = "sessionPavo";
    public final static String MODEL_CA = "modeleCA";
    public final static String MODEL_CA_EMP = "modeleCAEmp";
    public final static String PROPERTY_DEFAULT_EMAIL = "zas.user.email";

    public static String getLangueIsoFromCode(String code) {
        if (TITiers.CS_ALLEMAND.equals(code)) {
            return "DE";
        } else if (TITiers.CS_ITALIEN.equals(code)) {
            return "IT";
        } else {
            return "FR";
        }
    }

    private IFormatData affileFormater;
    // les champs
    private FWParametersSystemCodeManager csChampsListe = null;
    // les code application
    private FWParametersSystemCodeManager csCodeApplicationListe = null;
    // les criteres
    private FWParametersSystemCodeManager csCritereListe = null;
    // les messages
    private FWParametersSystemCodeManager csMessageListe = null;
    // les motifs
    private FWParametersSystemCodeManager csMotifsListe = null;
    // les pays
    private FWParametersSystemCodeManager csPaysListe = null;
    // les statuts
    private FWParametersSystemCodeManager csStatutListe = null;
    // les types
    private FWParametersSystemCodeManager csTypeListe = null;
    private List listAttendreClotureForCaisses;

    BITransaction remoteTransaction;

    /**
     * Constructeur du type HEApplication.
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public HEApplication() throws Exception {
        this(HEApplication.DEFAULT_APPLICATION_HERMES);
    }

    /**
     * Constructeur du type HEApplication.
     * 
     * @param id
     *            l'id de l'application
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    public HEApplication(String id) throws Exception {
        super(id);
        // logger = new FWLogIt(true, "hermes.log");
    }

    /**
     * Déclare les APIs de l'application
     */
    @Override
    protected void _declareAPI() {
        _addAPI(globaz.hermes.api.IHEAnnoncesViewBean.class, globaz.hermes.api.helper.IHEAnnoncesViewBeanHelper.class);
    }

    /**
     * Initialise l'application
     * 
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        try {
            cache.addFile("HERMESMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "HERMESMenu.xml non résolu : " + e.toString());
        }

    }

    @Override
    protected void _initializeCustomActions() {
        // action de gestion
        FWAction.registerActionCustom("hermes.gestion.inputAnnonce.rc2", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hermes.gestion.lot.chooseType", FWSecureConstants.READ);
        FWAction.registerActionCustom("hermes.gestion.rassemblement.saisirCI", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hermes.gestion.rassemblement.ajouterEcriture", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hermes.gestion.rappelImprimer.executer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hermes.parametrage.libererCI.executer", FWSecureConstants.UPDATE);

        // action par défaut dont on veut changer les droits (par exemple, par
        // défaut l'action "executer" demande les droit "READ")
        FWAction.registerActionCustom("hermes.gestion.rappel.afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hermes.parametrage.attenteEnvoi.modifier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("hermes.parametrage.annulerAnnonce.executer", FWSecureConstants.UPDATE);
    }

    /**
     * Lit les propriétés de l'application
     * 
     * @param properties
     *            les propriétés lues par le système
     * @exception java.lang.Exception
     *                si l'initialisation de l'application a échouée
     */
    protected void _readProperties(java.util.Properties properties) {
    }

    /**
     * @param aff
     */
    public void checkAffilie(String numAff) throws Exception {
        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isEmpty(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
            }
        }
        affileFormater.check(numAff);
    }

    /**
     * @param string
     * @param transaction
     */
    public void checkDateEngagement(String dateEngagement, String affilie, BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(dateEngagement)) {
            dateEngagement = "00.00." + JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
        }
        CIApplication mgrAffilie = new CIApplication();
        // AFAffiliation numeroAffiliation = new AFAffiliation();
        // On prend la bonne affiliation en fonction de la date d'engagement
        AFAffiliation numeroAffiliation = mgrAffilie.getAffilieByNo(transaction.getSession(), affilie, true, false,
                String.valueOf(JACalendar.getMonth(dateEngagement)),
                String.valueOf(JACalendar.getMonth(dateEngagement)),
                String.valueOf(JACalendar.getYear(dateEngagement)), String.valueOf(JACalendar.getDay(dateEngagement)),
                String.valueOf(JACalendar.getDay(dateEngagement)));
        // Ensuite on compare l'affiliation sélectionnée avec la date de
        // licenciement
        if (numeroAffiliation == null) {
            throw new Exception(transaction.getSession().getLabel("HERMES_AFFILIE_NON_VALIDE"));
        }
    }

    /**
     * @param session
     * @param transaction
     * @param bean
     */
    public void checkParameters(BSession session, BTransaction transaction, HEInputAnnonceViewBean bean) {
        try {
            if (!JadeStringUtil.isEmpty(bean.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
                CICompteIndividuelManager ciRecherche = new CICompteIndividuelManager();
                // création d'une nouvelle session
                BSession sessionPavo = (BSession) getSessionCI(session);

                // recherche du ci avec ce numéro avs
                ciRecherche.setSession(sessionPavo);
                ciRecherche
                        .setForNumeroAvs(StringUtils.removeDotsOnly(bean.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)));
                ciRecherche.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
                ciRecherche.find(transaction);
                if (ciRecherche.size() > 0) {
                    CICompteIndividuel cptIndivid = (CICompteIndividuel) ciRecherche.getFirstEntity();
                    if (!cptIndivid.getNomPrenom().equals(
                            StringUtils.fixName(bean.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF)))) {
                        transaction.addErrors(session.getLabel("HERMES_00017"));
                    }
                    if (!CodeSystem.getCodeUtilisateur(cptIndivid.getSexe(), cptIndivid.getSession()).equals(
                            bean.getField(IHEAnnoncesViewBean.SEXE))) {
                        transaction.addErrors(session.getLabel("HERMES_10021"));
                    }
                    if (!cptIndivid.getDateNaissance().equals(
                            bean.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA))) {
                        transaction.addErrors(session.getLabel("HERMES_00003"));
                    }
                    if (!CodeSystem.getCodeUtilisateur(cptIndivid.getPaysOrigineId(), sessionPavo).equals(
                            bean.getField(IHEAnnoncesViewBean.ETAT_ORIGINE))) {
                        transaction.addErrors(session.getLabel("HERMES_10022"));
                    }
                }
            }
        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
        }
    }

    private boolean existEcritureACacher(BSession session, String idCi) throws Exception {
        boolean existEcritureACacher = false;
        CIEcritureManager ecritureManager = new CIEcritureManager();
        ecritureManager.setCacherEcritureProtege(1);
        ecritureManager.setSession(session);
        ecritureManager.setForCompteIndividuelId(idCi);
        ecritureManager.find();
        if ((ecritureManager.size() != 0) && ecritureManager.hasEcritureProtegeParAffiliation()) {
            existEcritureACacher = true;
        }
        return existEcritureACacher;
    }

    // /**
    // * @see globaz.globall.db.BApplication#_setLocalPath(String)
    // */
    // public void _setLocalPath(String newLocalPath) {
    // super._setLocalPath(newLocalPath);
    // }
    // /**
    // * Method setLocalPath.
    // * @param string
    // */
    // public void setLocalPath(String localPath) {
    // super._setLocalPath(localPath);
    // }
    // /**
    // * Renvoie un object d'envoie email configuré selon les propriétés de
    // pavo.
    // * Date de création : (26.11.2002 08:19:47)
    // */
    // public FWSmtpClient getEmailSender() {
    // if (smtpClient == null) {
    // smtpClient = new FWSmtpClient();
    // smtpClient.setHostName(Jade.getInstance().getMailHostName());
    // smtpClient.setHostPort(Jade.getInstance().getMailHostPort());
    // smtpClient.setSender(Jade.getInstance().getMailSender());
    // }
    // return smtpClient;
    // }
    public String formatAffilie(String numAff) throws Exception {

        if (affileFormater == null) {
            String className = this.getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
            if (!JadeStringUtil.isEmpty(className)) {
                affileFormater = (IFormatData) Class.forName(className).newInstance();
                return affileFormater.format(numAff);
            } else {
                return numAff;
            }
        } else {
            return affileFormater.format(numAff);
        }
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
        return GlobazSystem.getApplication("PAVO");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsChampsListe(BSession session) throws Exception {
        if ((csChampsListe == null) || csChampsListe.isEmpty()
                || !csChampsListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csChampsListe = new FWParametersSystemCodeManager();
            csChampsListe.setForIdGroupe("HECHAMPS");
            csChampsListe.setForIdTypeCode("11100008");
            csChampsListe.setSession(session);
            csChampsListe.find(BManager.SIZE_NOLIMIT);
        }
        return csChampsListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsCodeApplicationListe(BSession session) throws Exception {
        if ((csCodeApplicationListe == null) || csCodeApplicationListe.isEmpty()
                || !csCodeApplicationListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csCodeApplicationListe = new FWParametersSystemCodeManager();
            csCodeApplicationListe.setForIdGroupe("HECODAPP");
            csCodeApplicationListe.setForIdTypeCode("11100001");
            csCodeApplicationListe.setSession(session);
            csCodeApplicationListe.find(BManager.SIZE_NOLIMIT);
        }
        return csCodeApplicationListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsCritereListe(BSession session) throws Exception {
        if ((csCritereListe == null) || csCritereListe.isEmpty()
                || !csCritereListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csCritereListe = new FWParametersSystemCodeManager();
            csCritereListe.setForIdGroupe("HECRITER");
            csCritereListe.setForIdTypeCode("11100003");
            csCritereListe.setSession(session);
            csCritereListe.find(BManager.SIZE_NOLIMIT);
        }
        return csCritereListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMessageListe(BSession session) throws Exception {
        if ((csMessageListe == null) || csMessageListe.isEmpty()
                || !csMessageListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csMessageListe = new FWParametersSystemCodeManager();
            csMessageListe.setForIdGroupe("HEMSG");
            csMessageListe.setForIdTypeCode("11100005");
            csMessageListe.setSession(session);
            csMessageListe.find(BManager.SIZE_NOLIMIT);
        }
        return csMessageListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMotifsListe(BSession session) throws Exception {
        if ((csMotifsListe == null) || csMotifsListe.isEmpty()
                || !csMotifsListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csMotifsListe = new FWParametersSystemCodeManager();
            csMotifsListe.setForIdGroupe("HEMOTIFS");
            csMotifsListe.setForIdTypeCode("11100002");
            csMotifsListe.setSession(session);
            csMotifsListe.find(BManager.SIZE_NOLIMIT);
        }
        return csMotifsListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public synchronized FWParametersSystemCodeManager getCsMotifsListe(BSession session, boolean actif)
            throws Exception {
        if ((csMotifsListe == null) || csMotifsListe.isEmpty()
                || !csMotifsListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csMotifsListe = new FWParametersSystemCodeManager();
            csMotifsListe.setForIdGroupe("HEMOTIFS");
            csMotifsListe.setForIdTypeCode("11100002");
            csMotifsListe.setForActif(new Boolean(actif));
            csMotifsListe.setSession(session);
            csMotifsListe.find(BManager.SIZE_NOLIMIT);
        }
        return csMotifsListe;
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
            csPaysListe.setForActif(Boolean.TRUE);
            csPaysListe.setSession(session);
            csPaysListe.find(BManager.SIZE_NOLIMIT);
        }
        return csPaysListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsStatutListe(BSession session) throws Exception {
        if ((csStatutListe == null) || csStatutListe.isEmpty()
                || !csStatutListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csStatutListe = new FWParametersSystemCodeManager();
            csStatutListe.setForIdGroupe("HESTATUT");
            csStatutListe.setForIdTypeCode("11100007");
            csStatutListe.setSession(session);
            csStatutListe.find(BManager.SIZE_NOLIMIT);
        }
        return csStatutListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeListe(BSession session) throws Exception {
        if ((csTypeListe == null) || csTypeListe.isEmpty()
                || !csTypeListe.getSession().getIdLangue().equals(session.getIdLangue())) {
            csTypeListe = new FWParametersSystemCodeManager();
            csTypeListe.setForIdGroupe("HETYPE");
            csTypeListe.setForIdTypeCode("11100006");
            csTypeListe.setSession(session);
            csTypeListe.find(BManager.SIZE_NOLIMIT);
        }
        return csTypeListe;
    }

    public String getDateNNSS() {
        return this.getProperty("zas.nnss.dateProduction");
    }

    public List getListCaisseAttendreCloture() {
        if (listAttendreClotureForCaisses == null) {
            String caisses = this.getProperty("zas.attendre.cloture.liste.caisses");
            if (!JadeStringUtil.isEmpty(caisses)) {
                StringTokenizer st = new StringTokenizer(caisses, ",");
                String[] listC = new String[st.countTokens()];
                while (st.hasMoreTokens()) {
                    listC[st.countTokens() - 1] = st.nextToken();
                }
                listAttendreClotureForCaisses = Arrays.asList(listC);
            } else {
                // si la propriété n'existe pas, prendre seulement le numéro de
                // la caisse
                caisses = this.getProperty("noCaisse") + "." + this.getProperty("noAgence");
                String[] listC = { caisses };
                listAttendreClotureForCaisses = Arrays.asList(listC);
            }
        }
        return listAttendreClotureForCaisses;
    }

    public String getModeleCA() {
        return this.getProperty(HEApplication.MODEL_CA, "HERMES_LETTRE_REMISE_CA_ACC");
    }

    public String getModeleCAEmp() {
        return this.getProperty(HEApplication.MODEL_CA_EMP, "HERMES_LETTRE_REMISE_CA_ACC_EMP");
    }

    /*
     * public static FWLogIt getLogger() { return logger; }
     */
    /**
     * Insérez la description de la méthode ici. Date de création : (22.05.2003 14:06:05)
     * 
     * @return globaz.globall.api.BITransaction
     */
    public globaz.globall.api.BITransaction getRemoteTransaction() {
        return remoteTransaction;
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
    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute(HEApplication.KEY_SESSION_PAVO);
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = getApplication("PAVO").newSession(local);
            local.setAttribute(HEApplication.KEY_SESSION_PAVO, remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /***/
    public boolean isCiOuvert(BSession local, String numeroAVS) throws Exception {
        BTransaction pavoTransaction = null;
        try {
            // tout ce qu'il faut pour le remote
            CICompteIndividuel remoteCI = new CICompteIndividuel();
            BSession pavoSession = (BSession) getSessionCI(local);
            pavoTransaction = (BTransaction) pavoSession.newTransaction();
            pavoTransaction.openTransaction();
            remoteCI.setSession(pavoSession);
            remoteCI = CICompteIndividuel.loadCI(StringUtils.removeDots(numeroAVS), pavoTransaction);
            // ICICompteIndividuel.CS_REGISTRE_ASSURES;
            if ((remoteCI != null) && remoteCI.isCiOuvert().booleanValue()) { // Le
                // CI
                // est
                // ouvert
                if (remoteCI.getRegistre().equals(ICICompteIndividuel.CS_REGISTRE_ASSURES)) {
                    // Le CI est au registre des assurés
                    // transaction.closeTransaction();
                    return true;
                } else {
                    // transaction.closeTransaction();
                    return false;
                }
            } else { // Le CI est pas ouvert
                // transaction.closeTransaction();
                return false;
            }
        } catch (Exception ex) {
            throw new Exception("HERMES_00016");
            // return false;
        } finally {
            if (pavoTransaction != null) {
                try {
                    pavoTransaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isCISecure(BSession sessionHermes, String numAVS) {
        try {
            // non on n'a pas déjà demandé le revenu pour cet instance
            // recherche du compte individuel en relation avec l'annonce
            CICompteIndividuelManager ciRecherche = new CICompteIndividuelManager();
            // création d'une nouvelle session
            BSession sessionPavo = (BSession) ((HEApplication) GlobazServer.getCurrentSystem().getApplication(
                    HEApplication.DEFAULT_APPLICATION_HERMES)).getSessionCI(sessionHermes);

            // recherche du ci avec ce numéro avs
            ciRecherche.setSession(sessionPavo);
            ciRecherche.setForNumeroAvs(numAVS);
            ciRecherche.find();
            if (ciRecherche.size() > 0) {
                // on a trouvé le ci dans PAVO !
                CICompteIndividuel ci = (CICompteIndividuel) ciRecherche.getFirstEntity();
                if (CICompteIndividuel.CS_ACCESS_0.equals(ci.getAccesSecurite())) {
                    // le niveau de sécurité du ci est = 0, donc le ci n'est pas
                    // protégé !
                    // Vérification de la sécurité sur une affiliation
                    CIEcritureManager ecritureManager = new CIEcritureManager();
                    ecritureManager.setSession(sessionPavo);
                    ecritureManager.setCacherEcritureProtege(1);
                    ecritureManager.setForCompteIndividuelId(ci.getCompteIndividuelId());
                    ecritureManager.find();
                    // si le manager contient des écritures protégées par
                    // affiliation on suspens l'annonce
                    if (ecritureManager.hasEcritureProtegeParAffiliation()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // le niveau de sécurité du ci n'est pas égal à 0, donc ci
                    // protégé !
                    return true;
                }
            } else {
                // si le ci n'existe pas, il n'est pas sécurisé
                return false;
            }
        } catch (Exception e) {
            // si une erreur se produit, par sécurité, on signale le ci comme
            // sécurisé
            e.printStackTrace();
            return true;
        }
    }

    public boolean isRevenuCache(String userId, BSession sessionHermes, String numAVS) {
        try {
            // non on n'a pas déjà demandé le revenu pour cet instance
            // recherche du compte individuel en relation avec l'annonce
            CICompteIndividuelManager ciRecherche = new CICompteIndividuelManager();
            // création d'une nouvelle session
            BSession sessionPavo = (BSession) ((HEApplication) sessionHermes.getApplication())
                    .getSessionCI(sessionHermes);
            // création d'une nouvelle transaction
            BTransaction newTrans = null;
            try {
                newTrans = new BTransaction(sessionPavo);
                newTrans.openTransaction();
                // recherche du ci avec ce numéro avs
                ciRecherche.setSession(sessionPavo);
                ciRecherche.setForNumeroAvs(numAVS);
                ciRecherche.find(newTrans);
                if (ciRecherche.size() != 0) {
                    // on a trouvé le ci dans PAVO !
                    CICompteIndividuel ci = (CICompteIndividuel) ciRecherche.getEntity(0);
                    // faire une nouvelle transaction
                    if (ci.hasUserShowRight(newTrans, userId)
                            && !existEcritureACacher(sessionPavo, ci.getCompteIndividuelId())) {
                        // l'utilisateur a les droits pour voir le revenu
                        return false;
                    } else {
                        // l'utilisateur loggé n'a pas les droits
                        return true;
                    }
                } else {
                    // si le ci n'existe pas, on retourne le revenu
                    return false;
                }
            } catch (Exception e) {
                // _addError(newTrans, newTrans.getErrors().toString());
                throw (e);
            } finally {
                if (newTrans != null) {
                    newTrans.closeTransaction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
