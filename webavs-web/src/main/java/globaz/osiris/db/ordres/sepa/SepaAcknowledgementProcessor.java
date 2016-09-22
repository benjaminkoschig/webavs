package globaz.osiris.db.ordres.sepa;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.jcraft.jsch.ChannelSftp;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.CustomerPaymentStatusReportV03CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalGroupInformation20CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalPaymentInformation1CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.PaymentTransactionInformation25CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReason6Choice;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReasonInformation8CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionGroupStatus3Code;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionIndividualStatus3CodeCH;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupeManager;
import globaz.osiris.db.ordres.CAOrdreRejete;
import globaz.osiris.db.ordres.CAOrdreVersement;
import globaz.osiris.db.ordres.CAOrdreVersementManager;

public class SepaAcknowledgementProcessor extends AbstractSepa {
    private static final Logger LOG = LoggerFactory.getLogger(SepaAcknowledgementProcessor.class);

    public static final String NAMESPACE_PAIN002 = "http://www.six-interbank-clearing.com/de/pain.002.001.03.ch.02.xsd";

    /** Where to look to find a ssh private key (legacy file from Jade) */
    public static final String LEGACY_JADE_CONFIG_FILE = "/JadeFsServer.xml";
    public static final String PROTOCOL_NAME = "JadeFsServiceSftp";
    public static final String PRIVATEKEY_NODENAME_PREFIX = "private.key.";

    private static final String TRANSACTION_PREFIX = "OV-";
    private BSession session;
    private String title;
    private String body;

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    private ChannelSftp connect(BSession session) {
        String privateKey = loadPrivateKeyPathFromJadeConfigFile();

        // try fetching configuration from database
        String login = null;
        String password = null;
        Integer port = null;
        String host = null;
        try {
            BApplication app = session.getApplication();
            host = CAProperties.ISO_SEPA_FTP_HOST.getValue();
            String sport = CAProperties.ISO_SEPA_FTP_PORT.getValue();

            if (StringUtils.isNotBlank(sport)) {
                port = Integer.parseInt(sport);
            }

            login = CAProperties.ISO_SEPA_FTP_USER.getValue();
            password = CAProperties.ISO_SEPA_FTP_PASS.getValue();

        } catch (Exception e) {
            throw new SepaException("unable to retrieve ftp config: " + e, e);
        }

        // go connect
        ChannelSftp client = connect(host, port, login, password, privateKey);

        return client;
    }

    protected String loadPrivateKeyPathFromJadeConfigFile() {
        // try fetching a private ssh key from legacy config files, mimic Jade behavior
        InputStream jadeFsServerConfig = getClass().getResourceAsStream(LEGACY_JADE_CONFIG_FILE);

        if (jadeFsServerConfig == null) {
            LOG.info("file {} not found. skipping retrieval of private ssh key for connecting to SFTP",
                    LEGACY_JADE_CONFIG_FILE);
            return null;
        }

        String privateKey = null;

        Document doc = parseDocument(jadeFsServerConfig);
        NodeList allProtocols = doc.getDocumentElement().getElementsByTagName("protocols");

        for (int i = 0; i < allProtocols.getLength(); i++) {
            Element protocols = (Element) allProtocols.item(i);
            NodeList allProtocol = protocols.getElementsByTagName("protocol");

            for (int j = 0; j < allProtocol.getLength(); j++) {
                Element protocol = (Element) allProtocol.item(i);

                if (PROTOCOL_NAME.equals(protocol.getAttribute("name"))) {
                    NodeList protocolChildren = protocol.getChildNodes();

                    for (int k = 0; k < protocolChildren.getLength(); k++) {
                        Node n = protocolChildren.item(k);

                        if (n instanceof Element && n.getNodeName().startsWith(PRIVATEKEY_NODENAME_PREFIX)) {
                            privateKey = StringUtils.trimToNull(n.getTextContent());
                            LOG.info("resolved private ssh key to be {}", privateKey);
                            break;
                        }
                    }
                }

                if (privateKey != null) {
                    break;
                }
            }

            if (privateKey != null) {
                break;
            }
        }

        return privateKey;
    }

    public void findAndProcessAllAcknowledgements(BSession session) throws PropertiesException {

        ChannelSftp client = connect(session);
        String[] listFiles;

        String folder = CAProperties.ISO_SEPA_FTP_002_FOLDER.getValue();
        String foldername = null;
        if (!folder.isEmpty()) {
            foldername = "./" + folder;
        } else {
            foldername = ".";
        }

        listFiles = listFiles(client, foldername);

        for (String file : listFiles) {
            String originalFilename = file;
            // tester si nous somme sur la // plateforme isotest.postfinance // pour les zip
            if (CAProperties.ISO_SEPA_FTP_HOST.getValue().startsWith("isotest")) {
                if (!originalFilename.toLowerCase().endsWith(".zip")) {
                    LOG.debug("skipped non xml file: {}", originalFilename);
                    continue;
                }

            } else {

                if (!originalFilename.toLowerCase().endsWith(".xml")) {
                    LOG.debug("skipped non xml file: {}", originalFilename);
                    continue;
                }
            }
            LOG.info("processing file with name {}", originalFilename);
            ByteArrayOutputStream baos = null;
            ByteArrayInputStream bais = null;

            try {
                baos = new ByteArrayOutputStream();
                retrieveData(client, originalFilename, baos);

                bais = new ByteArrayInputStream(baos.toByteArray());
                processAcknowledgement(bais);

                renameFile(client, originalFilename, originalFilename + ".archived");
            } catch (Exception e) {
                LOG.error("an error occured when processing the file {}: {}", originalFilename, e, e);
            } finally {
                IOUtils.closeQuietly(baos);
                IOUtils.closeQuietly(bais);
            }
        }
    }

    /**
     * Traite une "quittance" SEPA (aka Acknowledgement) qui informe de la bonne acceptation d'un ordre envoyé
     * précédemment.
     *
     * @throws SepaException en cas de souci lors du traitement.
     *
     * @see com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document
     */
    public void processAcknowledgement(InputStream source) {
        // 03. Vérifier la présence de quittances
        Document doc = parseDocument(source);

        // 04. Vérifier s’il s’agit d’une réponse pain.002
        if (!NAMESPACE_PAIN002.equals(doc.getDocumentElement().getNamespaceURI())) {
            throw new SepaException("the provided document is not a PAIN002 - Acknowledgement response");
        }

        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack = unmarshall(doc,
                com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document.class);

        List<String> warnings = new ArrayList<String>();
        processAck(ack, warnings);

        if (!warnings.isEmpty()) {
            sendAlertEmail(
                    "Alertes concernant l'ordre groupé "
                            + ack.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId(),
                    StringUtils.join(warnings, "\n\n"));
        }
    }

    private void sendAlertEmail(String title, String body) {
        // TODO implémenter l'envoi d'un mail
        LOG.info("MAIL TO SEND:");
        this.title = title;
        LOG.info("Title: {}", title);
        this.body = body;
        LOG.info("Body:\n{}", body);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private void processAck(com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document acknowledgement,
            List<String> warnings) {
        CustomerPaymentStatusReportV03CH paymentStatusReport = acknowledgement.getCstmrPmtStsRpt();

        OriginalGroupInformation20CH orgnlGrpInfAndSts = paymentStatusReport.getOrgnlGrpInfAndSts();
        String messageId = orgnlGrpInfAndSts.getOrgnlMsgId();

        // 05. Récupérer l’ordre groupé associé à la quittance
        CAOrdreGroupe ordre = findOrdreGroupeById(messageId);

        // 05. A
        if (ordre == null) {
            return;
        }

        // 05. B
        if (APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME.equals(ordre.getIsoCsOrdreStatutExec())) {
            warnings.add("Ordre Groupé " + messageId + " - déjà confirmé\n" + "Une quittance pour l'Ordre Groupé "
                    + messageId + " - " + ordre.getMotif()
                    + " a déjà été traitée. Le status d'execution de l'ordre est à CONFIRME");
            // envoyer un mail d'alerte... le status était déjà confirmé, on a reçu un doublon?
            return;
        }

        // 05. C

        // 06. Traiter la quittance A-Level
        TransactionGroupStatus3Code grpSts = orgnlGrpInfAndSts.getGrpSts();
        if (grpSts == null) {
            // cf. spéc: "Si la balise <GrpSts> est absente, on procède comme si la réponse était PART"
            grpSts = TransactionGroupStatus3Code.PART;
        }

        List<StatusReasonInformation8CH> stsRsnInf = orgnlGrpInfAndSts.getStsRsnInf();
        List<String> comments = convertAdditionalCommentsToList(stsRsnInf);

        switch (grpSts) {
            case ACCP:
            case ACWC:
                // "Accepté" ou "Accepté avec modifications" sont traités de la même façon!
            case ACTC:
                // "L'ordre est techniquement accepté" -> même comportement que ses ptits copains // TODO comprendre ce
                // que cela veut dire!
                markOrdreGroupeConfirmed(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET);
                markAllTransactions(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET);
                break;
            case RJCT:
                // "Rejeté" entièrement
                markOrdreGroupeConfirmed(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE);
                markAllTransactions(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE);
                warnings.add("Ordre Groupé " + messageId + " - Entièrement Rejeté\n" + "L'Ordre Groupé " + messageId
                        + " - " + ordre.getMotif()
                        + " a été refusé par l'organisme financier. Les messages d'information suivants ont été inclus dans la réponse:\n\n  -"
                        + StringUtils.join(comments.toArray(), "\n  -"));
                handleBLevel(paymentStatusReport, ordre, warnings);
                break;
            case PART:
                // "Accepté partiellement"
                markOrdreGroupeConfirmed(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL);
                markAllTransactions(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL);
                warnings.add("Ordre Groupé " + messageId + " - Partiellement Exécuté\n" + "L'Ordre Groupé " + messageId
                        + " - " + ordre.getMotif()
                        + " a été partiellement exécuté par l'organisme financier. Les messages d'information suivants ont été inclus dans la réponse:\n\n  -"
                        + StringUtils.join(comments.toArray(), "\n  -"));
                handleBLevel(paymentStatusReport, ordre, warnings);
                break;

            // --------------------------------------------------------
            // FIXME Tous les autres status sont non-implémentés!
            case RCVD:
                // break;
            case PDNG:
                // break;
            case ACSP:
                // break;
            case ACSC:
                // break;
            default:
                throw new AssertionError("could not process status code: " + grpSts);
        }
    }

    // 07. Traiter la quittance B-Level (quittance par genre de transaction)
    private void handleBLevel(CustomerPaymentStatusReportV03CH paymentStatusReport, CAOrdreGroupe ordreGroupe,
            List<String> warnings) {
        for (OriginalPaymentInformation1CH blevel : paymentStatusReport.getOrgnlPmtInfAndSts()) {
            TransactionGroupStatus3Code status = blevel.getPmtInfSts();
            List<StatusReasonInformation8CH> reasons = blevel.getStsRsnInf();

            /*
             * Si < PmtInfSts > contient la valeur RJCT et que la balise <StsRsnInf> est
             * aussi présente, toutes les transactions du même genre doivent être
             * invalidées (étape 08)
             */
            List<TransactionGroupStatus3Code> waitedStatus = new ArrayList<TransactionGroupStatus3Code>();
            waitedStatus.add(TransactionGroupStatus3Code.RJCT);
            waitedStatus.add(TransactionGroupStatus3Code.PART);

            if (waitedStatus.contains(status)) {
                if (reasons != null && !reasons.isEmpty()) {
                    /*
                     * Si < PmtInfSts > contient la valeur RJCT et que la balise <StsRsnInf> est aussi présente, toutes
                     * les transactions du même genre doivent être invalidées (étape 08)
                     */

                    // 08. Invalider les transactions d’un même genre suite à un rejet B-Level
                    String messageTypeId = blevel.getOrgnlPmtInfId();

                    List<Object> selectedTransactions = new ArrayList<Object>();
                    if ("GT-01".equals(messageTypeId)) {
                        // TODO GT-01 : sélectionner les virements bancaires ou postaux en suisse liés à l’ordre groupé

                        // ça veut dire quoi "sélectionner les virements xxx"???
                    } else if ("GT-02".equals(messageTypeId)) {
                        // TODO GT-02 : sélectionner les mandats de paiement en suisse
                    } else if ("GT-03".equals(messageTypeId)) {
                        // TODO GT-03 : sélectionner les virements BVR en suisse
                    } else if ("GT-11".equals(messageTypeId)) {
                        // TODO GT-11 : sélectionner les virements bancaires ou postaux internationaux
                    } else if ("GT-12".equals(messageTypeId)) {
                        // TODO GT-12 : sélectionner les mandats de paiements internationaux
                    } else {
                        warnings.add("Ordre Groupé " + ordreGroupe.getIdOrdreGroupe()
                                + " - Genre de transaction inconnu\n" + "L'Ordre Groupé "
                                + ordreGroupe.getIdOrdreGroupe() + " - " + ordreGroupe.getMotif()
                                + " contient un genre de transaction inconnu: " + messageTypeId
                                + ". Impossible de traiter l'annulation des transactions correspondantes.");
                    }

                    for (Object o : selectedTransactions) {
                        // TODO persist as OrdreRejete
                    }

                } else {
                    /*
                     * Si < PmtInfSts > contient la valeur RJCT et qu’il n’y a pas de balise <StsRsnInf>, cela signifie
                     * que les rejets sont décrits au niveau des transactions. Dans ce cas, on passe simplement à
                     * l’étape 09.
                     */
                    // 09. Traiter la quittance C-Level (quittance par transaction individuelle)
                    for (PaymentTransactionInformation25CH transactionInformations : blevel.getTxInfAndSts()) {
                        // Seuls les rejets seront traités. Le code ACWC (accepté avec changements) est ignoré.
                        TransactionIndividualStatus3CodeCH cLevelStatus = transactionInformations.getTxSts();

                        if (cLevelStatus != TransactionIndividualStatus3CodeCH.RJCT) {
                            continue;
                        }

                        String orderTxId = transactionInformations.getOrgnlEndToEndId();

                        /*
                         * La transaction ne peut pas être identifiée dans les cas suivants :
                         * - La valeur de la balise <OrgnlInstrId>ne commence pas par TR-
                         */
                        String transactionId = transactionInformations.getOrgnlInstrId();
                        if (!transactionId.startsWith(TRANSACTION_PREFIX)) {
                            warnings.add("Transaction Rejetée - " + transactionId + "/" + orderTxId + "\n"
                                    + "La transaction " + transactionId + " ne commence pas par le préfixe attendu '"
                                    + TRANSACTION_PREFIX + "'");
                            continue;
                        }

                        String transactionIdNoPrefix = transactionId.substring(TRANSACTION_PREFIX.length());

                        /* - La transaction n’est pas trouvée dans CAOPOVP.idOrdre */
                        CAOrdreVersement ordre = getOrdreVersement(transactionIdNoPrefix);
                        if (ordre == null) {
                            // TODO décider du comportement si la transaction n'est pas trouvée!
                            warnings.add("Transaction Rejetée - " + transactionId + "/" + orderTxId + "\n"
                                    + "La transaction " + transactionId
                                    + " n'a pas pu être trouvée dans la base de données.");
                            continue;
                        }

                        /* - La transaction est trouvée mais n’appartient pas à cet ordre groupé */
                        if (!StringUtils.equals(ordreGroupe.getIdOrdreGroupe(),
                                ordre.getOrdreGroupe().getIdOrdreGroupe())) {
                            // TODO décider du comportement si la transaction n'est pas trouvée!
                            warnings.add("Transaction Rejetée - " + transactionId + "/" + orderTxId + "\n"
                                    + "La transaction " + transactionId + " n'appartient pas à l'ordre groupé "
                                    + ordreGroupe.getIdOrdreGroupe() + ", mais à "
                                    + ordre.getOrdreGroupe().getIdOrdreGroupe());
                            continue;
                        }

                        // OriginalTransactionReference13CH sss = transactionInformations.getOrgnlTxRef();

                        // Si l’identification réussit, enregistrer le motif de rejet en base de données
                        List<StatusReasonInformation8CH> cLevelReasons = transactionInformations.getStsRsnInf();
                        for (StatusReasonInformation8CH xxx : cLevelReasons) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setSession(getSession());
                            rejected.setIdOrdre(ordre.getIdOrdre());

                            StatusReason6Choice rsn = xxx.getRsn();
                            rejected.setCode(rsn.getCd());
                            rejected.setProprietary(rsn.getPrtry());
                            rejected.setAdditionalInformations(StringUtils.join(xxx.getAddtlInf(), '\n'));

                            try {
                                rejected.add();
                            } catch (Exception e) {
                                throw new SepaException("could not save CAOrdreRejete.", e);
                            }
                        }
                    }
                }
            } else if (status == TransactionGroupStatus3Code.ACCP || status == TransactionGroupStatus3Code.ACWC) {
                /*
                 * Si la balise <PmtInfSts> contient la valeur ACCP ou ACWC, on passe à la prochaine quittance B-Level <
                 * OrgnlPmtInfAndSts>.
                 */
                // donc ràf
            } else {
                // ràf
            }
        }
    }

    private CAOrdreVersement getOrdreVersement(String transactionIdNoPrefix) {
        // FIXME code-review de ce morceau par un expert JADE pleazzz

        CAOrdreVersement ordreVersement = new CAOrdreVersement();
        ordreVersement.setSession(getSession());
        ordreVersement.setIdOrdre(transactionIdNoPrefix);

        try {
            ordreVersement.retrieve();
            return ordreVersement;
        } catch (Exception e) {
            // _addError(tx, e.getMessage());
            throw new SepaException(e.getClass().getName(), e);
        }
    }

    private List<String> convertAdditionalCommentsToList(List<StatusReasonInformation8CH> stsRsnInf) {
        List<String> comments = new ArrayList<String>();

        if (stsRsnInf == null) {
            return comments;
        }

        for (StatusReasonInformation8CH statusReadon : stsRsnInf) {
            StatusReason6Choice reason = statusReadon.getRsn();

            String cd = reason.getCd();
            if (StringUtils.isNotBlank(cd)) {
                comments.add(cd);
            }

            String prtry = reason.getPrtry();
            if (StringUtils.isNotBlank(prtry)) {
                comments.add(prtry);
            }

            for (String addInf : statusReadon.getAddtlInf()) {
                if (StringUtils.isNotBlank(addInf)) {
                    comments.add(addInf);
                }
            }
        }

        return comments;
    }

    private void markAllTransactions(CAOrdreGroupe ordre, String newStatus) {
        // FIXME code-review de ce morceau par un expert JADE pleazzz

        CAOrdreVersementManager m = new CAOrdreVersementManager();
        m.setSession(getSession());

        m.setForIdOrdreGroupe(ordre.getIdOrdreGroupe());
        try {
            m.find();
        } catch (Exception e) {
            throw new SepaException("could not search for transactions: " + ordre.getIdOrdreGroupe() + ": " + e, e);
        }

        List<CAOrdreVersement> orders = m.toList();
        for (CAOrdreVersement ov : orders) {
            // TODO
            // order.set();
        }
    }

    private void markOrdreGroupeConfirmed(CAOrdreGroupe ordre, String newStatus) {
        ordre.setIsoCsOrdreStatutExec(APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME);
        if (newStatus != null && !newStatus.isEmpty()) {
            ordre.setIsoCsTransmissionStatutExec(newStatus);
        }
        try {
            ordre.update();
        } catch (Exception e) {
            throw new SepaException("could not save order: " + ordre.getIdOrdreGroupe() + ": " + e, e);
        }
    }

    private CAOrdreGroupe findOrdreGroupeById(String messageId) {
        CAOrdreGroupeManager manager = new CAOrdreGroupeManager();
        manager.setSession(getSession());

        manager.setForIdOrdreGroupe(messageId.substring(CAOrdreGroupe.NUM_LIVRAISON_PERFIX.length()));

        try {
            manager.find();
        } catch (Exception e) {
            throw new SepaException("unexpected exception searching for a " + CAOrdreGroupe.class.getName() + ": " + e,
                    e);
        }
        CAOrdreGroupe ordre = (CAOrdreGroupe) manager.getFirstEntity();

        return ordre;
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
