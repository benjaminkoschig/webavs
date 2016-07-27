package globaz.osiris.db.ordres.sepa;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.CustomerPaymentStatusReportV03CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.OriginalGroupInformation20CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReason6Choice;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.StatusReasonInformation8CH;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.TransactionGroupStatus3Code;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupeManager;

@ThreadSafe
public class SepaAcknowledgementProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(SepaAcknowledgementProcessor.class);

    public static final String CONFIRMED = "CONFIRMED"; // TODO cr�er la valeur + mettre le bon code syst�me

    public static final String NAMESPACE_PAIN002 = "http://www.six-interbank-clearing.com/de/pain.002.001.03.ch.02.xsd";

    private static final String COMPLETED = "COMPLETED"; // TODO mettre le bon code syst�me
    private static final String REJECTED = "REJECTED"; // TODO mettre le bon code syst�me
    private static final String PARTIAL = "PARTIAL"; // TODO mettre le bon code syst�me

    public enum SepaAcknoledgementProcessingStatus {
        OK,
        MESSAGE_NOT_FOUND,
        MESSAGE_ALREADY_CONFIRMED;
    }

    public static /* final */ class SepaException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public SepaException() {
            super();
        }

        public SepaException(String message, Throwable cause) {
            super(message, cause);
        }

        public SepaException(String message) {
            super(message);
        }

        public SepaException(Throwable cause) {
            super(cause);
        }
    }

    private <T> T unmarshallToClass(InputStream source, Class<? extends T> clazz) {
        Document doc = parseDocument(source);
        return unmarshall(doc, clazz);
    }

    private Document parseDocument(InputStream source) {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            doc = documentBuilder.parse(source);
        } catch (ParserConfigurationException e) {
            throw new SepaException("XML Parser error: " + e, e);
        } catch (SAXException e) {
            throw new SepaException("XML Error: " + e, e);
        } catch (IOException e) {
            throw new SepaException("IO error trying to parse source stream: " + e, e);
        }
        return doc;
    }

    // how to convert xml document/node/elements into java objects
    private <T> T unmarshall(Document doc, Class<? extends T> clazz) {
        Unmarshaller unmarshaller;
        try {
            JAXBContext jc = JAXBContext
                    .newInstance(com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document.class);

            unmarshaller = jc.createUnmarshaller();
            unmarshaller.setEventHandler(new DefaultValidationEventHandler());

            return unmarshaller.unmarshal(doc, clazz).getValue();
        } catch (JAXBException e) {
            throw new SepaException(
                    "unable to convert xml document to java object of class " + clazz.getName() + ": " + e, e);
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
        if (!NAMESPACE_PAIN002.equals(doc.getNamespaceURI())) {
            throw new SepaException("the provided document is not a PAIN002 - Acknowledgement response");
        }

        com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document ack = unmarshall(doc,
                com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document.class);

        SepaAcknoledgementProcessingStatus status = processAck(ack);
    }

    private SepaAcknoledgementProcessingStatus processAck(
            com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document acknowledgement) {
        CustomerPaymentStatusReportV03CH paymentStatusReport = acknowledgement.getCstmrPmtStsRpt();

        OriginalGroupInformation20CH orgnlGrpInfAndSts = paymentStatusReport.getOrgnlGrpInfAndSts();
        String messageId = orgnlGrpInfAndSts.getOrgnlMsgId();

        // 05. R�cup�rer l�ordre group� associ� � la quittance
        CAOrdreGroupe ordre = findOrdreGroupeById(messageId);

        // 05. A
        if (ordre == null) {
            return SepaAcknoledgementProcessingStatus.MESSAGE_NOT_FOUND;
        }

        // 05. B
        if (CONFIRMED.equals(ordre.getEtat())) {
            // envoyer un mail d'alerte... le status �tait d�j� confirm�, on a re�u un doublon?
            return SepaAcknoledgementProcessingStatus.MESSAGE_ALREADY_CONFIRMED;
        }

        // 05. C

        // 06. Traiter la quittance A-Level
        TransactionGroupStatus3Code grpSts = orgnlGrpInfAndSts.getGrpSts();
        if (grpSts == null) {
            // cf. sp�c: "Si la balise <GrpSts> est absente, on proc�de comme si la r�ponse �tait PART"
            grpSts = TransactionGroupStatus3Code.PART;
        }

        // TODO On en fait quoi de �a??
        List<StatusReasonInformation8CH> stsRsnInf = orgnlGrpInfAndSts.getStsRsnInf();

        if (stsRsnInf != null && !stsRsnInf.isEmpty()) {
            for (StatusReasonInformation8CH statusReadon : stsRsnInf) {
                StatusReason6Choice reason = statusReadon.getRsn();

                String cd = reason.getCd();
                String prtry = reason.getPrtry();

                for (String addInf : statusReadon.getAddtlInf()) {

                }
            }
        }

        // String x = paymentStatusReport.getOrgnlPmtInfAndSts().

        switch (grpSts) {
            case ACCP:
            case ACWC:
                // "Accept�" ou "Accept� avec modifications" sont trait�s de la m�me fa�on!
            case ACTC:
                // "L'ordre est techniquement accept�" -> m�me comportement que ses ptits copains // TODO comprendre ce
                // que cela veut dire!
                markOrdreGroupeConfirmed(ordre);
                markTransactions(ordre, COMPLETED);
                break;
            case RJCT:
                // "Rejet�" enti�rement
                markOrdreGroupeConfirmed(ordre);
                markTransactions(ordre, REJECTED);
                sendWarningEmail("Ordre Group� " + messageId + " - Enti�rement Rejet�");
                break;
            case PART:
                // "Accept� partiellement"
                markOrdreGroupeConfirmed(ordre);
                markTransactions(ordre, PARTIAL);
                sendWarningEmail("Ordre Group� " + messageId + " - Partiellement Accept�");
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

        return SepaAcknoledgementProcessingStatus.OK;
    }

    private void sendWarningEmail(String string) {
        LOG.info("i should have send an email here: !" + string);
    }

    private void markTransactions(CAOrdreGroupe ordre, String newStatus) {
        // TODO comment retrouver l'ensemble des transaction de cet ordre group�?
    }

    private void markOrdreGroupeConfirmed(CAOrdreGroupe ordre) {
        ordre.setEtat(CONFIRMED);

        try {
            ordre.save(); // TODO save � chaque fois?
        } catch (Exception e) {
            throw new SepaException("could not save order: " + ordre.getId() + ": " + e, e);
        }
    }

    private CAOrdreGroupe findOrdreGroupeById(String messageId) {

        CAOrdreGroupeManager manager = new CAOrdreGroupeManager();
        // FIXME initialiser correctement le manager?
        // manager.setSession(JadeThread.currentContext().);

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
