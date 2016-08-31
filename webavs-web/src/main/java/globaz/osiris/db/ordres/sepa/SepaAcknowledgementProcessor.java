package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupeManager;
import globaz.osiris.db.ordres.CAOrdreRejete;
import globaz.osiris.db.ordres.CAOrdreVersement;
import globaz.osiris.db.ordres.CAOrdreVersementManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.CustomerPaymentStatusReportV03CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalGroupInformation20CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalPaymentInformation1CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.PaymentTransactionInformation25CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReason6Choice;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReasonInformation8CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionGroupStatus3Code;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionIndividualStatus3CodeCH;

@ThreadSafe
public class SepaAcknowledgementProcessor extends AbstractSepa {
    private static final Logger LOG = LoggerFactory.getLogger(SepaAcknowledgementProcessor.class);
    private static final String SEPA_FTP_FOLDER = "sepa.ftp.ack.folder";
    public static final String NAMESPACE_PAIN002 = "http://www.six-interbank-clearing.com/de/pain.002.001.03.ch.02.xsd";

    private static final String TRANSACTION_PREFIX = "TR-";

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    private FTPClient connect(BSession session) {
        // try fetching configuration from database
        String login = null;
        String password = null;
        String folder = null;
        Integer port = null;
        String host = null;
        try {
            BApplication app = session.getApplication();
            host = app.getProperty(SEPA_FTP_HOST);
            String sport = app.getProperty(SEPA_FTP_PORT);

            if (StringUtils.isNotBlank(sport)) {
                port = Integer.parseInt(sport);
            }

            login = app.getProperty(SEPA_FTP_USER);
            password = app.getProperty(SEPA_FTP_PASS);
            folder = app.getProperty(SEPA_FTP_FOLDER);
        } catch (Exception e) {
            throw new SepaException("unable to retrieve ftp config: " + e, e);
        }

        // go connect
        FTPClient client = connect(host, port, login, password);

        if (StringUtils.isNotBlank(folder)) {
            try {
                if (!client.changeWorkingDirectory(folder)) {
                    throw new SepaException("unable to move to directoy " + folder);
                }
            } catch (IOException e) {
                throw new SepaException("unable to move to directoy " + folder + ": " + e, e);
            }
        }

        return client;
    }

    public void findAndProcessAllAcknowledgements(BSession session) {
        // BSession session = null; // FIXME trouver une session, on fait comment?

        FTPClient client = connect(session);
        FTPFile[] listFiles;

        try {
            listFiles = client.listFiles();
        } catch (IOException e) {
            throw new SepaException("could not list remote files: " + e, e);
        }

        for (FTPFile file : listFiles) {
            String originalFilename = file.getName();

            if (!originalFilename.toLowerCase().endsWith(".xml")) {
                LOG.debug("skipped non xml file: {}", originalFilename);
                continue;
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
            sendAlertEmail("Alertes concernant l'ordre groupé " + ack.getCstmrPmtStsRpt().getGrpHdr().getMsgId(),
                    StringUtils.join(warnings, "\n\n"));
        }
    }

    private void sendAlertEmail(String title, String body) {
        // TODO implémenter l'envoi d'un mail
        LOG.info("MAIL TO SEND:");
        LOG.info("Title: {}", title);
        LOG.info("Body:\n{}", body);
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
        if (APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME.equals(ordre.getEtat())) {
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
                markOrdreGroupeConfirmed(ordre);
                markAllTransactions(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET);
                break;
            case RJCT:
                // "Rejeté" entièrement
                markOrdreGroupeConfirmed(ordre);
                markAllTransactions(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_REJETE);
                warnings.add("Ordre Groupé "
                        + messageId
                        + " - Entièrement Rejeté\n"
                        + "L'Ordre Groupé "
                        + messageId
                        + " - "
                        + ordre.getMotif()
                        + " a été refusé par l'organisme financier. Les messages d'information suivants ont été inclus dans la réponse:\n\n  -"
                        + StringUtils.join(comments.toArray(), "\n  -"));
                handleBLevel(paymentStatusReport, ordre, warnings);
                break;
            case PART:
                // "Accepté partiellement"
                markOrdreGroupeConfirmed(ordre);
                markAllTransactions(ordre, APIOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL);
                warnings.add("Ordre Groupé "
                        + messageId
                        + " - Partiellement Exécuté\n"
                        + "L'Ordre Groupé "
                        + messageId
                        + " - "
                        + ordre.getMotif()
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
            if (status == TransactionGroupStatus3Code.RJCT) {
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
                        // ??
                    }

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
                        if (!StringUtils.equals(ordreGroupe.getId(), ordre.getOrdreGroupe().getId())) {
                            // TODO décider du comportement si la transaction n'est pas trouvée!
                            warnings.add("Transaction Rejetée - " + transactionId + "/" + orderTxId + "\n"
                                    + "La transaction " + transactionId + " n'appartient pas à l'ordre groupé "
                                    + ordreGroupe.getId() + ", mais à " + ordre.getOrdreGroupe().getId());
                            continue;
                        }

                        // OriginalTransactionReference13CH sss = transactionInformations.getOrgnlTxRef();

                        // Si l’identification réussit, enregistrer le motif de rejet en base de données
                        List<StatusReasonInformation8CH> cLevelReasons = transactionInformations.getStsRsnInf();
                        for (StatusReasonInformation8CH xxx : cLevelReasons) {
                            CAOrdreRejete rejected = new CAOrdreRejete();
                            rejected.setIdOrdre(ordre.getId());

                            StatusReason6Choice rsn = xxx.getRsn();
                            rejected.setCode(rsn.getCd());
                            rejected.setProprietary(rsn.getPrtry());
                            rejected.setAdditionalInformations(StringUtils.join(xxx.getAddtlInf(), '\n'));

                            rejected.save();
                        }
                    }
                } else {
                    /*
                     * Si < PmtInfSts > contient la valeur RJCT et qu’il n’y a pas de balise <StsRsnInf>, cela signifie
                     * que les rejets sont décrits au niveau des transactions. Dans ce cas, on passe simplement à
                     * l’étape 09.
                     */
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
        BSession session = BSessionUtil.getSessionFromThreadContext();
        BTransaction tx = session.getCurrentThreadTransaction();

        CAOrdreVersement ordreVersement = new CAOrdreVersement();
        ordreVersement.setSession(session);
        ordreVersement.setIdOrdre(transactionIdNoPrefix);

        try {
            ordreVersement.retrieve(tx);
            if (tx.hasErrors()) {
                // _addError(tx, session.getLabel("????"));
                throw new SepaException(tx.getErrors().toString());
            }
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
        m.setSession(BSessionUtil.getSessionFromThreadContext());

        m.setForIdOrdreGroupe(ordre.getId());
        try {
            m.find();
        } catch (Exception e) {
            throw new SepaException("could not search for transactions: " + ordre.getId() + ": " + e, e);
        }

        List<CAOrdreVersement> orders = m.toList();
        for (CAOrdreVersement order : orders) {
            // order.set
            // TODO ?
        }
    }

    private void markOrdreGroupeConfirmed(CAOrdreGroupe ordre) {
        ordre.setEtat(APIOrdreGroupe.ISO_ORDRE_STATUS_CONFIRME);

        try {
            ordre.save(); // TODO save à chaque fois?
        } catch (Exception e) {
            throw new SepaException("could not save order: " + ordre.getId() + ": " + e, e);
        }
    }

    private CAOrdreGroupe findOrdreGroupeById(String messageId) {
        // FIXME code-review de ce morceau par un expert JADE pleazzz
        CAOrdreGroupeManager manager = new CAOrdreGroupeManager();
        manager.setSession(BSessionUtil.getSessionFromThreadContext());

        manager.setForIdOrdreGroupe(messageId);

        try {
            manager.find();
        } catch (Exception e) {
            throw new SepaException("unexpected exception searching for a " + CAOrdreGroupe.class.getName() + ": " + e,
                    e);
        }
        CAOrdreGroupe ordre = (CAOrdreGroupe) manager.getFirstEntity();

        return ordre;
    }
}
