package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.db.BSessionUtil;
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
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.util.CommonNSSFormater;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
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
import ch.gdk_cds.xmlns.da_64a_common._1.ClaimInsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOReceptionException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_401_1;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_402_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Taux;
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

        List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteFinalOngletPaiement = createLignesPaiement(
                decomptesFinaux, periodeAObserver);

        File listePrinted = printList(listLigneDecompteFinal);

        String[] files = new String[1];
        if (listePrinted != null) {
            files[0] = listePrinted.getPath();
        }

        sendMail(files);
    }

    protected List<SimpleOutputList_Decompte_5234_401_1> createLignesPaiement(
            List<CertificateOfLossArrivalType> decomptesTrimestriels, Periode periodeAObserver)
            throws JadePersistenceException, JadeNoBusinessLogSessionError {

        List<SimpleOutputList_Decompte_5234_401_1> listLigneDecompteTrimestriel = new ArrayList<SimpleOutputList_Decompte_5234_401_1>();

        Montant totalMontantPrime = new Montant(0);
        Montant totalMontantParticipation = new Montant(0);
        Montant totalInterets = new Montant(0);
        Montant totalFrais = new Montant(0);
        Montant totalTotal = new Montant(0);
        for (CertificateOfLossArrivalType decompteTrim : decomptesTrimestriels) {
            SimpleOutputList_Decompte_5234_401_1 ligneDecompte = new SimpleOutputList_Decompte_5234_401_1();

            if (decompteTrim.getDebtorWithClaim().getDebtor().getDebtorNP() != null) {
                DebtorNPType debiteur = decompteTrim.getDebtorWithClaim().getDebtor().getDebtorNP();
                CommonNSSFormater nssFormateur = new CommonNSSFormater();

                String nssDebiteurFormate = "";
                try {
                    nssDebiteurFormate = nssFormateur.format(debiteur.getVn().toString());
                } catch (Exception e) {
                    nssDebiteurFormate = "N/A";
                }

                FamilleContribuable familleContribuable = searchPersonne(nssDebiteurFormate, periodeAObserver);
                if (familleContribuable != null) {

                    List<InsuredPersonWithClaimType> listAssures = decompteTrim.getInsuredPersonWithClaim();
                    int cptPersonne = 0;
                    for (InsuredPersonWithClaimType insuredPersonWithClaimType : listAssures) {
                        ligneDecompte = new SimpleOutputList_Decompte_5234_401_1();
                        if (cptPersonne == 0) {
                            ligneDecompte.setNssDebiteur(nssDebiteurFormate);

                            ligneDecompte.setNomPrenomDebiteur(debiteur.getOfficialName() + " "
                                    + debiteur.getFirstName());

                            TypesOfLossEnum typeActe = getTypeActe(decompteTrim.getCertificateOfLoss().getTypeOfLoss());
                            ligneDecompte.setTypeActe(typeActe.getValue());

                            Montant interets = new Montant(decompteTrim.getDebtorWithClaim().getClaimDebtor()
                                    .getInterests());
                            ligneDecompte.setInterets(interets);
                            totalInterets = totalInterets.add(interets);

                            Montant frais = new Montant(decompteTrim.getDebtorWithClaim().getClaimDebtor()
                                    .getExpenses());
                            ligneDecompte.setFrais(frais);
                            totalFrais = totalFrais.add(frais);

                            Montant total = new Montant(decompteTrim.getDebtorWithClaim().getClaimDebtor()
                                    .getTotalClaim());
                            ligneDecompte.setTotal(total);
                            totalTotal = totalTotal.add(total);
                        }

                        InsuredPersonType insuredPersonne = insuredPersonWithClaimType.getInsuredPerson();
                        String nssAssureFormate = "";
                        try {
                            nssAssureFormate = nssFormateur.format(String.valueOf(insuredPersonne.getVn()));
                        } catch (Exception e) {
                            nssAssureFormate = "N/A";
                        }
                        ligneDecompte.setNssAssure(nssAssureFormate);

                        ligneDecompte.setNomPrenomAssure(insuredPersonne.getOfficialName() + " "
                                + insuredPersonne.getFirstName());

                        ClaimInsuredPersonType premium = insuredPersonWithClaimType.getPremium();
                        ClaimInsuredPersonType costSharing = insuredPersonWithClaimType.getCostSharing();
                        Periode periodePremiumOrSharing = null;
                        if (costSharing != null) {
                            periodePremiumOrSharing = new Periode(
                                    getDateFromXmlToString(costSharing.getClaimStartDate()),
                                    getDateFromXmlToString(costSharing.getClaimEndDate()));
                            ligneDecompte.setSharingPeriode(periodePremiumOrSharing);
                            Montant sharingMontant = new Montant(costSharing.getClaimAmount());
                            ligneDecompte.setSharingMontant(sharingMontant);
                            totalMontantParticipation = totalMontantParticipation.add(sharingMontant);
                        }

                        if (premium != null) {
                            periodePremiumOrSharing = new Periode(getDateFromXmlToString(premium.getClaimStartDate()),
                                    getDateFromXmlToString(premium.getClaimEndDate()));
                            ligneDecompte.setPrimePeriode(periodePremiumOrSharing);
                            Montant primeMontant = new Montant(premium.getClaimAmount());
                            ligneDecompte.setPrimeMontant(primeMontant);
                            totalMontantPrime = totalMontantPrime.add(primeMontant);
                        }

                        StringBuilder colonneMessage = getColumnMessage(familleContribuable, periodePremiumOrSharing,
                                decompteTrim.getDebtorWithClaim().getClaimDebtor());

                        ligneDecompte.setMessage(colonneMessage.toString());
                        cptPersonne++;
                        listLigneDecompteTrimestriel.add(ligneDecompte);
                    }
                }
                // creerAnnonce(familleContribuable.getSimpleFamille(), decompteTrim.getDebtorWithClaim(), message);
            } else if (decompteTrim.getDebtorWithClaim().getDebtor().getDebtorJP() != null) {
                JadeThread.logWarn(this.getClass().getName(),
                        "Les personnes morales ne sont pas prises en charge par l'application");
            }
        }

        SimpleOutputList_Decompte_5234_402_1 ligneTotal = new SimpleOutputList_Decompte_5234_402_1();
        ligneTotal.setNssDebiteur("Total");
        ligneTotal.setNomPrenomDebiteur("");
        ligneTotal.setTypeActe("");
        ligneTotal.setNssAssure("");
        ligneTotal.setNomPrenomAssure("");
        ligneTotal.setPrimePeriode(null);
        ligneTotal.setPrimeMontant(totalMontantPrime);
        ligneTotal.setSharingPeriode(null);
        ligneTotal.setSharingMontant(totalMontantParticipation);
        ligneTotal.setInterets(totalInterets);
        ligneTotal.setFrais(totalFrais);
        ligneTotal.setTotal(totalTotal);
        listLigneDecompteTrimestriel.add(ligneTotal);

        SimpleOutputList_Decompte_5234_402_1 ligneTotal85 = new SimpleOutputList_Decompte_5234_402_1();
        ligneTotal85.setNssDebiteur("85% du total");
        ligneTotal85.setNomPrenomDebiteur("");
        ligneTotal85.setTypeActe("");
        ligneTotal85.setNssAssure("");
        ligneTotal85.setNomPrenomAssure("");
        ligneTotal85.setPrimePeriode(null);
        Taux taux85 = new Taux(85);
        Montant totalMontantPrime85 = totalMontantPrime.multiply(taux85).normalize();
        ligneTotal85.setPrimeMontant(totalMontantPrime85);
        ligneTotal85.setSharingPeriode(null);
        Montant totalMontantParticipation85 = totalMontantParticipation.multiply(taux85).normalize();
        ligneTotal85.setSharingMontant(totalMontantParticipation85);
        Montant totalInterets85 = totalInterets.multiply(taux85).normalize();
        ligneTotal85.setInterets(totalInterets85);
        Montant totalFrais85 = totalFrais.multiply(taux85).normalize();
        ligneTotal85.setFrais(totalFrais85);
        Montant totalTotal85 = totalTotal.multiply(taux85).normalize();
        ligneTotal85.setTotal(totalTotal85);
        listLigneDecompteTrimestriel.add(ligneTotal85);

        return listLigneDecompteTrimestriel;
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
}
