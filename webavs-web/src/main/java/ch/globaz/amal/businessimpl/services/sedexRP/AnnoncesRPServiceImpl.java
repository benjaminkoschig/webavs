package ch.globaz.amal.businessimpl.services.sedexRP;

import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.gdk_cds.xmlns.pv_5205_000801._3.Message;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.businessimpl.services.sedexRP.builder.*;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import com.google.common.collect.Lists;
import globaz.jade.smtp.JadeSmtpClient;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;
import ch.ech.xmlns.ech_0090._1.EnvelopeType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTypeDemandeSubside;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.sedexRP.AnnoncesRPService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.handler.AnnonceHandlerAbstract;
import ch.globaz.amal.businessimpl.services.sedexRP.handler.AnnonceRPHandlerFactory;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.amal.process.annonce.AnnonceSedexProcess;
import globaz.amal.utils.AMSedexHelper;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;

/**
 * Implémentation des services pour les annonces RP AMAL
 *
 * @author cbu
 *
 */
public class AnnoncesRPServiceImpl implements AnnoncesRPService {
    public static String CSV_SEPARATOR = ";";
    private ArrayList<String> arrayListErrorsCreation = new ArrayList<String>();
    private ArrayList<String> arrayListErrorsSend = new ArrayList<String>();
    private ArrayList<String> arrayListErrorsSedexId = new ArrayList<String>();
    private JadeContext context;
    private boolean isSimulation = false;
    private Map<String, Object> mapEtatDec = new HashMap<String, Object>();
    private Map<String, ArrayList<String>> mapListErrors = new HashMap<String, ArrayList<String>>();
    private String passSedex;
    private BSession session;
    private String userSedex;

    /**
     * Construit la string qui contiendra les noms des fichiers a inclure dans le mail
     *
     * @param fileDecreeInventory
     * @param mapReturnInfos
     * @return
     */
    private String[] _buildFilesString(String fileDecreeInventory,
            Map<String, AnnonceBuilderReturnInfosContainer> mapReturnInfos) {

        String zipFileName = _zipFiles(mapReturnInfos, "Statement_" + JACalendar.today().toStr("") + ".zip");
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (zipFileName != null) {
            arrayFiles.add(zipFileName);
        }
        arrayFiles.add(fileDecreeInventory);

        String files[] = new String[arrayFiles.size()];
        int ind = 0;
        for (String file : arrayFiles) {
            files[ind] = file;
            ind++;
        }
        return files;
    }

    /**
     * Création d'une annonce 'Etat des décisions'. Appelé depuis la page des assureurs
     *
     * @param annonceInfos
     * @param idCaisse
     * @return Une arrayList avec les erreurs éventuelles
     */
    private AnnonceBuilderReturnInfosContainer _createAnnonceEtatDecisions(AnnonceInfosContainer annonceInfos,
            String idCaisse) {

        AnnonceBuilderReturnInfosContainer returnInfosContainer = new AnnonceBuilderReturnInfosContainer();
        returnInfosContainer.setIdCaisse(idCaisse);
        SimpleAnnonceSedex annonceSedex = new SimpleAnnonceSedex();
        annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.ETAT_DECISIONS.getValue());
        annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.ETAT_DECISIONS.getValue());

        try {
            // Récupération du builder
            AnnonceBuilderAbstract annonceBuilderAbstract = AnnonceBuilderFactory.getAnnonceBuilder(annonceSedex,
                    annonceInfos);
            // Construction de l'header
            Object annonceHeader = annonceBuilderAbstract.generateHeader();
            // Construction du content
            Object annonceContent = annonceBuilderAbstract.generateContent();

            if (annonceContent != null) {
                ch.gdk_cds.xmlns.pv_5203_000501._3.ContentType ct = (ch.gdk_cds.xmlns.pv_5203_000501._3.ContentType) annonceContent;
                if (ct.getDecreeInventory().getDecreeInventoryElement() != null) {
                    int nbCree = ct.getDecreeInventory().getDecreeInventoryElement().size();
                    returnInfosContainer.setNbElements(nbCree);
                    returnInfosContainer.addOneElementCreated();
                }
                mapEtatDec.put(idCaisse, ct.getDecreeInventory());
            }

            // Si on a des infos de retour, on le set dans le tableau
            if (annonceBuilderAbstract.getArrayErrors().size() > 0) {
                returnInfosContainer.setErreurCreationList(annonceBuilderAbstract.getArrayErrors());
            } else {
                if (!isSimulation) {
                    if ((annonceContent != null) && (annonceHeader != null)) {

                        // Construction du message ==> assemblage de l'header et du content
                        Object message = annonceBuilderAbstract.createMessage(annonceHeader, annonceContent);

                        // Instancie les services JAXB
                        JAXBServices jaxb = JAXBServices.getInstance();
                        Class<?>[] addClasses = new Class[] {};
                        try {
                            // On va chercher la class associée a utiliser pour "unmarshaller"
                            Class<?> classMessage = annonceBuilderAbstract.whichClass();
                            addClasses = new Class[] { Class.forName(classMessage.getName()) };
                        } catch (ClassNotFoundException cnfe) {
                            throw new AnnonceSedexException(cnfe.getMessage());
                        }
                        try {
                            String file = jaxb.marshal(message, false, false, addClasses);
                            // Envoi de l'annonce
                            JadeSedexService.getInstance().sendSimpleMessage(file, null);
                            returnInfosContainer.setPathToXmlFile(file);
                            returnInfosContainer.addOneElementSended();
                        } catch (Exception e) {
                            if (e.getMessage() != null) {
                                throw new AnnonceSedexException("Errors marshalling message ==> " + e.getMessage());
                            } else {
                                throw new AnnonceSedexException(
                                        "Errors marshalling message ==> " + e.getCause().getMessage());
                            }
                        }

                        // Mise à jour de l'annonce SimpleAnnonceSedex
                        String messageHeaderAsString = JadeStringUtil
                                .decodeUTF8(annonceBuilderAbstract.getHeaderMessage(message).toString());
                        annonceSedex.setMessageHeader(messageHeaderAsString);
                        annonceSedex.setMessageEmetteur(annonceBuilderAbstract.getSenderId(message));
                        annonceSedex.setMessageRecepteur(annonceBuilderAbstract.getRecipientId(message));
                        annonceSedex.setNumeroCourant(annonceBuilderAbstract.getBusinessProcessId(message));
                        annonceSedex.setNumeroDecision(annonceBuilderAbstract.getDecreeId(message));
                        annonceSedex.setMessageId(annonceBuilderAbstract.getMessageId(message));
                        annonceSedex.setTraitement(AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());
                        annonceSedex.setStatus(AMStatutAnnonceSedex.ENVOYE.getValue());
                        annonceSedex.setVersionXSD(annonceBuilderAbstract.getMinorVersion(message));
                        annonceSedex.setIdTiersCM(AMSedexRPUtil.getIdTiersFromSedexId(annonceInfos.getRecipientId()));
                        annonceSedex.setDateMessage(annonceInfos.getReferenceDay());
                        AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

                    }
                }
            }

        } catch (Exception e) {
            _setErrorOnCreation(annonceSedex, e);
        }

        return returnInfosContainer;
    }

    /**
     * Création de l'enveloppe pour les messages groupés
     *
     * @param envelopeMessageId
     * @param msgType
     * @param recipientId
     * @return
     * @throws DatatypeConfigurationException
     * @throws JAXBException
     * @throws SAXException
     * @throws MalformedURLException
     * @throws IOException
     * @throws JAXBValidationError
     * @throws JAXBValidationWarning
     * @throws JadeSedexDirectoryInitializationException
     */
    protected String _createEnvelope(String envelopeMessageId, String msgType, String recipientId) throws Exception {
        try {
            // On prépare l'enveloppe sedex (ech-0090)
            ch.ech.xmlns.ech_0090._1.ObjectFactory of0090 = new ch.ech.xmlns.ech_0090._1.ObjectFactory();
            EnvelopeType enveloppe = of0090.createEnvelopeType();
            enveloppe.setEventDate(JAXBUtil.getXmlCalendarTimestamp());
            enveloppe.setMessageDate(JAXBUtil.getXmlCalendarTimestamp());
            enveloppe.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
            enveloppe.setMessageId(envelopeMessageId);
            enveloppe.setMessageType(Integer.parseInt(msgType));
            enveloppe.setVersion("1.0");
            enveloppe.getRecipientId().add(recipientId);

            JAXBElement<EnvelopeType> element = of0090.createEnvelope(enveloppe);
            String envelopeFile = JAXBServices.getInstance().marshal(element, false, false, new Class<?>[] {});
            return envelopeFile;
        } catch (Exception e) {
            throw new Exception("Error creating envelope file ! ==> " + e.getMessage());
        }
    }

    /**
     * Création d'une liste CSV contenant les états de décisions
     *
     * @param anneeHistorique
     *
     * @return uri de la liste crée
     */
    private String _createListeCSVEtatDecisions(String anneeHistorique) {
        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();
        for (String key : mapEtatDec.keySet()) {
            ch.gdk_cds.xmlns.pv_5203_000501._3.DecreeInventoryType dit = (ch.gdk_cds.xmlns.pv_5203_000501._3.DecreeInventoryType) mapEtatDec
                    .get(key);

            for (ch.gdk_cds.xmlns.pv_5203_000501._3.DecreeInventoryElementType element : dit
                    .getDecreeInventoryElement()) {
                StringBuffer sbCsv = new StringBuffer();

                try {
                    AdministrationComplexModel admc = TIBusinessServiceLocator.getAdministrationService().read(key);
                    sbCsv.append(admc.getTiers().getDesignation1() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                } catch (Exception e) {
                    sbCsv.append(key + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                }

                sbCsv.append(
                        AMSedexRPUtil.getDateXMLToString(dit.getInventoryDate()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(
                        AMSedexRPUtil.getDateXMLToString(dit.getBeginDate()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(AMSedexRPUtil.getDateXMLToString(dit.getEndDate()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append("'" + element.getDecreeId() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(element.getBeginMonth() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(element.getEndMonth() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(element.getAmount() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(element.getLastBusinessProcessId() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                // -------------------------------------------------------------------
                // personType
                // -------------------------------------------------------------------
                sbCsv.append(element.getPerson().getVn() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(element.getPerson().getOfficialName() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                sbCsv.append(element.getPerson().getFirstName() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                if ("2".equals(element.getPerson().getSex())) {
                    sbCsv.append("F" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                } else {
                    sbCsv.append("H" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                }
                sbCsv.append(AMSedexRPUtil.getDateXMLToString(element.getPerson().getDateOfBirth())
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);

                listRecords.add(sbCsv);
            }
        }

        // CSV Line Header
        String lineHeader = "";
        lineHeader += "Caisse" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        // -------------------------------------------------------------------
        // decreeInventoryElementType
        // -------------------------------------------------------------------
        lineHeader += "Date référence" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Début période" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Fin période" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "DecreeId" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Mois de début" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Mois de fin" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Montant mensuel RP" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Dernier no courant" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        // -------------------------------------------------------------------
        // personType
        // -------------------------------------------------------------------
        lineHeader += "NNSS" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Nom" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Prénom" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Sexe" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Date de naissance" + AnnoncesRPServiceImpl.CSV_SEPARATOR;

        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());

        return _writeFile(listRecords, lineHeader, "decreeInventory_" + anneeHistorique + "_" + dateToday + ".csv");
    }

    /**
     * Méthode générique pour créer une annonce initialisé
     *
     * @param simpleAnnonceSedex
     * @param idDetailFamille
     * @param idContribuable
     * @param idTiersCM
     * @param msgType
     * @param msgSubType
     * @param traitement
     * @param status
     * @return
     * @throws AnnonceSedexException
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private SimpleAnnonceSedex _createSimpleAnnonceSedex(SimpleAnnonceSedex simpleAnnonceSedex, String idDetailFamille,
            String idContribuable, String idTiersCM, String msgType, String msgSubType, String traitement,
            String status) throws AnnonceSedexException, DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (idDetailFamille != null) {
            simpleAnnonceSedex.setIdDetailFamille(idDetailFamille);
        }
        if (idContribuable != null) {
            simpleAnnonceSedex.setIdContribuable(idContribuable);
        }
        if (idTiersCM != null) {
            simpleAnnonceSedex.setIdTiersCM(idTiersCM);
        }
        simpleAnnonceSedex.setMessageType(msgType);
        simpleAnnonceSedex.setMessageSubType(msgSubType);
        simpleAnnonceSedex.setTraitement(traitement);
        simpleAnnonceSedex.setStatus(status);
        simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(simpleAnnonceSedex);
        return simpleAnnonceSedex;
    }

    /**
     * Création du SimpleSedexMessage
     *
     * @param simpleAnnonceSedex
     * @return
     * @throws AnnonceSedexException
     * @throws ClassNotFoundException
     * @throws JAXBException
     * @throws SAXException
     * @throws MalformedURLException
     * @throws IOException
     * @throws JAXBValidationError
     * @throws JAXBValidationWarning
     */
    protected SimpleSedexMessage _createSimpleSedexMessage(SimpleAnnonceSedex simpleAnnonceSedex)
            throws AnnonceSedexException, ClassNotFoundException, JAXBException, SAXException, MalformedURLException,
            IOException, JAXBValidationError, JAXBValidationWarning {
        SimpleSedexMessage sedexMessage = new SimpleSedexMessage();
        // Récupération du builder
        AnnonceBuilderAbstract annonceBuilderAbstract = AnnonceBuilderFactory.getAnnonceBuilder(simpleAnnonceSedex);

        // Récupération de la class associée à l'annonce
        Class<?>[] addClasses = new Class[] { Class.forName(annonceBuilderAbstract.whichClass().getName()) };
        // Construction de l'objet JAXB Header depuis la chaine XML
        Object o_header = _getHeaderAnnonce(simpleAnnonceSedex, addClasses);
        // Construction de l'objet JAXB Content depuis la chaine XML
        Object o_content = _getContentAnnonce(simpleAnnonceSedex, addClasses);
        // Construction du message
        Object o_message = annonceBuilderAbstract.createMessage(o_header, o_content);

        // Marshalling pour créer le fichier XML
        String file = JAXBServices.getInstance().marshal(o_message, false, false, addClasses);

        // Set des properties du SimpleSedexMessage
        sedexMessage.fileLocation = file;
        sedexMessage.action = annonceBuilderAbstract.getAction(o_message);
        return sedexMessage;
    }

    /**
     * Création d'une liste CSV contenant les annonces qui seront crée et/ou envoyées
     *
     * @param annonceSedexSearchToCreate
     *            model search pour la sélection des annonces
     * @return
     */
    private String _createSimulationFile(ComplexAnnonceSedexSearch annonceSedexSearchToCreate) {

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();
        for (JadeAbstractModel model : annonceSedexSearchToCreate.getSearchResults()) {
            ComplexAnnonceSedex annonceSedex = (ComplexAnnonceSedex) model;

            StringBuffer sbCsv = new StringBuffer();

            sbCsv.append(
                    annonceSedex.getSimpleAnnonceSedex().getIdAnnonceSedex() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            try {
                AdministrationComplexModel admc = TIBusinessServiceLocator.getAdministrationService()
                        .read(annonceSedex.getSimpleAnnonceSedex().getIdTiersCM());
                sbCsv.append(admc.getTiers().getDesignation1() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            } catch (Exception e) {
                sbCsv.append(annonceSedex.getSimpleDetailFamille().getNoCaisseMaladie()
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            }

            sbCsv.append(annonceSedex.getSimpleAnnonceSedex().getMessageType() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(
                    annonceSedex.getSimpleAnnonceSedex().getMessageSubType() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(
                    annonceSedex.getSimpleAnnonceSedex().getMessageSubType()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getNomPrenom() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(
                    annonceSedex.getContribuable().getFamille().getNomPrenom() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getContribuable().getContribuable().getNoContribuable()
                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);

            if (AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue()
                    .equals(annonceSedex.getSimpleAnnonceSedex().getMessageSubType())) {
                try {
                    // Recherche de la dernière confirmation arrivée sur le subside
                    ComplexAnnonceSedexSearch lastConfirmationFoundSearch = new ComplexAnnonceSedexSearch();
                    lastConfirmationFoundSearch
                            .setForSDXIdDetailFamille(annonceSedex.getSimpleDetailFamille().getIdDetailFamille());
                    ArrayList<String> arrayInSubType = new ArrayList<String>();
                    // Sélection de toutes les annonces de type 'Confirmation décision' liées au subside
                    arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());
                    lastConfirmationFoundSearch.setInSDXMessageSubType(arrayInSubType);
                    lastConfirmationFoundSearch
                            .setForCMIdTiersCaisse(annonceSedex.getSimpleAnnonceSedex().getIdTiersCM());
                    lastConfirmationFoundSearch.setOrderKey("orderByIdSedexMsg");
                    lastConfirmationFoundSearch = AmalServiceLocator.getComplexAnnonceSedexService()
                            .search(lastConfirmationFoundSearch);

                    ComplexAnnonceSedex complexAnnonceSedex = new ComplexAnnonceSedex();
                    // Si on a trouvé une confirmation
                    if (lastConfirmationFoundSearch.getSize() > 0) {
                        ComplexAnnonceSedex lastConfirmationFound = (ComplexAnnonceSedex) lastConfirmationFoundSearch
                                .getSearchResults()[0];

                        // On a trouvé la confirmation, on recherche maintenant l'annonce DECREE qui lui est rattachée
                        ComplexAnnonceSedexSearch originalDecreeSearch = new ComplexAnnonceSedexSearch();
                        arrayInSubType = new ArrayList<String>();
                        arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue());
                        originalDecreeSearch.setInSDXMessageSubType(arrayInSubType);
                        originalDecreeSearch
                                .setForSDXNoDecision(lastConfirmationFound.getSimpleAnnonceSedex().getNumeroDecision());
                        originalDecreeSearch = AmalServiceLocator.getComplexAnnonceSedexService()
                                .search(originalDecreeSearch);

                        if (originalDecreeSearch.getSize() > 0) {
                            // On a trouvé le decree original
                            complexAnnonceSedex = (ComplexAnnonceSedex) originalDecreeSearch.getSearchResults()[0];
                            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5201_000101._3.Message.class };

                            // Construction de l'objet JAXB

                            SimpleAnnonceSedex simpleAnnonceSedex = AnnonceSedexTransformation
                                    .transformationCurrentVersion(complexAnnonceSedex.getSimpleAnnonceSedex());
                            complexAnnonceSedex.setSimpleAnnonceSedex(simpleAnnonceSedex);

                            ByteArrayInputStream input = new ByteArrayInputStream(
                                    complexAnnonceSedex.getSimpleAnnonceSedex().getMessageContent().getBytes("UTF-8"));
                            // Instancie les services JAXB
                            JAXBServices jaxb = JAXBServices.getInstance();
                            ch.gdk_cds.xmlns.pv_5201_000101._3.Message objDecisionOrigingal = (ch.gdk_cds.xmlns.pv_5201_000101._3.Message) jaxb
                                    .unmarshal(input, false, false, addClasses);

                            sbCsv.append(objDecisionOrigingal.getContent().getDecree().getAmount()
                                    + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                        }
                    } else {
                        sbCsv.append(annonceSedex.getSimpleDetailFamille().getMontantContribution()
                                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                    }
                } catch (Exception e) {
                    sbCsv.append("Erreur" + AnnoncesRPServiceImpl.CSV_SEPARATOR);
                }
            } else {
                sbCsv.append(annonceSedex.getSimpleDetailFamille().getMontantContribution()
                        + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            }
            sbCsv.append(annonceSedex.getSimpleDetailFamille().getSupplExtra() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleDetailFamille().getDebutDroit() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleDetailFamille().getFinDroit() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getNoAVS() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getDateNaissance() + AnnoncesRPServiceImpl.CSV_SEPARATOR);

            if (mapListErrors.containsKey(annonceSedex.getSimpleAnnonceSedex().getIdAnnonceSedex())) {
                ArrayList<String> listErrors = mapListErrors
                        .get(annonceSedex.getSimpleAnnonceSedex().getIdAnnonceSedex());
                sbCsv.append(listErrors.toString() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            } else {
                sbCsv.append(" " + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            }

            listRecords.add(sbCsv);
        }

        String lineHeader = "";
        lineHeader += "idMessageSedex" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Destinataire" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Sous-type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Libellé sous-type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Contribuable" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "No contribuable" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Montant" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Supplément extra" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Début droit" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Fin droit" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "NSS membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Date de naissance membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Erreurs" + AnnoncesRPServiceImpl.CSV_SEPARATOR;

        String file = _writeFile(listRecords, lineHeader, "simulationMessages.csv");
        return file;
    }

    /**
     * Création des annonces
     *
     * @param annonceSedexSearchToCreate
     *            Model search pour la sélection des annonces à créer
     */
    private int _creationAnnoncesRP(ComplexAnnonceSedexSearch annonceSedexSearchToCreate) {
        int nbItemsCreated = 0;
        for (JadeAbstractModel model : annonceSedexSearchToCreate.getSearchResults()) {
            ComplexAnnonceSedex annonceSedex = (ComplexAnnonceSedex) model;
            try {
                AnnonceInfosContainer annonceInfosContainer = new AnnonceInfosContainer();

                String idSedexCaisse = null;
                try {
                    idSedexCaisse = AMSedexRPUtil
                            .getSedexIdFromIdTiers(annonceSedex.getCaisseMaladie().getTiers().getIdTiers());
                } catch (AnnonceSedexException e1) {
                    // TODO Code Review
                    throw new Exception(e1);
                }
                annonceInfosContainer.setRecipientId(idSedexCaisse);
                String periodeFrom = "01.01." + annonceSedex.getSimpleDetailFamille().getAnneeHistorique();
                String periodeTo = "31.12." + annonceSedex.getSimpleDetailFamille().getAnneeHistorique();
                annonceInfosContainer.setPeriodeTo(periodeTo);
                annonceInfosContainer.setPeriodeFrom(periodeFrom);

                AnnonceBuilderAbstract annonceBuilderAbstract = AnnonceBuilderFactory
                        .getAnnonceBuilder(annonceSedex.getSimpleAnnonceSedex(), annonceInfosContainer);

                if (annonceSedex.getSimpleDetailFamille().isNew()) {
                    if (!JadeStringUtil.isBlankOrZero(annonceSedex.getSimpleAnnonceSedex().getIdDetailFamille())) {
                        SimpleDetailFamille simpleDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                                .read(annonceSedex.getSimpleAnnonceSedex().getIdDetailFamille());
                        // TODO Code Review si simpleDetail famille peut etre null
                        if (!simpleDetailFamille.isNew()) {
                            annonceBuilderAbstract.setSimpleDetailFamille(simpleDetailFamille);
                        }
                    }
                } else {
                    annonceBuilderAbstract.setSimpleDetailFamille(annonceSedex.getSimpleDetailFamille());
                }

                Object annonceHeader = annonceBuilderAbstract.generateHeader();
                Object annonceContent = annonceBuilderAbstract.generateContent();
                Object message = annonceBuilderAbstract.createMessage(annonceHeader, annonceContent);

                if (!isSimulation) {
                    String messageContentAsString = JadeStringUtil
                            .decodeUTF8(annonceBuilderAbstract.getContentMessage(message).toString());
                    annonceSedex.getSimpleAnnonceSedex().setMessageContent(messageContentAsString);
                    String messageHeaderAsString = JadeStringUtil
                            .decodeUTF8(annonceBuilderAbstract.getHeaderMessage(message).toString());
                    annonceSedex.getSimpleAnnonceSedex().setMessageHeader(messageHeaderAsString);
                    annonceSedex.getSimpleAnnonceSedex()
                            .setMessageEmetteur(annonceBuilderAbstract.getSenderId(message));
                    annonceSedex.getSimpleAnnonceSedex().setMessageId(annonceBuilderAbstract.getMessageId(message));
                    annonceSedex.getSimpleAnnonceSedex()
                            .setMessageRecepteur(annonceBuilderAbstract.getRecipientId(message));
                    annonceSedex.getSimpleAnnonceSedex()
                            .setNumeroCourant(annonceBuilderAbstract.getBusinessProcessId(message));
                    annonceSedex.getSimpleAnnonceSedex().setNumeroDecision(annonceBuilderAbstract.getDecreeId(message));
                    annonceSedex.getSimpleAnnonceSedex()
                            .setTraitement(AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());
                    annonceSedex.getSimpleAnnonceSedex().setStatus(AMStatutAnnonceSedex.CREE.getValue());
                    annonceSedex.getSimpleAnnonceSedex().setVersionXSD(annonceBuilderAbstract.getMinorVersion(message));
                    AmalImplServiceLocator.getSimpleAnnonceSedexService().update(annonceSedex.getSimpleAnnonceSedex());
                }
                nbItemsCreated++;
            } catch (Exception e) {
                _setErrorOnCreation(annonceSedex.getSimpleAnnonceSedex(), e);
            }
        }
        return nbItemsCreated;
    }

    /**
     * Unmarshall la string du content de l'annonce pour en faire un objet
     *
     * @param simpleAnnonceSedex
     *            l'annonce qui contient le content en string
     * @param addClasses
     *            la class qui définit l'annonce
     * @return
     * @throws AnnonceSedexException
     */
    private Object _getContentAnnonce(SimpleAnnonceSedex simpleAnnonceSedex, Class<?>[] addClasses)
            throws AnnonceSedexException {
        Object o_content = null;
        try {
            // Construction de l'objet JAXB Content
            ByteArrayInputStream inputContent = new ByteArrayInputStream(
                    simpleAnnonceSedex.getMessageContent().getBytes("UTF-8"));
            o_content = JAXBServices.getInstance().unmarshal(inputContent, false, false, addClasses);
            // o_content = JAXBServices.getInstance().unmarshal(inputContent, false, false, new Class[] {});
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw new AnnonceSedexException("Errors unmarshalling content ==> " + e.getMessage());
            } else {
                throw new AnnonceSedexException("Errors unmarshalling content ==> " + e.getCause().getMessage());
            }

        }
        return o_content;
    }

    /**
     * Unmarshall la string de l'header de l'annonce pour en faire un objet
     *
     * @param simpleAnnonceSedex
     *            l'annonce qui contient le header en string
     * @param addClasses
     *            la class qui définit l'annonce
     * @return
     * @throws AnnonceSedexException
     */
    private Object _getHeaderAnnonce(SimpleAnnonceSedex simpleAnnonceSedex, Class<?>[] addClasses)
            throws AnnonceSedexException {
        Object o_header = null;
        try {
            ByteArrayInputStream inputHeader = new ByteArrayInputStream(
                    simpleAnnonceSedex.getMessageHeader().getBytes("UTF-8"));
            o_header = JAXBServices.getInstance().unmarshal(inputHeader, false, false, addClasses);
            // o_header = JAXBServices.getInstance().unmarshal(inputHeader, false, false, new Class[] {});
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw new AnnonceSedexException("Errors unmarshalling header ==> " + e.getMessage());
            } else {
                throw new AnnonceSedexException("Errors unmarshalling header ==> " + e.getCause().getMessage());
            }
        }
        return o_header;
    }

    /**
     * Initialisation d'une annonce
     *
     * @param idContribuable
     * @param idDetailFamille
     * @param msgType
     * @param msgSubType
     * @return
     * @throws AnnonceSedexException
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AnnonceException
     */
    private SimpleAnnonceSedex _initAnnonce(String idContribuable, String idDetailFamille,
            AMMessagesTypesAnnonceSedex msgType, AMMessagesSubTypesAnnonceSedex msgSubType, boolean subsideIsEnabled)
            throws AnnonceSedexException, DetailFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, AnnonceException {

        SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();

        // Lecture du subside
        SimpleDetailFamille currentSubside = AmalImplServiceLocator.getSimpleDetailFamilleService()
                .read(idDetailFamille);

        // Test si l'idSedex existe, si on ne trouve rien, on ne crée même pas l'annonce
        String idSedexCaisse = null;
        try {
            idSedexCaisse = AMSedexRPUtil.getSedexIdFromIdTiers(currentSubside.getNoCaisseMaladie());
        } catch (AnnonceSedexException e1) {
            idSedexCaisse = null;
            String infoCaisse = " idAdministration : " + currentSubside.getNoCaisseMaladie();
            try {
                AdministrationComplexModel administrationComplexModel = TIBusinessServiceLocator
                        .getAdministrationService().read(currentSubside.getNoCaisseMaladie());
                infoCaisse = " " + administrationComplexModel.getTiers().getDesignation1() + " (id"
                        + currentSubside.getNoCaisseMaladie() + ")";
            } catch (Exception e) {
                // On garde l'infoCaisse tel qu'on l'a initialisé
            }

            JadeLogger.warn(null, "Pas d'idSEDEX pour la caisse :" + infoCaisse);
        }
        if (JadeStringUtil.isBlankOrZero(idSedexCaisse)) {
            // Pas d'id SEDEX, on ne crée pas d'annonce
            return simpleAnnonceSedex;
        }

        // -------------------------------------------------------------
        // 0) CONTROLE DU TYPE DE MESSAGE A CREER EN ETAT INITIAL
        // -------------------------------------------------------------
        switch (msgSubType) {
            case NOUVELLE_DECISION:
                // ----------------------------------------------------
                // SI HISTORIQUE, MESSAGE INTERRUPTION NECESSAIRE AVANT
                // ----------------------------------------------------
                SimpleAnnonceSedexSearch currentSearch = new SimpleAnnonceSedexSearch();
                currentSearch.setForIdContribuable(idContribuable);
                currentSearch.setForIdDetailFamille(idDetailFamille);
                currentSearch.setDefinedSearchSize(0);
                currentSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(currentSearch);

                SimpleAnnonceSearch simpleAnnonceCosamaSearch = new SimpleAnnonceSearch();
                simpleAnnonceCosamaSearch.setForIdDetailFamille(idDetailFamille);
                simpleAnnonceCosamaSearch = AmalImplServiceLocator.getSimpleAnnonceService()
                        .search(simpleAnnonceCosamaSearch);

                boolean bInitInterruption = false;
                boolean bInitNewDecision = false;
                String lastCM = "";
                // Si on ne trouve rien (no cosama, ni sedex)
                if ((currentSearch.getSize() == 0) && (simpleAnnonceCosamaSearch.getSize() == 0)) {
                    bInitNewDecision = true;
                } else if ((currentSearch.getSize() == 0) && (simpleAnnonceCosamaSearch.getSize() > 0)) {
                    // Si on ne trouve qu'une annonce COSAMA, on doit d'abord interrompre le cosama
                    bInitInterruption = true;
                    bInitNewDecision = true;

                    if (simpleAnnonceCosamaSearch.getSize() > 0) {
                        SimpleAnnonce simpleAnnonce = (SimpleAnnonce) simpleAnnonceCosamaSearch.getSearchResults()[0];
                        // On récupère le dernier id caisse maladie pour le STOP
                        lastCM = simpleAnnonce.getNoCaisseMaladie();
                    }
                } else {
                    // On recherche si des messages sont en cours d'envoi
                    SimpleAnnonceSedex pendingAnnonceDecree = _lookForPendingAnnonce(idContribuable, idDetailFamille);

                    if (pendingAnnonceDecree == null) {
                        // Sinon on récupère les annonces SEDEX de type Decree et Stop avec leur réponses
                        SortedMap<Long, SimpleAnnonceSedex> messagesOrderedById = getOrderedMapOfAnnoncesSedex(
                                currentSearch);
                        // -----------------------------------------------------------------------------
                        // Contrôle que le dernier message soit un message de confirmation ou de rejet
                        // -----------------------------------------------------------------------------
                        if (messagesOrderedById.size() > 0) {
                            SimpleAnnonceSedex lastAnnonce = getLastAnnonce(currentSearch, messagesOrderedById);

                            if (lastAnnonce != null) {
                                if (lastAnnonce.getMessageSubType()
                                        .equals(AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue())) {
                                    // Si dernière annonce était une interruption >> nouvelle décision
                                    bInitNewDecision = true;
                                    boolean lastStopCanceled = true;
                                    // BZ9277 - Effectuer une interruption lorsque la dernière annonce est un
                                    // écourtement
                                    try {
                                        lastStopCanceled = _isLastStopCanceled(lastAnnonce);
                                    } catch (Exception e) {
                                        throw new AnnonceException(e.toString());
                                    }
                                    if (!lastStopCanceled) {
                                        // c'est un écourtement, il faut alors aussi créer une interruption
                                        bInitInterruption = true;
                                        lastCM = lastAnnonce.getIdTiersCM();
                                    }
                                } else if (lastAnnonce.getMessageSubType()
                                        .equals(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue())) {
                                    // Si dernière annonce était une décision >> contrôle si interruption est nécessaire
                                    BigDecimal lastContribution = null;
                                    String lastDebutDroit = "";
                                    String lastFinDroit = "";
                                    boolean lastWasAssiste = false;
                                    lastCM = lastAnnonce.getIdTiersCM();
                                    try {
                                        Class<?>[] addClasses = new Class[] {
                                                ch.gdk_cds.xmlns.pv_5201_000101._3.Message.class };

                                        // Instancie les services JAXB
                                        JAXBServices jaxb = JAXBServices.getInstance();

                                        lastAnnonce = AnnonceSedexTransformation
                                                .transformationCurrentVersion(lastAnnonce);
                                        // Construction de l'objet JAXB Content
                                        ByteArrayInputStream inputContent = new ByteArrayInputStream(
                                                lastAnnonce.getMessageContent().getBytes("UTF-8"));
                                        ch.gdk_cds.xmlns.pv_5201_000101._3.Message o_content = (ch.gdk_cds.xmlns.pv_5201_000101._3.Message) jaxb
                                                .unmarshal(inputContent, false, false, addClasses);

                                        lastContribution = o_content.getContent().getDecree().getAmount();
                                        lastDebutDroit = String
                                                .valueOf(o_content.getContent().getDecree().getBeginMonth().getMonth())
                                                + "." + String.valueOf(
                                                        o_content.getContent().getDecree().getBeginMonth().getYear());
                                        // cas x.xxxx, >> il faut xx.xxxx
                                        if (lastDebutDroit.length() == 6) {
                                            lastDebutDroit = "0" + lastDebutDroit;
                                        }
                                        lastFinDroit = String
                                                .valueOf(o_content.getContent().getDecree().getEndMonth().getMonth())
                                                + "." + String.valueOf(
                                                        o_content.getContent().getDecree().getEndMonth().getYear());
                                        if (lastFinDroit.length() == 6) {
                                            // cas x.xxxx, >> il faut xx.xxxx
                                            lastFinDroit = "0" + lastFinDroit;
                                        }
                                        // type de demande : si limitation == true, alors c'était un subside assisté
                                        // sinon,
                                        // c'est autre
                                        lastWasAssiste = o_content.getContent().getDecree().isLimitation();
                                    } catch (Exception ex) {
                                        JadeThread.logInfo("AnnoncesRPServiceImpl._initAnnonce()", ex.getMessage());
                                        ex.printStackTrace();
                                    }

                                    // contrôle montant alloué
                                    BigDecimal montantContribution = new BigDecimal(
                                            currentSubside.getMontantContributionAvecSupplExtra());
                                    if (montantContribution.compareTo(lastContribution) != 0) {
                                        bInitInterruption = true;
                                        bInitNewDecision = true;
                                    }
                                    // contrôle date de début - date de fin
                                    if (!lastDebutDroit.equals(currentSubside.getDebutDroit())) {
                                        bInitInterruption = true;
                                    } else {
                                        String currentFinDroit = currentSubside.getFinDroit();
                                        if (JadeStringUtil.isBlankOrZero(currentFinDroit)) {
                                            currentFinDroit = "12." + currentSubside.getAnneeHistorique();
                                        }
                                        if (!lastFinDroit.equals(currentFinDroit)) {
                                            bInitInterruption = true;
                                        }
                                    }
                                    // contrôle CM
                                    if (!lastCM.equals(currentSubside.getNoCaisseMaladie())) {
                                        bInitInterruption = true;
                                        bInitNewDecision = true;
                                    }

                                    // BZ 9303
                                    // contrôle type demande
                                    // Si c'était un assisté avant et que ce ne l'est plus
                                    // OU
                                    // Si ce n'était pas un assisté et que ca l'est devenu
                                    if ((lastWasAssiste && !AMTypeDemandeSubside.ASSISTE.getValue()
                                            .equals(currentSubside.getTypeDemande()))
                                            || (!lastWasAssiste && AMTypeDemandeSubside.ASSISTE.getValue()
                                                    .equals(currentSubside.getTypeDemande()))) {
                                        bInitInterruption = true;
                                        bInitNewDecision = true;
                                    }

                                }
                            } else {
                                if (messagesOrderedById.size() > 0) {
                                    // Annonces avec rejet, on crée une nouvelle décision
                                    bInitNewDecision = true;

                                    // On va tester si une annonce COSAMA existe pour créer une interruption le cas
                                    // échéant,
                                    // sinon la nouvelle décision sera refusée
                                    SimpleAnnonce lastAnnonceCosama = _lastAnnonceCosama(idDetailFamille);
                                    if (lastAnnonceCosama != null) {
                                        bInitInterruption = true;
                                    }
                                }
                            }
                        }
                    } else {
                        // On met a jour l'annonce
                        pendingAnnonceDecree.setIdTiersCM(currentSubside.getNoCaisseMaladie());
                        pendingAnnonceDecree = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                                .update(pendingAnnonceDecree);
                    }

                }
                // ---------------------------------------------------------
                // INITIALISATION D'UNE INTERRUPTION
                // ---------------------------------------------------------
                if (bInitInterruption) {
                    _createSimpleAnnonceSedex(simpleAnnonceSedex, idDetailFamille, idContribuable, lastCM,
                            msgType.getValue(), AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue(),
                            IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                            IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                }
                // ---------------------------------------------------------
                // INITIALISATION D'UNE NOUVELLE DECISION
                // ---------------------------------------------------------
                if (bInitNewDecision) {
                    simpleAnnonceSedex = new SimpleAnnonceSedex();
                    _createSimpleAnnonceSedex(simpleAnnonceSedex, idDetailFamille, idContribuable,
                            currentSubside.getNoCaisseMaladie(), msgType.getValue(), msgSubType.getValue(),
                            IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                            IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                }
                break;
            case INTERRUPTION:
                SimpleAnnonceSedex lastDecreeConfirmed = _lastDecreeConfirmed(idDetailFamille);
                SimpleAnnonce lastAnnonceCosama = _lastAnnonceCosama(idDetailFamille);
                boolean decreeConfirmed = lastDecreeConfirmed != null;
                boolean annonceCosamaExist = lastAnnonceCosama != null;

                // ---------------------------------------------------------
                // INITIALISATION D'UNE INTERRUPTION
                // ---------------------------------------------------------
                // Seulement si on a une décision confirmée OU si aucune annonce n'est confirmé mais qu'une annonce
                // COSAMA
                // existe
                if (decreeConfirmed || (!decreeConfirmed && annonceCosamaExist)) {

                    String lastCMId = "0";
                    if (decreeConfirmed) {
                        lastCMId = lastDecreeConfirmed.getIdTiersCM();
                    }
                    if (annonceCosamaExist) {
                        lastCMId = lastAnnonceCosama.getNoCaisseMaladie();
                    }

                    _createSimpleAnnonceSedex(simpleAnnonceSedex, idDetailFamille, idContribuable, lastCMId,
                            msgType.getValue(), AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue(),
                            IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                            IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                }

                // Si on a une annonce cosama et pas d'annonce SEDEX et si c'est un subside actif
                if (annonceCosamaExist && !decreeConfirmed && subsideIsEnabled) {
                    simpleAnnonceSedex = new SimpleAnnonceSedex();
                    _createSimpleAnnonceSedex(simpleAnnonceSedex, idDetailFamille, idContribuable,
                            currentSubside.getNoCaisseMaladie(), msgType.getValue(),
                            AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue(),
                            IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                            IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                }

                break;
            default:
                _createSimpleAnnonceSedex(simpleAnnonceSedex, null, null, null, msgType.getValue(),
                        msgSubType.getValue(), IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                        IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                break;
        }

        return simpleAnnonceSedex;
    }

    /**
     * Initialisation d'une annonce 'Demande de rapport'. <br>
     * <br>
     * Appelé depuis la page des assureurs
     *
     * @param idDetailFamille
     * @param idCaisse
     * @param periodeFrom
     * @param periodeTo
     */
    private void _initAnnonceDemandeRapport(String idDetailFamille, String idCaisse, String periodeFrom,
            String periodeTo) {
        String idSedexCaisse = null;
        try {
            idSedexCaisse = AMSedexRPUtil.getSedexIdFromIdTiers(idCaisse);
        } catch (AnnonceSedexException e1) {
            idSedexCaisse = null;
        }

        // Si l'idSedex n'existe pas, on ne crée même pas l'annonce
        if (!JadeStringUtil.isBlankOrZero(idSedexCaisse)) {
            SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
            try {
                SimpleDetailFamille currentSubside = AmalImplServiceLocator.getSimpleDetailFamilleService()
                        .read(idDetailFamille);
                simpleAnnonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.DEMANDE_RAPPORT_ASSURANCE.getValue());
                simpleAnnonceSedex
                        .setMessageSubType(AMMessagesSubTypesAnnonceSedex.DEMANDE_RAPPORT_ASSURANCE.getValue());
                simpleAnnonceSedex.setIdDetailFamille(idDetailFamille);
                simpleAnnonceSedex.setIdContribuable(currentSubside.getIdContribuable());
                simpleAnnonceSedex.setIdTiersCM(idCaisse);
                simpleAnnonceSedex.setTraitement(IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue());
                simpleAnnonceSedex.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(simpleAnnonceSedex);
            } catch (Exception e) {
                _setErrorOnCreation(simpleAnnonceSedex, e);
            }
        }
    }

    private boolean _isLastStopCanceled(SimpleAnnonceSedex lastAnnonce) throws Exception {
        Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5201_000201._3.Message.class };

        // Instancie les services JAXB
        JAXBServices jaxb = JAXBServices.getInstance();

        lastAnnonce = AnnonceSedexTransformation.transformationCurrentVersion(lastAnnonce);

        // Construction de l'objet JAXB Content
        ByteArrayInputStream inputContent = new ByteArrayInputStream(lastAnnonce.getMessageContent().getBytes("UTF-8"));
        ch.gdk_cds.xmlns.pv_5201_000201._3.Message ct_stop = (ch.gdk_cds.xmlns.pv_5201_000201._3.Message) jaxb
                .unmarshal(inputContent, false, false, addClasses);

        // Date de stop de l'interruption
        XMLGregorianCalendar stopDateTime = ct_stop.getContent().getStop().getStopMonth();
        String s_day = "01.";
        String s_month = JadeStringUtil.fillWithZeroes(String.valueOf(stopDateTime.getMonth()), 2);
        String s_year = JadeStringUtil.fillWithZeroes(String.valueOf(stopDateTime.getYear()), 4);
        String stopMonth = s_day + s_month + "." + s_year;

        // Date de début du decree
        XMLGregorianCalendar XmlStopBeginMonth = ct_stop.getContent().getStop().getDecree().getBeginMonth();
        String s_dayBeginMonth = "01.";
        String s_monthBeginMonth = JadeStringUtil.fillWithZeroes(String.valueOf(XmlStopBeginMonth.getMonth()), 2);
        String s_yearBeginMonth = JadeStringUtil.fillWithZeroes(String.valueOf(XmlStopBeginMonth.getYear()), 4);
        String decreeBeginMonth = s_dayBeginMonth + s_monthBeginMonth + "." + s_yearBeginMonth;

        // // Date de fin du decree
        // XMLGregorianCalendar XmlStopEndMonth = ct_stop.getContent().getStop().getDecree().getEndMonth();
        // String s_dayEndMonth = "01.";
        // String s_monthEndMonth = JadeStringUtil.fillWithZeroes(String.valueOf(XmlStopEndMonth.getMonth()), 2);
        // String s_yearEndMonth = JadeStringUtil.fillWithZeroes(String.valueOf(XmlStopEndMonth.getYear()), 4);
        // String decreeEndMonth = s_dayEndMonth + s_monthEndMonth + "." + s_yearEndMonth;

        // Si la date de fin du stop est avant la date de début du decree c'est une annulation
        if (JadeDateUtil.isDateBefore(stopMonth, decreeBeginMonth)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne true si une annonce COSAMA existe pour le dossier
     *
     * @param idDetailFamille
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private SimpleAnnonce _lastAnnonceCosama(String idDetailFamille)
            throws JadePersistenceException, AnnonceException, JadeApplicationServiceNotAvailableException {
        // Récupération des annonces COSAMA
        SimpleAnnonceSearch simpleAnnonceCosamaSearch = new SimpleAnnonceSearch();
        simpleAnnonceCosamaSearch.setForIdDetailFamille(idDetailFamille);
        simpleAnnonceCosamaSearch.setOrderKey("dateAnnonceRIP");
        simpleAnnonceCosamaSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(simpleAnnonceCosamaSearch);

        SimpleAnnonce simpleAnnonce = null;
        if (simpleAnnonceCosamaSearch.getSize() > 0) {
            simpleAnnonce = (SimpleAnnonce) simpleAnnonceCosamaSearch.getSearchResults()[0];
        }

        return simpleAnnonce;
    }

    private List<SimpleAnnonceSedex> _lastAllDecreeConfirmed(String idDetailFamille)
            throws JadePersistenceException, AnnonceSedexException, JadeApplicationServiceNotAvailableException {
        SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = _findAllNouvelleDecisionEnvoyeFromIdDetailFamille(idDetailFamille);

        List<SimpleAnnonceSedex> simpleAnnonceSedexConfirmed = new ArrayList<>();
        for (JadeAbstractModel abstractSimpleAnnonceSedex : simpleAnnonceSedexSearch.getSearchResults()) {
            SimpleAnnonceSedex simpleAnnonceSedex = (SimpleAnnonceSedex) abstractSimpleAnnonceSedex;

            // Si une des annonces SEDEX a été confirmée
            if (AnnonceBuilderAbstract.isAnnonceConfirmed(simpleAnnonceSedex, false)) {
                simpleAnnonceSedexConfirmed.add(simpleAnnonceSedex);
            }
        }

        return simpleAnnonceSedexConfirmed;
    }

    private SimpleAnnonceSedexSearch _findAllNouvelleDecisionEnvoyeFromIdDetailFamille(String idDetailFamille)
            throws JadePersistenceException, AnnonceSedexException, JadeApplicationServiceNotAvailableException {
        SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();
        simpleAnnonceSedexSearch.setForIdDetailFamille(idDetailFamille);
        simpleAnnonceSedexSearch.setForMessageSubType(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue());
        ArrayList<String> listStatus = new ArrayList<>();
        listStatus.add(AMStatutAnnonceSedex.ENVOYE.getValue());
        simpleAnnonceSedexSearch.setInStatus(listStatus);
        simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                .search(simpleAnnonceSedexSearch);
        return simpleAnnonceSedexSearch;
    }

    /**
     * Retourne true si une annonce a été confirmée et non interrompue dans le subside passé en paramètre
     *
     * @param idDetailFamille
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private SimpleAnnonceSedex _lastDecreeConfirmed(String idDetailFamille)
            throws JadePersistenceException, AnnonceSedexException, JadeApplicationServiceNotAvailableException {
        SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = _findAllNouvelleDecisionEnvoyeFromIdDetailFamille(idDetailFamille);

        SimpleAnnonceSedex simpleAnnonceSedexConfirmed = null;
        for (JadeAbstractModel abstractSimpleAnnonceSedex : simpleAnnonceSedexSearch.getSearchResults()) {
            SimpleAnnonceSedex simpleAnnonceSedex = (SimpleAnnonceSedex) abstractSimpleAnnonceSedex;

            // Si une des annonces SEDEX a été confirmée
            if (AnnonceBuilderAbstract.isAnnonceConfirmed(simpleAnnonceSedex)) {
                simpleAnnonceSedexConfirmed = simpleAnnonceSedex;
            }
        }
        return simpleAnnonceSedexConfirmed;
    }

    /**
     * Charge les annonces à créer sans spécifier les valeurs pour les critères :
     *
     * <ul>
     * <li>Type du message</>
     * <li>Caisse(s)</>
     * <li>Année historique</>
     * </ul>
     * Une annonce à créer est une annonce qui est dans un état "Initial" ou "Erreur-crée"
     *
     * @return Model search contenant les annonces
     */
    private ComplexAnnonceSedexSearch _loadAnnonceToCreate() {
        return this._loadAnnonceToCreate(null, null, null);
    }

    /**
     * Charge les annonces à créer en spécifiant les critères :
     *
     * <ul>
     * <li>Type du message</>
     * <li>Caisse(s)</>
     * <li>Année historique</>
     * </ul>
     *
     * Une annonce à créer est une annonce qui est dans un état "Initial" ou "Erreur-crée"
     *
     * @param msgType
     * @param selectedIdCaisses
     * @param anneeHistorique
     * @return
     */
    private ComplexAnnonceSedexSearch _loadAnnonceToCreate(String msgType, List<String> selectedIdCaisses,
            String anneeHistorique) {

        ComplexAnnonceSedexSearch annonceSedexSearch = new ComplexAnnonceSedexSearch();
        try {
            if (!JadeStringUtil.isBlankOrZero(msgType)) {
                annonceSedexSearch.setForSDXMessageType(msgType);
            }

            if (!JadeStringUtil.isBlankOrZero(anneeHistorique)) {
                annonceSedexSearch.setForSUBAnneeHistorique(anneeHistorique);
            }

            if (!(selectedIdCaisses == null) && (selectedIdCaisses.size() > 0)) {
                annonceSedexSearch.setInCMIdTiersCaisse((ArrayList) selectedIdCaisses);
            }

            // FOR DEBUG ONLY
            // ArrayList<String> listIdContribuables = new ArrayList<String>();
            // listIdContribuables.add("70466");
            // annonceSedexSearch.setInSDXIdContribuable(listIdContribuables);

            ArrayList<String> inStatus = new ArrayList<String>();
            inStatus.add(IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
            inStatus.add(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue());
            annonceSedexSearch.setInSDXStatus(inStatus);
            annonceSedexSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            annonceSedexSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(annonceSedexSearch);
        } catch (Exception e) {
            JadeThread.logError("Error loading annonces to create !", e.getMessage());
        }

        return annonceSedexSearch;
    }

    /**
     * Charge les annonces à envoyer sans spécifier les valeurs pour les critères :
     *
     * <ul>
     * <li>Type du message</>
     * <li>Caisse(s)</>
     * <li>Année historique</>
     * </ul>
     * Une annonce à envoyer est une annonce qui est dans un état "Créé" ou "Erreur-envoyé"
     *
     * @return Model search contenant les annonces
     */
    private Map<String, Map<String, List<SimpleAnnonceSedex>>> _loadAnnonceToSend() {
        return this._loadAnnonceToSend(null, null, null);

    }

    /**
     * Charge les annonces à envoyer en spécifiant les critères :
     *
     * <ul>
     * <li>Type du message</>
     * <li>Caisse(s)</>
     * <li>Année historique</>
     * </ul>
     *
     * Une annonce à envoyer est une annonce qui est dans un état "Créé" ou "Erreur-envoyé"
     *
     * @param msgType
     * @param selectedIdCaisses
     * @param anneeHistorique
     * @return
     */
    private Map<String, Map<String, List<SimpleAnnonceSedex>>> _loadAnnonceToSend(String msgType,
            List<String> selectedIdCaisses, String anneeHistorique) {
        Map<String, Map<String, List<SimpleAnnonceSedex>>> mapCMAnnonces = new HashMap<String, Map<String, List<SimpleAnnonceSedex>>>();
        try {
            ComplexAnnonceSedexSearch annonceSedexSearch = new ComplexAnnonceSedexSearch();

            // FOR DEBUG ONLY
            // ArrayList<String> listIdContribuables = new ArrayList<String>();
            // listIdContribuables.add("70466");
            // annonceSedexSearch.setInSDXIdContribuable(listIdContribuables);

            if (!JadeStringUtil.isBlankOrZero(msgType)) {
                annonceSedexSearch.setForSDXMessageType(msgType);
            }

            if (!JadeStringUtil.isBlankOrZero(anneeHistorique)) {
                annonceSedexSearch.setForSUBAnneeHistorique(anneeHistorique);
            }

            if (!(selectedIdCaisses == null) && (selectedIdCaisses.size() > 0)) {
                annonceSedexSearch.setInCMIdTiersCaisse((ArrayList) selectedIdCaisses);
            }

            ArrayList<String> inStatus = new ArrayList<String>();
            inStatus.add(IAMCodeSysteme.AMStatutAnnonceSedex.CREE.getValue());
            inStatus.add(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_ENVOYE.getValue());
            annonceSedexSearch.setInSDXStatus(inStatus);
            annonceSedexSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            annonceSedexSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(annonceSedexSearch);

            for (JadeAbstractModel model : annonceSedexSearch.getSearchResults()) {
                Map<String, List<SimpleAnnonceSedex>> mapTypeAnnonces = new HashMap<String, List<SimpleAnnonceSedex>>();
                List<SimpleAnnonceSedex> listAnnonces = new ArrayList<SimpleAnnonceSedex>();
                ComplexAnnonceSedex complexAnnonceSedex = (ComplexAnnonceSedex) model;
                if (!mapCMAnnonces.containsKey(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageRecepteur())) {
                    listAnnonces.add(complexAnnonceSedex.getSimpleAnnonceSedex());
                    mapTypeAnnonces.put(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageType(), listAnnonces);
                    mapCMAnnonces.put(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageRecepteur(),
                            mapTypeAnnonces);
                } else {
                    mapTypeAnnonces = mapCMAnnonces
                            .get(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageRecepteur());
                    if (!mapTypeAnnonces.containsKey(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageType())) {
                        mapTypeAnnonces.put(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageType(),
                                new ArrayList<SimpleAnnonceSedex>());
                    }
                    listAnnonces = mapTypeAnnonces.get(complexAnnonceSedex.getSimpleAnnonceSedex().getMessageType());
                    listAnnonces.add(complexAnnonceSedex.getSimpleAnnonceSedex());
                }
            }
        } catch (Exception e) {
            JadeThread.logError("_loadAnnonceToSend", "Error loading annonce to send --> " + e.getMessage());
        }

        return mapCMAnnonces;
    }

    private SimpleAnnonceSedex _lookForPendingAnnonce(String idContribuable, String idDetailFamille)
            throws JadePersistenceException, AnnonceSedexException, JadeApplicationServiceNotAvailableException {
        SimpleAnnonceSedex pendingAnnonceDecree = null;
        SimpleAnnonceSedexSearch currentSearchNotSent = new SimpleAnnonceSedexSearch();
        currentSearchNotSent.setForIdContribuable(idContribuable);
        currentSearchNotSent.setForIdDetailFamille(idDetailFamille);
        ArrayList<String> inStatus = new ArrayList<String>();
        inStatus.add(IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
        inStatus.add(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue());
        currentSearchNotSent.setInStatus(inStatus);
        ArrayList<String> inMessageSubType = new ArrayList<String>();
        inMessageSubType.add(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue());
        currentSearchNotSent.setInMessageSubType(inMessageSubType);
        currentSearchNotSent.setDefinedSearchSize(0);
        currentSearchNotSent = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(currentSearchNotSent);

        for (JadeAbstractModel abstractModel : currentSearchNotSent.getSearchResults()) {
            SimpleAnnonceSedex pendingAnnonce = (SimpleAnnonceSedex) abstractModel;

            if (pendingAnnonce.getMessageSubType()
                    .equals(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue())) {
                pendingAnnonceDecree = pendingAnnonce;
            }
        }
        return pendingAnnonceDecree;
    }

    /**
     * Retourne un modèle search contenant les annonces qui correpondent aux critères entrés sur la page sedex_rc.jsp
     * (appel ajax)
     *
     * @param filters
     *            Filtres à appliquer
     * @param order
     *            Ordre à appliquer
     * @return
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private ComplexAnnonceSedexSearch _searchAnnoncesForExport(String filters, String order)
            throws JadePersistenceException, AnnonceSedexException, JadeApplicationServiceNotAvailableException {
        // Récupération des valeurs de filtres
        Map<String, String> mapFilterValue = initMapFilterValue(filters);
        // Mapping des filtres avec le model search
        ComplexAnnonceSedexSearch annonceSedexSearch = initialiseComplexAnnonceSedexSearch(mapFilterValue, order);

        // Lancement de la recherche
        annonceSedexSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        annonceSedexSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(annonceSedexSearch);
        return annonceSedexSearch;
    }


    private ComplexAnnonceSedexSearch initialiseComplexAnnonceSedexSearch(Map<String, String> mapFilterValue, String order) {

        ComplexAnnonceSedexSearch annonceSedexSearch = new ComplexAnnonceSedexSearch();
        annonceSedexSearch.setWhereKey("rcListe");

        for (String mapKey : mapFilterValue.keySet()) {
            String mapVal = mapFilterValue.get(mapKey);

            if ("forCMIdTiersGroupe".equals(mapKey) && "0".equals(mapVal)) {
                annonceSedexSearch.setWhereKey("rcListeSansGroupe");
            } else {
                if (JadeStringUtil.isBlankOrZero(mapVal) || "undefined".equals(mapVal)) {
                    continue;
                }
            }

            if ("setForOrder".equals(mapKey)) {
            } else if ("forMessageType".equals(mapKey)) {
                annonceSedexSearch.setForSDXMessageType(mapVal);
            } else if ("inMessageSubType".equals(mapKey)) {
                List<String> listVal = JadeStringUtil.tokenize(mapVal, "|");
                ArrayList<String> arrayListSubTypes = new ArrayList<String>();
                for (String val : listVal) {
                    arrayListSubTypes.add(val);
                }
                annonceSedexSearch.setInSDXMessageSubType(arrayListSubTypes);
            } else if ("inSDXStatus".equals(mapKey)) {
                List<String> listVal = JadeStringUtil.tokenize(mapVal, "|");
                ArrayList<String> arrayListStatus = new ArrayList<String>();
                for (String val : listVal) {
                    arrayListStatus.add(val);
                }
                annonceSedexSearch.setInSDXStatus(arrayListStatus);
            } else if ("inSDXTraitement".equals(mapKey)) {
                List<String> listVal = JadeStringUtil.tokenize(mapVal, "|");
                ArrayList<String> arrayListTraitement = new ArrayList<String>();
                for (String val : listVal) {
                    arrayListTraitement.add(val);
                }
                annonceSedexSearch.setInSDXTraitement(arrayListTraitement);
            } else if ("forDateMessageGOE".equals(mapKey)) {
                annonceSedexSearch.setForSDXDateMessageGOE(mapVal);
            } else if ("forDateMessageLOE".equals(mapKey)) {
                annonceSedexSearch.setForSDXDateMessageLOE(mapVal);
            } else if ("forSUBAnneeHistorique".equals(mapKey)) {
                annonceSedexSearch.setForSUBAnneeHistorique(mapVal);
            } else if ("likeCMNomCaisse".equals(mapKey)) {
                annonceSedexSearch.setLikeCMNomCaisse(mapVal);
            } else if ("forCMNumCaisse".equals(mapKey)) {
                annonceSedexSearch.setForCMNumCaisse(mapVal);
            } else if ("forCMIdTiersGroupe".equals(mapKey)) {
                annonceSedexSearch.setForCMIdTiersGroupe(mapVal);
            }
        }

        try {
            if (!JadeStringUtil.isBlankOrZero(order)) {
                String[] order_splitted = order.split(":");
                if (order_splitted.length > 1) {
                    annonceSedexSearch.setOrderKey(order_splitted[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // On ne fait rien, order pas appliqué
        }
        return annonceSedexSearch;
    }

    private Map<String, String> initMapFilterValue(String filters) {
        Map<String, String> mapFilterValue = new HashMap<String, String>();
        String[] pairs = filters.split(";");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            if (keyValue.length > 1) {
                mapFilterValue.put(keyValue[0], keyValue[1]);
            }
        }
        return mapFilterValue;
    }

    /**
     * Envoi d'annonce (simple)
     *
     * @param simpleAnnonceSedex
     *            Annonce à envoyer
     */
    private boolean _sendAnnonce(SimpleAnnonceSedex simpleAnnonceSedex) {
        try {
            SimpleSedexMessage sedexMessage = _createSimpleSedexMessage(simpleAnnonceSedex);

            List<SimpleSedexMessage> listSedexMessages = new ArrayList<SimpleSedexMessage>();
            sedexMessage.increment = "1";
            listSedexMessages.add(sedexMessage);

            // Envoi de l'annonce
            JadeSedexService.getInstance().sendGroupedMessage(sedexMessage);

            // On ajout la date à l'annonce
            String dateToday = "";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            dateToday = sdf.format(cal.getTime());
            simpleAnnonceSedex.setDateMessage(dateToday);

            // On set le status à "Envoyé"
            simpleAnnonceSedex.setStatus(AMStatutAnnonceSedex.ENVOYE.getValue());
            AmalImplServiceLocator.getSimpleAnnonceSedexService().update(simpleAnnonceSedex);
            return true;
        } catch (Exception e) {
            _setErrorOnSend(simpleAnnonceSedex, e);
        }
        return false;
    }

    /**
     * Envoi d'annonces (groupées)
     *
     * @param listAnnonces
     *            Annonces à envoyer
     */
    private int _sendGroupedAnnonce(List<SimpleAnnonceSedex> listAnnonces) {

        List<SimpleSedexMessage> listSedexMessages = new ArrayList<SimpleSedexMessage>();
        String envelopeMessageId = JadeUUIDGenerator.createLongUID().toString();
        String msgType = "";
        String recipientId = "";
        int nbItemsSend = 0;
        int incr = 1;
        ArrayList<SimpleAnnonceSedex> listAnnoncesUpdated = new ArrayList<SimpleAnnonceSedex>();
        for (SimpleAnnonceSedex simpleAnnonceSedex : listAnnonces) {
            // Envoi groupé en une seul annonce de toutes les annonces de prime tarifaire
            if (AMMessagesTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue().equals(simpleAnnonceSedex.getMessageType())) {
                try {
                    // La création du message regroupé n'a lieu qu'une fois pour toutes les annonces de prime tarifaires
                    if (nbItemsSend == 0) {
                        String originalContent = simpleAnnonceSedex.getMessageContent();
                        msgType = simpleAnnonceSedex.getMessageType();

                        recipientId = AMSedexRPUtil.getSedexIdFromIdTiers(simpleAnnonceSedex.getIdTiersCM());

                        // Remplace le contenu initial de la première annonce par le resultat du regroupement des personnes pour généré un envoi groupé
                        simpleAnnonceSedex.setMessageContent(regroupeAnnoncesPrimeTarifaire(listAnnonces, simpleAnnonceSedex));

                        SimpleSedexMessage sedexMessage = _createSimpleSedexMessage(simpleAnnonceSedex);
                        sedexMessage.increment = String.valueOf(incr);
                        listSedexMessages.add(sedexMessage);

                        // Reset le contenu initial de la première annonce car les annonces groupées ne sont pas sauvegardées en DB
                        simpleAnnonceSedex.setMessageContent(originalContent);

                        nbItemsSend++;
                        incr++;
                    }

                    // Update en status "Envoyé" toutes les annonces de prime tarifaire impliquées dans l'envoi groupé
                    if (!arrayListErrorsSedexId.contains(simpleAnnonceSedex.getIdAnnonceSedex())) {
                        String dateToday = "";
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        dateToday = sdf.format(cal.getTime());
                        // On ajout la date à l'annonce
                        simpleAnnonceSedex.setDateMessage(dateToday);
                        // On set le status à "Envoyé"
                        simpleAnnonceSedex.setStatus(AMStatutAnnonceSedex.ENVOYE.getValue());
                        AmalImplServiceLocator.getSimpleAnnonceSedexService().update(simpleAnnonceSedex);
                        listAnnoncesUpdated.add(simpleAnnonceSedex);
                    }
                } catch (Exception e) {
                    _setErrorOnSend(simpleAnnonceSedex, e);
                    arrayListErrorsSedexId.add(simpleAnnonceSedex.getIdAnnonceSedex());
                }
            } else {
                try {
                    msgType = simpleAnnonceSedex.getMessageType();

                    recipientId = AMSedexRPUtil.getSedexIdFromIdTiers(simpleAnnonceSedex.getIdTiersCM());

                    SimpleSedexMessage sedexMessage = _createSimpleSedexMessage(simpleAnnonceSedex);
                    sedexMessage.increment = String.valueOf(incr);
                    listSedexMessages.add(sedexMessage);

                    String dateToday = "";
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    dateToday = sdf.format(cal.getTime());
                    // On ajout la date à l'annonce
                    simpleAnnonceSedex.setDateMessage(dateToday);
                    // On set le status à "Envoyé"
                    simpleAnnonceSedex.setStatus(AMStatutAnnonceSedex.ENVOYE.getValue());
                    AmalImplServiceLocator.getSimpleAnnonceSedexService().update(simpleAnnonceSedex);
                    listAnnoncesUpdated.add(simpleAnnonceSedex);
                    nbItemsSend++;
                    incr++;
                } catch (Exception e) {
                    _setErrorOnSend(simpleAnnonceSedex, e);
                }
            }
        }

        try {

            // Boucle FOR pour gérer et envoyer les annonces par lot de 100 et ainsi éviter un out of memory
            List<SimpleSedexMessage> subListAnnoncesToSend = new ArrayList<SimpleSedexMessage>();
            int fromIndex = 0;

            int toIndex = 100;

            boolean lastPacket = false;
            if (listSedexMessages.size() <= 100) {
                toIndex = listSedexMessages.size();
                lastPacket = true;
            }
            boolean iterateAnnonces = true;

            subListAnnoncesToSend = listSedexMessages.subList(fromIndex, toIndex);

            while (iterateAnnonces) {
                String envelopeFile = _createEnvelope(envelopeMessageId, msgType, recipientId);
                // Envoi de l'annonce
                JadeSedexService.getInstance().sendGroupedMessage(envelopeFile, subListAnnoncesToSend);

                if (lastPacket) {
                    iterateAnnonces = false;
                    break;
                }

                fromIndex = toIndex;
                toIndex = toIndex + 100;

                if (toIndex > listSedexMessages.size()) {
                    toIndex = listSedexMessages.size();
                    lastPacket = true;
                }

                try {
                    subListAnnoncesToSend = listSedexMessages.subList(fromIndex, toIndex);
                } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    iterateAnnonces = false;
                }
            }
        } catch (Exception e) {
            try {
                for (SimpleAnnonceSedex annonceToSetOnError : listAnnoncesUpdated) {
                    annonceToSetOnError.setStatus(AMStatutAnnonceSedex.ERROR_ENVOYE.getValue());
                    AmalImplServiceLocator.getSimpleAnnonceSedexService().update(annonceToSetOnError);
                }
            } catch (Exception e2) {
                _setErrorOnSend(null, e2);
            }
            _setErrorOnSend(null, e);
        }
        return nbItemsSend;
    }

    /**
     * Regroupement des annonces de primes tarifaire
     * La première annonces est utilisé comme modèle pour
     * générer la structure de l'annonces (entêtes etc...)
     * puis les personnes trouvées dans les autres annonces
     * sont ajoutées une par une au contenu de l'annonce.
     *
     * @param listAnnoncesDemandePrimeTarifaire
     * @param simpleAnnonceSedex
     *
     * @return une annonce contenant toutes les personnes trouvées dans la liste de prime tarifaire
     */
    private String regroupeAnnoncesPrimeTarifaire(List<SimpleAnnonceSedex> listAnnoncesDemandePrimeTarifaire, SimpleAnnonceSedex simpleAnnonceSedex)  throws AnnonceSedexException, ClassNotFoundException, JAXBException, SAXException, MalformedURLException,
            IOException, JAXBValidationError, JAXBValidationWarning {

        String messageContent = simpleAnnonceSedex.getMessageContent();

        // Récupération du builder
        AnnonceBuilderAbstract annonceBuilderAbstract = AnnonceBuilderFactory.getAnnonceBuilder(simpleAnnonceSedex);

        // Récupération de la class associée à l'annonce
        Class<?>[] addClasses = new Class[]{Class.forName(annonceBuilderAbstract.whichClass().getName())};
        // Construction de l'objet JAXB Header depuis la chaine XML
        Object o_header = _getHeaderAnnonce(simpleAnnonceSedex, addClasses);
        // Construction de l'objet JAXB Content depuis la chaine XML
        Object o_content = _getContentAnnonce(simpleAnnonceSedex, addClasses);

        if (o_content instanceof Message) {
            // Evite un doublon de la première personne
            ((Message) o_content).getContent().getPremiumQuery().getPerson().clear();

            // Regroupement des personnes trouvé dans le contenu de la listeAnnonces
            for (SimpleAnnonceSedex simpleAnnonceSedexLoop : listAnnoncesDemandePrimeTarifaire) {
                if (!arrayListErrorsSedexId.contains(simpleAnnonceSedexLoop.getIdAnnonceSedex())) {
                    try {
                        // Construction de l'objet JAXB Content depuis la chaine XML
                        Object o_content_sub = _getContentAnnonce(simpleAnnonceSedexLoop, addClasses);

                        // Ajoute la personne dans la liste de personnes
                        if (o_content_sub instanceof Message) {
                            ((Message) o_content).getContent().getPremiumQuery().getPerson().add(((Message) o_content_sub).getContent().getPremiumQuery().getPerson().get(0));
                        }
                    } catch (Exception e) {
                        _setErrorOnSend(simpleAnnonceSedexLoop, e);
                        arrayListErrorsSedexId.add(simpleAnnonceSedex.getIdAnnonceSedex());
                    }
                }
            }
        }

        // Construction du message
        Object o_message = annonceBuilderAbstract.createMessage(o_header, o_content);

        // Marshalling pour créer le String
        StringWriter swFull = new StringWriter();
        JAXBServices.getInstance().marshal(o_message, swFull, false, false, addClasses);
        messageContent = swFull.toString();

        return messageContent;
    }

    /**
     * Set l'annonce en état ERREUR CREE
     *
     * @param annonceSedex
     * @param e
     */
    private void _setErrorOnCreation(SimpleAnnonceSedex annonceSedex, Throwable e) {
        try {
            String typeMsg = AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(annonceSedex.getMessageSubType());

            String refMembre = "";
            try {
                if (!JadeStringUtil.isBlankOrZero(annonceSedex.getIdDetailFamille())) {
                    SimpleDetailFamille simpleDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                            .read(annonceSedex.getIdDetailFamille());

                    if (!simpleDetailFamille.isNew()
                            && !JadeStringUtil.isBlankOrZero(simpleDetailFamille.getIdFamille())) {
                        SimpleFamille simpleFamille = AmalImplServiceLocator.getSimpleFamilleService()
                                .read(simpleDetailFamille.getIdFamille());
                        refMembre = " - " + simpleFamille.getNomPrenom();

                    }
                }
            } catch (Exception ex) {
                refMembre = "";
            }

            arrayListErrorsCreation.add("Annonce #" + annonceSedex.getIdAnnonceSedex() + " (" + typeMsg + refMembre
                    + ") " + " ==> " + e.getMessage() + " / " + e.toString());

            if (mapListErrors.containsKey(annonceSedex.getIdAnnonceSedex())) {
                ArrayList<String> listErrors = mapListErrors.get(annonceSedex.getIdAnnonceSedex());
                listErrors.add(e.getMessage());
            } else {
                ArrayList<String> listErrors = new ArrayList<String>();
                listErrors.add(e.getMessage());
                mapListErrors.put(annonceSedex.getIdAnnonceSedex(), listErrors);
            }

            JadeThread.logClear();
            if (!isSimulation) {
                annonceSedex.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue());
                annonceSedex.setTraitement(IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue());
                annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().update(annonceSedex);
            }
        } catch (Exception ex) {
            e.printStackTrace();
            // Mise a jour échouée, on ne fait rien...
        }
    }

    /**
     * Set l'annonce en état ERREUR ENVOYE
     *
     * @param annonceSedex
     * @param e
     */
    private void _setErrorOnSend(SimpleAnnonceSedex annonceSedex, Throwable e) {
        try {
            String refMembre = "";
            try {
                if (!JadeStringUtil.isBlankOrZero(annonceSedex.getIdDetailFamille())) {
                    SimpleDetailFamille simpleDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService()
                            .read(annonceSedex.getIdDetailFamille());

                    if (!simpleDetailFamille.isNew()
                            && !JadeStringUtil.isBlankOrZero(simpleDetailFamille.getIdFamille())) {
                        SimpleFamille simpleFamille = AmalImplServiceLocator.getSimpleFamilleService()
                                .read(simpleDetailFamille.getIdFamille());
                        refMembre = " - " + simpleFamille.getNomPrenom();

                    }
                }
            } catch (Exception ex2) {
                refMembre = "";
            }

            if (annonceSedex == null) {
                arrayListErrorsSend.add(e.getMessage());
            } else {
                arrayListErrorsSend.add("IdAnnonce : " + annonceSedex.getIdAnnonceSedex() + refMembre + " ==> "
                        + e.getMessage() + " / " + e.toString());
                JadeThread.logClear();
                annonceSedex.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_ENVOYE.getValue());
                annonceSedex.setTraitement(IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue());
                annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().update(annonceSedex);
            }
        } catch (Exception ex) {
            arrayListErrorsSend.add(ex.getMessage());
            e.printStackTrace();
            // Mise a jour échouée, on ne fait rien...
        }
    }

    /**
     * Création du fichier CSV...
     *
     * @param listRecords
     *            Ligne à écrire dans le fichier
     * @param lineHeader
     *            Entête du fichier
     * @param fileNameWithExt
     *            Nom du fichier avec l'extension
     * @return
     */
    public static String _writeFile(List<StringBuffer> listRecords, String lineHeader, String fileNameWithExt) {
        System.setProperty("line.separator", "\r\n");

        // Create file
        FileWriter fstream;
        BufferedWriter out;
        String file = Jade.getInstance().getPersistenceDir() + fileNameWithExt;
        try {
            fstream = new FileWriter(new File(file));
            out = new BufferedWriter(fstream);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            out.write(lineHeader);
            out.newLine();

            for (StringBuffer currentRecord : listRecords) {
                out.write(currentRecord.toString());
                out.newLine();
            }
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return file;
    }

    /**
     * Zip it
     *
     * @param zipFile
     *            output ZIP file location
     */
    private String _zipFiles(Map<String, AnnonceBuilderReturnInfosContainer> mapReturnInfos, String zipFile) {
        String zipFilename = Jade.getInstance().getPersistenceDir() + zipFile;
        try {
            Map<String, byte[]> myMap = new HashMap<String, byte[]>();
            for (String idCaisse : mapReturnInfos.keySet()) {
                AnnonceBuilderReturnInfosContainer container = mapReturnInfos.get(idCaisse);

                if (!JadeStringUtil.isEmpty(container.getPathToXmlFile())) {
                    String file = container.getPathToXmlFile();
                    String prefix_caisse = idCaisse;
                    try {
                        CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                        cmSearch.setForIdTiersCaisse(idCaisse);
                        cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                        cmSearch.setWhereKey("rcListe");
                        cmSearch.setDefinedSearchSize(0);
                        cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                        if (cmSearch.getSize() > 0) {
                            CaisseMaladie currentCMIndividuel = (CaisseMaladie) cmSearch.getSearchResults()[0];
                            String currentCMNo = currentCMIndividuel.getNumCaisse();
                            String currentCMName = currentCMIndividuel.getNomCaisse();
                            prefix_caisse = currentCMNo + "_" + currentCMName;
                        }
                    } catch (Exception e) {
                        // On n'affichera que l'idTiers dans le nom du fichier
                    }

                    String keyZip = prefix_caisse + "_" + JadeFilenameUtil.extractFilename(file);
                    FileInputStream fstream = null;
                    fstream = new FileInputStream(file);
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine = "";
                    String fullFile = "";
                    while ((strLine = br.readLine()) != null) {
                        fullFile += strLine + System.getProperty("line.separator");
                    }
                    myMap.put(keyZip, fullFile.getBytes());
                }
            }

            OutputStream myZipFile = new FileOutputStream(zipFilename);
            JadeZipUtil.zip(myZipFile, myMap);
        } catch (Exception e) {
            JadeThread.logError(null, "Error while zipping files");
            return null;
        }
        return zipFilename;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#changeStatus(java.lang.String, java.lang.String)
     */
    @Override
    public String changeStatus(String idAnnonce, String newStatusId) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException {
        try {
            if (JadeStringUtil.isBlankOrZero(idAnnonce)) {
                JadeThread.logError("AnnoncesRPServiceImpl#changeStatus", "idAnnonce can't be null !");
            }

            if (JadeStringUtil.isBlankOrZero(newStatusId)) {
                JadeThread.logError("AnnoncesRPServiceImpl#changeStatus", "id new status can't be null !");
            }

            SimpleAnnonceSedex simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                    .read(idAnnonce);

            if (AMStatutAnnonceSedex.ENVOYE.getValue().equals(simpleAnnonceSedex.getStatus())) {
                if (!simpleAnnonceSedex.isNew()) {
                    simpleAnnonceSedex.setStatus(newStatusId);
                    simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                            .update(simpleAnnonceSedex);
                    return simpleAnnonceSedex.getIdAnnonceSedex();
                }
            }
        } catch (Exception e) {
            return "0";
        }

        return "0";
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#changeTraitement(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String changeTraitement(String idAnnonce, String newTraitementId) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException {
        try {
            String errorMsg = "";
            if (JadeStringUtil.isBlankOrZero(idAnnonce)) {
                JadeThread.logError("AnnoncesRPServiceImpl#changeTraitement", "idAnnonce can't be null !");
                errorMsg = "idAnnonce can't be null !";
            }

            if (JadeStringUtil.isBlankOrZero(newTraitementId)) {
                JadeThread.logError("AnnoncesRPServiceImpl#changeTraitement", "id new traitement can't be null !");
                errorMsg = "id new traitement can't be null !";
            }

            SimpleAnnonceSedex simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                    .read(idAnnonce);

            if (AMTraitementsAnnonceSedex.A_TRAITER.getValue().equals(simpleAnnonceSedex.getTraitement())) {
                if (!simpleAnnonceSedex.isNew()) {
                    simpleAnnonceSedex.setTraitement(newTraitementId);
                    simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                            .update(simpleAnnonceSedex);
                } else {
                    JadeThread.logError("AnnoncesRPServiceImpl#changeTraitement",
                            "Aucune annonce trouvé avec l'id " + idAnnonce + "!");
                    errorMsg = "Aucune annonce trouvé avec l'id " + idAnnonce + "!";
                }
            }

            return simpleAnnonceSedex.getIdAnnonceSedex() + "_" + errorMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return "0_" + e.toString();
        }
    }

    @Override
    public String cloneAnnonce(String idAnnonce) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException {

        if (JadeStringUtil.isBlankOrZero(idAnnonce)) {
            JadeThread.logError("AnnoncesRPServiceImpl#changeStatus", "idAnnonce can't be null !");
        }
        try {
            SimpleAnnonceSedex simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                    .read(idAnnonce);

            SimpleAnnonceSedex newSimpleAnnonce = new SimpleAnnonceSedex();
            if (!simpleAnnonceSedex.isNew()
                    && AMStatutAnnonceSedex.ENVOYE.getValue().equals(simpleAnnonceSedex.getStatus())) {
                newSimpleAnnonce.setIdContribuable(simpleAnnonceSedex.getIdContribuable());
                newSimpleAnnonce.setIdDetailFamille(simpleAnnonceSedex.getIdDetailFamille());
                newSimpleAnnonce.setIdTiersCM(simpleAnnonceSedex.getIdTiersCM());
                newSimpleAnnonce.setMessageType(simpleAnnonceSedex.getMessageType());
                newSimpleAnnonce.setMessageSubType(simpleAnnonceSedex.getMessageSubType());
                newSimpleAnnonce.setMessageType(simpleAnnonceSedex.getMessageType());
                newSimpleAnnonce.setTraitement(IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue());
                newSimpleAnnonce.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                // BZ 9195 - SEDEX RP : Permettre la réémission d'une annonce en tout temps
                newSimpleAnnonce.setReferenceIdMessage(simpleAnnonceSedex.getIdAnnonceSedex());
                AmalImplServiceLocator.getSimpleAnnonceSedexService().create(newSimpleAnnonce);
            }

            return newSimpleAnnonce.getIdAnnonceSedex();
        } catch (Exception e) {
            return "0";
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#createAndSendAnnonce(java.lang.String,
     * java.util.ArrayList, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, Object> createAndSendAnnonce(String typeMessage, List<String> selectedIdCaisses,
            String idGroupe, String anneeHistorique, boolean _isSimulation) {

        if (!JadeStringUtil.isEmpty(idGroupe)) {
            selectedIdCaisses = new ArrayList<String>();
            CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
            cmSearch.setForIdTiersGroupe(idGroupe);
            cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
            cmSearch.setWhereKey("rcListeOnlyActifs");
            cmSearch.setDefinedSearchSize(0);
            try {
                cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                for (int iCm = 0; iCm < cmSearch.getSize(); iCm++) {
                    CaisseMaladie currentCM = (CaisseMaladie) cmSearch.getSearchResults()[iCm];
                    if (!selectedIdCaisses.contains(currentCM.getIdTiersCaisse())) {
                        selectedIdCaisses.add(currentCM.getIdTiersCaisse());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        int nbItemsCreated = 0;
        int nbItemsSend = 0;
        arrayListErrorsSend = new ArrayList<String>();
        arrayListErrorsCreation = new ArrayList<String>();
        mapListErrors = new HashMap<String, ArrayList<String>>();
        Map<String, Object> mapReturn = new HashMap<String, Object>();
        isSimulation = _isSimulation;
        if (AMMessagesTypesAnnonceSedex.NOUVELLE_DECISION.getValue().equals(typeMessage)
                || AMMessagesTypesAnnonceSedex.INTERRUPTION.getValue().equals(typeMessage)
                || AMMessagesTypesAnnonceSedex.DEMANDE_RAPPORT_ASSURANCE.getValue().equals(typeMessage)
                || AMMessagesTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue().equals(typeMessage)) {
            // --------------------------------------------------------------------------------------------
            // Traitement nouvelle décision ou interruption (== 5201) ou 'Demande de rapport' (==5202)
            // ou d'une demande de prime tarifaire (5205)
            // --------------------------------------------------------------------------------------------
            // Selection des annonces à créer
            ComplexAnnonceSedexSearch annonceSedexSearchToCreate = this._loadAnnonceToCreate(typeMessage,
                    selectedIdCaisses, anneeHistorique);
            // Création des annonces
            nbItemsCreated = _creationAnnoncesRP(annonceSedexSearchToCreate);

            // On n'envoie pas en mode simulation...
            if (!isSimulation) {
                // Selection des annonces à envoyer
                Map<String, Map<String, List<SimpleAnnonceSedex>>> annoncesToSend = this._loadAnnonceToSend(typeMessage,
                        selectedIdCaisses, anneeHistorique);
                // Envoi des annonces
                nbItemsSend = sendAnnonces(annoncesToSend);
            } else {
                // ...mais on crée le fichier résumé
                String simulationFile = _createSimulationFile(annonceSedexSearchToCreate);
                String files[] = { simulationFile };
                mapReturn.put(AnnonceSedexProcess.ATTACHED_FILES, files);
            }

            // Récupération des infos de retour
            mapReturn.put(AnnonceSedexProcess.ERROR_CREATION, arrayListErrorsCreation);
            mapReturn.put(AnnonceSedexProcess.ERROR_ENVOI, arrayListErrorsSend);
            mapReturn.put(AnnonceSedexProcess.NB_ITEMS_CREATED, nbItemsCreated);
            mapReturn.put(AnnonceSedexProcess.NB_ITEMS_SEND, nbItemsSend);
        } else if (AMMessagesTypesAnnonceSedex.ETAT_DECISIONS.getValue().equals(typeMessage)) {
            // --------------------------------------------------------------------------------------------
            // Traitement annonce 'Etat des décisions'
            // --------------------------------------------------------------------------------------------
            Map<String, AnnonceBuilderReturnInfosContainer> mapReturnInfos = new HashMap<String, AnnonceBuilderReturnInfosContainer>();
            // Initialisation du container qui contiendras les infos utiles à l'annonce
            AnnonceInfosContainer annonceInfos = new AnnonceInfosContainer();
            String dateToday = "";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            dateToday = sdf.format(cal.getTime());
            annonceInfos.setReferenceDay(dateToday);
            String periodeFrom = "01.01." + anneeHistorique;
            String periodeTo = "31.12." + anneeHistorique;
            annonceInfos.setPeriodeFrom(periodeFrom);
            annonceInfos.setPeriodeTo(periodeTo);

            mapEtatDec = new HashMap<String, Object>();
            // Itération sur les caisses afin de créer une annonce par caisse sélectionnée
            for (String idCaisse : selectedIdCaisses) {
                String idSedexCaisse = null;

                // Récupération de l'idSedex et erreur si pas trouvé
                try {
                    idSedexCaisse = AMSedexRPUtil.getSedexIdFromIdTiers(idCaisse);
                } catch (AnnonceSedexException e1) {
                    idSedexCaisse = null;
                }
                if (JadeStringUtil.isBlankOrZero(idSedexCaisse)) {
                    // On ne continue pas plus loin si on n'a pas d'idSedex
                    continue;
                }

                annonceInfos.setRecipientId(idSedexCaisse);

                // Création de l'annonce pour la caisse en cours
                AnnonceBuilderReturnInfosContainer returnInfosContainer = _createAnnonceEtatDecisions(annonceInfos,
                        idCaisse);

                nbItemsCreated = returnInfosContainer.getNbCreation();
                // // Si on a des infos de retour, on le set dans le tableau
                if (returnInfosContainer.getErreurCreationList().size() > 0) {
                    arrayListErrorsCreation.addAll(returnInfosContainer.getErreurCreationList());
                }

                mapReturnInfos.put(idCaisse, returnInfosContainer);
            }

            String fileDecreeInventory = _createListeCSVEtatDecisions(anneeHistorique);

            String[] files = _buildFilesString(fileDecreeInventory, mapReturnInfos);

            mapReturn.put(AnnonceSedexProcess.ATTACHED_FILES, files);
            mapReturn.put(AnnonceSedexProcess.MAP_RETURNINFOS, mapReturnInfos);
            // Récupération des infos de retour
            mapReturn.put(AnnonceSedexProcess.ERROR_CREATION, arrayListErrorsCreation);
            mapReturn.put(AnnonceSedexProcess.ERROR_ENVOI, arrayListErrorsSend);
            mapReturn.put(AnnonceSedexProcess.NB_ITEMS_CREATED, nbItemsCreated);
            mapReturn.put(AnnonceSedexProcess.NB_ITEMS_SEND, nbItemsSend);
        } else {
            JadeThread.logError("AnnonceRPServiceImpl.createAndSendAnnonce()",
                    "Type de message ou action inconnue (" + typeMessage + ")");
        }

        return mapReturn;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#createAnnonce(java.lang.String)
     */
    @Override
    public ComplexAnnonceSedex createAnnonce(String idMessageSedex) {

        try {
            ComplexAnnonceSedexSearch complexAnnonceSedexSearch = new ComplexAnnonceSedexSearch();
            complexAnnonceSedexSearch.setForSDXIdAnnonceSedex(idMessageSedex);
            complexAnnonceSedexSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            complexAnnonceSedexSearch = AmalServiceLocator.getComplexAnnonceSedexService()
                    .search(complexAnnonceSedexSearch);

            if (complexAnnonceSedexSearch.getSize() == 1) {
                _creationAnnoncesRP(complexAnnonceSedexSearch);
                return (ComplexAnnonceSedex) complexAnnonceSedexSearch.getSearchResults()[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#createAnnonces()
     */
    @Override
    public ComplexAnnonceSedexSearch createAnnonces() {
        ComplexAnnonceSedexSearch annonceSedexSearchToCreate = this._loadAnnonceToCreate();
        _creationAnnoncesRP(annonceSedexSearchToCreate);
        return annonceSedexSearchToCreate;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#deleteAnnonce(java.lang.String)
     */
    @Override
    public void deleteAnnonce(String idMessageSedex) throws JadeApplicationException, JadePersistenceException {

        SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
        simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().read(idMessageSedex);

        if (!simpleAnnonceSedex.isNew()
                && (IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue().equals(simpleAnnonceSedex.getStatus())
                        || IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_CREE.getValue()
                                .equals(simpleAnnonceSedex.getStatus())
                        || IAMCodeSysteme.AMStatutAnnonceSedex.ERROR_ENVOYE.getValue()
                                .equals(simpleAnnonceSedex.getStatus()))) {
            AmalImplServiceLocator.getSimpleAnnonceSedexService().delete(simpleAnnonceSedex);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#exportListAnnonces(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String exportListAnnonces(String filters, String order)
            throws JadeApplicationException, JadePersistenceException {
        ComplexAnnonceSedexSearch annonceSedexSearch = _searchAnnoncesForExport(filters, order);
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();
        for (JadeAbstractModel model : annonceSedexSearch.getSearchResults()) {
            ComplexAnnonceSedex annonceSedex = (ComplexAnnonceSedex) model;

            StringBuffer sbCsv = createLineForExport(currentSession, annonceSedex);

            listRecords.add(sbCsv);
        }

        // CSV Line Header
        String lineHeader = createHeaderForExportAnnonceRP();

        return _writeFile(listRecords, lineHeader, "exportAnnoncesRP.csv");
    }

    public static StringBuffer createLineForExport(BSession currentSession, ComplexAnnonceSedex annonceSedex) {
        StringBuffer sbCsv = new StringBuffer();
        sbCsv.append(
                annonceSedex.getSimpleAnnonceSedex().getIdAnnonceSedex() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getSimpleAnnonceSedex().getIdContribuable() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getSimpleAnnonceSedex().getIdDetailFamille() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleAnnonceSedex().getDateMessage() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleAnnonceSedex().getMessageType() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(JadeStringUtil.fillWithZeroes(annonceSedex.getSimpleAnnonceSedex().getMessageSubType(), 6)
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(
                annonceSedex.getSimpleAnnonceSedex().getMessageSubType()) + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getSimpleAnnonceSedex().getMessageEmetteur() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getSimpleAnnonceSedex().getMessageRecepteur() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(currentSession.getCodeLibelle(annonceSedex.getSimpleAnnonceSedex().getStatus())
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(currentSession.getCodeLibelle(annonceSedex.getSimpleAnnonceSedex().getTraitement())
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getSimpleAnnonceSedex().getNumeroDecision() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleAnnonceSedex().getNumeroCourant() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getContribuable().getFamille().getNomPrenom() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleFamille().getNomPrenom() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleFamille().getDateNaissance() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(currentSession.getCodeLibelle(annonceSedex.getSimpleFamille().getPereMereEnfant())
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleFamille().getIsContribuable() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleDetailFamille().getDebutDroit() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleDetailFamille().getFinDroit() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleDetailFamille().getMontantContribution()
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getSimpleDetailFamille().getMontantSupplement() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleDetailFamille().getMontantContributionAvecSupplExtra()
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getSimpleDetailFamille().getNoAssure() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(annonceSedex.getCaisseMaladie().getAdmin().getCodeAdministration()
                + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        sbCsv.append(
                annonceSedex.getCaisseMaladie().getTiers().getDesignation1() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
        return sbCsv;
    }

    public static String createHeaderForExportAnnonceRP() {
        String lineHeader = "";
        lineHeader += "idAnnonceSedex" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "idContribuable" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "idDetailFamille" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Date" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Sous-type" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Sous-type_Libelle" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Emetteur" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Récepteur" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Status" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Traitement" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Numéro_decision" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Numéro_traitement" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "NomPrénom_contribuable" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "NomPrenom_membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "DateNaissance_membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Type_membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "isContribuable" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Debut_Droit" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Fin_Droit" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Contribution" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Supplément" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Total" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "No_assuré" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Num_Caisse_maladie" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Caisse_maladie" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        return lineHeader;
    }

    @Override
    public String exportListAnnoncesReponsePT(String filters, String order) throws JadeApplicationException, JadePersistenceException {

        // Récupération des filtres
        Map<String, String> mapFilterValue = initMapFilterValue(filters);

        // Construction de l'objet de recherche
        ComplexAnnonceSedexSearch annonceSedexSearch = initialiseComplexAnnonceSedexSearch(mapFilterValue, order);

        // On force la recherche des annonces de réponse prime tarifaire
        annonceSedexSearch.setInSDXMessageSubType(Lists.newArrayList(AMMessagesSubTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue()));
        annonceSedexSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        annonceSedexSearch.setForCMTypeDemande(AMTypeDemandeSubside.PC.getValue()); // On veut que les PC

        // Lancement de la recherche
        annonceSedexSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(annonceSedexSearch);

        // Génération du fichier excel
        String filename = genererCsvForListAnnoncesReponsePT(annonceSedexSearch);

        // Envoi du fichier
        sendEmailForListAnnoncesReponsePT(mapFilterValue, filename);

        return "";
    }

    private String genererCsvForListAnnoncesReponsePT(ComplexAnnonceSedexSearch annonceSedexSearch) throws JadeApplicationException, JadePersistenceException {

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();
        for (JadeAbstractModel model : annonceSedexSearch.getSearchResults()) {
            ComplexAnnonceSedex annonceSedex = (ComplexAnnonceSedex) model;

            StringBuffer sbCsv = new StringBuffer();

            PersonneEtendueComplexModel personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService()
                    .read(annonceSedex.getSimpleFamille().getIdTier());
            if ((personneEtendueComplexModel == null) || personneEtendueComplexModel.isNew()) {
                throw new AnnonceSedexException("PersonneEtendueComplexModel not found with idTier : "
                        + annonceSedex.getSimpleFamille().getIdTier());
            }

            sbCsv.append(personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getNomPrenom() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getNomPrenomUpper() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getDateNaissance()+ AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleAnnonceSedex().getMontantPrimeTarifaire() + AnnoncesRPServiceImpl.CSV_SEPARATOR);
            sbCsv.append(annonceSedex.getSimpleFamille().getIdTier());

            // On ajoute que les annonces qui ont des primes tarifaires supérieur à 0
            // si = 0, la réponse est un rejet et on traite pas les rejets.
            BigDecimal montantPrimeTarifaire = new BigDecimal(annonceSedex.getSimpleAnnonceSedex().getMontantPrimeTarifaire());
            if(!(new BigDecimal("0.00")).equals(montantPrimeTarifaire)) {
                listRecords.add(sbCsv);
            }
        }

        // CSV Line Header
        String lineHeader = "";
        lineHeader += "NSS" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "NomPrenom_membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Prenom_membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "DateNaissance_membre" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "Prime_effective" + AnnoncesRPServiceImpl.CSV_SEPARATOR;
        lineHeader += "idTiersBeneficiare";


        String filename = _writeFile(listRecords, lineHeader, "exportAnnoncesReponsePT.csv");
        return filename;
    }

    private void sendEmailForListAnnoncesReponsePT(Map<String, String> mapFilterValue, String filename) {
        String[] filenames = {filename};
        String subject = "Export reponse annonce PT";
        String message = "La génération du fichier a été éffectué avec succès";

        try {
            JadeSmtpClient.getInstance().sendMail(mapFilterValue.get("listeForEmail"), subject, message, filenames);
        } catch (Exception e) {
            JadeThread.logError(
                    "sendEmailForListAnnoncesReponsePT",
                    "Erreur lors de l'envoi du mail du processus de génération des annonces PT par lot : "
                            + e.getMessage());
        }
    }


    /**
     * Retourne un contexte. Si nécessaire il est initialisé
     *
     * @return le contexte
     *
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    public JadeContext getContext() throws Exception {
        if (context == null) {
            context = initContext(getSession()).getContext();
        }
        return context;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#getContribuableListSEDEXAnnonces(java.lang.String)
     */
    @Override
    public ArrayList<SimpleAnnonceSedex> getContribuableListSEDEXAnnonces(String idContribuable)
            throws JadeApplicationException, JadePersistenceException {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#getDetailsAnnonce(java.lang.String)
     */
    @Override
    public StringBuffer getDetailsAnnonce(String idAnnonceSedex) {
        Object o_jaxb = getJaxbElementFromAnnonce(idAnnonceSedex);

        // LinkedHashMap<String, String> mapDetails = new LinkedHashMap<String, String>();
        StringBuffer mapDetails = new StringBuffer();
        try {
            if (o_jaxb == null) {
                mapDetails.append("Aucune donnée pour cette annonce !");
            } else {
                mapDetails = AnnonceRPHandlerFactory.getAnnonceHandler(o_jaxb).getDetailsAnnonce();
            }

        } catch (JadeApplicationException e) {
            e.printStackTrace();
        }

        return mapDetails;
    }

    /**
     * Retourne l'object JAXB de l'annonce dont l'id est passé en paramètre
     *
     * @param idAnnonceSedex
     * @return
     */
    private Object getJaxbElementFromAnnonce(String idAnnonceSedex) {
        try {
            SimpleAnnonceSedex simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                    .read(idAnnonceSedex);

            if (JadeStringUtil.isBlankOrZero(simpleAnnonceSedex.getMessageContent())) {
                return null;
            }

            ByteArrayInputStream is = new ByteArrayInputStream(
                    simpleAnnonceSedex.getMessageContent().getBytes("UTF-8"));

            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5201_000101._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5211_000102._3.Message.class, ch.gdk_cds.xmlns.pv_5211_000103._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5201_000201._3.Message.class, ch.gdk_cds.xmlns.pv_5211_000202._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5211_000203._3.Message.class, ch.gdk_cds.xmlns.pv_5211_000301._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5202_000401._3.Message.class, ch.gdk_cds.xmlns.pv_5212_000402._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5203_000501._3.Message.class, ch.gdk_cds.xmlns.pv_5213_000601._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5214_000701._3.Message.class, ch.gdk_cds.xmlns.pv_5205_000801._3.Message.class,
                    ch.gdk_cds.xmlns.pv_5215_000802._3.Message.class};
            // Class<?>[] addClasses = new Class[] {};
            JAXBServices jaxb = JAXBServices.getInstance();
            Object jaxbElement = jaxb.unmarshal(is, false, false, addClasses);
            return jaxbElement;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected SimpleAnnonceSedex getLastAnnonce(SimpleAnnonceSedexSearch currentSearch,
            SortedMap<Long, SimpleAnnonceSedex> messagesOrderedById) {

        // Y a t-il une décision qui a été confirmé et qui n'a pas été interrompue ?
        // Si oui --> Last = Nouvelle décision
        // Si non --> Last = Interruption
        String numeroDecision = "";
        SimpleAnnonceSedex lastAnnonce = null;

        for (JadeAbstractModel abstractModel : currentSearch.getSearchResults()) {
            SimpleAnnonceSedex simpleAnnonceSedex = (SimpleAnnonceSedex) abstractModel;

            if (simpleAnnonceSedex.getMessageSubType()
                    .equals(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue())
                    && AnnonceBuilderAbstract.isAnnonceConfirmed(simpleAnnonceSedex)) {
                numeroDecision = simpleAnnonceSedex.getNumeroDecision();
                // Dernière annonce == Nouvelle décision
                return simpleAnnonceSedex;
            }
        }

        // On regarde sinon si c'est une interruption
        if (JadeStringUtil.isBlankOrZero(numeroDecision)) {
            Set<Long> messagesOrderedKey = messagesOrderedById.keySet();
            int i = 0;
            do {
                i++;
                SimpleAnnonceSedex lastMessage = messagesOrderedById
                        .get(messagesOrderedKey.toArray()[messagesOrderedKey.size() - i]);

                if (lastMessage.getMessageSubType()
                        .equals(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue())) {
                    numeroDecision = lastMessage.getNumeroDecision();
                }

            } while (JadeStringUtil.isBlankOrZero(numeroDecision) && (i < messagesOrderedById.size()));
        }

        if (!JadeStringUtil.isEmpty(numeroDecision)) {
            for (int iAnnonce = 0; iAnnonce < currentSearch.getSize(); iAnnonce++) {
                SimpleAnnonceSedex currentAnnonce = (SimpleAnnonceSedex) currentSearch.getSearchResults()[iAnnonce];
                if (currentAnnonce.getMessageSubType().equals(AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue())) {
                    if (currentAnnonce.getNumeroDecision().equals(numeroDecision)) {
                        // Dernier = Interruption
                        lastAnnonce = currentAnnonce;
                        break;
                    }
                }
            }
        }
        return lastAnnonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#getListSEDEXAnnonces(java.lang.String)
     */
    @Override
    public ArrayList<SimpleAnnonceSedex> getListSEDEXAnnonces(String idDetailFamille)
            throws JadeApplicationException, JadePersistenceException {
        ArrayList<SimpleAnnonceSedex> arrayListAnnonces = new ArrayList<SimpleAnnonceSedex>();

        SimpleAnnonceSedexSearch annonceSedexSearch = new SimpleAnnonceSedexSearch();
        annonceSedexSearch.setForIdDetailFamille(idDetailFamille);
        annonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(annonceSedexSearch);
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        for (JadeAbstractModel model : annonceSedexSearch.getSearchResults()) {
            SimpleAnnonceSedex annonceSedex = (SimpleAnnonceSedex) model;

            if (AnnonceBuilderAbstract.isAnnonceConfirmed(annonceSedex)) {
                annonceSedex.setAjax_isConfirmed(Boolean.TRUE);
            }

            String libelleStatus = currentSession.getCodeLibelle(annonceSedex.getStatus());
            annonceSedex.setAjax_libelleStatus(libelleStatus);

            boolean isEcourtement = false;
            if (AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue().equals(annonceSedex.getMessageSubType())
                    && AMStatutAnnonceSedex.ENVOYE.getValue().equals(annonceSedex.getStatus())) {
                String beginMonth = AMSedexHelper.getValueFromBalise(annonceSedex, "beginMonth");
                String stopMonth = AMSedexHelper.getValueFromBalise(annonceSedex, "ns2:stopMonth");

                String begin = "01." + JadeStringUtil.substring(beginMonth, 5, 2) + "."
                        + JadeStringUtil.substring(beginMonth, 0, 4);
                String stop = "01." + JadeStringUtil.substring(stopMonth, 5, 2) + "."
                        + JadeStringUtil.substring(stopMonth, 0, 4);

                // Si la date de stop est après la date de début, c'est un écourtement
                if (JadeDateUtil.areDatesEquals(stop, begin) || JadeDateUtil.isDateAfter(stop, begin)) {
                    isEcourtement = true;
                }
            }

            String libelleAnnonceSedex = AMMessagesSubTypesAnnonceSedex
                    .getSubTypeCSLibelle(annonceSedex.getMessageSubType());
            if (isEcourtement) {
                libelleAnnonceSedex += " (écourtement)";
            }
            annonceSedex.setAjax_libelleAnnonce(libelleAnnonceSedex);

            CaisseMaladieSearch caisseMaladieSearch = new CaisseMaladieSearch();
            caisseMaladieSearch.setForIdTiersCaisse(annonceSedex.getIdTiersCM());
            caisseMaladieSearch = AmalServiceLocator.getCaisseMaladieService().search(caisseMaladieSearch);

            if (caisseMaladieSearch.getSize() == 1) {
                CaisseMaladie cm = (CaisseMaladie) caisseMaladieSearch.getSearchResults()[0];
                String cmAffiche = cm.getNumCaisse() + " - " + cm.getNomCaisse();
                annonceSedex.setAjax_nomCM(cmAffiche);
            }

            arrayListAnnonces.add(annonceSedex);
        }

        return arrayListAnnonces;
    }

    protected SortedMap<Long, SimpleAnnonceSedex> getOrderedMapOfAnnoncesSedex(SimpleAnnonceSedexSearch currentSearch) {
        SortedMap<Long, SimpleAnnonceSedex> messagesOrderedById = new TreeMap<Long, SimpleAnnonceSedex>();
        for (int iAnnonce = 0; iAnnonce < currentSearch.getSize(); iAnnonce++) {
            SimpleAnnonceSedex currentAnnonce = (SimpleAnnonceSedex) currentSearch.getSearchResults()[iAnnonce];
            if (currentAnnonce.getMessageSubType().equals(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue())
                    || currentAnnonce.getMessageSubType()
                            .equals(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue())
                    || currentAnnonce.getMessageSubType()
                            .equals(AMMessagesSubTypesAnnonceSedex.REJET_DECISION.getValue())
                    || currentAnnonce.getMessageSubType().equals(AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue())
                    || currentAnnonce.getMessageSubType()
                            .equals(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue())
                    || currentAnnonce.getMessageSubType()
                            .equals(AMMessagesSubTypesAnnonceSedex.REJET_INTERRUPTION.getValue())) {
                messagesOrderedById.put(Long.parseLong(currentAnnonce.getId()), currentAnnonce);
            }
        }
        return messagesOrderedById;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#getSedexXMLLines(java.lang.String)
     */
    @Override
    public String getSedexXMLLines(String idSedexAnnonce, String type)
            throws JadeApplicationException, JadePersistenceException {

        SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
        simpleAnnonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().read(idSedexAnnonce);
        if ("C".equals(type)) {
            return simpleAnnonceSedex.getMessageContent();
        } else if ("H".equals(type)) {
            return simpleAnnonceSedex.getMessageHeader();
        } else {
            return "";
        }
    }

    /**
     * Retourne une session. Si nécessaire elle est initialisée
     *
     * @return la session
     *
     * @throws Exception
     *             Exception levée si la session ne peut être initialisée
     */
    public BSession getSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(AMApplication.DEFAULT_APPLICATION_AMAL)
                    .newSession(userSedex, passSedex);
        }

        return session;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#importMessages(globaz.jade.sedex.message.SedexMessage)
     */
    @Override
    @OnReceive
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException {

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                // ---------------------------------------------------------
                // Contrôle de la réception d'un message groupé
                // ---------------------------------------------------------
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;
                Iterator<SimpleSedexMessage> messagesIterator = currentGroupedMessage.iterator();
                while (messagesIterator.hasNext()) {
                    try {
                        SimpleAnnonceSedex simpleAnnonceSedex = importMessagesSingle(messagesIterator.next());
                    } catch (AnnonceSedexReceptionOnlyException ex) {
                        JadeLogger.info(this, ex.getMessage());
                    }
                }
            } else if (message instanceof SimpleSedexMessage) {
                // ---------------------------------------------------------
                // Contrôle de la réception d'un message simple
                // ---------------------------------------------------------
                SimpleAnnonceSedex simpleAnnonceSedex = importMessagesSingle((SimpleSedexMessage) message);
            } else {
                JadeLogger.error(this,
                        "Une erreur s'est produite pendant la lecture d'une annonce RP : il ne s'agit pas d'un SimpleSedexMessage ou GroupedSedexMessage");
            }
        } catch (Exception e1) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant l'importation d'une annonce RP: " + e1.getMessage());
            throw new JadeApplicationRuntimeException(e1);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     *
     * @param currentSimpleMessage
     * @return
     * @throws MalformedURLException
     * @throws JAXBValidationError
     * @throws JAXBValidationWarning
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private SimpleAnnonceSedex importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws MalformedURLException, JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException,
            IOException, JadeApplicationException, JadePersistenceException {
        Object o_Message = null;
        Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5211_000102._3.Message.class,
                ch.gdk_cds.xmlns.pv_5211_000103._3.Message.class, ch.gdk_cds.xmlns.pv_5211_000202._3.Message.class,
                ch.gdk_cds.xmlns.pv_5211_000203._3.Message.class, ch.gdk_cds.xmlns.pv_5211_000301._3.Message.class,
                ch.gdk_cds.xmlns.pv_5212_000402._3.Message.class, ch.gdk_cds.xmlns.pv_5213_000601._3.Message.class,
                ch.gdk_cds.xmlns.pv_5214_000701._3.Message.class, ch.gdk_cds.xmlns.pv_5215_000802._3.Message.class };

        JAXBServices jaxbs = JAXBServices.getInstance();
        o_Message = jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
        AnnonceHandlerAbstract annonceHandlerAbstract = AnnonceRPHandlerFactory.getAnnonceHandler(o_Message);
        SimpleAnnonceSedex simpleAnnonceSedex = annonceHandlerAbstract.execute();
        return simpleAnnonceSedex;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#initAnnonceDemandeRapport(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void initAnnonceDemandeRapport(String idDetailFamille, String idCaisses, String anneeHistorique)
            throws JadeApplicationException, JadePersistenceException {
        // SimpleDetailFamille sdf = AmalImplServiceLocator.getSimpleDetailFamilleService().read(idDetailFamille);
        List<String> arrayIdCaisses = new ArrayList<String>();
        arrayIdCaisses = JadeStringUtil.tokenize(idCaisses, "|");
        String periodeFrom = "01.01." + anneeHistorique;
        String periodeTo = "31.12." + anneeHistorique;

        for (String idCaisse : arrayIdCaisses) {
            _initAnnonceDemandeRapport(idDetailFamille, idCaisse, periodeFrom, periodeTo);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#initAnnonceInterruption(java.lang.String,
     * java.lang.String)
     */
    @Override
    public SimpleAnnonceSedex initAnnonceInterruption(String idContribuable, String idDetailFamille,
            boolean subsideIsEnabled) throws JadeApplicationException, JadePersistenceException {
        return _initAnnonce(idContribuable, idDetailFamille, AMMessagesTypesAnnonceSedex.INTERRUPTION,
                AMMessagesSubTypesAnnonceSedex.INTERRUPTION, subsideIsEnabled);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#initAnnonceNouvelleDecision(java.lang.String,
     * java.lang.String)
     */
    @Override
    public SimpleAnnonceSedex initAnnonceNouvelleDecision(String idContribuable, String idDetailFamille)
            throws JadeApplicationException, JadePersistenceException {
        return _initAnnonce(idContribuable, idDetailFamille, AMMessagesTypesAnnonceSedex.NOUVELLE_DECISION,
                AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION, false);
    }

    /**
     * Initialise un contexte
     *
     * @param session
     *            session
     * @return le contexte initialisé
     * @throws Exception
     *             Exception levée si le contexte ne peut être initialisé
     */
    private JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(AMApplication.DEFAULT_APPLICATION_AMAL);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    @Override
    public SimpleAnnonceSedex initDecreeStopFromJsp(String idDetailFamille, String idContribuable, String msgSousType,
            String idTiersCaisse) throws AnnonceSedexException, DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
        // 5201 : annonces fréquentes : organes d’exécution ? assureurs-maladie

        _createSimpleAnnonceSedex(simpleAnnonceSedex, idDetailFamille, idContribuable, idTiersCaisse, "5201", msgSousType,
                IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());

        return simpleAnnonceSedex;
    }

    @Override
    public SimpleAnnonceSedex initDecreeStopForDemandePTFromJsp(String idDetailFamille, String idContribuable,
                                                                String idFamille, String anneeHistorique, Boolean membreFamille)
            throws AnnonceSedexException, DetailFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Si "membreFamille" est true, alors on doit créé une annonce pour tous les membres de la famille
        if(membreFamille) {

            // On cherche le detail famille lié
            SimpleDetailFamilleSearch detailFamilleSearch = new SimpleDetailFamilleSearch();
            detailFamilleSearch.setForIdContribuable(idContribuable);
            detailFamilleSearch.setForAnneeHistorique(anneeHistorique);
            AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailFamilleSearch);

            for (JadeAbstractModel modelSimpleDetailFamille : detailFamilleSearch.getSearchResults()) {
                SimpleDetailFamille simpleDetailFamille = (SimpleDetailFamille) modelSimpleDetailFamille;
                genererAnnonceDemandePT(simpleDetailFamille.getIdDetailFamille(), null, null, false);
            }

        } else {
            // Sinon on ne crée que pour le membre de famille spécifique.
            genererAnnonceDemandePT(idDetailFamille, null, null,false);
        }
        return null;
    }


    @Override
    public List<SimpleAnnonceSedex> genererAnnonceDemandePT(String idDetailFamille, String dateDebut, String dateFin, boolean simulation)
            throws DetailFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceSedexException {
        // 1. REcherche des décisions confirmées (et non intérompu)
        List<SimpleAnnonceSedex> allDecisionEnvoyeeConfirmee = _lastAllDecreeConfirmed(idDetailFamille);

        List<SimpleAnnonceSedex> annoncesDemandePT = new ArrayList<>();
        // 2. Pour chaque décision, on créé une annonce de PT
        for (SimpleAnnonceSedex decision : allDecisionEnvoyeeConfirmee) {

            // Vérification si la décision est bien dans la limite fourni
            if(estDansLaLimite(decision.getDateMessage(), dateDebut, dateFin)) {
                // 5205 : demande de prime tarifaire : organes d’exécution ? assureurs-maladie
                SimpleAnnonceSedex simpleAnnonceSedex = new SimpleAnnonceSedex();
                simpleAnnonceSedex =_createSimpleAnnonceSedex(simpleAnnonceSedex, idDetailFamille, decision.getIdContribuable(), decision.getIdTiersCM(),
                            AMMessagesTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue(),
                            AMMessagesSubTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue(),
                            IAMCodeSysteme.AMTraitementsAnnonceSedex.A_TRAITER.getValue(),
                            IAMCodeSysteme.AMStatutAnnonceSedex.INITIAL.getValue());
                annoncesDemandePT.add(simpleAnnonceSedex);

            }
        }

        return annoncesDemandePT;
    }

    public static boolean estDansLaLimite(String unFormattedDateDecision, String unFormattedDateDebut, String unFormattedDateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate dateDecision = LocalDate.parse(unFormattedDateDecision, formatter);

        boolean estDansLaLimite = true;

        if(StringUtils.isNotEmpty(unFormattedDateDebut)){
            LocalDate dateDebut = LocalDate.parse(unFormattedDateDebut, formatter);
            estDansLaLimite = dateDebut.isBefore(dateDecision) || dateDebut.isEqual(dateDecision);
        }
        if(StringUtils.isNotEmpty(unFormattedDateFin)){
            LocalDate dateFin = LocalDate.parse(unFormattedDateFin, formatter);
            estDansLaLimite = estDansLaLimite && (dateFin.isAfter(dateDecision) || dateFin.isEqual(dateDecision));
        }

        return estDansLaLimite;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#sendAnnonces()
     */
    @Override
    public void sendAllAnnonces() {
        // Récupération des annonces à envoyer
        Map<String, Map<String, List<SimpleAnnonceSedex>>> annonceSedexSearch = this._loadAnnonceToSend();

        // Envoi
        sendAnnonces(annonceSedexSearch);
    }

    /**
     * Détermine si il faut effectuer un envoi simple ou groupé et effectue le ou les envois
     *
     * @param mapAnnonceSedexSearch
     * @return
     */
    public int sendAnnonces(Map<String, Map<String, List<SimpleAnnonceSedex>>> mapAnnonceSedexSearch) {
        int nbItemsSend = 0;
        for (String keyMapReceipt : mapAnnonceSedexSearch.keySet()) {
            Map<String, List<SimpleAnnonceSedex>> mapTypes = mapAnnonceSedexSearch.get(keyMapReceipt);
            for (String keyMapTypes : mapTypes.keySet()) {
                List<SimpleAnnonceSedex> listAnnonces = mapTypes.get(keyMapTypes);

                if (listAnnonces.size() == 1) {
                    boolean envoiOk = _sendAnnonce(listAnnonces.get(0));
                    if (envoiOk) {
                        nbItemsSend++;
                    }
                } else {
                    int nb = _sendGroupedAnnonce(listAnnonces);
                    nbItemsSend = nbItemsSend + nb;
                }

            }
        }
        return nbItemsSend;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#setAllAnnoncesManual(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String setAllAnnoncesManual(String filters, String order) {
        try {
            ComplexAnnonceSedexSearch annonceSedexSearch = _searchAnnoncesForExport(filters, order);

            for (JadeAbstractModel modelAnnonce : annonceSedexSearch.getSearchResults()) {
                ComplexAnnonceSedex complexAnnonceSedex = (ComplexAnnonceSedex) modelAnnonce;
                try {

                    if (AMTraitementsAnnonceSedex.A_TRAITER.getValue()
                            .equals(complexAnnonceSedex.getSimpleAnnonceSedex().getTraitement())) {
                        AmalImplServiceLocator.getAnnoncesRPService().changeTraitement(
                                complexAnnonceSedex.getSimpleAnnonceSedex().getIdAnnonceSedex(),
                                AMTraitementsAnnonceSedex.TRAITE_MANU.getValue());
                    }
                } catch (Exception e) {
                    JadeThread.logError("setAllAnnoncesManual",
                            "Erreur mise à jour de l'annonce en traité manuellement ! (idAnnonce : "
                                    + complexAnnonceSedex.getSimpleAnnonceSedex().getIdAnnonceSedex() + ") ==> "
                                    + e.toString());
                }
            }
        } catch (Exception ex) {
            JadeThread.logError("setAllAnnoncesManual", ex.getMessage());
        }

        return "";
    }

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     *
     * @param properties
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    @Setup
    public void setUp(Properties properties)
            throws JadeDecryptionNotSupportedException, JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "Réception message RP AMAL: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: user sedex non renseigné. ");
        }
        userSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "Réception message RP AMAL: pass sedex non renseigné. ");
            throw new IllegalStateException("Réception message RP AMAL: pass sedex non renseigné. ");
        }
        passSedex = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.amal.business.services.sedexRP.AnnoncesRPService#simulateAnnonce()
     */
    @Override
    public void simulateAnnonce(String idMessageSedex, String subTypeReponse) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idMessageSedex)) {
            throw new Exception("idMessageSedex can't be null !");
        }

        if (JadeStringUtil.isBlankOrZero(idMessageSedex)) {
            throw new Exception("type reponse can't be null !");
        }

        SimpleAnnonceSedex sas = AmalImplServiceLocator.getSimpleAnnonceSedexService().read(idMessageSedex);

        if (AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue().equals(sas.getMessageSubType())
                || AMMessagesSubTypesAnnonceSedex.INTERRUPTION.getValue().equals(sas.getMessageSubType())) {

            String dateToday = "";
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            dateToday = sdf.format(cal.getTime());

            SimpleAnnonceSedex sasToCreate = new SimpleAnnonceSedex();
            sasToCreate.setDateMessage(dateToday);
            sasToCreate.setIdContribuable(sas.getIdContribuable());
            sasToCreate.setIdDetailFamille(sas.getIdDetailFamille());
            sasToCreate.setIdTiersCM(sas.getIdTiersCM());
            sasToCreate.setMessageEmetteur(sas.getMessageRecepteur());
            sasToCreate.setMessageRecepteur(sas.getMessageEmetteur());
            sasToCreate.setMessageSubType(subTypeReponse);
            sasToCreate.setMessageType("5211");
            sasToCreate.setNumeroDecision(sas.getNumeroDecision());

            // Recherche du numéro courant suivant...
            SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();
            simpleAnnonceSedexSearch.setForIdTiersCM(sas.getIdTiersCM());
            simpleAnnonceSedexSearch.setOrderKey("orderByNoCourantDesc");
            simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService()
                    .search(simpleAnnonceSedexSearch);

            BigInteger nextNoCourant = null;
            if (simpleAnnonceSedexSearch.getSize() == 0) {
                nextNoCourant = new BigInteger("1");
            } else {
                SimpleAnnonceSedex annonceSedex = (SimpleAnnonceSedex) simpleAnnonceSedexSearch.getSearchResults()[0];
                nextNoCourant = new BigInteger(annonceSedex.getNumeroCourant()).add(new BigInteger("1"));
            }
            sasToCreate.setNumeroCourant(nextNoCourant.toString());
            sasToCreate.setStatus(AMStatutAnnonceSedex.RECU_SIMULE.getValue().toString());
            sasToCreate.setTraitement(AMTraitementsAnnonceSedex.TRAITE_MANU.getValue().toString());
            AmalImplServiceLocator.getSimpleAnnonceSedexService().create(sasToCreate);
        }
    }
}
