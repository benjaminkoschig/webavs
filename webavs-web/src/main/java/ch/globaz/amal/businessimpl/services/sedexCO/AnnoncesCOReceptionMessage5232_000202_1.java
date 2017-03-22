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
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.util.CommonNSSFormater;
import java.io.File;
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
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.ListOfClaimsGuaranteedAssumptionsType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOReceptionException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.ComparaisonAnnonceCreancePriseEnCharge;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOReceptionMessage5232_000202_1 extends AnnoncesCODefault {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_CREANCE_AVEC_GARANTIE = "ch.gdk_cds.xmlns.da_64a_5232_000202._1";
    List<String> errors = new ArrayList<String>();
    private String senderId = null;
    private String idTiersCM = null;
    private String anneePeriodeMessage = null;
    File fileAnnoncesRecues = null;

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
            SimpleAnnonceSedexCOXML simpleAnnonceSedexCOXML = saveXml(addClasses, message);

            senderId = message.getHeader().getSenderId();
            idTiersCM = AMSedexRPUtil.getIdTiersFromSedexId(senderId);

            List<String> personnesNotFound = run(simpleAnnonceSedexCOXML, message);

            sendMail(personnesNotFound);
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message");
        }
    }

    private void sendMail(List<String> personnesNotFound) throws Exception {
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

        String[] files = new String[1];
        if (fileAnnoncesRecues != null) {
            files[0] = fileAnnoncesRecues.getPath();
        }

        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                body.toString(), files);
    }

    private void sendMailListComparaisonOnly() throws Exception {
        String subject = "Contentieux Amal : Liste de comparaison";
        StringBuilder body = new StringBuilder();

        String[] files = new String[1];
        if (fileAnnoncesRecues != null) {
            files[0] = fileAnnoncesRecues.getPath();
        }

        JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                body.toString(), files);
    }

    private SimpleAnnonceSedexCOXML saveXml(Class<?>[] addClasses, Message message) throws JAXBException, SAXException,
            IOException, JAXBValidationError, JAXBValidationWarning, JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
        return annonceSedexCOXML;
    }

    private List<String> run(SimpleAnnonceSedexCOXML simpleAnnonceSedexCOXML, Message message)
            throws JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException,
            AnnonceSedexCOReceptionException {
        List<String> personnesNotFound = new ArrayList<String>();
        List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison = new ArrayList<ComparaisonAnnonceCreancePriseEnCharge>();

        // Récupération de la période (année)
        anneePeriodeMessage = String.valueOf(message.getContent().getListOfClaimsGuaranteedAssumptions()
                .getStatementStartDate().getYear());

        // Itération sur les créances trouvées dans le message
        for (ClaimDebtorGuaranteedAssumptionType claimDebtorGuaranteedAssumption : message.getContent()
                .getListOfClaimsGuaranteedAssumptions().getClaimDebtorGuaranteedAssumption()) {

            ComparaisonAnnonceCreancePriseEnCharge comparaisonAnnonceCreancePriseEnCharge = new ComparaisonAnnonceCreancePriseEnCharge();
            DebtorWithClaimType debiteur = claimDebtorGuaranteedAssumption.getDebtorWithClaim();
            List<InsuredPersonWithClaimType> listePersonnesAssurees = claimDebtorGuaranteedAssumption
                    .getInsuredPersonWithClaim();

            creerAnnonce(debiteur, message, simpleAnnonceSedexCOXML);

            for (InsuredPersonWithClaimType personneAssuree : listePersonnesAssurees) {
                SimplePersonneANePasPoursuivre personneANePasPoursuivre = flagPersonne(personneAssuree,
                        personnesNotFound);

                comparaisonAnnonceCreancePriseEnCharge = getLigneComparaison(personneANePasPoursuivre);
                listeComparaison.add(comparaisonAnnonceCreancePriseEnCharge);
            }
        }

        generationListeDifferences(listeComparaison);

        return personnesNotFound;

    }

    // private List<ComparaisonAnnonceCreancePriseEnCharge> getLigneComparaison()
    // throws SimplePersonneANePasPoursuivreException, JadeApplicationServiceNotAvailableException,
    // JadePersistenceException {
    // SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
    // personneANePasPoursuivreSearch.setForAnnee(anneePeriodeMessage);
    // personneANePasPoursuivreSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
    // personneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().search(
    // personneANePasPoursuivreSearch);
    //
    // List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison = new
    // ArrayList<ComparaisonAnnonceCreancePriseEnCharge>();
    // for (JadeAbstractModel model_simplePersonneANePasPoursuivre : personneANePasPoursuivreSearch.getSearchResults())
    // {
    // SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre = (SimplePersonneANePasPoursuivre)
    // model_simplePersonneANePasPoursuivre;
    // ComparaisonAnnonceCreancePriseEnCharge comparaisonAnnonceCreancePriseEnCharge =
    // getLigneComparaison(simplePersonneANePasPoursuivre);
    // listeComparaison.add(comparaisonAnnonceCreancePriseEnCharge);
    // }
    //
    // generationListeDifferences(listeComparaison);
    //
    // return listeComparaison;
    // }

    protected ComparaisonAnnonceCreancePriseEnCharge getLigneComparaison(
            SimplePersonneANePasPoursuivre personneANePasPoursuivre) {

        ComparaisonAnnonceCreancePriseEnCharge comparaisonAnnonceCreancePriseEnCharge = new ComparaisonAnnonceCreancePriseEnCharge();
        try {
            CommonNSSFormater nssFormateur = new CommonNSSFormater();
            String nssDebiteurFormate = nssFormateur.format(personneANePasPoursuivre.getNss());
            ContribuableSearch contribuableSearch = new ContribuableSearch();
            contribuableSearch.setLikeNss(nssDebiteurFormate);
            contribuableSearch = AmalServiceLocator.getContribuableService().search(contribuableSearch);
            Contribuable contribuable = null;

            if (contribuableSearch.getNbOfResultMatchingQuery() > 0) {
                contribuable = (Contribuable) contribuableSearch.getSearchResults()[0];
            }

            if (contribuable == null) {
                throw new Exception("Contribuable not exist !");
            }
            comparaisonAnnonceCreancePriseEnCharge.setNoContribuable(contribuable.getPersonneEtendue()
                    .getPersonneEtendue().getNumContribuableActuel());
            comparaisonAnnonceCreancePriseEnCharge.setNss(contribuable.getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel());
            comparaisonAnnonceCreancePriseEnCharge.setNomPrenom(contribuable.getPersonneEtendue().getTiers()
                    .getDesignation1()
                    + " " + contribuable.getPersonneEtendue().getTiers().getDesignation2());
            comparaisonAnnonceCreancePriseEnCharge.setLocalite(personneANePasPoursuivre.getNpaLocalite());
            comparaisonAnnonceCreancePriseEnCharge.setAnnee(anneePeriodeMessage);
            comparaisonAnnonceCreancePriseEnCharge.setMontantCreance(new Montant(personneANePasPoursuivre
                    .getMontantCreance()));
            // Mettre le nom de l'assureur
            comparaisonAnnonceCreancePriseEnCharge.setAssureur(idTiersCM);

            SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            personneANePasPoursuivreSearch.setForNSS(personneANePasPoursuivre.getNss());
            personneANePasPoursuivreSearch.setForAnnee(anneePeriodeMessage);
            personneANePasPoursuivreSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            personneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().search(
                    personneANePasPoursuivreSearch);

            if (!personneANePasPoursuivre.getFlagEnvoi()) {
                comparaisonAnnonceCreancePriseEnCharge.setMessage("Contribuable non demandé");
            } else if (personneANePasPoursuivre.getFlagEnvoi() && !personneANePasPoursuivre.getFlagReponse()) {
                comparaisonAnnonceCreancePriseEnCharge.setMessage("Non confirmé");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return comparaisonAnnonceCreancePriseEnCharge;
    }

    protected void generationListeDifferences(List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison) {
        try {
            Details details = new Details();
            details.add("Reçu le", Date.now().getSwissValue());
            details.newLigne();

            SimpleOutputListBuilder listeComparaisonBuilder = SimpleOutputListBuilderJade.newInstance()
                    .outputNameAndAddPath("Comparaisons").addList(listeComparaison)
                    .classElementList(ComparaisonAnnonceCreancePriseEnCharge.class)
                    .addTitle("Liste de comparaison des annonces de prise en charge", Align.LEFT)
                    .addSubTitle("Comparaisons").addHeaderDetails(details);

            fileAnnoncesRecues = listeComparaisonBuilder.asXls().build();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SimplePersonneANePasPoursuivre flagPersonne(InsuredPersonWithClaimType personneAssuree,
            List<String> personnesNotFound) throws AnnonceSedexCOReceptionException {

        try {
            SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            personneANePasPoursuivreSearch.setForNSS(String.valueOf(personneAssuree.getInsuredPerson().getVn()));
            personneANePasPoursuivreSearch.setForAnnee(anneePeriodeMessage);
            personneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().search(
                    personneANePasPoursuivreSearch);

            Montant totalCreance = new Montant("0");
            if (personneAssuree.getPremium() != null) {
                totalCreance = totalCreance.add(new Montant(personneAssuree.getPremium().getClaimAmount()));
            }

            if (personneAssuree.getCostSharing() != null) {
                totalCreance = totalCreance.add(new Montant(personneAssuree.getCostSharing().getClaimAmount()));
            }

            String npa_localite = "";
            if (personneAssuree.getInsuredPerson().getAddress().getCountry() == 8100) {
                npa_localite = personneAssuree.getInsuredPerson().getAddress().getSwissZipCode().toString();
            } else {
                npa_localite = personneAssuree.getInsuredPerson().getAddress().getForeignZipCode().toString();
            }
            npa_localite += " " + personneAssuree.getInsuredPerson().getAddress().getMunicipalityName();

            // S'il existe, on passe le flag reception a true, sinon on créé une ligne
            if (personneANePasPoursuivreSearch.getNbOfResultMatchingQuery() == 1) {
                SimplePersonneANePasPoursuivre personneANePasPoursuivre = (SimplePersonneANePasPoursuivre) personneANePasPoursuivreSearch
                        .getSearchResults()[0];

                if (!personneANePasPoursuivre.getFlagEnvoi()) {
                    personnesNotFound.add(personneAssuree.getInsuredPerson().getVn() + " - "
                            + personneAssuree.getInsuredPerson().getOfficialName() + " "
                            + personneAssuree.getInsuredPerson().getFirstName());
                }

                personneANePasPoursuivre.setFlagReponse(Boolean.TRUE);
                personneANePasPoursuivre.setNomPrenom(personneAssuree.getInsuredPerson().getOfficialName() + " "
                        + personneAssuree.getInsuredPerson().getFirstName());
                personneANePasPoursuivre.setNpaLocalite(npa_localite);
                personneANePasPoursuivre.setMontantCreance(totalCreance.getValue());

                AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().update(personneANePasPoursuivre);
                checkJadeThreadErrors();

                return personneANePasPoursuivre;
            } else if (personneANePasPoursuivreSearch.getNbOfResultMatchingQuery() == 0) {
                personnesNotFound.add(personneAssuree.getInsuredPerson().getVn() + " - "
                        + personneAssuree.getInsuredPerson().getOfficialName() + " "
                        + personneAssuree.getInsuredPerson().getFirstName());
                SimplePersonneANePasPoursuivre personneANePasPoursuivreNonExistante = new SimplePersonneANePasPoursuivre();
                personneANePasPoursuivreNonExistante.setFlagEnvoi(false);
                personneANePasPoursuivreNonExistante.setFlagReponse(true);
                personneANePasPoursuivreNonExistante.setMontantCreance(totalCreance.getValue());
                personneANePasPoursuivreNonExistante.setAnnee(anneePeriodeMessage);
                personneANePasPoursuivreNonExistante.setNss(String.valueOf(personneAssuree.getInsuredPerson().getVn()));
                personneANePasPoursuivreNonExistante.setNomPrenom(personneAssuree.getInsuredPerson().getOfficialName()
                        + " " + personneAssuree.getInsuredPerson().getFirstName());
                personneANePasPoursuivreNonExistante.setNpaLocalite(npa_localite);
                personneANePasPoursuivreNonExistante.setIdTiersCM(idTiersCM);
                AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().create(
                        personneANePasPoursuivreNonExistante);
                checkJadeThreadErrors();

                return personneANePasPoursuivreNonExistante;
            } else {
                throw new AnnonceSedexCOReceptionException(
                        "Plusieurs personne à ne pas poursuivre trouvée pour l'année " + anneePeriodeMessage
                                + " et le nss " + personneAssuree.getInsuredPerson().getVn());
            }
        } catch (Exception ex) {
            throw new AnnonceSedexCOReceptionException("Erreur pendant l'update de la personne a ne pas poursuivre", ex);
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

    private void creerAnnonce(DebtorWithClaimType debiteur, Message message,
            SimpleAnnonceSedexCOXML simpleAnnonceSedexCOXML) {

        try {
            // Sauvegarde de l'annonce dans la table
            HeaderType header = message.getHeader();
            ContentType content = message.getContent();
            ListOfClaimsGuaranteedAssumptionsType claims = content.getListOfClaimsGuaranteedAssumptions();

            String vn = debiteur.getDebtor().getDebtorNP().getVn().toString();
            CommonNSSFormater nssFormater = new CommonNSSFormater();
            String nssFormate = nssFormater.format(vn);
            PersonneEtendueSearchComplexModel personneEtendueSearchComplexModel = new PersonneEtendueSearchComplexModel();
            personneEtendueSearchComplexModel.setForNumeroAvsActuel(nssFormate);
            personneEtendueSearchComplexModel.setFor_isInactif("2");
            personneEtendueSearchComplexModel = TIBusinessServiceLocator.getPersonneEtendueService().find(
                    personneEtendueSearchComplexModel);

            PersonneEtendueComplexModel personneEtendueComplexModel = null;
            if (personneEtendueSearchComplexModel.getSearchResults().length > 0) {
                personneEtendueComplexModel = (PersonneEtendueComplexModel) personneEtendueSearchComplexModel
                        .getSearchResults()[0];
            }

            SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue());
            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setXmlId(simpleAnnonceSedexCOXML.getId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(AMSedexRPUtil.getIdTiersFromSedexId(header.getSenderId()));
            annonceSedexCO.setIdMembre(personneEtendueComplexModel.getTiers().getIdTiers());
            // annonceSedexCO.setIdContribuable(membreFamille.getIdContribuable());
            // annonceSedexCO.setIdFamille(membreFamille.getIdFamille());
            annonceSedexCO.setDateAnnonce(AMSedexRPUtil.getDateXMLToString(claims.getStatementDate()));
            annonceSedexCO.setInterets(debiteur.getClaimDebtor().getInterests().toString());
            annonceSedexCO.setFrais(debiteur.getClaimDebtor().getExpenses().toString());
            annonceSedexCO.setTotalCreance(debiteur.getClaimDebtor().getTotalClaim().toString());
            JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();
        } catch (Exception ex) {
            errors.add("Erreur pendant la création de l'enregistrement SimpleAnnonceSedexCO" + ex.getMessage());
        }
    }

    // private ComparaisonAnnonceCreancePriseEnCharge fillComparaison(SimplePersonneANePasPoursuivre persPasPoursuivre)
    // throws AnnonceSedexCOReceptionException, JadePersistenceException, FamilleException,
    // JadeApplicationServiceNotAvailableException {
    // FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
    // ComparaisonAnnonceCreancePriseEnCharge comparaison = new ComparaisonAnnonceCreancePriseEnCharge();
    //
    // CommonNSSFormater nssFormateur = new CommonNSSFormater();
    // String nssDebiteurFormate = "";
    // try {
    // nssDebiteurFormate = nssFormateur.format(persPasPoursuivre.getNss());
    // } catch (Exception e) {
    // throw new AnnonceSedexCOReceptionException("Erreur formattage nss", e);
    // }
    //
    // familleContribuableSearch.setForNNSS(nssDebiteurFormate);
    // familleContribuableSearch.setForAnneeHistorique(persPasPoursuivre.getAnnee());
    // familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService()
    // .search(familleContribuableSearch);
    //
    // FamilleContribuable familleContribuable = null;
    // if (familleContribuableSearch.getNbOfResultMatchingQuery() >= 1) {
    // if (familleContribuableSearch.getNbOfResultMatchingQuery() > 1) {
    // System.out.println("+ de 1 résultat");
    // }
    // familleContribuable = (FamilleContribuable) familleContribuableSearch.getSearchResults()[0];
    // comparaison.setNoContribuable(familleContribuable.getSimpleContribuable().getNoContribuable());
    // comparaison.setNss(familleContribuable.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
    // comparaison.setNomPrenom(familleContribuable.getPersonneEtendue().getTiers().getDesignation1() + " "
    // + familleContribuable.getPersonneEtendue().getTiers().getDesignation2());
    // comparaison.setAnnee(familleContribuable.getSimpleDetailFamille().getAnneeHistorique());
    // comparaison.setAssureur(persPasPoursuivre.getIdTiersCM());
    // comparaison.setMontantCreance(new Montant(persPasPoursuivre.getMontantCreance()));
    // }
    //
    // return comparaison;
    // }

    // private void saveDebiteur(SimpleFamille membreFamille, DebtorWithClaimType debiteur, Message message)
    // throws AnnonceSedexCOReceptionException, JAXBValidationError, JAXBValidationWarning, JAXBException,
    // SAXException, IOException, AnnonceSedexException {
    // if (membreFamille == null || membreFamille.isNew()) {
    // throw new IllegalArgumentException("membreFamille can't be null !");
    // }
    //
    // if (debiteur == null) {
    // throw new IllegalArgumentException("debiteur can't be null !");
    // }
    //
    // try {
    // creerAnnonce(membreFamille, debiteur, message);
    // flagPersonne(membreFamille, debiteur, "");
    // checkJadeThreadErrors();
    // } catch (JadePersistenceException e) {
    // throw new AnnonceSedexCOReceptionException("Erreur lors de la sauvegarde du débiteur "
    // + debiteur.getDebtor().getDebtorNP().getVn(), e);
    // }
    //
    // }
}
