/*
 * Créé le 17 juillet 2008
 */
package globaz.corvus.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.corvus.annonce.service.REAnnonceXmlService;
import globaz.corvus.annonce.service.REAnnonces10eXmlService;
import globaz.corvus.annonce.service.REAnnonces9eXmlService;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1AManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.properties.REProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.rc.PoolFussType;
import ch.admin.zas.rc.PoolKopfType;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.ree.sedex.ValidationException;
import ch.horizon.jaspe.util.JACalendar;

/**
 * @author jmc
 * 
 */
public class REEnvoyerAnnoncesXMLProcess extends BProcess {

    private static final String XSD_FOLDER = "/xsd/P2020/annoncesRC/";
    private static final String XSD_NAME = "MeldungZurZas.xsd";

    private static final Logger LOG = LoggerFactory.getLogger(REEnvoyerAnnoncesXMLProcess.class);

    private transient Marshaller marshaller;

    private boolean modeTest = true;

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

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

            PoolMeldungZurZAS.Lot lotAnnonces = initPoolMeldungZurZASLot(REProperties.CENTRALE_TEST.getBooleanValue(),
                    CommonProperties.KEY_NO_CAISSE.getValue());

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
     * 
     * @param poolKopfTest if you need to set TEST flag into header
     * @param poolKopfSender
     * @return
     * @throws PropertiesException
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    private PoolMeldungZurZAS.Lot initPoolMeldungZurZASLot(boolean poolKopfTest, String poolKopfSender)
            throws PropertiesException, ParseException, DatatypeConfigurationException {
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();
        ch.admin.zas.pool.PoolMeldungZurZAS.Lot lot = factoryPool.createPoolMeldungZurZASLot();
        PoolKopfType poolKopf = factoryType.createPoolKopfType();
        if (poolKopfTest) {
            poolKopf.setTest("TEST");
        }
        poolKopf.setSender(poolKopfSender);

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

        return lot;
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
        return CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication());
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
        return CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication());
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
     * Aiguille la préparation de l'annonce passée en paramètre sur la bonne méthode <br/>
     * selon si c'est une annonce sous la 9ème ou 10ème révision, et si c'est une augmentation ou une diminution
     * 
     * @param annonce
     *            l'annonce à préparer
     * @throws Exception
     *             si une erreur dans la validation par ANAKIN surivent, une exception est lancée
     */
    void prepareEnvoieAnnonce(REAnnoncesAbstractLevel1A annonce, PoolMeldungZurZAS.Lot poolMeldungLot) throws Exception {

        REAnnonceXmlService abstractService = resolveAnnonceVersionService(annonce);

        Object annonceXml = abstractService.getAnnonceXml(annonce, forMoisAnneeComptable, getSession(),
                getTransaction());

        validateUnitMessage(annonceXml);

        poolMeldungLot
                .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                .add(annonceXml);

    }

    protected REAnnonceXmlService resolveAnnonceVersionService(REAnnoncesAbstractLevel1A annonce) throws Exception {
        int codeApplication = Integer.parseInt(annonce.getCodeApplication());
        switch (codeApplication) {
            case 41:
            case 42:
            case 43:
                return REAnnonces9eXmlService.getInstance();
            case 44:
            case 45:
            case 46:
                return REAnnonces10eXmlService.getInstance();
            default:

                getMemoryLog().logMessage("Code Application inconnu", FWMessage.ERREUR,
                        getSession().getLabel("ENVOYER_ANNONCES"));
                throw new Exception("Code Application inconnu : " + codeApplication);

        }
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
     * Méthode qui génère le fichier en fonction d'un lot d'annonces en input
     * 
     * @return l'uri du fichier généré
     * @throws Exception
     */
    private String genereFichier(PoolMeldungZurZAS.Lot lotAnnonce) throws Exception {
        String fileName;
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        PoolMeldungZurZAS pool = factoryPool.createPoolMeldungZurZAS();
        pool.getLot().add(lotAnnonce);
        initMarshaller(pool);
        fileName = Jade.getInstance().getSharedDir() + getFileNameTimeStamp();

        File f = new File(fileName);
        f.createNewFile();

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.warn("JAXB validation error : " + event.getMessage(), this);
                    return false;
                }
            });
            marshaller.marshal(pool, f);

        } catch (JAXBException exception) {
            LOG.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            exception.printStackTrace();
            throw exception;

        }
        return fileName;

    }

    private Marshaller initMarshaller(Object element) throws SAXException, JAXBException {
        if (marshaller == null) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = getClass().getResource(XSD_FOLDER + XSD_NAME);
            Schema schema = sf.newSchema(url);

            JAXBContext jc = JAXBContext.newInstance(element.getClass());

            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(schema);
        }
        return marshaller;
    }

    /**
     * CAUTION, only element choice of a PoolMeldungZurZAS.Lot
     * 
     * possible object are element of Lot {@link PoolMeldungZurZAS.Lot }
     * 
     * @param element : must be an element to put on a PoolMeldungZurZAS.Lot
     * @throws ValidationException
     * @throws SAXException
     * @throws JAXBException
     */
    public void validateUnitMessage(Object element) throws ValidationException, SAXException, JAXBException {
        PoolMeldungZurZAS pool;
        final List<String> validationErrors = new LinkedList<String>();
        try {
            ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
            pool = factoryPool.createPoolMeldungZurZAS();
            PoolMeldungZurZAS.Lot lot = initPoolMeldungZurZASLot(true, "validateUnitMessage");
            lot.getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                    .add(element);
            pool.getLot().add(lot);
            initMarshaller(pool);

            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.warn("JAXB validation error : " + event.getMessage(), this);
                    validationErrors.add(event.getMessage());
                    return true;
                }

            });

            marshaller.marshal(pool, new ByteArrayOutputStream());

        } catch (JAXBException exception) {
            LOG.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        } catch (Exception e) {
            LOG.error("impossible d'initialier un PoolMeldungZurZAS", e);
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

    }

}
