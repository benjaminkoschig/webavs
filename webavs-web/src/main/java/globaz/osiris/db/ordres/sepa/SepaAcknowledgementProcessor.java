package globaz.osiris.db.ordres.sepa;

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
import globaz.osiris.db.ordres.OrdreGroupeWrapper;
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

    private static final class RemoteAck {
        String remotePath;
        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document document;
    }

    public void findAndProcessAllAcknowledgements(BSession session) throws PropertiesException {
        setSession(session);
        ChannelSftp client = getClient();
        String[] listFiles;

        Set<OrdreGroupeWrapper> ogProcessed = new HashSet<OrdreGroupeWrapper>();
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

                // 04. Vérifier s’il s’agit d’une réponse pain.002
                if (!NAMESPACE_PAIN002.equals(doc.getDocumentElement().getNamespaceURI())) {
                    LOG.info("the file {} is not of type pain002", originalFilename);
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
                final OrdreGroupeWrapper ogWrapper = processAck(remoteAck.document);
                if (ogWrapper != null && ogWrapper.getOrdreGroupe() != null) {
                    ogProcessed.add(ogWrapper);
                    deleteFile(client, remoteAck.remotePath);
                }
            } catch (SepaException e) {
                LOG.error("could not process ack {}", remoteAck.remotePath, e);
            }
        }
        CAListOrdreRejeteProcess listORProcess = new CAListOrdreRejeteProcess();
        listORProcess.addMail(CAProperties.ISO_SEPA_RESPONSABLE_OG_EMAIL.getValue());
        for (OrdreGroupeWrapper ogWrapperTraite : ogProcessed) {
            listORProcess.process(getSession(), ogWrapperTraite.getOrdreGroupe(), ogWrapperTraite.getReasons());
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

        processAcknowledgement(ack);
    }

    public OrdreGroupeWrapper processAcknowledgement(com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack) {
        return processAck(ack);

    }

    private OrdreGroupeWrapper processAck(com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document acknowledgement) {

        // Balise la plus haute contenant le A-Level, Les B-Level....
        CustomerPaymentStatusReportV03CH paymentStatusReport = acknowledgement.getCstmrPmtStsRpt();

        // Balise Original group information and status du A-Level
        OriginalGroupInformation20CH orgnlGrpInfAndSts = paymentStatusReport.getOrgnlGrpInfAndSts();

        // Le message Id du A-Level qui correspond à notre OG-[id d'un OG]
        String messageId = orgnlGrpInfAndSts.getOrgnlMsgId();

        // Récupérer l’ordre groupé associé à la quittance
        OrdreGroupeWrapper ogWrapper = findOrdreGroupeFromMsgId(messageId);

        // On ignore si ce n'est pas un ordre groupé chez nous
        if (ogWrapper == null || ogWrapper.getOrdreGroupe() == null) {
            return null;
        }

        if (APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME.equals(ogWrapper.getOrdreGroupe().getIsoCsOrdreStatutExec())) {
            LOG.warn(
                    "Ordre Groupé {} - déjà confirmé\nUne quittance pour l'Ordre Groupé {} - {} a déjà été traitée. Le status d'execution de l'ordre est à CONFIRME",
                    messageId, messageId, ogWrapper.getOrdreGroupe().getMotif());
        }

        // Traiter la quittance A-Level
        TransactionGroupStatus3Code grpSts = orgnlGrpInfAndSts.getGrpSts();
        if (grpSts == null) {
            // cf. spéc: "Si la balise <GrpSts> est absente, on procède comme si la réponse était PARTIEL"
            grpSts = TransactionGroupStatus3Code.PART;
        }

        final List<StatusReasonInformation8CH> stsRsnInf = orgnlGrpInfAndSts.getStsRsnInf();

        for (StatusReasonInformation8CH reason : stsRsnInf) {
            ogWrapper.addReason(StringUtils.join(reason.getAddtlInf(), '\n'));
        }

        switch (grpSts) {
            case ACCP: // "Accepté" ou "Accepté avec modifications" ou "Accepté techniquement"
            case ACTC:
            case ACWC:
                markOrdreGroupeTransmissionStatus(ogWrapper.getOrdreGroupe(), APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET);
                break;
            case RJCT: // "Rejeté" entièrement
                markOrdreGroupeTransmissionStatus(ogWrapper.getOrdreGroupe(), APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE);
                LOG.info(
                        "Ordre Groupé {} - Entièrement Rejeté\nL'Ordre Groupé {} - {} a été refusé par l'organisme financier.",
                        messageId, messageId, ogWrapper.getOrdreGroupe().getMotif());
                handleBLevel(paymentStatusReport, ogWrapper);
                break;
            case PART: // "Accepté partiellement"
                LOG.info(
                        "Ordre Groupé {} - Partiellement Exécuté\nL'Ordre Groupé {} - {} a été partiellement exécuté par l'organisme financier.",
                        messageId, messageId, ogWrapper.getOrdreGroupe().getMotif());
                handleBLevel(paymentStatusReport, ogWrapper);
                break;
            case RCVD:
            case PDNG:
            case ACSP:
            case ACSC:
            default:
                throw new AssertionError("could not process status code: " + grpSts);
        }

        return ogWrapper;
    }

    // 07. Traiter la quittance B-Level (quittance par genre de transaction)
    private void handleBLevel(CustomerPaymentStatusReportV03CH paymentStatusReport, OrdreGroupeWrapper ogWrapper) {
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
                markOrdreGroupeTransmissionStatus(ogWrapper.getOrdreGroupe(), APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL);
                if (reasons != null && !reasons.isEmpty()) {
                    /*
                     * Si < PmtInfSts > contient la valeur RJCT et que la balise <StsRsnInf> est aussi présente, toutes
                     * les transactions du même genre doivent être invalidées (étape 08)
                     */

                    // 08. Invalider les transactions d’un même genre suite à un rejet B-Level
                    final String messageTypeId = blevel.getOrgnlPmtInfId().replaceAll(
                            ogWrapper.getOrdreGroupe().getNumLivraison(), "");

                    List<APICommonOdreVersement> selectedTransactions = new ArrayList<APICommonOdreVersement>();

                    for (APICommonOdreVersement ordreV : getOpOVfromOG(ogWrapper.getOrdreGroupe())) {
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
                                "Ordre Groupé {} - Genre de transaction inconnu\nL'Ordre Groupé {} - {} ne contient pas d'ordre du type de retour: {}. Impossible de traiter l'annulation des transactions correspondantes.",
                                ogWrapper.getOrdreGroupe().getIdOrdreGroupe(), ogWrapper.getOrdreGroupe()
                                        .getIdOrdreGroupe(), ogWrapper.getOrdreGroupe().getMotif(), messageTypeId);
                    } else {
                        for (APICommonOdreVersement o : selectedTransactions) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setSession(getSession());
                            rejected.setIdOperation(o.getIdOperation());
                            rejected.setIdOrdreGroupe(ogWrapper.getOrdreGroupe().getIdOrdreGroupe());
                            StatusReasonInformation8CH rsn = reasons.get(0);
                            rejected.setCode(rsn.getRsn().getCd());
                            rejected.setProprietary(rsn.getRsn().getPrtry());
                            rejected.setAdditionalInformations(StringUtils.join(rsn.getAddtlInf(), '\n'));

                            ogWrapper.addReason(StringUtils.join(rsn.getAddtlInf(), '\n'));

                            try {
                                rejected.add();
                            } catch (Exception e) {
                                throw new SepaException("could not save CAOrdreRejete.", e);
                            }
                        }
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
                            LOG.debug(
                                    "Transaction Rejetée - {}/{}\nLa transaction {}  ne commence pas par le préfixe attendu '{}'",
                                    transactionId, orderTxId, transactionId, TRANSACTION_PREFIX);
                            continue;
                        }

                        String transactionIdNoPrefix = transactionId.substring(TRANSACTION_PREFIX.length());

                        /* - La transaction n’est pas trouvée dans CAOPOVP.idOrdre */
                        CAOrdreVersement ov = getOrdreVersement(transactionIdNoPrefix);
                        if (ov == null) {
                            // TODO décider du comportement si la transaction n'est pas trouvée!
                            LOG.warn(
                                    "Transaction Rejetée - {}/{}\nLa transaction {} n'a pas pu être trouvée dans la base de données.",
                                    transactionId, orderTxId, transactionId);
                            continue;
                        }

                        /* - La transaction est trouvée mais n’appartient pas à cet ordre groupé */
                        if (!StringUtils.equals(ogWrapper.getOrdreGroupe().getIdOrdreGroupe(), ov.getOrdreGroupe()
                                .getIdOrdreGroupe())) {
                            LOG.error(
                                    "Transaction Rejetée - {}/{}\nLa transaction {} n'appartient pas à l'ordre groupé {}, mais à {}",
                                    transactionId, orderTxId, transactionId, ogWrapper.getOrdreGroupe()
                                            .getIdOrdreGroupe(), ov.getOrdreGroupe().getIdOrdreGroupe());
                            continue;
                        }

                        // OriginalTransactionReference13CH sss = transactionInformations.getOrgnlTxRef();

                        // Si l’identification réussit, enregistrer le motif de rejet en base de données
                        List<StatusReasonInformation8CH> cLevelReasons = transactionInformations.getStsRsnInf();
                        for (StatusReasonInformation8CH xxx : cLevelReasons) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setSession(getSession());
                            rejected.setIdOperation(ov.getIdOrdre());

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
                markOrdreGroupeTransmissionStatus(ogWrapper.getOrdreGroupe(), APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET);
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

    private void markOrdreGroupeTransmissionStatus(CAOrdreGroupe ordre, String newStatus) {
        // mettre à jour les status de transaction cumulé comme suit
        // .|ACPR status à appliquer sur:
        // A|=vvv aucun
        // C|x=vv complet
        // P|xx=v partiel
        // R|xxx= rejeté
        if (newStatus != null && !newStatus.isEmpty()) {
            // rejeté s'applique toujours
            if (APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE.equals(newStatus)) {
                ordre.setIsoCsTransmissionStatutExec(newStatus);
                // partiel partout sauf sur rejete
            } else if (APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL.equals(newStatus)
                    && !APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE.equals(ordre.getIsoCsTransmissionStatutExec())) {
                ordre.setIsoCsTransmissionStatutExec(newStatus);
                // Complet que sur aucun
            } else if (APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET.equals(newStatus)
                    && APIOrdreGroupe.ISO_TRANSAC_STATUS_AUCUNE.equals(ordre.getIsoCsTransmissionStatutExec())) {
                ordre.setIsoCsTransmissionStatutExec(newStatus);
            }
            // then alway set confirmed and save
            markOrdreGroupeConfirmed(ordre);
        }
    }

    private void markOrdreGroupeConfirmed(CAOrdreGroupe ordre) {
        ordre.setIsoCsOrdreStatutExec(APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME);
        try {
            ordre.update();
        } catch (Exception e) {
            throw new SepaException("could not save order: " + ordre.getIdOrdreGroupe() + ": " + e, e);
        }
    }

    private OrdreGroupeWrapper findOrdreGroupeFromMsgId(String messageId) {
        CAOrdreGroupeManager manager = new CAOrdreGroupeManager();
        manager.setSession(getSession());

        manager.setForIdOrdreGroupe(messageId.substring(CAOrdreGroupe.NUM_LIVRAISON_PERFIX.length()));

        try {
            manager.find();
        } catch (Exception e) {
            throw new SepaException("unexpected exception searching for a " + CAOrdreGroupe.class.getName() + ": " + e,
                    e);
        }

        // First entity peut renvoyer null, alors nous aurons l'ordre groupé wrappe avec un og à null
        return new OrdreGroupeWrapper((CAOrdreGroupe) manager.getFirstEntity());
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
