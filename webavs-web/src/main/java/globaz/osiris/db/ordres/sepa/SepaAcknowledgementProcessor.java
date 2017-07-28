package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupeManager;
import globaz.osiris.db.ordres.CAOrdreRecouvrement;
import globaz.osiris.db.ordres.CAOrdreRejete;
import globaz.osiris.db.ordres.CAOrdreVersement;
import globaz.osiris.db.ordres.OrdreGroupeWrapper;
import globaz.osiris.db.ordres.sepa.utils.CASepaORConverterUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaOVConverterUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaPain001GroupeOGKey;
import globaz.osiris.db.ordres.sepa.utils.CASepaPain008GroupeOGKey;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final String TRANSACTION_PREFIX_PAIN002 = CASepaOVConverterUtils.INSTRUCTION_ID_PREFIX;
    private static final String TRANSACTION_PREFIX_PAIN008 = CASepaORConverterUtils.INSTRUCTION_ID_PREFIX;
    private BSession session;

    private static final class RemoteAck {
        String remotePath;
        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document document;
    }

    public void findAndProcessAllAcknowledgements(BSession session) throws Exception {
        setSession(session);
        final ChannelSftp client = getClient();

        final String propFolderPain002FromP001 = CAProperties.ISO_SEPA_FTP_002_FROM_PAIN001_FOLDER.getValue();
        final String propFolderPain002FromP008 = CAProperties.ISO_SEPA_FTP_002_FROM_PAIN008_FOLDER.getValue();

        String folderPain002FromP001 = null;
        if (propFolderPain002FromP001.isEmpty()) {
            folderPain002FromP001 = ".";
        } else {
            folderPain002FromP001 = "./" + propFolderPain002FromP001;
        }

        boolean usingPain002FromPain008 = false;

        String folderPain002FromP008 = null;
        if (!propFolderPain002FromP008.isEmpty() && propFolderPain002FromP008.trim().length() > 0) {
            folderPain002FromP008 = "./" + propFolderPain002FromP008;
            usingPain002FromPain008 = true;
        }

        processListFiles(session, client, listFiles(client, folderPain002FromP001), folderPain002FromP001);

        if (usingPain002FromPain008) {
            processListFiles(session, client, listFiles(client, folderPain002FromP008), folderPain002FromP008);
        }
    }

    private void processListFiles(BSession session, ChannelSftp client, String[] listFiles, String foldername)
            throws Exception, PropertiesException {

        List<RemoteAck> remoteAcks = new ArrayList<RemoteAck>();
        Map<String, OrdreGroupeWrapper> ogProcessed = new HashMap<String, OrdreGroupeWrapper>();

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
                Document doc = CAJaxbUtil.parseDocument(bais);

                // 04. Vérifier s’il s’agit d’une réponse pain.002
                if (!NAMESPACE_PAIN002.equals(doc.getDocumentElement().getNamespaceURI())) {
                    LOG.info("the file {} is not of type pain002", originalFilename);
                    continue;
                }

                LOG.info("queuing file with name {}", originalFilename);

                com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack = CAJaxbUtil.unmarshall(doc,
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
                    final String key = ogWrapper.getOrdreGroupe().getNumLivraison();
                    if (ogProcessed.containsKey(key)) {
                        ogProcessed.get(key).addAllReason(ogWrapper.getReasons());
                    } else {
                        ogProcessed.put(key, ogWrapper);
                    }
                    session.getCurrentThreadTransaction().commit();
                    deleteFile(client, remoteAck.remotePath);
                }
            } catch (SepaException e) {
                LOG.error("could not process ack {}", remoteAck.remotePath, e);
            } catch (Exception e) {
                LOG.error("Impossible to save state after process Pain002 file" + e);
                throw e;
            }
        }
        CAListOrdreRejeteProcess listORProcess = new CAListOrdreRejeteProcess();
        listORProcess.addMail(CAProperties.ISO_SEPA_RESPONSABLE_OG_EMAIL.getValue());
        for (OrdreGroupeWrapper ogWrapperTraite : ogProcessed.values()) {
            listORProcess.process(getSession(), ogWrapperTraite.getOrdreGroupe(), ogWrapperTraite.getReasons());
        }
    }

    /**
     * Traite une "quittance" SEPA (aka Acknowledgement) qui informe de la bonne acceptation d'un ordre envoyé
     * précédemment.
     * 
     * @throws Exception
     * 
     * @throws SepaException en cas de souci lors du traitement.
     * 
     * @see com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document
     */
    public void processAcknowledgement(InputStream source) {
        // 03. Vérifier la présence de quittances
        Document doc = CAJaxbUtil.parseDocument(source);

        // 04. Vérifier s’il s’agit d’une réponse pain.002
        if (!NAMESPACE_PAIN002.equals(doc.getDocumentElement().getNamespaceURI())) {
            throw new SepaException("the provided document is not a PAIN002 - Acknowledgement response");
        }
        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack = CAJaxbUtil.unmarshall(doc,
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

                    final String reason = generateORfotKey(ogWrapper.getOrdreGroupe(), messageTypeId, reasons);

                    ogWrapper.addReason(reason);

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

                        String prefixFound = getPrefixTransaction(transactionId);

                        if (prefixFound == null) {
                            LOG.debug(
                                    "Transaction Rejetée - {}/{}\nLa transaction {}  ne commence pas par les préfixes attendus '{}'",
                                    transactionId, orderTxId, transactionId, TRANSACTION_PREFIX_PAIN002 + ","
                                            + TRANSACTION_PREFIX_PAIN008);
                            continue;
                        }

                        String transactionIdNoPrefix = transactionId.substring(prefixFound.length());

                        String idOrdre;
                        if (CAOrdreGroupe.VERSEMENT.equals(ogWrapper.getOrdreGroupe().getTypeOrdreGroupe())) {
                            CAOrdreVersement ov = getOrdreVersement(transactionIdNoPrefix);
                            if (ov == null) {
                                LOG.warn(
                                        "Transaction Rejetée - {}/{}\nLa transaction {} n'a pas pu être trouvée dans la base de données.",
                                        transactionId, orderTxId, transactionId);
                                continue;
                            }

                            if (!StringUtils.equals(ogWrapper.getOrdreGroupe().getIdOrdreGroupe(), ov.getOrdreGroupe()
                                    .getIdOrdreGroupe())) {
                                LOG.error(
                                        "Transaction Rejetée - {}/{}\nLa transaction {} n'appartient pas à l'ordre groupé {}, mais à {}",
                                        transactionId, orderTxId, transactionId, ogWrapper.getOrdreGroupe()
                                                .getIdOrdreGroupe(), ov.getOrdreGroupe().getIdOrdreGroupe());
                                continue;
                            }
                            idOrdre = ov.getIdOrdre();
                        } else {
                            CAOrdreRecouvrement or = getOrdreRecouvrement(transactionIdNoPrefix);
                            if (or == null) {
                                LOG.warn(
                                        "Transaction Rejetée - {}/{}\nLa transaction OR {} n'a pas pu être trouvée dans la base de données.",
                                        transactionId, orderTxId, transactionId);
                                continue;
                            }

                            if (!StringUtils.equals(ogWrapper.getOrdreGroupe().getIdOrdreGroupe(), or.getOrdreGroupe()
                                    .getIdOrdreGroupe())) {
                                LOG.error(
                                        "Transaction Rejetée - {}/{}\nLa transaction OV {} n'appartient pas à l'ordre groupé {}, mais à {}",
                                        transactionId, orderTxId, transactionId, ogWrapper.getOrdreGroupe()
                                                .getIdOrdreGroupe(), or.getOrdreGroupe().getIdOrdreGroupe());
                                continue;
                            }
                            idOrdre = or.getIdOrdre();
                        }

                        // Si l’identification réussit, enregistrer le motif de rejet en base de données
                        List<StatusReasonInformation8CH> cLevelReasons = transactionInformations.getStsRsnInf();
                        for (StatusReasonInformation8CH xxx : cLevelReasons) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setSession(getSession());
                            rejected.setIdOperation(idOrdre);
                            rejected.setIdOrdreGroupe(ogWrapper.getOrdreGroupe().getIdOrdreGroupe());
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

    private String getPrefixTransaction(String transactionId) {
        if (transactionId == null) {
            return null;
        }

        String prefixFound = transactionId.startsWith(TRANSACTION_PREFIX_PAIN002) ? TRANSACTION_PREFIX_PAIN002 : null;

        if (prefixFound == null) {
            prefixFound = transactionId.startsWith(TRANSACTION_PREFIX_PAIN008) ? TRANSACTION_PREFIX_PAIN008 : null;
        }
        return prefixFound;
    }

    private String generateORfotKey(CAOrdreGroupe og, String key, List<StatusReasonInformation8CH> reasons) {
        // extract error code and message once
        List<String> rsnCodes = new ArrayList<String>();
        List<String> rsnProprietarys = new ArrayList<String>();
        List<String> rsnAddInfos = new ArrayList<String>();
        for (StatusReasonInformation8CH rsn : reasons) {
            rsnCodes.add(rsn.getRsn().getCd());
            rsnProprietarys.add(rsn.getRsn().getPrtry());
            rsnAddInfos.add(StringUtils.join(rsn.getAddtlInf(), '/'));
        }
        final String rsnCode = StringUtils.join(rsnCodes, '|');
        final String rsnProprietary = StringUtils.join(rsnProprietarys, '|');
        final String rsnAddInfo = StringUtils.join(rsnAddInfos, '|');

        if (CAOrdreGroupe.VERSEMENT.equals(og.getTypeOrdreGroupe())) {
            // iterate on Operation to create each Error if match the key
            CAOperationOrdreVersement ordreV = null;
            CAOperationOrdreVersementManager mgr = new CAOperationOrdreVersementManager();
            mgr.setSession(getSession());
            mgr.setForIdOrdreGroupe(og.getIdOrdreGroupe());
            mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
            BStatement cursorOpen;
            try {
                cursorOpen = mgr.cursorOpen(getSession().getCurrentThreadTransaction());

                while ((ordreV = (CAOperationOrdreVersement) mgr.cursorReadNext(cursorOpen)) != null) {
                    rejectOnMatchGroupKey(og, key, rsnCode, rsnProprietary, rsnAddInfo, ordreV);
                }
            } catch (Exception e) {
                LOG.error("error in accessing DB on OperationOrdreVersement ", e);
                throw new SepaException("error in accessing DB on OperationOrdreVersement", e);
            }
            mgr.clear();
        } else {
            // iterate on Operation to create each Error if match the key
            CAOperationOrdreRecouvrement ordreR = null;
            CAOperationOrdreRecouvrementManager mgr = new CAOperationOrdreRecouvrementManager();
            mgr.setSession(getSession());
            mgr.setForIdOrdreGroupe(og.getIdOrdreGroupe());
            mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);

            BStatement cursorOpen;
            try {
                cursorOpen = mgr.cursorOpen(getSession().getCurrentThreadTransaction());

                while ((ordreR = (CAOperationOrdreRecouvrement) mgr.cursorReadNext(cursorOpen)) != null) {
                    rejectOnMatchGroupKey(og, key, rsnCode, rsnProprietary, rsnAddInfo, ordreR);
                }
            } catch (Exception e) {
                LOG.error("error in accessing DB on OperationOrdreVersement ", e);
                throw new SepaException("error in accessing DB on OperationOrdreVersement", e);
            }
            mgr.clear();
        }

        return rsnAddInfo;
    }

    /**
     * create an CAOrdreRejete object if the calculated key from record match the watched key given into the pain002
     * description
     * 
     * @param og
     * @param key
     * @param rsnCode
     * @param rsnProprietary
     * @param rsnAddInfo
     * @param ordreV
     * @throws Exception
     */
    private void rejectOnMatchGroupKey(CAOrdreGroupe og, String key, final String rsnCode, final String rsnProprietary,
            final String rsnAddInfo, CAOperationOrdreVersement ordreV) {
        try {
            final CAAdressePaiementFormatter adpf = new CAAdressePaiementFormatter();
            adpf.setAdressePaiement(ordreV.getAdressePaiement());
            CASepaPain001GroupeOGKey messageIdKey = new CASepaPain001GroupeOGKey(ordreV, adpf);
            if (key.equals(messageIdKey.getKeyString())) {
                CAOrdreRejete rejected = new CAOrdreRejete();
                rejected.setSession(getSession());
                rejected.setIdOperation(ordreV.getIdOperation());
                rejected.setIdOrdreGroupe(og.getIdOrdreGroupe());
                rejected.setCode(rsnCode);
                rejected.setProprietary(rsnProprietary);
                rejected.setAdditionalInformations(rsnAddInfo);
                try {
                    rejected.add();
                } catch (Exception e) {
                    throw new SepaException("could not save CAOrdreRejete.", e);
                }
            }
        } catch (Exception e) {
            throw new SepaException("could not verify Blevel GroupKey of OV.", e);
        }
    }

    private void rejectOnMatchGroupKey(CAOrdreGroupe og, String key, final String rsnCode, final String rsnProprietary,
            final String rsnAddInfo, CAOperationOrdreRecouvrement ordreR) {
        try {
            final CAAdressePaiementFormatter adpf = new CAAdressePaiementFormatter();
            adpf.setAdressePaiement(ordreR.getAdressePaiement());

            CASepaPain008GroupeOGKey messageIdKeyCHTA = new CASepaPain008GroupeOGKey(ordreR, adpf,
                    CAFormatRecouvrementISOCHTA.MODE);
            CASepaPain008GroupeOGKey messageIdKeyCHDD = new CASepaPain008GroupeOGKey(ordreR, adpf,
                    CAFormatRecouvrementISOCHDD.MODE);

            if (key.equals(messageIdKeyCHTA.getKeyString()) || key.equals(messageIdKeyCHDD.getKeyString())) {
                CAOrdreRejete rejected = new CAOrdreRejete();
                rejected.setSession(getSession());
                rejected.setIdOperation(ordreR.getIdOperation());
                rejected.setIdOrdreGroupe(og.getIdOrdreGroupe());
                rejected.setCode(rsnCode);
                rejected.setProprietary(rsnProprietary);
                rejected.setAdditionalInformations(rsnAddInfo);
                try {
                    rejected.add();
                } catch (Exception e) {
                    throw new SepaException("could not save CAOrdreRejete.", e);
                }
            }
        } catch (Exception e) {
            throw new SepaException("could not verify Blevel GroupKey of OV.", e);
        }
    }

    private CAOrdreVersement getOrdreVersement(String transactionIdNoPrefix) {

        CAOrdreVersement ordreVersement = new CAOrdreVersement();
        ordreVersement.setSession(getSession());
        ordreVersement.setIdOrdre(transactionIdNoPrefix);

        try {
            ordreVersement.retrieve();
            return ordreVersement;
        } catch (Exception e) {
            throw new SepaException(e.getClass().getName(), e);
        }
    }

    private CAOrdreRecouvrement getOrdreRecouvrement(String transactionIdNoPrefix) {

        CAOrdreRecouvrement ordreRecouvrement = new CAOrdreRecouvrement();
        ordreRecouvrement.setSession(getSession());
        ordreRecouvrement.setIdOrdre(transactionIdNoPrefix);

        try {
            ordreRecouvrement.retrieve();
            return ordreRecouvrement;
        } catch (Exception e) {
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
        if (newStatus != null && !newStatus.isEmpty()) {

            /*** Si le statut de l'ordre est VALIDE, on remplace direct par le statut du XML */
            if (APIOrdreGroupe.ISO_TRANSAC_STATUS_VALIDE.equals(ordre.getIsoCsTransmissionStatutExec())) {
                ordre.setIsoCsTransmissionStatutExec(newStatus);
            } else {

                /*** Si le statut du XML est REJETE, on change directement le statut de l'ordre par REJETE */
                if (APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE.equals(newStatus)) {
                    ordre.setIsoCsTransmissionStatutExec(newStatus);

                    /***
                     * Si le statut du XML est PARTIEL et le statut de l'ordre est différent de REJETE, on change
                     * directement le statut de l'ordre par PARTIEL
                     */
                } else if (APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL.equals(newStatus)
                        && !APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE.equals(ordre.getIsoCsTransmissionStatutExec())) {
                    ordre.setIsoCsTransmissionStatutExec(newStatus);

                    /***
                     * Si le statut du XML est COMPLET et le statut de l'ordre est différent de AUCUN, on change
                     * directement le statut de l'ordre par COMPLET
                     */
                } else if (APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET.equals(newStatus)
                        && APIOrdreGroupe.ISO_TRANSAC_STATUS_AUCUNE.equals(ordre.getIsoCsTransmissionStatutExec())) {
                    ordre.setIsoCsTransmissionStatutExec(newStatus);
                }
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

    @Override
    protected CAProperties getHost() {
        return CAProperties.ISO_SEPA_FTP_HOST;
    }

    @Override
    protected CAProperties getPort() {
        return CAProperties.ISO_SEPA_FTP_PORT;
    }

    @Override
    protected CAProperties getUser() {
        return CAProperties.ISO_SEPA_FTP_USER;
    }

    @Override
    protected CAProperties getPassword() {
        return CAProperties.ISO_SEPA_FTP_PASS;
    }
}
