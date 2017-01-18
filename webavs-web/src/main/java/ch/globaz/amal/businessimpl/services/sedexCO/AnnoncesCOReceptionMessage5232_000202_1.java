package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.da_64a_5232_000202._1.ContentType;
import ch.gdk_cds.xmlns.da_64a_5232_000202._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5232_000202._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.ClaimDebtorGuaranteedAssumptionType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.ListOfClaimsGuaranteedAssumptionsType;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOReceptionException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

public class AnnoncesCOReceptionMessage5232_000202_1 extends AnnoncesCODefault {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_CREANCE_AVEC_GARANTIE = "ch.gdk_cds.xmlns.da_64a_5232_000202._1";

    /**
     * Préparation des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
     * 
     * @param properties
     * @throws JadeDecryptionNotSupportedException
     * @throws JadeEncrypterNotFoundException
     * @throws Exception
     */
    @Setup
    public void setUp(Properties properties) throws Exception {

        getUserAndPassSedex(properties);

        JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE_CLASS_FOR_READ_SEDEX_CREANCE_AVEC_GARANTIE);
        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();
    }

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
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws JadeSedexMessageNotHandledException {

        try {
            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.da_64a_5232_000202._1.Message.class };
            jaxbs = JAXBServices.getInstance();
            Message message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            // Sauvegarde du code XML de l'annonce dans la table
            saveXml(addClasses, message);

            List<String> personnesNotFound = run(message);

            String subject = "Contentieux Amal : réception des annonces de prise en charge effectuée avec succès !";
            StringBuilder body = new StringBuilder();
            if (!personnesNotFound.isEmpty()) {
                if (personnesNotFound.size() > 1) {
                    subject = "Contentieux Amal : " + personnesNotFound.size()
                            + " personnes non connues détectées lors de la réception des annonces de prise en charge !";
                } else {
                    subject = "Contentieux Amal : 1 personne non connue détectée lors de la réception des annonces de prise en charge !";
                }
                body.append("Liste des personnes non trouvées :\n");

                for (String personne : personnesNotFound) {
                    body.append("   -" + personne + "\n");
                }
            }

            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    body.toString(), null);
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message");
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

    /**
     * @param message
     * @return La liste des personnes non trouvées
     * @throws JAXBValidationError
     * @throws JAXBValidationWarning
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     * @throws AnnonceSedexCOReceptionException
     */
    private List<String> run(Message message) throws JAXBValidationError, JAXBValidationWarning, JAXBException,
            SAXException, IOException, AnnonceSedexCOReceptionException {
        List<String> personnesNotFound = new ArrayList<String>();
        List<String> personnesMultipleFound = new ArrayList<String>();

        // Itération sur les créances trouvées dans le message
        for (ClaimDebtorGuaranteedAssumptionType claimDebtorGuaranteedAssumption : message.getContent()
                .getListOfClaimsGuaranteedAssumptions().getClaimDebtorGuaranteedAssumption()) {

            try {
                DebtorWithClaimType debiteur = claimDebtorGuaranteedAssumption.getDebtorWithClaim();

                if (debiteur.getDebtor().getDebtorNP() != null) {
                    Long vn = debiteur.getDebtor().getDebtorNP().getVn();

                    if (vn != null) {
                        SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
                        simpleFamilleSearch.setLikeNoAVS(vn.toString());
                        simpleFamilleSearch.setForFinDefinitive("0");
                        simpleFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(
                                simpleFamilleSearch);

                        if (simpleFamilleSearch.getSize() == 1) {
                            SimpleFamille membreFamille = (SimpleFamille) simpleFamilleSearch.getSearchResults()[0];
                            saveDebiteur(membreFamille, debiteur, message);
                        } else if (simpleFamilleSearch.getSize() > 1) {
                            personnesMultipleFound.add(vn.toString());
                        } else {
                            personnesNotFound.add(vn.toString());
                        }
                    }
                } else if (debiteur.getDebtor().getDebtorJP() != null) {
                    JadeThread.logWarn(this.getClass().getName(),
                            "Les personnes morales ne sont pas prises en charge par l'application");
                    // throw new AnnonceSedexCOReceptionException(
                    // "Les personnes morales ne sont pas prises en charge par l'application");
                }
            } catch (JadePersistenceException jpe) {
                throw new AnnonceSedexCOReceptionException("Erreur de persistence de l'annonce", jpe);
            } catch (FamilleException fe) {
                throw new AnnonceSedexCOReceptionException("Erreur lors de la recherche du membre famille", fe);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new AnnonceSedexCOReceptionException("Erreur technique ", e);
            }
        }

        return personnesNotFound;

    }

    private void saveDebiteur(SimpleFamille membreFamille, DebtorWithClaimType debiteur, Message message)
            throws AnnonceSedexCOReceptionException, JAXBValidationError, JAXBValidationWarning, JAXBException,
            SAXException, IOException {
        if (membreFamille == null || membreFamille.isNew()) {
            throw new IllegalArgumentException("membreFamille can't be null !");
        }

        if (debiteur == null) {
            throw new IllegalArgumentException("debiteur can't be null !");
        }

        try {
            creerAnnonce(membreFamille, debiteur, message);
            checkJadeThreadErrors();
        } catch (JadePersistenceException e) {
            throw new AnnonceSedexCOReceptionException("Erreur lors de la sauvegarde du débiteur "
                    + debiteur.getDebtor().getDebtorNP().getVn(), e);
        }

    }

    private void checkJadeThreadErrors() throws JadePersistenceException {
        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] msg = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR);
            List<String> errors = new ArrayList<String>();
            for (JadeBusinessMessage jadeBusinessMessage : msg) {
                errors.add(jadeBusinessMessage.getMessageId());
            }
            String strErrors = StringUtils.join(errors, ";");
            throw new JadePersistenceException("Erreur(s) pendant la création de l'annonce : " + strErrors);
        }

    }

    private void creerAnnonce(SimpleFamille membreFamille, DebtorWithClaimType debiteur, Message message)
            throws JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException,
            JadePersistenceException {
        // Sauvegarde de l'annonce dans la table
        HeaderType header = message.getHeader();
        ContentType content = message.getContent();
        ListOfClaimsGuaranteedAssumptionsType claims = content.getListOfClaimsGuaranteedAssumptions();

        SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
        annonceSedexCO.setMessageId(header.getMessageId());
        annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
        annonceSedexCO.setMessageType(header.getMessageType());
        annonceSedexCO.setMessageSubType(header.getSubMessageType());
        annonceSedexCO.setMessageEmetteur(header.getSenderId());
        annonceSedexCO.setMessageRecepteur(header.getRecipientId());
        annonceSedexCO.setIdContribuable(membreFamille.getIdContribuable());
        annonceSedexCO.setIdFamille(membreFamille.getIdFamille());
        annonceSedexCO.setDateAnnonce(AMSedexRPUtil.getDateXMLToString(claims.getStatementDate()));
        annonceSedexCO.setPeriodeDebut(AMSedexRPUtil.getDateXMLToString(claims.getStatementStartDate()));
        annonceSedexCO.setPeriodeFin(AMSedexRPUtil.getDateXMLToString(claims.getStatementEndDate()));
        annonceSedexCO.setInterets(debiteur.getClaimDebtor().getInterests().toString());
        annonceSedexCO.setFrais(debiteur.getClaimDebtor().getExpenses().toString());
        annonceSedexCO.setTotalCreance(debiteur.getClaimDebtor().getTotalClaim().toString());
        JadePersistenceManager.add(annonceSedexCO);
    }
}
