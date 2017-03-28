package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
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
import globaz.pyxis.util.CommonNSSFormater;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.ContentType;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossArrivalType;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossPaymentType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.PaymentType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_DecomptePaiement_5234_402_1;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_401_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
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
    private String idTiersSender;

    public enum PaymentCategoryEnum {
        PAIEMENT_DEBITEUR("1"),
        RP_RETROACTIVE("2"),
        ANNULATION("3");

        private String value;

        public String getValue() {
            return value;
        }

        private PaymentCategoryEnum(String value) {
            this.value = value;
        }
    }

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
            SimpleAnnonceSedexCO simpleAnnonceSedexCO = saveAnnonce(addClasses, message);
            saveXml(addClasses, message, simpleAnnonceSedexCO);

            senderId = message.getHeader().getSenderId();
            idTiersSender = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
            run(message, simpleAnnonceSedexCO);

        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message", e);
        }
    }

    private SimpleAnnonceSedexCO saveAnnonce(Class<?>[] addClasses, Message message) {
        // Sauvegarde de l'annonce dans la table
        HeaderType header = message.getHeader();
        ContentType content = message.getContent();

        try {
            SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(idTiersSender);
            annonceSedexCO.setPeriodeDebut(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossFinalStatement().getStatementStartDate()));
            annonceSedexCO.setPeriodeFin(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getCertificateOfLossFinalStatement().getStatementEndDate()));
            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.ENVOYE.getValue());
            annonceSedexCO.setDateAnnonce(Date.now().getSwissValue());
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();
            return annonceSedexCO;
        } catch (Exception ex) {
            JadeThread.logError("AnnoncesCOReceptionMessage5234_000402_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce du décompte final! (Msg id : " + header.getMessageId()
                            + ") => " + ex.getMessage());
        }

        return null;
    }

    private void saveXml(Class<?>[] addClasses, Message message, SimpleAnnonceSedexCO simpleAnnonceSedexCO)
            throws JAXBException, SAXException, IOException, JAXBValidationError, JAXBValidationWarning,
            JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
    }

    /**
     * @param message
     * @return La liste des personnes non trouvées
     * @throws Exception
     */
    protected void run(Message message, SimpleAnnonceSedexCO simpleAnnonceSedexCO) throws Exception {
        List<CertificateOfLossArrivalType> decomptesFinaux = message.getContent().getCertificateOfLossFinalStatement()
                .getCertificateOfLossArrival();
        List<CertificateOfLossPaymentType> decomptesFinauxPaiement = message.getContent()
                .getCertificateOfLossFinalStatement().getCertificateOfLossPayment();
        Date debutPeriodeAObserver = new Date(message.getContent().getCertificateOfLossFinalStatement()
                .getStatementStartDate().toGregorianCalendar().getTime());
        Date finPeriodeAObserver = new Date(message.getContent().getCertificateOfLossFinalStatement()
                .getStatementEndDate().toGregorianCalendar().getTime());
        Periode periodeAObserver = new Periode(debutPeriodeAObserver.getSwissMonthValue(),
                finPeriodeAObserver.getSwissMonthValue());

        List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteFinal = createLigneDecompte(decomptesFinaux,
                periodeAObserver, simpleAnnonceSedexCO);

        List<SimpleOutputList_DecomptePaiement_5234_402_1> listLigneDecompteFinalPaiement = createLignesPaiement(
                decomptesFinauxPaiement, periodeAObserver);

        File listePrinted = printList(listLigneDecompteFinal, listLigneDecompteFinalPaiement);

        String[] files = new String[1];
        if (listePrinted != null) {
            files[0] = listePrinted.getPath();
        }

        prepareEmail(files);
    }

    protected List<SimpleOutputList_DecomptePaiement_5234_402_1> createLignesPaiement(
            List<CertificateOfLossPaymentType> decomptesFinalPaiement, Periode periodeAObserver)
            throws JadePersistenceException, JadeNoBusinessLogSessionError {

        List<SimpleOutputList_DecomptePaiement_5234_402_1> listLigneDecomptePaiementFinal = new ArrayList<SimpleOutputList_DecomptePaiement_5234_402_1>();

        Montant totalPaiementDebiteur = new Montant(0);
        Montant totalRPRetro = new Montant(0);
        Montant totalAnnulation = new Montant(0);
        Montant totalTotal = new Montant(0);
        for (CertificateOfLossPaymentType decompteFinalPaiement : decomptesFinalPaiement) {
            SimpleOutputList_DecomptePaiement_5234_402_1 ligneDecomptePaiement = new SimpleOutputList_DecomptePaiement_5234_402_1();

            if (decompteFinalPaiement.getDebtor().getDebtorNP() != null) {
                DebtorNPType debiteur = decompteFinalPaiement.getDebtor().getDebtorNP();
                CommonNSSFormater nssFormateur = new CommonNSSFormater();

                String nssDebiteurFormate = "";
                try {
                    nssDebiteurFormate = nssFormateur.format(debiteur.getVn().toString());
                } catch (Exception e) {
                    nssDebiteurFormate = "N/A";
                }

                ligneDecomptePaiement.setNssDebiteur(nssDebiteurFormate);
                ligneDecomptePaiement.setNomPrenomDebiteur(debiteur.getOfficialName() + " " + debiteur.getFirstName());
                TypesOfLossEnum typeActe = getTypeActe(decompteFinalPaiement.getCertificateOfLoss().getTypeOfLoss());
                ligneDecomptePaiement.setTypeActe(typeActe.getCs());

                for (PaymentType paiement : decompteFinalPaiement.getPayment()) {
                    if (PaymentCategoryEnum.PAIEMENT_DEBITEUR.getValue().equals(paiement.getPaymentCategory())) {
                        Montant paiementDebiteur = new Montant(paiement.getTotalAmount());
                        ligneDecomptePaiement.setPaiementDebiteur(paiementDebiteur);
                        totalPaiementDebiteur = totalPaiementDebiteur.add(paiementDebiteur);
                    } else if (PaymentCategoryEnum.RP_RETROACTIVE.getValue().equals(paiement.getPaymentCategory())) {
                        Montant rpRetro = new Montant(paiement.getTotalAmount());
                        ligneDecomptePaiement.setRpRetro(rpRetro);
                        totalRPRetro = totalRPRetro.add(rpRetro);
                    } else if (PaymentCategoryEnum.ANNULATION.getValue().equals(paiement.getPaymentCategory())) {
                        Montant annulation = new Montant(paiement.getTotalAmount());
                        ligneDecomptePaiement.setAnnulation(annulation);
                        totalAnnulation = totalAnnulation.add(annulation);
                    }
                }

                listLigneDecomptePaiementFinal.add(ligneDecomptePaiement);
            } else if (decompteFinalPaiement.getDebtor().getDebtorJP() != null) {
                JadeThread.logWarn(this.getClass().getName(),
                        "Les personnes morales ne sont pas prises en charge par l'application");
            }
        }

        SimpleOutputList_DecomptePaiement_5234_402_1 lignePaiementTotal = new SimpleOutputList_DecomptePaiement_5234_402_1();
        lignePaiementTotal.setNssDebiteur("Total");
        lignePaiementTotal.setNomPrenomDebiteur("");
        lignePaiementTotal.setPaiementDebiteur(totalPaiementDebiteur);
        lignePaiementTotal.setRpRetro(totalRPRetro);
        lignePaiementTotal.setAnnulation(totalAnnulation);
        lignePaiementTotal.setTypeActe(null);
        totalTotal = totalTotal.add(totalPaiementDebiteur).add(totalRPRetro).add(totalAnnulation);
        lignePaiementTotal.setTypeActe(totalTotal.toStringFormat());
        listLigneDecomptePaiementFinal.add(lignePaiementTotal);

        return listLigneDecomptePaiementFinal;
    }

    private File printList(List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteFinaux,
            List<SimpleOutputList_DecomptePaiement_5234_402_1> listLigneDecompteFinalPaiement) {

        if (listLigneDecompteFinaux.isEmpty()) {
            return null;
        }

        if (listLigneDecompteFinalPaiement.isEmpty()) {
            return null;
        }

        Details details = new Details();
        details.add("Reçu le", Date.now().getSwissValue());
        details.newLigne();
        Configuration config = Configurations.buildeDefault();
        config.setRowDecorator(new RowDecorator<Object>() {
            @Override
            public RowStyle decorat(Object outputList, int rowNum, int size) {
                final RowStyle rowStyle = new RowStyle();
                if (rowNum % 2 == 0) {
                    rowStyle.setBackGroundColor(247, 252, 255);
                }

                String nss = "";
                if (outputList instanceof SimpleOutputList_Decompte_5234_401_1) {
                    nss = ((SimpleOutputList_Decompte_5234_401_1) outputList).getNssDebiteur();
                } else if (outputList instanceof SimpleOutputList_DecomptePaiement_5234_402_1) {
                    nss = ((SimpleOutputList_DecomptePaiement_5234_402_1) outputList).getNssDebiteur();
                }

                if ("Total".equals(nss) || "85% du total".equals(nss)) {
                    rowStyle.setBackGroundColor(203, 210, 212);
                }
                return rowStyle;
            }
        });

        String nomCaisseMaladieEmetteur = getNomCaisseMaladie(idTiersSender);

        SimpleOutputListBuilder builderOnglet1 = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("decompteAnnuel").addList(listLigneDecompteFinaux)
                .addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Décompte final").configure(config)
                .addHeaderDetails(details);
        SimpleOutputListBuilder builderOnglet2 = builderOnglet1.jump().addList(listLigneDecompteFinalPaiement)
                .addTitle(nomCaisseMaladieEmetteur, Align.LEFT).addSubTitle("Paiement").addHeaderDetails(details);

        // Onglet2 contient onglet 1
        File file = builderOnglet2.asXls().build();

        return file;
    }

    @Override
    protected void prepareEmail(String[] files) throws Exception {
        sendMail(files, "Décompte final");
    }

    // private void sendMail(String[] files) throws Exception {
    // String subject = "Contentieux Amal : réception des annonces 'Décompte final' effectuée avec succès !";
    // StringBuilder body = new StringBuilder();
    // if (!errors.isEmpty()) {
    // if (errors.size() > 1) {
    // subject = "Contentieux Amal : " + errors.size()
    // + " personnes non connues détectées lors de la réception des annonces de décompte final !";
    // } else {
    // subject =
    // "Contentieux Amal : 1 personne non connue détectée lors de la réception des annonces de décompte final !";
    // }
    // body.append("Liste des personnes non trouvées :\n");
    //
    // for (Entry<String, String> error : errors.entrySet()) {
    // String type = error.getKey();
    // String msg = error.getValue();
    // body.append("   -" + type + " - " + msg + "\n");
    // }
    // }
    //
    // JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
    // body.toString(), files);
    // }
}
