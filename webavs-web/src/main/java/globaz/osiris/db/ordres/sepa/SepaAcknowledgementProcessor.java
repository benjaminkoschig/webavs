package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupeManager;
import globaz.osiris.db.ordres.CAOrdreRejete;
import globaz.osiris.db.ordres.CAOrdreVersement;
import globaz.osiris.db.ordres.sepa.utils.CASepaGroupeOGKey;
import globaz.osiris.db.ordres.sepa.utils.CASepaOVConverterUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.ChannelSftp;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.CustomerPaymentStatusReportV03CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalGroupInformation20CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalPaymentInformation1CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.PaymentTransactionInformation25CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReason6Choice;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReasonInformation8CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionGroupStatus3Code;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionIndividualStatus3CodeCH;

public class SepaAcknowledgementProcessor extends AbstractSepa {
    private static final Logger LOG = LoggerFactory.getLogger(SepaAcknowledgementProcessor.class);

    public static final String NAMESPACE_PAIN002 = "http://www.six-interbank-clearing.com/de/pain.002.001.03.ch.02.xsd";

    private static final String TRANSACTION_PREFIX = CASepaOVConverterUtils.INSTRUCTION_ID_PREFIX;
    private BSession session;

    /** Connecte sur le ftp cible, dans le folder adapt� � l'envoi de messages SEPA. */
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

    private static final class RemoteAck {
        String remotePath;
        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document document;
    }

    public void findAndProcessAllAcknowledgements(BSession session) throws PropertiesException {
        setSession(session);
        ChannelSftp client = connect(session);
        String[] listFiles;

        Set<CAOrdreGroupe> ogProcessed = new HashSet<CAOrdreGroupe>();
        String folder = CAProperties.ISO_SEPA_FTP_002_FOLDER.getValue();
        String foldername = null;
        if (folder.isEmpty()) {
            foldername = ".";
        } else {
            foldername = "./" + folder;
        }

        // get all available files,
        List<RemoteAck> remoteAcks = new ArrayList<RemoteAck>();

        listFiles = listFiles(client, foldername);

        for (String file : listFiles) {
            String originalFilename = foldername + file;

            if (!originalFilename.toLowerCase().endsWith(".xml")) {
                LOG.debug("skipped non xml file: {}", originalFilename);
                continue;
            }

            ByteArrayOutputStream baos = null;
            ByteArrayInputStream bais = null;

            try {
                baos = new ByteArrayOutputStream();
                retrieveData(client, originalFilename, baos);

                bais = new ByteArrayInputStream(baos.toByteArray());
                Document doc = parseDocument(bais);

                // 04. V�rifier s�il s�agit d�une r�ponse pain.002
                if (!NAMESPACE_PAIN002.equals(doc.getDocumentElement().getNamespaceURI())) {
                    LOG.info("the file {} is not of type pain002");
                    continue;
                }

                LOG.info("queuing file with name {}", originalFilename);

                com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack = unmarshall(doc,
                        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document.class);

                RemoteAck remoteAck = new RemoteAck();
                remoteAck.remotePath = originalFilename;
                remoteAck.document = ack;

                remoteAcks.add(remoteAck);
            } catch (SepaException e) {
                LOG.error("an error occured when processing the file {}: {}", originalFilename, e, e);
            } finally {
                IOUtils.closeQuietly(baos);
                IOUtils.closeQuietly(bais);
            }
        }

        // order by Payment date
        Collections.sort(remoteAcks, new Comparator<RemoteAck>() {
            @Override
            public int compare(RemoteAck o1, RemoteAck o2) {
                if (o1 == o2) {
                    return 0;
                }

                return o1.document.getCstmrPmtStsRpt().getGrpHdr().getCreDtTm()
                        .compare(o2.document.getCstmrPmtStsRpt().getGrpHdr().getCreDtTm());
            }
        });

        // do the job, then delete remote files
        for (RemoteAck remoteAck : remoteAcks) {
            try {
                LOG.info("processing file with name {}", remoteAck.remotePath);
                ogProcessed.add(processAck(remoteAck.document));
                deleteFile(client, remoteAck.remotePath);
            } catch (SepaException e) {
                LOG.error("could not process ack {}", remoteAck.remotePath, e);
            }
        }
        CAListOrdreRejeteProcess listORProcess = new CAListOrdreRejeteProcess();
        listORProcess.addMail(CAProperties.ISO_SEPA_RESPONSABLE_OG_EMAIL.getValue());
        for (CAOrdreGroupe ogTraite : ogProcessed) {
            listORProcess.process(getSession(), ogTraite);
        }
    }

    /**
     * Traite une "quittance" SEPA (aka Acknowledgement) qui informe de la bonne acceptation d'un ordre envoy�
     * pr�c�demment.
     * 
     * @throws SepaException en cas de souci lors du traitement.
     * 
     * @see com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document
     */
    public void processAcknowledgement(InputStream source) {
        // 03. V�rifier la pr�sence de quittances
        Document doc = parseDocument(source);

        // 04. V�rifier s�il s�agit d�une r�ponse pain.002
        if (!NAMESPACE_PAIN002.equals(doc.getDocumentElement().getNamespaceURI())) {
            throw new SepaException("the provided document is not a PAIN002 - Acknowledgement response");
        }
        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack = unmarshall(doc,
                com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document.class);

        processAcknowledgement(ack);
    }

    public CAOrdreGroupe processAcknowledgement(com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack) {
        return processAck(ack);

    }

    private CAOrdreGroupe processAck(com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document acknowledgement) {
        CustomerPaymentStatusReportV03CH paymentStatusReport = acknowledgement.getCstmrPmtStsRpt();

        OriginalGroupInformation20CH orgnlGrpInfAndSts = paymentStatusReport.getOrgnlGrpInfAndSts();
        String messageId = orgnlGrpInfAndSts.getOrgnlMsgId();

        // 05. R�cup�rer l�ordre group� associ� � la quittance
        CAOrdreGroupe ordreGroupe = findOrdreGroupeFromMsgId(messageId);

        // 05. A
        if (ordreGroupe == null) {
            return null;
        }

        // 05. B
        if (APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME.equals(ordreGroupe.getIsoCsOrdreStatutExec())) {
            LOG.warn(
                    "Ordre Group� {} - d�j� confirm�\nUne quittance pour l'Ordre Group� {} - {} a d�j� �t� trait�e. Le status d'execution de l'ordre est � CONFIRME",
                    messageId, messageId, ordreGroupe.getMotif());
        }

        // 05. C

        // 06. Traiter la quittance A-Level
        TransactionGroupStatus3Code grpSts = orgnlGrpInfAndSts.getGrpSts();
        if (grpSts == null) {
            // cf. sp�c: "Si la balise <GrpSts> est absente, on proc�de comme si la r�ponse �tait PART"
            grpSts = TransactionGroupStatus3Code.PART;
        }

        List<StatusReasonInformation8CH> stsRsnInf = orgnlGrpInfAndSts.getStsRsnInf();

        switch (grpSts) {
            case ACCP:
                markOrdreGroupeConfirmed(ordreGroupe, APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET);
                break;
            case ACWC:
                // "Accept�" ou "Accept� avec modifications" sont trait�s de la m�me fa�on!
            case ACTC:
                // "L'ordre est techniquement accept�" -> m�me comportement que ses ptits copains // TODO comprendre ce
                // que cela veut dire!
                break;
            case RJCT:
                // "Rejet�" enti�rement
                markOrdreGroupeConfirmed(ordreGroupe, APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE);
                LOG.info(
                        "Ordre Group� {} - Enti�rement Rejet�\nL'Ordre Group� {} - {} a �t� refus� par l'organisme financier.",
                        messageId, messageId, ordreGroupe.getMotif());
                handleBLevel(paymentStatusReport, ordreGroupe);
                break;
            case PART:
                // "Accept� partiellement"
                markOrdreGroupeConfirmed(ordreGroupe, APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL);
                LOG.info(
                        "Ordre Group� {} - Partiellement Ex�cut�\nL'Ordre Group� {} - {} a �t� partiellement ex�cut� par l'organisme financier.",
                        messageId, messageId, ordreGroupe.getMotif());
                handleBLevel(paymentStatusReport, ordreGroupe);
                break;

            // --------------------------------------------------------
            // FIXME Tous les autres status sont non-impl�ment�s!
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
        return ordreGroupe;

    }

    // 07. Traiter la quittance B-Level (quittance par genre de transaction)
    private void handleBLevel(CustomerPaymentStatusReportV03CH paymentStatusReport, CAOrdreGroupe ordreGroupe) {
        for (OriginalPaymentInformation1CH blevel : paymentStatusReport.getOrgnlPmtInfAndSts()) {
            TransactionGroupStatus3Code status = blevel.getPmtInfSts();
            List<StatusReasonInformation8CH> reasons = blevel.getStsRsnInf();

            /*
             * Si < PmtInfSts > contient la valeur RJCT et que la balise <StsRsnInf> est
             * aussi pr�sente, toutes les transactions du m�me genre doivent �tre
             * invalid�es (�tape 08)
             */
            List<TransactionGroupStatus3Code> waitedStatus = new ArrayList<TransactionGroupStatus3Code>();
            waitedStatus.add(TransactionGroupStatus3Code.RJCT);
            waitedStatus.add(TransactionGroupStatus3Code.PART);

            if (waitedStatus.contains(status)) {
                if (reasons != null && !reasons.isEmpty()) {
                    /*
                     * Si < PmtInfSts > contient la valeur RJCT et que la balise <StsRsnInf> est aussi pr�sente, toutes
                     * les transactions du m�me genre doivent �tre invalid�es (�tape 08)
                     */

                    // 08. Invalider les transactions d�un m�me genre suite � un rejet B-Level
                    String messageTypeId = blevel.getOrgnlPmtInfId();

                    List<APICommonOdreVersement> selectedTransactions = new ArrayList<APICommonOdreVersement>();

                    for (APICommonOdreVersement ordreV : getOpOVfromOG(ordreGroupe)) {
                        try {
                            CASepaGroupeOGKey messageIdKey = new CASepaGroupeOGKey(ordreV);
                            if (messageTypeId.equals(messageIdKey.getKeyString())) {
                                selectedTransactions.add(ordreV);
                            }
                        } catch (Exception e) {
                            throw new SepaException("could not verify Blevel GroupKey of OV.", e);
                        }
                    }

                    if (selectedTransactions.isEmpty()) {
                        LOG.debug(
                                "Ordre Group� {} - Genre de transaction inconnu\nL'Ordre Group� {} - {} ne contient pas d'ordre du type de retour: {}. Impossible de traiter l'annulation des transactions correspondantes.",
                                ordreGroupe.getIdOrdreGroupe(), ordreGroupe.getIdOrdreGroupe(), ordreGroupe.getMotif(),
                                messageTypeId);
                    } else {
                        for (APICommonOdreVersement o : selectedTransactions) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setSession(getSession());
                            rejected.setIdOrdre(o.getIdOperation());

                            StatusReasonInformation8CH rsn = reasons.get(0);
                            rejected.setProprietary(rsn.getRsn().getCd());
                            rejected.setProprietary(rsn.getRsn().getPrtry());
                            rejected.setAdditionalInformations(StringUtils.join(rsn.getAddtlInf(), '\n'));

                            try {
                                rejected.add();
                            } catch (Exception e) {
                                throw new SepaException("could not save CAOrdreRejete.", e);
                            }
                        }
                    }

                } else {
                    /*
                     * Si < PmtInfSts > contient la valeur RJCT et qu�il n�y a pas de balise <StsRsnInf>, cela signifie
                     * que les rejets sont d�crits au niveau des transactions. Dans ce cas, on passe simplement �
                     * l��tape 09.
                     */
                    // 09. Traiter la quittance C-Level (quittance par transaction individuelle)
                    for (PaymentTransactionInformation25CH transactionInformations : blevel.getTxInfAndSts()) {
                        // Seuls les rejets seront trait�s. Le code ACWC (accept� avec changements) est ignor�.
                        TransactionIndividualStatus3CodeCH cLevelStatus = transactionInformations.getTxSts();

                        if (cLevelStatus != TransactionIndividualStatus3CodeCH.RJCT) {
                            continue;
                        }

                        String orderTxId = transactionInformations.getOrgnlEndToEndId();

                        /*
                         * La transaction ne peut pas �tre identifi�e dans les cas suivants :
                         * - La valeur de la balise <OrgnlInstrId>ne commence pas par TR-
                         */
                        String transactionId = transactionInformations.getOrgnlInstrId();
                        if (!transactionId.startsWith(TRANSACTION_PREFIX)) {
                            LOG.debug(
                                    "Transaction Rejet�e - {}/{}\nLa transaction {}  ne commence pas par le pr�fixe attendu '{}'",
                                    transactionId, orderTxId, transactionId, TRANSACTION_PREFIX);
                            continue;
                        }

                        String transactionIdNoPrefix = transactionId.substring(TRANSACTION_PREFIX.length());

                        /* - La transaction n�est pas trouv�e dans CAOPOVP.idOrdre */
                        CAOrdreVersement ov = getOrdreVersement(transactionIdNoPrefix);
                        if (ov == null) {
                            // TODO d�cider du comportement si la transaction n'est pas trouv�e!
                            LOG.warn(
                                    "Transaction Rejet�e - {}/{}\nLa transaction {} n'a pas pu �tre trouv�e dans la base de donn�es.",
                                    transactionId, orderTxId, transactionId);
                            continue;
                        }

                        /* - La transaction est trouv�e mais n�appartient pas � cet ordre group� */
                        if (!StringUtils.equals(ordreGroupe.getIdOrdreGroupe(), ov.getOrdreGroupe().getIdOrdreGroupe())) {
                            LOG.error(
                                    "Transaction Rejet�e - {}/{}\nLa transaction {} n'appartient pas � l'ordre group� {}, mais � {}",
                                    transactionId, orderTxId, transactionId, ordreGroupe.getIdOrdreGroupe(), ov
                                            .getOrdreGroupe().getIdOrdreGroupe());
                            continue;
                        }

                        // OriginalTransactionReference13CH sss = transactionInformations.getOrgnlTxRef();

                        // Si l�identification r�ussit, enregistrer le motif de rejet en base de donn�es
                        List<StatusReasonInformation8CH> cLevelReasons = transactionInformations.getStsRsnInf();
                        for (StatusReasonInformation8CH xxx : cLevelReasons) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setSession(getSession());
                            rejected.setIdOrdre(ov.getIdOrdre());

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
                 * Si la balise <PmtInfSts> contient la valeur ACCP ou ACWC, on passe � la prochaine quittance B-Level <
                 * OrgnlPmtInfAndSts>.
                 */
                // donc r�f
            } else {
                // r�f
            }
        }
    }

    private List<APICommonOdreVersement> getOpOVfromOG(CAOrdreGroupe og) {
        CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
        mgr.setSession(getSession());
        mgr.setForIdOrdreGroupe(og.getIdOrdreGroupe());
        mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new SepaException("could not search for transactions: " + og.getIdOrdreGroupe() + ": " + e, e);
        }
        List<APICommonOdreVersement> ovs = mgr.toList();
        return ovs;
    }

    private CAOrdreVersement getOrdreVersement(String transactionIdNoPrefix) {

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

    private CAOrdreGroupe findOrdreGroupeFromMsgId(String messageId) {
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
