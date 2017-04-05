package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5234_000402._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossArrivalType;
import ch.gdk_cds.xmlns.da_64a_common._1.CertificateOfLossFinalStatementType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.SimpleOutputList_Decompte_5234_401_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;

public class AnnoncesCOReceptionMessage5234_000402_1 extends AnnoncesCOReceptionMessage5234_000401_1 {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_DECOMPTE_FINAL = "ch.gdk_cds.xmlns.da_64a_5234_000402._1";
    private String idTiersCaisseMaladie = null;
    private List<String> personnesNotFound = null;
    private Message message = null;

    public AnnoncesCOReceptionMessage5234_000402_1() {
        personnesNotFound = new ArrayList<String>();
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

    @Override
    @OnReceive
    public void importMessages(SedexMessage message) throws JadeSedexMessageNotHandledException {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                // ---------------------------------------------------------
                // Contrôle de la réception d'un message groupé
                // ---------------------------------------------------------
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;
                Iterator<SimpleSedexMessage> messagesIterator = currentGroupedMessage.iterator();
                while (messagesIterator.hasNext()) {
                    importMessagesSingle(messagesIterator.next());
                }
            } else if (message instanceof SimpleSedexMessage) {
                // ---------------------------------------------------------
                // Contrôle de la réception d'un message simple
                // ---------------------------------------------------------
                importMessagesSingle((SimpleSedexMessage) message);
            } else {
                JadeLogger
                        .error(this,
                                "Une erreur s'est produite pendant la lecture d'une annonce CO : il ne s'agit pas d'un SimpleSedexMessage ou GroupedSedexMessage");
            }
        } catch (Exception e) {
            JadeLogger.error(this, "SEDEX: error receiving message ");
            e.printStackTrace();
            JadeLogger.error(this, e);
            throw new JadeSedexMessageNotHandledException("Erreur dans la réception d'une annonce SEDEX CO: ");
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

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
            message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            // Sauvegarde du code XML de l'annonce dans la table
            SimpleAnnonceSedexCO annonceSedexCO = persistAnnonce(addClasses);

            List<SimpleOutputList_Decompte_5234_401_1> sheetDecomptes = generateList(annonceSedexCO);
            File fileDecompte = printList(sheetDecomptes);
            sendMail(fileDecompte, "Décompte final");
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message", e);
        }
    }

    private SimpleAnnonceSedexCO persistAnnonce(Class<?>[] addClasses) {

        // Sauvegarde de l'annonce dans la table
        HeaderType header = message.getHeader();
        SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
        try {
            String idTiersSender = AMSedexRPUtil.getIdTiersFromSedexId(message.getHeader().getSenderId());

            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(idTiersSender);
            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue());
            annonceSedexCO.setDateAnnonce(Date.now().getSwissValue());

            CertificateOfLossFinalStatementType certificateOfLossArrivalType = message.getContent()
                    .getCertificateOfLossFinalStatement();

            annonceSedexCO.setStatementDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementDate()));
            annonceSedexCO.setStatementStartDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementStartDate()));
            annonceSedexCO.setStatementEndDate(AMSedexRPUtil.getDateXMLToString(certificateOfLossArrivalType
                    .getStatementEndDate()));
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();

            // Sauvegarde du XML dans la base
            saveXml(addClasses, annonceSedexCO);

            // Sauvegarde des débiteurs et personnes assurées
            saveDebiteursPersonnesAssurees(annonceSedexCO);
        } catch (Exception ex) {
            JadeThread.logError(
                    "AnnoncesCOReceptionMessage5234_000402_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce du décompte trimestriel ! (Msg id : "
                            + header.getMessageId() + ") => " + ex.getMessage());
        }

        return annonceSedexCO;
    }

    private void saveXml(Class<?>[] addClasses, SimpleAnnonceSedexCO simpleAnnonceCO) throws JAXBException,
            SAXException, IOException, JAXBValidationError, JAXBValidationWarning, JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setIdAnnonceSedex(simpleAnnonceCO.getIdAnnonceSedexCO());
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
    }

    @Override
    protected String getIdTiersCaisseMaladie() {
        try {
            if (idTiersCaisseMaladie == null) {
                idTiersCaisseMaladie = AMSedexRPUtil.getIdTiersFromSedexId(message.getHeader().getSenderId());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return idTiersCaisseMaladie;
    }

    @Override
    protected void saveDebiteursPersonnesAssurees(SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        List<CertificateOfLossArrivalType> decomptesTrimestriels = message.getContent()
                .getCertificateOfLossFinalStatement().getCertificateOfLossArrival();

        for (CertificateOfLossArrivalType certificateOfLossArrivalType : decomptesTrimestriels) {
            try {
                DebtorWithClaimType debtorWithClaim = certificateOfLossArrivalType.getDebtorWithClaim();
                DebtorNPType debtorNP = debtorWithClaim.getDebtor().getDebtorNP();
                List<InsuredPersonWithClaimType> insuredPersonTypes = certificateOfLossArrivalType
                        .getInsuredPersonWithClaim();

                if (debtorNP == null) {
                    JadeThread.logInfo(this.getClass().getName(), "Débiteur non personne physique");
                    continue;
                }

                SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur = new SimpleAnnonceSedexCODebiteur();
                annonceSedexCODebiteur.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
                annonceSedexCODebiteur.setNssDebiteur(String.valueOf(debtorNP.getVn()));
                annonceSedexCODebiteur.setNomPrenomDebiteur(debtorNP.getOfficialName() + " " + debtorNP.getFirstName());
                annonceSedexCODebiteur.setNpaLocaliteDebiteur(getNPALocalite(debtorNP.getAddress()));
                annonceSedexCODebiteur.setRueNumeroDebiteur(getRueNumero(debtorNP.getAddress()));
                TypesOfLossEnum typeActe = getTypeActe(certificateOfLossArrivalType.getCertificateOfLoss()
                        .getTypeOfLoss());
                annonceSedexCODebiteur.setActe(typeActe.getCs());
                annonceSedexCODebiteur.setInterets(debtorWithClaim.getClaimDebtor().getInterests().toString());
                annonceSedexCODebiteur.setFrais(debtorWithClaim.getClaimDebtor().getExpenses().toString());
                annonceSedexCODebiteur.setTotal(debtorWithClaim.getClaimDebtor().getTotalClaim().toString());

                if (debtorNP.getVn() != null && debtorNP.getVn() > 0) {
                    annonceSedexCODebiteur = findCorrespondanceDebiteur(debtorNP, annonceSedexCODebiteur,
                            insuredPersonTypes);

                    if (annonceSedexCODebiteur != null && annonceSedexCODebiteur.getIdFamille() != null) {
                        SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
                        simpleAnnonceSedexCOPersonne.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
                        simpleAnnonceSedexCOPersonne.setIdFamille(annonceSedexCODebiteur.getIdFamille());
                        simpleAnnonceSedexCOPersonne.setIdContribuable(annonceSedexCODebiteur.getIdContribuable());
                        try {
                            AmalServiceLocator.getSimpleAnnonceSedexCOPersonneService().create(
                                    simpleAnnonceSedexCOPersonne);
                        } catch (Exception ex) {
                            throw new JadePersistenceException(ex.getMessage());
                        }
                    }
                }
                annonceSedexCODebiteur = (SimpleAnnonceSedexCODebiteur) JadePersistenceManager
                        .add(annonceSedexCODebiteur);

                for (InsuredPersonWithClaimType insuredPersonWithClaimType : insuredPersonTypes) {
                    InsuredPersonType insuredPerson = insuredPersonWithClaimType.getInsuredPerson();

                    SimpleAnnonceSedexCOAssure annonceSedexCOAssure = new SimpleAnnonceSedexCOAssure();
                    findCorrespondancePersonneAssuree(insuredPerson, annonceSedexCOAssure, message.getContent()
                            .getCertificateOfLossFinalStatement().getStatementDate());
                    annonceSedexCOAssure.setIdAnnonceSedexCODebiteur(annonceSedexCODebiteur.getId());

                    annonceSedexCOAssure.setNssAssure(String.valueOf(insuredPerson.getVn()));
                    annonceSedexCOAssure.setNomPrenomAssure(insuredPerson.getOfficialName() + " "
                            + insuredPerson.getFirstName());
                    annonceSedexCODebiteur.setNpaLocaliteDebiteur(getNPALocalite(insuredPerson.getAddress()));
                    annonceSedexCODebiteur.setRueNumeroDebiteur(getRueNumero(insuredPerson.getAddress()));

                    if (insuredPersonWithClaimType.getPremium() != null) {
                        annonceSedexCOAssure.setPrimePeriodeDebut(AMSedexRPUtil
                                .getDateXMLToString(insuredPersonWithClaimType.getPremium().getClaimStartDate()));
                        annonceSedexCOAssure.setPrimePeriodeFin(AMSedexRPUtil
                                .getDateXMLToString(insuredPersonWithClaimType.getPremium().getClaimEndDate()));
                        annonceSedexCOAssure.setPrimeMontant(insuredPersonWithClaimType.getPremium().getClaimAmount()
                                .toString());
                    }

                    if (insuredPersonWithClaimType.getCostSharing() != null) {
                        annonceSedexCOAssure.setCostSharingPeriodeDebut(AMSedexRPUtil
                                .getDateXMLToString(insuredPersonWithClaimType.getCostSharing().getClaimStartDate()));
                        annonceSedexCOAssure.setCostSharingPeriodeFin(AMSedexRPUtil
                                .getDateXMLToString(insuredPersonWithClaimType.getCostSharing().getClaimEndDate()));
                        annonceSedexCOAssure.setCostSharingMontant(insuredPersonWithClaimType.getCostSharing()
                                .getClaimAmount().toString());
                    }
                    JadePersistenceManager.add(annonceSedexCOAssure);
                }
            } catch (JadePersistenceException jpe) {
                JadeThread.logError(this.getClass().getName(), "Erreur pendant la création du débiteur en DB");
            }
        }
    }

}
