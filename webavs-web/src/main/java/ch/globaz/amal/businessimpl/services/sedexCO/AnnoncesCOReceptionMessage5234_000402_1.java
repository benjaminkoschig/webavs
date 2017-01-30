package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.db.BSessionUtil;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.ContentType;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossArrivalType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorWithClaimType;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOReceptionException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.RowDecorator;
import ch.globaz.simpleoutputlist.configuration.RowStyle;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOReceptionMessage5234_000402_1 extends AnnoncesCOReceptionMessage5234_000401_1 {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_FINAL = "ch.gdk_cds.xmlns.da_64a_5234_000402._1";
    Map<String, String> errors = new HashMap<String, String>();
    private String idTiersSender;

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     * 
     * @param properties
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    @Override
    @Setup
    public void setUp(Properties properties) throws Exception {

        getUserAndPassSedex(properties);

        JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_FINAL);
        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();
    }

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     * 
     * @param currentSimpleMessage
     * @return
     * @throws JadeSedexMessageNotHandledException
     */
    @Override
    protected void importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws JadeSedexMessageNotHandledException {

        try {
            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.da_64a_5234_000402._1.Message.class };
            jaxbs = JAXBServices.getInstance();
            Message message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            // Sauvegarde du code XML de l'annonce dans la table
            saveXml(addClasses, message);

            senderId = message.getHeader().getSenderId();
            idTiersSender = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
            run(message);
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message", e);
        }
    }

    private void saveXml(Class<?>[] addClasses, Message message) throws JAXBException, SAXException, IOException,
            JAXBValidationError, JAXBValidationWarning, JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
    }

    private void creerAnnonce(SimpleFamille membreFamille, DebtorWithClaimType debiteur, Message message)
            throws JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException,
            JadePersistenceException, AnnonceSedexCOReceptionException {
        try {
            // Sauvegarde de l'annonce dans la table
            HeaderType header = message.getHeader();
            ContentType content = message.getContent();

            SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(AMSedexRPUtil.getIdTiersFromSedexId(header.getSenderId()));
            annonceSedexCO.setIdContribuable(membreFamille.getIdContribuable());
            annonceSedexCO.setIdFamille(membreFamille.getIdFamille());
            annonceSedexCO.setInterets(debiteur.getClaimDebtor().getInterests().toString());
            annonceSedexCO.setFrais(debiteur.getClaimDebtor().getExpenses().toString());
            annonceSedexCO.setTotalCreance(debiteur.getClaimDebtor().getTotalClaim().toString());
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();
        } catch (AnnonceSedexException ase) {
            throw new AnnonceSedexCOReceptionException(ase.getMessage(), ase);
        }
    }

    /**
     * @param message
     * @return La liste des personnes non trouvées
     * @throws Exception
     */
    protected void run(Message message) throws Exception {
        List<CertificateOfLossArrivalType> decomptesFinaux = message.getContent().getCertificateOfLossFinalStatement()
                .getCertificateOfLossArrival();
        Date debutPeriodeAObserver = new Date(message.getContent().getCertificateOfLossFinalStatement()
                .getStatementStartDate().toGregorianCalendar().getTime());
        Date finPeriodeAObserver = new Date(message.getContent().getCertificateOfLossFinalStatement()
                .getStatementEndDate().toGregorianCalendar().getTime());
        Periode periodeAObserver = new Periode(debutPeriodeAObserver.getSwissMonthValue(),
                finPeriodeAObserver.getSwissMonthValue());

        List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteFinal = createLigneDecompte(decomptesFinaux,
                periodeAObserver);

        File listePrinted = printList(listLigneDecompteFinal);

        String[] files = new String[1];
        if (listePrinted != null) {
            files[0] = listePrinted.getPath();
        }

        sendMail(files);
    }

    private void sendMail(String[] files) throws Exception {
        String subject = "Contentieux Amal : réception des annonces de décompte final effectuée avec succès !";
        StringBuilder body = new StringBuilder();
        if (!errors.isEmpty()) {
            if (errors.size() > 1) {
                subject = "Contentieux Amal : " + errors.size()
                        + " personnes non connues détectées lors de la réception des annonces de décompte final !";
            } else {
                subject = "Contentieux Amal : 1 personne non connue détectée lors de la réception des annonces de décompte final !";
            }
            body.append("Liste des personnes non trouvées :\n");

            for (Entry<String, String> error : errors.entrySet()) {
                String type = error.getKey();
                String msg = error.getValue();
                body.append("   -" + type + " - " + msg + "\n");
            }
        }

        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                body.toString(), files);
    }

    private File printList(List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteFinaux) {

        if (listLigneDecompteFinaux.isEmpty()) {
            return null;
        }

        Details details = new Details();
        details.add("Reçu le", Date.now().getSwissValue());
        details.newLigne();
        Configuration config = Configurations.buildeDefault();
        config.setRowDecorator(new RowDecorator<SimpleOutputList_Decompte_5234_401_1>() {
            @Override
            public RowStyle decorat(SimpleOutputList_Decompte_5234_401_1 outputList_Decompte5234_402_1, int rowNum,
                    int size) {
                final RowStyle rowStyle = new RowStyle();
                if (rowNum % 2 == 0) {
                    rowStyle.setBackGroundColor(247, 252, 255);
                }

                if ("Total".equals(outputList_Decompte5234_402_1.getNssDebiteur())
                        || "85% du total".equals(outputList_Decompte5234_402_1.getNssDebiteur())) {
                    rowStyle.setBackGroundColor(203, 210, 212);
                }
                return rowStyle;
            }
        });

        String nomCaisseMaladieEmetteur = getNomCaisseMaladie(idTiersSender);

        SimpleOutputListBuilder builder = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("list5234_00402").addList(listLigneDecompteFinaux)
                .classElementList(SimpleOutputList_Decompte_5234_401_1.class);
        File file = builder.addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Décompte final")
                .configure(config).addHeaderDetails(details).jump().addList(listLigneDecompteFinaux)
                .addSubTitle("Paiement").classElementList(SimpleOutputList_Decompte_5234_401_1.class).asXls().build();

        return file;
    }
}
