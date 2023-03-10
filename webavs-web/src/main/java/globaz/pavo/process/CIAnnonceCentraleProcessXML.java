package globaz.pavo.process;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.pool.PoolMeldungZurZAS.Lot;
import ch.admin.zas.rc.IKStatistikMeldungType;
import ch.admin.zas.rc.IKStatistikMeldungType.Aufzeichnungen;
import ch.admin.zas.rc.IKStatistikMeldungType.Aufzeichnungen.EintragungIKMeldung;
import ch.admin.zas.rc.IKStatistikMeldungType.Aufzeichnungen.IKKopf;
import ch.admin.zas.rc.IKStatistikMeldungType.StandIKStatistikNacher;
import ch.admin.zas.rc.PoolFussType;
import ch.admin.zas.rc.PoolKopfType;
import ch.admin.zas.rc.TEintragungIKMinDat.Beitragsdauer;
import ch.admin.zas.rc.TStandIKStatistik;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleManager;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;

public class CIAnnonceCentraleProcessXML extends BProcess {
    private static final long serialVersionUID = 1L;
    public static final String ENCODING = "Cp037";
    public static final int MAX_ECRITURE_LOT_DEFAUT = 50000;
    public static final String MODE_EXECUTION_INFOROM_D0064 = "modeExecutionInforomD0064";
    private static final String REPORT_REVENUS_DEFAULT = "00000000000";
    private String agence = "";
    private String anneeAA = "";
    private CIAnnonceCentrale annonceCentrale = null;
    private String caisse = "";
    private static final int AFFILE_LENGTH = 11;
    private int maxEcrituresASelect = 0;
    private String modeExecution = "";
    private String montantInitial = "";
    private BigInteger totalRevenus = null;
    private static final int NUM_AVS_LENGTH = 11;
    private String typeEnregistrement = CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE;
    private static final String XSD_FOLDER = "/xsd/P2020/annoncesRC/";
    private static final String XSD_NAME = "MeldungZurZas.xsd";
    protected ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();
    private static final Logger LOG = LoggerFactory.getLogger(CIAnnonceCentraleGenerationProcess.class);
    IKStatistikMeldungType annonceXML;
    Marshaller marshaller = null;
    Marshaller marshallerIKStat = null;
    private static CIAnnonceCentraleProcessXML instance = null;
    private HashMap<Aufzeichnungen, ArrayList<ValidationEvent>> listError;
    private Aufzeichnungen compteCI;

    /**
     * Constructor for CIAnnonceCentraleProcess.
     */
    public CIAnnonceCentraleProcessXML() {
        super();
    }

    public static CIAnnonceCentraleProcessXML getInstance() {
        if (instance == null) {
            instance = new CIAnnonceCentraleProcessXML();
        }
        return instance;
    }

    public CIAnnonceCentraleProcessXML(BSession session) {
        super(session);
        setState(session.getLabel("MSG_ANN_CEN_PROC_START"));
    }

    public CIAnnonceCentraleProcessXML(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            // init valeurs communes

            caisse = StringUtils.padBeforeString(getSession().getApplication().getProperty(CIApplication.CODE_CAISSE),
                    "0", 3);
            agence = StringUtils.padBeforeString(getSession().getApplication().getProperty(CIApplication.CODE_AGENCE),
                    "0", 3);
            anneeAA = DateUtils.getYear(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ).substring(2, 4);

            InitProcess();
            //

            genererEcritureXML();

            System.out.println(DateUtils.getTimeStamp() + "Processus termin? -> Transaction ok");
        } catch (Exception e) {
            if (e instanceof CIAnnonceCentraleException) {
                CIAnnonceCentraleException exp = (CIAnnonceCentraleException) e;
                System.err.println(exp.toString());
                System.out.println(DateUtils.getTimeStamp() + "Transaction pas ok -> rollback");
                if (!exp.isPathError()) {
                    getTransaction().addErrors(exp.getErrorMessages());
                } else {
                    getTransaction().addErrors(exp.getFTPError());
                }

            } else {
                System.err.println(e.getMessage());
                System.out.println(DateUtils.getTimeStamp() + "Transaction pas ok -> rollback");
                getTransaction().addErrors(e.getMessage());
            }
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setModeExecution(String modeExecution) {
        this.modeExecution = modeExecution;

    }

    public void setAnnonceCentrale(CIAnnonceCentrale annonceCentrale) {
        this.annonceCentrale = annonceCentrale;

    }

    private void InitProcess() throws Exception {

        CIAnnonceCentraleManager annoncePrecedentes = new CIAnnonceCentraleManager();
        annoncePrecedentes.setSession(getSession());
        annoncePrecedentes.setForAnnee(DateUtils.getYear(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ));
        if (isModeExecutionInforomD0064()) {
            annoncePrecedentes.setBeforeDateEnvoi(
                    DateUtils.convertDate(annonceCentrale.getDateEnvoi(), DateUtils.JJMMAAAA_DOTS, DateUtils.AAAAMMJJ));
            annoncePrecedentes.setInStatut(CIAnnonceCentrale.CS_ETAT_GENERE + "," + CIAnnonceCentrale.CS_ETAT_ENVOYE);
        }

        annoncePrecedentes.setOrderByDateEnvoiDesc();
        annoncePrecedentes.find(getTransaction(), 1);
        String startDate = "";
        if (annoncePrecedentes.size() > 0) {
            // on prend le dernier lot envoy?
            CIAnnonceCentrale derniereAnnonce = (CIAnnonceCentrale) annoncePrecedentes.getFirstEntity();
            if (CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE.equals(derniereAnnonce.getIdTypeAnnonce())) {
                // on a une annonce annonce qui a ?t? faite au pr?alable ->
                // return le montant m?moris?
                totalRevenus = new BigInteger(derniereAnnonce.getMontantTotal());
                String dateDerniere = DateUtils.convertDate(derniereAnnonce.getDateEnvoi(), DateUtils.JJMMAAAA_DOTS,
                        DateUtils.AAAAMMJJ);
                if (Integer.parseInt(dateDerniere) > Integer.parseInt(DateUtils.getCurrentDateAMJ())) {
                    startDate = dateDerniere;
                } else {
                    startDate = DateUtils.getCurrentDateAMJ();
                }
            } else {
                throw new CIAnnonceCentraleException(getSession().getLabel("MSG_ANN_CEN_ERREUR_DER_ANNEE"));
            }
        } else {
            // aucune annonce ? la centrale effectu? auparavant -> donc soit
            // revenuDefault soit un montnant definit
            totalRevenus = new BigInteger(
                    StringUtils.isStringEmpty(getMontantInitial()) ? CIAnnonceCentraleProcessXML.REPORT_REVENUS_DEFAULT
                            : getMontantInitial());
            startDate = DateUtils.getCurrentDateAMJ();
        }
        listError = new HashMap<Aufzeichnungen, ArrayList<ValidationEvent>>();
        System.out.println("Param?tres de d?part du process : date de g\u00e9n\u00e9ration " + startDate);
        System.out.println("                                  report du revenu " + totalRevenus.toString());
    }

    private boolean isModeExecutionInforomD0064() {
        return CIAnnonceCentraleProcess.MODE_EXECUTION_INFOROM_D0064.equalsIgnoreCase(modeExecution);
    }

    public String getMontantInitial() {
        return montantInitial;
    }

    private void genererEcritureXML() throws Exception {
        String dateEnvoiPrevu;
        dateEnvoiPrevu = annonceCentrale.getDateEnvoi();
        dateEnvoiPrevu = DateUtils.convertDate(dateEnvoiPrevu, DateUtils.JJMMAAAA_DOTS, DateUtils.AAAAMMJJ);
        PoolMeldungZurZAS.Lot lotAnnonces = null;
        lotAnnonces = initPoolMeldungZurZASLot(CIUtil.isTestXML(getSession()),
                CommonProperties.KEY_NO_CAISSE.getValue());

        System.out.println("G\u00e9n\u00e9ration du lot num\u00e9ro :" + 1);
        System.out.println(
                DateUtils.getTimeStamp() + "Recherche des \u00e9critures \u00e0  annoncer \u00e0  la centrale...");
        setState(getSession().getLabel("MSG_ANN_CEN_PROC_RECHERCHE"));
        CIEcritureManager ecrituresAAnnoncer = new CIEcritureManager();
        ecrituresAAnnoncer.setSession(getSession());
        ecrituresAAnnoncer.setForDateAnnonceCentrale("0");
        // prendre seulement les genres 6,7
        ecrituresAAnnoncer.setForIdTypeCompteCompta("true");

        if (isModeExecutionInforomD0064()) {
            ecrituresAAnnoncer.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } else {
            ecrituresAAnnoncer.changeManagerSize(getMaxEcrituresASelect() + 1);

            // on recherche toujours le max + 1
            // ainsi on est s?r que le deuxi?me lot aura au moins une ?criture (si
            // nbre trouve = nombre max

            ecrituresAAnnoncer.find(getTransaction(), getMaxEcrituresASelect() + 1);
        }
        setState(getSession().getLabel("MSG_ANN_CEN_PROC_GENERER"));
        System.out.println(DateUtils.getTimeStamp() + "Nombre d'annonce trouv\u00e9es :" + ecrituresAAnnoncer.size());

        boolean isEncoreLotApres;
        if (isModeExecutionInforomD0064()) {
            isEncoreLotApres = false;
        } else {
            isEncoreLotApres = (ecrituresAAnnoncer.size() - getMaxEcrituresASelect()) > 0;
        }

        if (isModeExecutionInforomD0064() && (ecrituresAAnnoncer.size() == 0)) {
            getMemoryLog().logMessage(
                    getSession().getLabel("CI_ANNONCE_CENTRALE_GENERATION_AUCUNE_INSCRIPTION_A_ANNONCER"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }
        if (ecrituresAAnnoncer.size() > 0) {
            // cr?ation du prepare statement pour la mise ? jour des ecritures
            BPreparedStatement ecrPrepared = new BPreparedStatement(getTransaction());

            try {
                ecrPrepared.prepareStatement(ecrituresAAnnoncer.getSqlForUpdateDateCentrale());
                ecrPrepared.setInt(1, Integer.parseInt(DateUtils.getCurrentDateAMJ()));
            } catch (Exception e) {
                throw new CIAnnonceCentraleException(e.getMessage());
            }

            int maxAAnnonces = isEncoreLotApres ? ecrituresAAnnoncer.size() - 1 : ecrituresAAnnoncer.size();
            annonceXML = factoryType.createIKStatistikMeldungType();
            annonceXML.setKasseZweigstelleIKFuehrend(caisse + agence);
            annonceXML.setVerarbeitungsjahr(retourneXMLGregorianCalendarFromYear(anneeAA));
            annonceXML.setStandIKStatistikVorher(generateStandIKStatistikVorher(totalRevenus));

            for (int i = 0; i < maxAAnnonces; i++) {
                CIEcriture ciEcr = (CIEcriture) ecrituresAAnnoncer.get(i);
                try {
                    prepareEnvoieAnnonce(ciEcr, annonceXML);
                    compteCI = annonceXML.getAufzeichnungen().get(i);
                    validateAnnonceCI(compteCI);
                } catch (Exception e) {
                    LOG.error("Erreur lors de la pr?paration des annonces : " + e.toString(), e);
                    throw e;
                }
                totalRevenus = totalRevenus.add(new BigInteger(getMontantSigne(ciEcr)));
                ecrPrepared.setInt(2, Integer.parseInt(ciEcr.getEcritureId()));
                ecrPrepared.execute();

            }
            if (!listError.isEmpty()) {
                CIAnnonceCentraleException exp = new CIAnnonceCentraleException();
                exp.setErrors(listError);
                throw exp;
            }
            annonceXML.setStandIKStatistikNacher(generateStandIKStatistikNacher(totalRevenus));

            lotAnnonces
                    .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                    .add(annonceXML);

            if (isModeExecutionInforomD0064()) {
                if (CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE.equalsIgnoreCase(annonceCentrale.getIdTypeAnnonce())) {
                    setTypeEnregistrement(CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE);
                } else if (CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE
                        .equalsIgnoreCase(annonceCentrale.getIdTypeAnnonce())) {
                    setTypeEnregistrement(CIAnnonceCentrale.TYPE_ENR_DERNIERE_ANNONCE_ANNEE);
                }

            }
            String fileName = null;
            try {
                fileName = genereFichier(lotAnnonces);
            } catch (JAXBException e) {
                LOG.error("Erreur lors de la g?n?ration du fichier : " + e.toString(), e);
                throw e;
            }
            try {
                envoiFichier(fileName);
            } catch (Exception e) {
                if (e instanceof JadeServiceActivatorException) {
                    JadeServiceActivatorException error = (JadeServiceActivatorException) e;
                    if (error.getThrowable() instanceof FileNotFoundException) {
                        CIAnnonceCentraleException exception = new CIAnnonceCentraleException(e.toString());
                        exception.setIsPatchError(true);
                        throw exception;
                    }
                }
                LOG.error("Erreur lors de l'envoi : " + e.toString(), e);
                throw e;
            }
            CIAnnonceCentrale annonce;
            if (isModeExecutionInforomD0064()) {

                annonceCentrale.setNbrInscriptions(String.valueOf(ecrituresAAnnoncer.size()));
                annonceCentrale.setMontantTotal(totalRevenus.toString());
                annonceCentrale.setIdEtat(CIAnnonceCentrale.CS_ETAT_ENVOYE);
                annonceCentrale.update(getTransaction());

            } else {
                System.out.println(DateUtils.getTimeStamp() + "Ajout d'une nouvelle entr?e dans la table CIANCEP");

                // m?morisation de la g?n?ration dans la base et mise ? jour des
                // annonces g?n?r?es
                annonce = new CIAnnonceCentrale();
                annonce.setSession(getSession());
                annonce.setDateCreation(JACalendar.todayJJsMMsAAAA());
                annonce.setDateEnvoi(
                        DateUtils.convertDate(dateEnvoiPrevu, DateUtils.AAAAMMJJ, DateUtils.JJMMAAAA_DOTS));
                annonce.setNbrInscriptions(
                        String.valueOf(isEncoreLotApres ? ecrituresAAnnoncer.size() - 1 : ecrituresAAnnoncer.size()));
                annonce.setMontantTotal(totalRevenus.toString());
                annonce.setIdTypeAnnonce(getCodeSystemeTypeEnregistrement(isEncoreLotApres));
                annonce.setIdEtat(CIAnnonceCentrale.CS_ETAT_GENERE);
                annonce.add(getTransaction());

            }

        }
    }

    /**
     * Returns the typeEnregistrement.
     *
     * @return String
     */
    public String getTypeEnregistrement(boolean isEncoreEnrAfter) {
        if (isEncoreEnrAfter) {
            return CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE;
        } else {
            return typeEnregistrement;
        }
    }

    private String getCodeSystemeTypeEnregistrement(boolean isEncoreEnrApres) {
        if (CIAnnonceCentrale.TYPE_ENR_ANNONCE_INTERCALAIRE.equals(getTypeEnregistrement(isEncoreEnrApres))) {
            return CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE;
        } else if (CIAnnonceCentrale.TYPE_ENR_DERNIERE_ANNONCE_ANNEE.equals(getTypeEnregistrement(isEncoreEnrApres))) {
            return CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE;
        } else {
            return "";
        }
    }

    private Lot initPoolMeldungZurZASLot(boolean poolKopfTest, String poolKopfSender)
            throws ParseException, DatatypeConfigurationException {
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        ch.admin.zas.pool.PoolMeldungZurZAS.Lot lot = factoryPool.createPoolMeldungZurZASLot();
        PoolKopfType poolKopf = factoryType.createPoolKopfType();
        if (poolKopfTest) {
            poolKopf.setTest("TEST");
        }
        poolKopf.setSender(poolKopfSender);

        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final String dateStr = JACalendar.todayJJsMMsAAAA();
        final java.util.Date dDate = format.parse(dateStr);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);

        XMLGregorianCalendar dealCloseDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        dealCloseDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        poolKopf.setErstellungsdatum(dealCloseDate);
        lot.setPoolKopf(poolKopf);

        PoolFussType poolFuss = factoryType.createPoolFussType();
        poolFuss.setEintragungengesamtzahl(0);
        lot.setPoolFuss(poolFuss);

        return lot;
    }

    private Marshaller initMarshaller(PoolMeldungZurZAS element) throws SAXException, JAXBException {
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

    private String genereFichier(PoolMeldungZurZAS.Lot lotAnnonce)
            throws CIAnnonceCentraleException, IOException, SAXException, JAXBException {
        String fileName;
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        PoolMeldungZurZAS pool = factoryPool.createPoolMeldungZurZAS();
        pool.getLot().add(lotAnnonce);
        lotAnnonce.getPoolFuss().setEintragungengesamtzahl(1);
        initMarshaller(pool);
        fileName = Jade.getInstance().getSharedDir() + getFileNameTimeStamp();

        File f = new File(fileName);
        boolean canCreateFile = f.createNewFile();
        if (!canCreateFile) {
            throw new CIAnnonceCentraleException("Unable to create file : " + fileName);
        }

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
            throw exception;

        }
        return fileName;

    }

    private void envoiFichier(String fichier) throws CIAnnonceCentraleException, JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        if (!JadeStringUtil.isBlank(CIUtil.getPathFTP(getSession()))) {
            JadeFsFacade.copyFile(fichier, CIUtil.getPathFTP(getSession()) + "/" + getFileNameTimeStamp());
        } else {
            LOG.error(getSession().getLabel("ERROR_PATH_BLANK"));
            CIAnnonceCentraleException excep = new CIAnnonceCentraleException(
                    getSession().getLabel("ERROR_PATH_BLANK"));
            excep.setIsPatchError(true);
            throw excep;
        }

    }

    private TStandIKStatistik generateStandIKStatistikVorher(BigInteger totalRevenusT) {
        TStandIKStatistik statAvant = factoryType.createTStandIKStatistik();
        if (totalRevenusT.compareTo(BigInteger.ZERO) < 0) {
            statAvant.setVorzeichenCode((short) 1);
            statAvant.setSummeEinkommen(totalRevenusT.abs());
        } else {
            statAvant.setVorzeichenCode((short) 0);
            statAvant.setSummeEinkommen(totalRevenusT.abs());
        }

        return statAvant;
    }

    private StandIKStatistikNacher generateStandIKStatistikNacher(BigInteger totalRevenusT) {
        StandIKStatistikNacher statApres = factoryType.createIKStatistikMeldungTypeStandIKStatistikNacher();
        if (CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE.equalsIgnoreCase(annonceCentrale.getIdTypeAnnonce())) {
            statApres.setLetzteMeldung((short) 1);
        } else if (CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE.equalsIgnoreCase(annonceCentrale.getIdTypeAnnonce())) {
            statApres.setLetzteMeldung((short) 2);
        }
        if (totalRevenusT.compareTo(BigInteger.ZERO) < 0) {
            statApres.setVorzeichenCode((short) 1);
            statApres.setSummeEinkommen(totalRevenusT.abs());
        } else {
            statApres.setVorzeichenCode((short) 0);
            statApres.setSummeEinkommen(totalRevenusT.abs());
        }

        return statApres;
    }

    private void prepareEnvoieAnnonce(CIEcriture ciEcr, IKStatistikMeldungType annonceXML)
            throws CIAnnonceCentraleException, DatatypeConfigurationException, JAXBException, SAXException,
            PropertiesException, ParseException {

        Aufzeichnungen notes = new Aufzeichnungen();
        EintragungIKMeldung annonceCI = new EintragungIKMeldung();

        /*
         * G?n?ration IKKopf
         */

        IKKopf ikkopf = new IKKopf();
        if (!JadeStringUtil.isBlankOrZero(ciEcr.getAvs())) {
            ikkopf.getVersichertennummer().add(ciEcr.getAvs());
        } else {
            ikkopf.getVersichertennummer().add("0");
        }
        ikkopf.setAnzahlVersicherte(0);
        notes.setIKKopf(ikkopf);
        /*
         * G?nration EintragungIKMeldung
         */
        try {
            annonceCI.setAKAbrechnungsNr(getAffilie(ciEcr));
        } catch (Exception e) {
            throw new CIAnnonceCentraleException(e.getMessage());
        }

        String genre = ciEcr.getGenreEcriture();
        if (!JadeStringUtil.isBlankOrZero(ciEcr.getGenreEcriture())) {
            annonceCI.setSchluesselzahlBeitragsart(Short.parseShort(genre.substring(genre.length() - 1)));
        } else {
            annonceCI.setSchluesselzahlBeitragsart((short) 0);
        }
        String extourne = ciEcr.getExtourne();
        if (!JadeStringUtil.isBlankOrZero(ciEcr.getExtourne())) {
            annonceCI.setSchluesselzahlStornoeintrag(Short.parseShort(extourne.substring(extourne.length() - 1)));
        } else {
            annonceCI.setSchluesselzahlStornoeintrag((short) 0);
        }
        if (!JadeStringUtil.isBlankOrZero(ciEcr.getParticulier())) {
            if (!JadeStringUtil.isBlankOrZero(ciEcr.getParticulier().substring(ciEcr.getParticulier().length() - 1))) {
                JAXBElement<Short> chiffrecleGenre = factoryType.createTEintragungIKMinDatBesondereSchluesselzahlIK(
                        Short.parseShort(ciEcr.getParticulier().substring(ciEcr.getParticulier().length() - 1)));
                annonceCI.setBesondereSchluesselzahlIK(chiffrecleGenre);
            }
        }

        if (!JadeStringUtil.isBlankOrZero(ciEcr.getPartBta())) {
            JAXBElement<Integer> bta = factoryType
                    .createTEintragungIKMinDatBetreuungsgutschriftBruchteil(Integer.parseInt(ciEcr.getPartBta()));
            annonceCI.setBetreuungsgutschriftBruchteil(bta);
        } else {
            JAXBElement<Integer> bta = factoryType
                    .createTEintragungIKMinDatBetreuungsgutschriftBruchteil(Integer.parseInt("0"));
            annonceCI.setBetreuungsgutschriftBruchteil(bta);
        }
        if (!JadeStringUtil.isBlankOrZero(ciEcr.getCodeSpecial())) {
            JAXBElement<Short> codeSpecial = factoryType.createTEintragungIKMinDatSonderfallcodeIK(
                    Short.parseShort(ciEcr.getCodeSpecial().substring(ciEcr.getCodeSpecial().length() - 1)));
            annonceCI.setSonderfallcodeIK(codeSpecial);
        } else {
            JAXBElement<Short> codeSpecial = factoryType.createTEintragungIKMinDatSonderfallcodeIK(null);
            codeSpecial.setNil(true);
            annonceCI.setSonderfallcodeIK(codeSpecial);
        }
        annonceCI.setBeitragsdauer(generateBeitragsdauer(ciEcr));
        annonceCI.setBeitragsjahr(retourneXMLGregorianCalendarFromYear(ciEcr.getAnnee().substring(2, 4)));
        annonceCI.setEinkommen(new BigDecimal(getMontant(ciEcr)));
        annonceCI.setVerarbeitungsjahr(retourneXMLGregorianCalendarFromYear(anneeAA));
        notes.getEintragungIKMeldung().add(annonceCI);
        annonceXML.getAufzeichnungen().add(notes);

    }

    private void validateAnnonceCI(Aufzeichnungen notes) throws CIAnnonceCentraleException,
            DatatypeConfigurationException, SAXException, JAXBException, PropertiesException, ParseException {
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        IKStatistikMeldungType annonceXMLTest = factoryType.createIKStatistikMeldungType();
        annonceXMLTest.setKasseZweigstelleIKFuehrend(caisse + agence);
        annonceXMLTest.setVerarbeitungsjahr(retourneXMLGregorianCalendarFromYear(anneeAA));
        annonceXMLTest.getAufzeichnungen().add(notes);
        annonceXMLTest.setStandIKStatistikVorher(generateStandIKStatistikVorher(BigInteger.ZERO));
        annonceXMLTest.setStandIKStatistikNacher(generateStandIKStatistikNacher(BigInteger.ZERO));
        PoolMeldungZurZAS.Lot lotAnnonces = initPoolMeldungZurZASLot(CIUtil.isTestXML(getSession()),
                CommonProperties.KEY_NO_CAISSE.getValue());
        lotAnnonces
                .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                .add(annonceXMLTest);
        PoolMeldungZurZAS pool = factoryPool.createPoolMeldungZurZAS();
        pool.getLot().add(lotAnnonces);
        lotAnnonces.getPoolFuss().setEintragungengesamtzahl(1);
        initMarshaller(pool);
        marshaller.setEventHandler(new ValidationEventHandler() {

            @Override
            public boolean handleEvent(ValidationEvent event) {
                LOG.warn("JAXB validation error : " + event.getMessage(), this);
                ArrayList<ValidationEvent> list;
                if (!listError.containsKey(compteCI)) {
                    list = new ArrayList<ValidationEvent>();
                } else {
                    list = listError.get(compteCI);
                }
                list.add(event);
                listError.put(compteCI, list);

                return true;
            }
        });
        marshaller.marshal(pool, new ByteArrayOutputStream());

    }

    private TStandIKStatistik generateStandIKStatistikVorher() {
        TStandIKStatistik statAvant = factoryType.createTStandIKStatistik();
        statAvant.setVorzeichenCode((short) 0);
        statAvant.setSummeEinkommen(BigInteger.ZERO);
        return statAvant;
    }

    private Beitragsdauer generateBeitragsdauer(CIEcriture ciEcr) {
        Beitragsdauer duree = factoryType.createTEintragungIKMinDatBeitragsdauer();
        duree.setAnfangsmonat(Short.parseShort(ciEcr.getMoisDebut()));
        duree.setEndmonat(Short.parseShort(ciEcr.getMoisFin()));
        return duree;
    }

    private String getFileNameTimeStamp() throws CIAnnonceCentraleException {
        String fileName = "M_" + CIUtil.getRacineNom();
        fileName = JadeFilenameUtil.addFilenameSuffixDateTimeDecimals(fileName);
        fileName = StringUtils.left(fileName, fileName.length() - 7) + "_" + StringUtils.right(fileName, 7);
        fileName = fileName + ".xml";
        return fileName;
    }

    public XMLGregorianCalendar retourneXMLGregorianCalendarFromYear(String dateYy)
            throws CIAnnonceCentraleException, DatatypeConfigurationException {
        XMLGregorianCalendar returnCalendar;
        GregorianCalendar gregory;
        if (new Integer(dateYy) > 48) {
            gregory = new GregorianCalendar(Integer.valueOf(dateYy) + 1900, 0, 1);
        } else {
            gregory = new GregorianCalendar(Integer.valueOf(dateYy) + 2000, 0, 1);
        }
        returnCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        returnCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

        return returnCalendar;
    }

    public void setTypeEnregistrement(String typeEnregistrement) {
        this.typeEnregistrement = typeEnregistrement;
    }

    /**
     * Returns the maxEcrituresASelect.
     *
     * @return int
     */
    public int getMaxEcrituresASelect() throws Exception {
        if (maxEcrituresASelect == 0) {
            String maxDansProp = getSession().getApplication().getProperty("maxEcritureParLot");
            if (JadeStringUtil.isNull(maxDansProp) || JadeStringUtil.isBlankOrZero(maxDansProp)) {
                return CIAnnonceCentraleProcessXML.MAX_ECRITURE_LOT_DEFAUT;
            } else {
                return Integer.parseInt(maxDansProp);
            }
        } else {
            return maxEcrituresASelect;
        }
    }

    private String getMontantSigne(CIEcriture ecriture) {
        int revenuTr = (int) Double.parseDouble(ecriture.getRevenu());
        if (JAUtil.isIntegerEmpty(ecriture.getExtourne()) || CIEcriture.CS_EXTOURNE_2.equals(ecriture.getExtourne())
                || CIEcriture.CS_EXTOURNE_6.equals(ecriture.getExtourne())
                || CIEcriture.CS_EXTOURNE_8.equals(ecriture.getExtourne())) {
            return String.valueOf(revenuTr);
        } else {
            return String.valueOf(-1 * revenuTr);
        }

    }

    private String getMontant(CIEcriture ecriture) {
        int revenuTr = (int) Double.parseDouble(ecriture.getRevenu());
        return String.valueOf(revenuTr);
    }

    private String getAffilie(CIEcriture ecriture) throws Exception {
        if (CIEcriture.CS_CIGENRE_5.equals(ecriture.getGenreEcriture())) {
            return "0";
        }
        if (!JadeStringUtil.isBlankOrZero(ecriture.getPartBta())) {
            return "11111111111";
        }
        if (CIEcriture.CS_CIGENRE_8.equals(ecriture.getGenreEcriture())) {
            String noAffilie = "";
            if (!JadeStringUtil.isIntegerEmpty(ecriture.getPartenaireId())) {
                CICompteIndividuel ciPart = new CICompteIndividuel();
                ciPart.setSession(getSession());
                ciPart.setCompteIndividuelId(ecriture.getPartenaireId());
                ciPart.retrieve();
                if (!ciPart.isNew()) {
                    noAffilie = ciPart.getNumeroAvsForSplitting();
                    if (noAffilie.contains("-")) {
                        noAffilie = "756" + JadeStringUtil.removeChar(noAffilie, '-');
                    }
                }
            }
            if(JadeStringUtil.isBlankOrZero(noAffilie)) {
                return "0";
            }
            return noAffilie;
        } else { // recherche journal
            CIJournal journal = ecriture.getJournal(null, false);
            if (!journal.isNew()) {
                if (CIJournal.CS_APG.equals(journal.getIdTypeInscription())) {
                    return "77777777777";
                } else if (CIJournal.CS_PANDEMIE.equals(journal.getIdTypeInscription())) {
                    return "55555555555";
                } else if (CIJournal.CS_IJAI.equals(journal.getIdTypeInscription())) {
                    return "88888888888";
                } else if (CIJournal.CS_ASSURANCE_CHOMAGE.equals(journal.getIdTypeInscription())||!JadeStringUtil.isBlankOrZero(ecriture.getCaisseChomage()) ) {
                    return ecriture.getCaisseChomageFormattee();
                } else if (CIJournal.CS_ASSURANCE_MILITAIRE.equals(journal.getIdTypeInscription())) {
                    return "66666666666";
                }
            }
            String numAffWithoutDot = StringUtils.removeDots(ecriture.getIdAffilie());
            if(JadeStringUtil.isBlankOrZero(numAffWithoutDot)) {
                return "0";
            }
            if (numAffWithoutDot.length() > CIAnnonceCentraleProcessXML.NUM_AVS_LENGTH) {
                numAffWithoutDot = numAffWithoutDot.substring(0, CIAnnonceCentraleProcessXML.NUM_AVS_LENGTH);
            }
            return numAffWithoutDot;
        }
    }

}
