/*
 * Créé le 17 juillet 2008
 */
package globaz.corvus.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.annonce.service.REAnnonces9eXmlService;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1AManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.properties.REProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonceLight;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.rc.PoolFussType;
import ch.admin.zas.rc.PoolKopfType;
import ch.globaz.common.properties.CommonProperties;
import ch.horizon.jaspe.util.JACalendar;

/**
 * @author jmc
 * 
 */
public class REEnvoyerAnnoncesXMLProcess extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(REEnvoyerAnnoncesXMLProcess.class);

    private boolean modeTest = true;

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String APPLICATION_ANNONCES = "HERMES";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List<Map<String, String>> annoncesAEnvoyer = new ArrayList<Map<String, String>>();

    private String forDateEnvoi = "";

    private String forMoisAnneeComptable = "";
    private Boolean isForAnnoncesSubsequentes = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REEnvoyerAnnoncesProcess.
     */
    public REEnvoyerAnnoncesXMLProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REEnvoyerAnnoncesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public REEnvoyerAnnoncesXMLProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe REEnvoyerAnnoncesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public REEnvoyerAnnoncesXMLProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
	 */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        try {

            ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
            PoolMeldungZurZAS lotAnnonces = factoryPool.createPoolMeldungZurZAS();
            ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();
            ch.admin.zas.pool.PoolMeldungZurZAS.Lot lot = factoryPool.createPoolMeldungZurZASLot();
            PoolKopfType poolKopf = factoryType.createPoolKopfType();
            if (REProperties.CENTRALE_TEST.getBooleanValue()) {
                poolKopf.setTest("TEST");
            }
            poolKopf.setSender(CommonProperties.KEY_NO_CAISSE.getValue());

            final DateFormat format = new SimpleDateFormat("dd.mm.yyyy");
            final String dateStr = JACalendar.todayjjMMMMaaaa();
            final java.util.Date dDate = format.parse(dateStr);

            GregorianCalendar gregory = new GregorianCalendar();
            gregory.setTime(dDate);

            XMLGregorianCalendar dealCloseDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
            poolKopf.setErstellungsdatum(dealCloseDate);
            lot.setPoolKopf(poolKopf);

            PoolFussType poolFuss = factoryType.createPoolFussType();
            poolFuss.setEintragungengesamtzahl(0);
            lot.setPoolFuss(poolFuss);

            lotAnnonces.getLot().add(lot);

            // On prend la date du jour qu'on mettra a toutes les annonces
            // envoyées (pour éviter le problème qui
            // surviendrait si le processus était lancé a 23H59 et se terminait
            // apres minuit.
            BSession session = getSession();
            REAnnoncesAbstractLevel1AManager mgr = null;
            REAnnoncesAbstractLevel1A annonce = null;
            BStatement statement = null;
            // on doit envoyer dans l'ordre : les annonces du mois et annee
            // comptable en état validé , les annonces
            // erronnées, et si réannonce, les annonces qui ont le meme mois et
            // année comptable qu'une annonce envoyée a
            // la date d'envoi et les annonces envoyées a cette date dont le
            // mois et annee Comptable est différent

            // si il ne s'agit pas d'une réannonce
            mgr = new REAnnoncesAbstractLevel1AManager();
            mgr.setHasAvsHistory(false);
            mgr.setSession(session);
            mgr.setForCsEtat(IREAnnonces.CS_ETAT_OUVERT);
            mgr.setForCodeEnregistrement("01");
            mgr.setForMoisRapport(forMoisAnneeComptable);

            if (getIsForAnnoncesSubsequentes()) {
                mgr.setForAnnoncesSubsequentes(true);
            }
            statement = mgr.cursorOpen(getTransaction());

            boolean hasError = false;

            while ((annonce = (REAnnoncesAbstractLevel1A) (mgr.cursorReadNext(statement))) != null) {
                annonce.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                annonce.update(getTransaction());
                try {
                    prepareEnvoieAnnonce(annonce, lotAnnonces);
                } catch (Exception ex) {
                    String err;
                    if (ex.getMessage() != null) {
                        err = ex.getMessage();
                    } else {
                        err = ex.toString();
                    }
                    // si une erreur est levée, on continue en gardant en mémoire l'erreur
                    if (err != null) {
                        StringBuilder messageBuilder = new StringBuilder();

                        // si première erreur, ajout du mois du rapport comme info (bas de l'email)
                        if (!hasError) {
                            messageBuilder.append(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_EMAIL_HEADER"))
                                    .append(" ");
                            messageBuilder.append(forMoisAnneeComptable).append("\n\n");

                            getMemoryLog().logMessage(messageBuilder.toString(), FWMessage.INFORMATION,
                                    getSession().getLabel("ENVOYER_ANNONCES"));

                            messageBuilder = new StringBuilder();
                        }

                        messageBuilder.append(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_TITLE")).append("\n");
                        messageBuilder.append(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_NO_AVS"))
                                .append(" : ").append(NSUtil.formatAVSUnknown(annonce.getNoAssAyantDroit()))
                                .append("\n");
                        messageBuilder.append(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_CODE_APPLICATION"))
                                .append(" : ").append(annonce.getCodeApplication()).append("\n");
                        messageBuilder.append(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_GENRE_PRESTATION"))
                                .append(" : ").append(annonce.getGenrePrestation()).append("\n");
                        messageBuilder.append(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_MESSAGE_ANAKIN"))
                                .append(" : {");

                        getMemoryLog().logMessage(messageBuilder.toString(), FWMessage.ERREUR,
                                getSession().getLabel("ENVOYER_ANNONCES"));
                        if (err.length() < 255) {
                            getMemoryLog().logMessage(err + "}\n", FWMessage.ERREUR,
                                    getSession().getLabel("ENVOYER_ANNONCES"));
                        } else {

                            // si le message est plus grand que 255 caractères, on découpe le message
                            // en plusieurs entrées dans le mail (la limite du framework pour une entrée
                            // est de 255 caractères)
                            String[] splitedErr = err.split("\n");
                            StringBuilder message = new StringBuilder();

                            for (String split : splitedErr) {
                                // s'il y a encore assez de place pour ce message dans cette entrée, on l'ajoute
                                if ((message.length() + split.length()) < 255) {
                                    message.append(split).append("\n");
                                } else {
                                    // sinon on écrit les messages précédents dans le mail et on crée une nouvelle
                                    // entrée avec ce message
                                    getMemoryLog().logMessage(message.toString(), FWMessage.ERREUR,
                                            getSession().getLabel("ENVOYER_ANNONCES"));

                                    message = new StringBuilder();
                                    message.append(split).append("\n");
                                }
                            }

                            // on écrit dans le mail les messages restants
                            message.append("}\n");
                            getMemoryLog().logMessage(message.toString(), FWMessage.ERREUR,
                                    getSession().getLabel("ENVOYER_ANNONCES"));
                        }
                    } else {
                        getMemoryLog().logMessage("Error for ARC id = " + annonce.getIdAnnonce(), FWMessage.ERREUR,
                                getSession().getLabel("ENVOYER_ANNONCES"));
                    }
                    hasError = true;
                    continue;
                }
            }

            // s'il y a eu une (ou plusieurs) erreur(s), levé d'une exception
            // de cette manière, on aura toutes les erreurs dans l'email de confirmation
            // plutôt qu'une erreur à la fois
            if (hasError) {
                throw new Exception(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERREUR_PREPARATION_ENVOI"));
            }

            mgr.cursorClose(statement);

            String fileName = genereFichier(lotAnnonces);
            envoiFichier(fileName);

        } catch (Exception e) {
            String message = getSession().getLabel("ENVOYER_ANNONCES");
            LOG.warn(message, e);

            StringBuilder messageBuilder = new StringBuilder();

            getMemoryLog().logMessage(messageBuilder.toString(), FWMessage.ERREUR, message);
            getMemoryLog().logMessage(e.toString() + "}\n", FWMessage.ERREUR, message);

            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                LOG.error(message, e);
            }
            return false;
        }
        return true;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    private void envoieAnnonces() throws Exception {

        BISession remoteSession = PRSession.connectSession(getSession(),
                REEnvoyerAnnoncesXMLProcess.APPLICATION_ANNONCES);

        // création de l'API
        IHEInputAnnonceLight remoteEcritureAnnonce = (IHEInputAnnonceLight) remoteSession
                .getAPIFor(IHEInputAnnonceLight.class);

        Iterator<Map<String, String>> iter = annoncesAEnvoyer.iterator();
        String currentNSS = "";
        String previousNSS;

        while (iter.hasNext()) {
            previousNSS = currentNSS;

            remoteEcritureAnnonce.clear();
            remoteEcritureAnnonce.setIdProgramme(REApplication.DEFAULT_APPLICATION_CORVUS);
            remoteEcritureAnnonce.setUtilisateur(getSession().getUserId());
            remoteEcritureAnnonce.setStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);
            Map<String, String> element = iter.next();
            remoteEcritureAnnonce.putAll(element);

            if (element.containsKey(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT)) {
                currentNSS = element.get(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT);
            } else {
                currentNSS = "";
            }

            try {
                remoteEcritureAnnonce.add(getTransaction());
            } catch (Exception e) {
                if ((e != null) && (e.toString() != null) && (e.toString().length() > 0)) {
                    String errorMsg = e.toString();
                    if (!JadeStringUtil.isBlankOrZero(currentNSS)) {
                        errorMsg += " : " + currentNSS;
                    } else if (!JadeStringUtil.isBlankOrZero(previousNSS)) {

                        errorMsg += " !arc.préc! : " + previousNSS;
                    }
                    throw new Exception(errorMsg, e);
                } else {
                    throw e;
                }
            }
        }

    }

    private void envoieChamp(Map<String, String> map, String clefAttribut, String valeurAttribut) {
        if (!JadeStringUtil.isEmpty(valeurAttribut)) {
            map.put(clefAttribut, valeurAttribut);
        }
    }

    protected String formatXPosAppendWithBlank(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append(" ");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

    private String formatXPosAppendWithZero(int nombrePos, boolean isAppendLeft, String value) {

        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append("0");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
            }
        }
        return result.toString();
    }

    List<Map<String, String>> getAnnoncesAEnvoyer() {
        return annoncesAEnvoyer;
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {

        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_FAILED");
        } else {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_SUCCESS");
        }

    }

    /**
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * getter pour l'attribut for mois annee comptable
     * 
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
    }

    public Boolean getIsForAnnoncesSubsequentes() {
        return isForAnnoncesSubsequentes;
    }

    /**
     * Retourne le numéro de l'agence défini dans la {@link globaz.globall.db.BApplication BApplication} <br/>
     * <br/>
     * Cette méthode a été externalisée pour pouvoir tester les méthodes de préparation d'envoie (
     * {@link #preparerAugmentation10Eme(REAnnoncesAugmentationModification10Eme, REAnnoncesAugmentationModification10Eme)
     * preparerAugmentation10Eme},
     * {@link #preparerAugmentation9Eme(REAnnoncesAugmentationModification9Eme, REAnnoncesAugmentationModification9Eme)
     * preparerAugmentation9Eme}, {@link #preparerDiminution10Eme(REAnnoncesDiminution10Eme) preparerDiminution10Eme},
     * {@link #preparerDiminution9Eme(REAnnoncesDiminution9Eme) preparerDiminution9Eme}) <br/>
     * en étant indépendant de l'application
     * 
     * @return le numéro de l'agence en format {@link java.lang.String String}
     * @throws Exception
     */
    String getNumeroAgenceFromApplication() throws Exception {
        return formatXPosAppendWithZero(3, true,
                CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()));
    }

    /**
     * Retourne le numéro de la caisse défini dans la {@link globaz.globall.db.BApplication BApplication} <br/>
     * <br/>
     * Cette méthode a été externalisée pour pouvoir tester les méthodes de préparation d'envoie (
     * {@link #preparerAugmentation10Eme(REAnnoncesAugmentationModification10Eme, REAnnoncesAugmentationModification10Eme)
     * preparerAugmentation10Eme},
     * {@link #preparerAugmentation9Eme(REAnnoncesAugmentationModification9Eme, REAnnoncesAugmentationModification9Eme)
     * preparerAugmentation9Eme}, {@link #preparerDiminution10Eme(REAnnoncesDiminution10Eme) preparerDiminution10Eme},
     * {@link #preparerDiminution9Eme(REAnnoncesDiminution9Eme) preparerDiminution9Eme}) <br/>
     * en étant indépendant de l'application
     * 
     * @return le numéro de la caisse en format {@link java.lang.String String}
     * @throws Exception
     */
    String getNumeroCaisseFromApplication() throws Exception {
        return formatXPosAppendWithZero(3, true,
                CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()));
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Enregistre une erreur qui sera retourner à l'utilisateur dans l'email de confirmation <br/>
     * L'erreur est enrichie avec le numéro AVS du tiers pour qui l'annonce est faite <br/>
     * ainsi que du code application de l'annonce et du genre de prestation <br/>
     * <br/>
     * Cette méthode est très spécifique à deux cas : lorsque le numéro de la caisse de l'annonce diffère du numéro de
     * la caisse de l'application, et idem pour le numéro de l'agence.
     * 
     * @param annonce
     *            l'annonce pour laquelle une erreur doit être notifiée à l'utilisateur
     * @param erreur
     *            le message d'erreur en préambule
     * @param numeroVoulu
     *            le numéro d'agence ou de caisse qui était désiré
     * @param numeroActuel
     *            le numéro d'agence ou de caisse de l'annonce (qui ne correspond pas à nos attente)
     */
    void logMessageAvecInfos(REAnnoncesAbstractLevel1A annonce, String erreur, String numeroVoulu, String numeroActuel) {

        String nss = "";

        try {
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), annonce.getIdTiers());
            nss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } catch (Exception ex) {
            nss = "NSS Not Found";
        }

        String codeApplication = annonce.getCodeApplication();
        String genrePrestation = annonce.getGenrePrestation();

        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append(erreur).append(" : ");
        messageBuilder.append(numeroActuel).append(" --> ").append(numeroVoulu);

        messageBuilder.append(" N° Assuré(e) : ").append(nss);

        messageBuilder.append(" dans l'annonce avec le code application :").append(codeApplication);
        messageBuilder.append(" et genre de prestation : ").append(genrePrestation);

        getMemoryLog().logMessage(messageBuilder.toString(), FWMessage.INFORMATION,
                getSession().getLabel("ENVOYER_ANNONCES"));
    }

    /**
     * Valide une annonce d'augmentation avec ANAKIN <br/>
     * Lance une exception si une erreur de parsing survient
     * 
     * @param enregistrement01
     * @param enregistrement02
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    void parseAugmentationAvecAnakin(REAnnoncesAbstractLevel1A enregistrement01,
            REAnnoncesAbstractLevel1A enregistrement02) throws Exception {

        Enumeration erreurs = REAnakinParser.getInstance().parse(getSession(), enregistrement01, enregistrement02,
                forMoisAnneeComptable);

        StringBuilder stringBuilder = new StringBuilder();
        while ((erreurs != null) && erreurs.hasMoreElements()) {
            AnnonceErreur erreur = (AnnonceErreur) erreurs.nextElement();
            stringBuilder.append(erreur.getMessage()).append("\n");
        }
        if (stringBuilder.length() > 0) {
            throw new Exception(stringBuilder.toString());
        }

    }

    /**
     * Valide une annonce de diminution avec ANAKIN <br/>
     * Lance une exception si une erreur de parsing survient
     * 
     * @param enregistrement01
     * @throws Exception
     */
    void parseDiminutionAvecAnakin(REAnnoncesAbstractLevel1A enregistrement01) throws Exception {
        parseAugmentationAvecAnakin(enregistrement01, null);
    }

    /**
     * Aiguille la préparation de l'annonce passée en paramètre sur la bonne méthode <br/>
     * selon si c'est une annonce sous la 9ème ou 10ème révision, et si c'est une augmentation ou une diminution
     * 
     * @param annonce
     *            l'annonce à préparer
     * @throws Exception
     *             si une erreur dans la validation par ANAKIN surivent, une exception est lancée
     */
    void prepareEnvoieAnnonce(REAnnoncesAbstractLevel1A annonce, PoolMeldungZurZAS poolMeldung) throws Exception {
        Map<String, String> attribs = new HashMap<String, String>();
        REAnnonces9eXmlService serviceAnnonces = new REAnnonces9eXmlService();
        int codeApplication = Integer.parseInt(annonce.getCodeApplication());

        switch (codeApplication) {
            case 41:
            case 43:
                REAnnoncesAugmentationModification9Eme augmentation9eme01 = new REAnnoncesAugmentationModification9Eme();
                augmentation9eme01.setSession(getSession());
                augmentation9eme01.setIdAnnonce(annonce.getIdAnnonce());
                augmentation9eme01.retrieve();

                REAnnoncesAugmentationModification9Eme augmentation9eme02 = new REAnnoncesAugmentationModification9Eme();
                augmentation9eme02.setSession(getSession());
                augmentation9eme02.setIdAnnonce(augmentation9eme01.getIdLienAnnonce());
                augmentation9eme02.retrieve();

                parseAugmentationAvecAnakin(augmentation9eme01, augmentation9eme02);

                if (codeApplication == 41) {
                    serviceAnnonces.annonceAugmentationOrdinaire9e(augmentation9eme01, augmentation9eme01);
                } else {
                    // préparer annonceModification
                }

                if (!augmentation9eme02.isNew()) {
                    augmentation9eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation9eme02.update(getTransaction());
                }
                break;
            case 42:
                REAnnoncesDiminution9Eme diminution9eme01 = new REAnnoncesDiminution9Eme();
                diminution9eme01.setSession(getSession());
                diminution9eme01.setIdAnnonce(annonce.getIdAnnonce());
                diminution9eme01.retrieve();

                parseDiminutionAvecAnakin(diminution9eme01);

                attribs.putAll(preparerDiminution9Eme(diminution9eme01));

                if (!attribs.isEmpty()) {
                    annoncesAEnvoyer.add(attribs);
                }
                break;
            case 44:
            case 46:
                REAnnoncesAugmentationModification10Eme augmentation10eme01 = new REAnnoncesAugmentationModification10Eme();
                augmentation10eme01.setSession(getSession());
                augmentation10eme01.setIdAnnonce(annonce.getIdAnnonce());
                augmentation10eme01.retrieve();

                REAnnoncesAugmentationModification10Eme augmentation10eme02 = new REAnnoncesAugmentationModification10Eme();
                augmentation10eme02.setSession(getSession());
                augmentation10eme02.setIdAnnonce(augmentation10eme01.getIdLienAnnonce());
                augmentation10eme02.retrieve();

                parseAugmentationAvecAnakin(augmentation10eme01, augmentation10eme02);

                attribs = preparerAugmentation10EmeEnregistrement01(augmentation10eme01);
                if (!attribs.isEmpty()) {
                    annoncesAEnvoyer.add(attribs);
                }

                attribs = preparerAugmentation10EmeEnregistrement02(augmentation10eme02);
                if (!attribs.isEmpty()) {
                    annoncesAEnvoyer.add(attribs);
                }

                if (!augmentation10eme02.isNew()) {
                    augmentation10eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation10eme02.update(getTransaction());
                }
                break;
            case 45:
                REAnnoncesDiminution10Eme diminution10eme01 = new REAnnoncesDiminution10Eme();
                diminution10eme01.setSession(getSession());
                diminution10eme01.setIdAnnonce(annonce.getIdAnnonce());
                diminution10eme01.retrieve();

                parseDiminutionAvecAnakin(diminution10eme01);

                attribs.putAll(preparerDiminution10Eme(diminution10eme01));

                if (!attribs.isEmpty()) {
                    annoncesAEnvoyer.add(attribs);
                }
                break;
            default:
                getMemoryLog().logMessage("Code Application inconnu", FWMessage.ERREUR,
                        getSession().getLabel("ENVOYER_ANNONCES"));
                break;
        }
    }

    Map<String, String> preparerAugmentation10EmeEnregistrement01(
            REAnnoncesAugmentationModification10Eme enregistrement01) throws Exception {
        HashMap<String, String> attribs = new HashMap<String, String>();

        // Enregistrement 01
        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_APPLICATION, enregistrement01.getCodeApplication());
        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_ENREGISTREMENT, enregistrement01.getCodeEnregistrement01());

        attribs.putAll(validerNumeroCaisseEtAgence(enregistrement01));

        envoieChamp(attribs, IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                formatXPosAppendWithBlank(20, false, enregistrement01.getReferenceCaisseInterne()));
        envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNoAssAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getPremierNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getSecondNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNouveauNoAssureAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                formatXPosAppendWithBlank(1, false, enregistrement01.getEtatCivil()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_REFUGIE,
                formatXPosAppendWithBlank(1, false, enregistrement01.getIsRefugie()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE,
                formatXPosAppendWithBlank(3, true, enregistrement01.getCantonEtatDomicile()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getGenrePrestation()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getDebutDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS,
                formatXPosAppendWithBlank(5, true, enregistrement01.getMensualitePrestationsFrancs()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getFinDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT,
                formatXPosAppendWithBlank(4, false, enregistrement01.getMoisRapport()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_DE_MUTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getCodeMutation()));

        return attribs;
    }

    Map<String, String> preparerAugmentation10EmeEnregistrement02(
            REAnnoncesAugmentationModification10Eme enregistrement02) throws Exception {
        HashMap<String, String> attribs = new HashMap<String, String>();
        if (!enregistrement02.isNew()) {
            // Enregistrement 02
            envoieChamp(attribs, IHEAnnoncesViewBean.CODE_APPLICATION, enregistrement02.getCodeApplication());
            envoieChamp(attribs, IHEAnnoncesViewBean.CODE_ENREGISTREMENT, enregistrement02.getCodeEnregistrement01());
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_ECHELLE_DE_RENTES,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getEchelleRente()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_AV_1973_AAMM,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCoEchelleRenteAv73()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_DES_1973_AAMM,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCoEchelleRenteDes73()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1948_72,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getDureeCotManquante48_72()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1973_78,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getDureeCotManquante73_78()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_ANNEE_COTISATIONS_DE_LA_CLASSE_AGE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getAnneeCotClasseAge()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REVENU_ANNUEL_MOYEN_DETERMINANT_EN_FRANCS,
                    formatXPosAppendWithBlank(8, true, enregistrement02.getRamDeterminant()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_DE_REVENUS_SPLITTES,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getCodeRevenuSplitte()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_POUR_DETERMINER_REVENU_ANNUEL_MOYEN_AAMM,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCotPourDetRAM()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_ANNEE_DE_NIVEAU,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getAnneeNiveau()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_EDUCATIVES_AADD,
                    formatXPosAppendWithBlank(4, true, enregistrement02.getNombreAnneeBTE()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_ASSISTANCE_AADD,
                    formatXPosAppendWithBlank(4, true, enregistrement02.getNbreAnneeBTA()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_TRANSITOIREs_AD,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getNbreAnneeBonifTrans()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_OFFICE_AI,
                    formatXPosAppendWithBlank(3, false, enregistrement02.getOfficeAICompetent()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEGRE_INVALIDITE,
                    formatXPosAppendWithBlank(3, true, enregistrement02.getDegreInvalidite()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_INFIRMITE,
                    formatXPosAppendWithBlank(5, false, enregistrement02.getCodeInfirmite()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT_MMAA,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getSurvenanceEvenAssure()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_AGE_DEBUT_INVALIDITE_AYANT_DROIT,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getAgeDebutInvalidite()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_GENRE_DROIT_API,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getGenreDroitAPI()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REDUCTION,
                    formatXPosAppendWithBlank(2, true, JadeStringUtil.removeChar(enregistrement02.getReduction(), '.')));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_1_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial1()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_2_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial2()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_3_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial3()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_4_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial4()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_5_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial5()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_NB_ANNEE_ANTICIPATION,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getNbreAnneeAnticipation()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REDUCTION_ANTICIPATION_FRANCS,
                    formatXPosAppendWithBlank(5, true, enregistrement02.getReductionAnticipation()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DATE_DEBUT_ANTICIPATION_MMAA,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDateDebutAnticipation()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_AJOURNEMENT_AMM,
                    formatXPosAppendWithBlank(3, false, enregistrement02.getDureeAjournement()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_SUPPLEMENT_AJOURNEMENT_FRANCS,
                    formatXPosAppendWithBlank(5, true, enregistrement02.getSupplementAjournement()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DATE_REVOCATION_AJOURNEMENT_MMAA,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDateRevocationAjournement()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_SURVIVANT_INVALIDE,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getIsSurvivant()));
        }
        return attribs;
    }

    Map<String, String> preparerAugmentation9EmeEnregistrement01(REAnnoncesAugmentationModification9Eme enregistrement01)
            throws Exception {
        HashMap<String, String> attribs = new HashMap<String, String>();

        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_APPLICATION, enregistrement01.getCodeApplication());
        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_ENREGISTREMENT, enregistrement01.getCodeEnregistrement01());

        attribs.putAll(validerNumeroCaisseEtAgence(enregistrement01));

        // Enregistrement 01
        envoieChamp(attribs, IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                formatXPosAppendWithBlank(20, false, enregistrement01.getReferenceCaisseInterne()));
        envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNoAssAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getPremierNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getSecondNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNouveauNoAssureAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                formatXPosAppendWithBlank(1, false, enregistrement01.getEtatCivil()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_REFUGIE,
                formatXPosAppendWithBlank(1, false, enregistrement01.getIsRefugie()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE,
                formatXPosAppendWithBlank(3, true, enregistrement01.getCantonEtatDomicile()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getGenrePrestation()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getDebutDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS,
                formatXPosAppendWithBlank(5, true, enregistrement01.getMensualitePrestationsFrancs()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS,
                formatXPosAppendWithBlank(5, true, enregistrement01.getMensualiteRenteOrdRemp()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getFinDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT,
                formatXPosAppendWithBlank(4, false, enregistrement01.getMoisRapport()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_DE_MUTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getCodeMutation()));

        return attribs;
    }

    Map<String, String> preparerAugmentation9EmeEnregistrement02(REAnnoncesAugmentationModification9Eme enregistrement02) {
        HashMap<String, String> attribs = new HashMap<String, String>();
        if (!enregistrement02.isNew()) {
            // Enregistrement 02
            envoieChamp(attribs, IHEAnnoncesViewBean.CODE_APPLICATION, enregistrement02.getCodeApplication());
            envoieChamp(attribs, IHEAnnoncesViewBean.CODE_ENREGISTREMENT, enregistrement02.getCodeEnregistrement01());
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REVENU_ANNUEL_MOYEN_DETERMINANT_EN_FRANCS,
                    formatXPosAppendWithBlank(8, true, enregistrement02.getRamDeterminant()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_POUR_DETERMINER_REVENU_ANNUEL_MOYEN_AAMM,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCotPourDetRAM()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_ANNEE_DE_NIVEAU,
                    formatXPosAppendWithBlank(2, false, enregistrement02.getAnneeNiveau()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REVENUS_PRIS_EN_COMPTE,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getRevenuPrisEnCompte()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_ECHELLE_DE_RENTES,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getEchelleRente()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_AV_1973_AAMM,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCoEchelleRenteAv73()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_DES_1973_AAMM,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDureeCoEchelleRenteDes73()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1948_72,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getDureeCotManquante48_72()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_ANNEE_COTISATIONS_DE_LA_CLASSE_AGE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getAnneeCotClasseAge()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_AJOURNEMENT_AMM,
                    formatXPosAppendWithBlank(3, false, enregistrement02.getDureeAjournement()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_SUPPLEMENT_AJOURNEMENT_FRANCS,
                    formatXPosAppendWithBlank(5, true, enregistrement02.getSupplementAjournement()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DATE_REVOCATION_AJOURNEMENT_MMAA,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getDateRevocationAjournement()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_LIMITES_DE_REVENU,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getIsLimiteRevenu()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_MINIMUM_GARANTIT,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getIsMinimumGaranti()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_OFFICEAI_COMPETENT_AYANT_DROIT,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getOfficeAICompetent()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_OFFICEAI_COMPETENT_EPOUSE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getOfficeAiCompEpouse()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEGREINVALIDITE_AYANT_DROIT,
                    formatXPosAppendWithBlank(3, true, enregistrement02.getDegreInvalidite()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEGREINVALIDITE_EPOUSE,
                    formatXPosAppendWithBlank(3, true, enregistrement02.getDegreInvaliditeEpouse()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODEINFIRMITE_AYANT_DROIT,
                    formatXPosAppendWithBlank(5, false, enregistrement02.getCodeInfirmite()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODEINFIRMITE_EPOUSE,
                    formatXPosAppendWithBlank(5, false, enregistrement02.getCodeInfirmiteEpouse()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT_MMAA,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getSurvenanceEvenAssure()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_SURVENANCE_EVENEMENT_ASSURE_EPOUSE_MMAA,
                    formatXPosAppendWithBlank(4, false, enregistrement02.getSurvenanceEvtAssureEpouse()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_AGE_DEBUT_INVALIDITE_AYANT_DROIT,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getAgeDebutInvalidite()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_AGE_DEBUT_INVALIDITE_EPOUSE,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getAgeDebutInvaliditeEpouse()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_GENRE_DROIT_API,
                    formatXPosAppendWithBlank(1, false, enregistrement02.getGenreDroitAPI()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REDUCTION,
                    formatXPosAppendWithBlank(2, true, JadeStringUtil.removeChar(enregistrement02.getReduction(), '.')));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_1_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial1()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_2_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial2()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_3_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial3()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_4_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial4()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_CAS_SPECIAL_5_CODE,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getCasSpecial5()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1973_78,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getDureeCotManquante73_78()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_REVENU_ANNUEL_MOYEN_SANS_BONIFI_TACHES_EDUC_FRANCS,
                    formatXPosAppendWithBlank(8, true, enregistrement02.getRevenuAnnuelMoyenSansBTE()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_BONIFI_TACHES_EDUC_MOYENNES_FRANCS,
                    formatXPosAppendWithBlank(6, true, enregistrement02.getBteMoyennePrisEnCompte()));
            envoieChamp(attribs, IHEAnnoncesViewBean.CS_NB_ANNEES_BONIFI_TACHES_EDUC,
                    formatXPosAppendWithBlank(2, true, enregistrement02.getNombreAnneeBTE()));
        }
        return attribs;
    }

    Map<String, String> preparerDiminution10Eme(REAnnoncesDiminution10Eme enregistrement01) throws Exception {
        HashMap<String, String> attribs = new HashMap<String, String>();

        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_APPLICATION, enregistrement01.getCodeApplication());
        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_ENREGISTREMENT, enregistrement01.getCodeEnregistrement01());

        attribs.putAll(validerNumeroCaisseEtAgence(enregistrement01));

        envoieChamp(attribs, IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                formatXPosAppendWithBlank(20, false, enregistrement01.getReferenceCaisseInterne()));
        envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNoAssAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getPremierNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getSecondNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNouveauNumeroAssureAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                formatXPosAppendWithBlank(1, false, enregistrement01.getEtatCivil()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_REFUGIE,
                formatXPosAppendWithBlank(1, false, enregistrement01.getIsRefugie()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE,
                formatXPosAppendWithBlank(3, true, enregistrement01.getCantonEtatDomicile()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getGenrePrestation()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getDebutDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS,
                formatXPosAppendWithBlank(5, true, enregistrement01.getMensualitePrestationsFrancs()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getFinDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT,
                formatXPosAppendWithBlank(4, false, enregistrement01.getMoisRapport()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_DE_MUTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getCodeMutation()));

        return attribs;
    }

    Map<String, String> preparerDiminution9Eme(REAnnoncesDiminution9Eme enregistrement01) throws Exception {
        HashMap<String, String> attribs = new HashMap<String, String>();

        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_APPLICATION, enregistrement01.getCodeApplication());
        envoieChamp(attribs, IHEAnnoncesViewBean.CODE_ENREGISTREMENT, enregistrement01.getCodeEnregistrement01());

        attribs.putAll(validerNumeroCaisseEtAgence(enregistrement01));

        envoieChamp(attribs, IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                formatXPosAppendWithBlank(20, false, enregistrement01.getReferenceCaisseInterne()));
        envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNoAssAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getPremierNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE,
                formatXPosAppendWithBlank(11, false, enregistrement01.getSecondNoAssComplementaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION,
                formatXPosAppendWithBlank(11, false, enregistrement01.getNouveauNumeroAssureAyantDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_ETAT_CIVIL_AYANT_DROIT,
                formatXPosAppendWithBlank(1, false, enregistrement01.getEtatCivil()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_REFUGIE,
                formatXPosAppendWithBlank(1, false, enregistrement01.getIsRefugie()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CANTON_ETAT_DOMICILE,
                formatXPosAppendWithBlank(3, true, enregistrement01.getCantonEtatDomicile()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getGenrePrestation()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_DEBUT_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getDebutDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS,
                formatXPosAppendWithBlank(5, true, enregistrement01.getMensualitePrestationsFrancs()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS,
                formatXPosAppendWithBlank(5, true, enregistrement01.getMensualiteRenteOrdinaire()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_FIN_DU_DROIT_MMAA,
                formatXPosAppendWithBlank(4, false, enregistrement01.getFinDroit()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT,
                formatXPosAppendWithBlank(4, false, enregistrement01.getMoisRapport()));
        envoieChamp(attribs, IHEAnnoncesViewBean.CS_CODE_DE_MUTATION,
                formatXPosAppendWithBlank(2, true, enregistrement01.getCodeMutation()));

        return attribs;
    }

    /**
     * setter pour l'attribut for date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    public void setIsForAnnoncesSubsequentes(Boolean isForAnnoncesSubsequentes) {
        this.isForAnnoncesSubsequentes = isForAnnoncesSubsequentes;
    }

    /**
     * Traitement commun à toutes les méthodes de préparation d'envoie qui vérifie que le numéro de caisse et d'agence
     * d'une annonce correspond à celui de l'application <br/>
     * dans le cas contraire, l'utilisateur est notifié de l'erreur par l'email de confirmation qui reçoit
     * 
     * @param annonce
     *            l'annonce à vérifier
     * @return un Map contenant les clé-valeurs pour le numéro d'agence et de caisse
     * @throws Exception
     */
    Map<String, String> validerNumeroCaisseEtAgence(REAnnoncesAbstractLevel1A annonce) throws Exception {
        HashMap<String, String> attribs = new HashMap<String, String>();

        String nc = getNumeroCaisseFromApplication();
        String na = getNumeroAgenceFromApplication();

        String nc2 = formatXPosAppendWithBlank(3, true, annonce.getNumeroCaisse());
        String na2 = formatXPosAppendWithBlank(3, true, annonce.getNumeroAgence());

        if ((nc != null) && (nc.compareTo(nc2) != 0)) {
            logMessageAvecInfos(annonce, "Mauvais no de caisse", nc, nc2);
        }
        if ((na != null) && (na.compareTo(na2) != 0)) {
            logMessageAvecInfos(annonce, "Mauvais no d'agence", na, na2);
        }

        if ((na != null) && (nc != null)) {
            envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_CAISSE, formatXPosAppendWithBlank(3, true, nc));
            envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_AGENCE, formatXPosAppendWithBlank(3, true, na));
        } else {
            envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_CAISSE, formatXPosAppendWithBlank(3, true, nc2));
            envoieChamp(attribs, IHEAnnoncesViewBean.NUMERO_AGENCE, formatXPosAppendWithBlank(3, true, na2));
        }

        return attribs;
    }

    /**
     * Méthode qui envoie le fichier à la centrale
     * 
     * @param fichier à envoyer à la centrale
     * @throws Exception
     */
    private void envoiFichier(String fichier) throws Exception {

        JadeFsFacade.copyFile(fichier, REProperties.FTP_CENTRALE_PATH.getValue() + "/" + getFileNameTimeStamp());
    }

    /**
     * Nous donne si c'est le mode test
     * 
     * @return le mode test
     */
    public boolean isModeTest() {
        return modeTest;
    }

    /**
     * set le mode de test
     * 
     * @param modeTest
     */
    public void setModeTest(boolean modeTest) {
        this.modeTest = modeTest;
    }

    /**
     * Méthode qui retourne un nom de fichier basé sur le timestamp
     * 
     * @return
     * @throws Exception
     */
    private String getFileNameTimeStamp() throws Exception {
        String fileName = "M_" + REProperties.RACINE_NOM_FICHIER_OUTPUT_ZAS.getValue();
        fileName = JadeFilenameUtil.addFilenameSuffixDateTimeDecimals(fileName);
        fileName = fileName + ".xml";
        return fileName;
    }

    /**
     * Méthode qui génère le fichier en fonction des annonces en input
     * 
     * @return l'uri du fichier généré
     * @throws Exception
     */
    private String genereFichier(PoolMeldungZurZAS poolMeldung) throws Exception {
        String fileName;
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL url = getClass().getResource("/xsd/P2020/annoncesRC/MeldungZurZas.xsd");
        Schema schema = sf.newSchema(url);

        JAXBContext jc = JAXBContext.newInstance(poolMeldung.getClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);
        fileName = Jade.getInstance().getSharedDir() + getFileNameTimeStamp();

        File f = new File(fileName);
        f.createNewFile();

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    // logger.warn("JAXB validation error : " + event.getMessage(), this);
                    return false;
                }
            });
            marshaller.marshal(poolMeldung, f);

        } catch (JAXBException exception) {
            // logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            exception.printStackTrace();
            throw exception;

        }
        return fileName;

    }

}
