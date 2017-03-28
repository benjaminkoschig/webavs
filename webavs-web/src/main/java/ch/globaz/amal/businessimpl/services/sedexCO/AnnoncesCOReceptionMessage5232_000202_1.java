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
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendue;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.ComparaisonAnnonceCreancePriseEnCharge;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
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
    protected File fileAnnoncesRecues = null;

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
            // Sauvegarde de l'annonce dans la table
            SimpleAnnonceSedexCO annonceSedexCO = creerAnnonceHeader(message);
            // Sauvegarde du code XML de l'annonce dans la table
            saveXml(addClasses, message, annonceSedexCO);

            senderId = message.getHeader().getSenderId();
            idTiersCM = AMSedexRPUtil.getIdTiersFromSedexId(senderId);

            List<String> personnesNotFound = run(message, annonceSedexCO);

            sendMail(personnesNotFound);
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message");
        }
    }

    private SimpleAnnonceSedexCOXML saveXml(Class<?>[] addClasses, Message message, SimpleAnnonceSedexCO annonceSedexCO)
            throws JAXBException, SAXException, IOException, JAXBValidationError, JAXBValidationWarning,
            JadePersistenceException {
        StringWriter sw = new StringWriter();
        jaxbs.marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setIdAnnonceSedex(annonceSedexCO.getIdAnnonceSedexCO());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        checkJadeThreadErrors();
        return annonceSedexCOXML;
    }

    private List<String> run(Message message, SimpleAnnonceSedexCO annonceSedexCO) throws JAXBValidationError,
            JAXBValidationWarning, JAXBException, SAXException, IOException, AnnonceSedexCOReceptionException {
        List<String> personnesNotFound = new ArrayList<String>();
        List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison = new ArrayList<ComparaisonAnnonceCreancePriseEnCharge>();

        // Récupération de la période (année)
        anneePeriodeMessage = String.valueOf(message.getContent().getListOfClaimsGuaranteedAssumptions()
                .getStatementStartDate().getYear());

        // Itération sur les créances trouvées dans le message
        for (ClaimDebtorGuaranteedAssumptionType claimDebtorGuaranteedAssumption : message.getContent()
                .getListOfClaimsGuaranteedAssumptions().getClaimDebtorGuaranteedAssumption()) {

            DebtorWithClaimType debiteur = claimDebtorGuaranteedAssumption.getDebtorWithClaim();
            List<InsuredPersonWithClaimType> listePersonnesAssurees = claimDebtorGuaranteedAssumption
                    .getInsuredPersonWithClaim();

            try {
                String vn = debiteur.getDebtor().getDebtorNP().getVn().toString();
                CommonNSSFormater nssFormater = new CommonNSSFormater();
                String nssFormate = nssFormater.format(vn);

                FamillePersonneEtendue famillePersonneEtendue = searchPersonne(nssFormate, listePersonnesAssurees);

                if (famillePersonneEtendue != null) {
                    // Création d'une entrée pour chaque membre
                    SimpleAnnonceSedexCOPersonne sedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
                    sedexCOPersonne.setIdAnnonceSedexCO(annonceSedexCO.getId());
                    sedexCOPersonne.setIdContribuable(famillePersonneEtendue.getSimpleFamille().getIdContribuable());
                    sedexCOPersonne.setIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
                    JadePersistenceManager.add(sedexCOPersonne);
                }
            } catch (Exception ex) {
                throw new AnnonceSedexCOReceptionException(ex.getMessage());
            }

            ComparaisonAnnonceCreancePriseEnCharge comparaisonAnnonceCreancePriseEnCharge = new ComparaisonAnnonceCreancePriseEnCharge();
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

    protected ComparaisonAnnonceCreancePriseEnCharge getLigneComparaison(
            SimplePersonneANePasPoursuivre personneANePasPoursuivre) throws AnnonceSedexCOReceptionException {

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
                throw new AnnonceSedexCOReceptionException("Contribuable not exist with NSS : " + nssDebiteurFormate);
            }

            SimpleDetailFamille subside = AmalServiceLocator.getDetailFamilleService().read(
                    personneANePasPoursuivre.getIdDetailFamille());
            if (subside == null) {
                throw new AnnonceSedexCOReceptionException("Subside not found : "
                        + personneANePasPoursuivre.getIdDetailFamille());
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

            if (IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue().equals(subside.getTypeDemande())) {
                comparaisonAnnonceCreancePriseEnCharge.setTypeSubside("A");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.DEMANDE.getValue().equals(subside.getTypeDemande())) {
                comparaisonAnnonceCreancePriseEnCharge.setTypeSubside("");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(subside.getTypeDemande())) {
                comparaisonAnnonceCreancePriseEnCharge.setTypeSubside("P");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue().equals(subside.getTypeDemande())) {
                comparaisonAnnonceCreancePriseEnCharge.setTypeSubside("R");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue().equals(subside.getTypeDemande())) {
                comparaisonAnnonceCreancePriseEnCharge.setTypeSubside("S");
            }

            AdministrationComplexModel administration = TIBusinessServiceLocator.getAdministrationService().read(
                    subside.getNoCaisseMaladie());
            comparaisonAnnonceCreancePriseEnCharge.setAssureur(administration.getTiers().getDesignation1());

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
            throw new AnnonceSedexCOReceptionException("Erreur lors de la génération de la ligne de comparaison ==> "
                    + ex.getMessage());
        }

        return comparaisonAnnonceCreancePriseEnCharge;
    }

    protected void generationListeDifferences(List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaison)
            throws AnnonceSedexCOReceptionException {
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
            throw new AnnonceSedexCOReceptionException("Erreur lors de la génération de la liste de comparaison ==> "
                    + ex.getMessage());
        }
    }

    private SimplePersonneANePasPoursuivre flagPersonne(InsuredPersonWithClaimType personneAssuree,
            List<String> personnesNotFound) throws AnnonceSedexCOReceptionException {

        try {

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

            SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            personneANePasPoursuivreSearch.setForNSS(String.valueOf(personneAssuree.getInsuredPerson().getVn()));
            personneANePasPoursuivreSearch.setForAnnee(anneePeriodeMessage);
            personneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().search(
                    personneANePasPoursuivreSearch);
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

    private SimpleAnnonceSedexCO creerAnnonceHeader(Message message) {

        try {
            // Sauvegarde de l'annonce dans la table
            HeaderType header = message.getHeader();
            ContentType content = message.getContent();
            ListOfClaimsGuaranteedAssumptionsType claims = content.getListOfClaimsGuaranteedAssumptions();

            SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue());
            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(AMSedexRPUtil.getIdTiersFromSedexId(header.getSenderId()));
            annonceSedexCO.setDateAnnonce(AMSedexRPUtil.getDateXMLToString(claims.getStatementDate()));
            annonceSedexCO = (SimpleAnnonceSedexCO) JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();
            return annonceSedexCO;
        } catch (Exception ex) {
            errors.add("Erreur pendant la création de l'enregistrement SimpleAnnonceSedexCO" + ex.getMessage());
        }
        return null;
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
}
