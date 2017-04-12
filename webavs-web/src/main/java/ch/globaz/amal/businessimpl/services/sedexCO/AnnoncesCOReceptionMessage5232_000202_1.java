package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
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
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
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
import FamilleException.SimplePersonneANePasPoursuivreException;
import ch.gdk_cds.xmlns.da_64a_5232_000202._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5232_000202._1.Message;
import ch.gdk_cds.xmlns.da_64a_common._1.ClaimDebtorGuaranteedAssumptionType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorNPType;
import ch.gdk_cds.xmlns.da_64a_common._1.DebtorWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonWithClaimType;
import ch.gdk_cds.xmlns.da_64a_common._1.ListOfClaimsGuaranteedAssumptionsType;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOReceptionException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
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
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOReceptionMessage5232_000202_1 extends AnnoncesCODefault {
    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_CREANCE_AVEC_GARANTIE = "ch.gdk_cds.xmlns.da_64a_5232_000202._1";
    private String senderId = null;
    protected Date statementStartDate = null;
    private Message message = null;
    private String idTiersCaisseMaladie = null;

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

    // CODEFAULT
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

    private SimpleAnnonceSedexCOXML saveXml(Class<?>[] addClasses, SimpleAnnonceSedexCO annonceSedexCO)
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

    /**
     * Méthode de lecture du message sedex en réception, et traitement
     * 
     * @param currentSimpleMessage
     * @return
     * @throws JadeSedexMessageNotHandledException
     */
    private void importMessagesSingle(SimpleSedexMessage currentSimpleMessage)
            throws JadeSedexMessageNotHandledException {
        File fileComparaisonSheet = null;
        try {
            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.da_64a_5232_000202._1.Message.class };
            jaxbs = JAXBServices.getInstance();
            message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            senderId = message.getHeader().getSenderId();
            // Sauvegarde de l'annonce dans la table
            SimpleAnnonceSedexCO annonceSedexCO = persistAnnonce(addClasses);

            List<ComparaisonAnnonceCreancePriseEnCharge> comparaisonSheet = creationListeAnnonceRecue();
            fileComparaisonSheet = printList(comparaisonSheet);
        } catch (AnnonceSedexCOReceptionException asre) {
            logErrors("AnnoncesCOReceptionMessage5232_000202_1.importMessagesSingle()",
                    "Erreur pendant le traitement de la liste : " + asre.getMessage(), asre);
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5232_000402_1.importMessagesSingle()", "Erreur unmarshall message : "
                    + ex.getMessage(), ex);
        }
    }

    @Override
    protected String getSubjectMail() {
        return "Contentieux Amal : réception des annonces 'Créance avec garantie de prise en charge' effectuée avec succès !";
    }

    public File createAndPrintList(String annee, String idTiers) {
        List<ComparaisonAnnonceCreancePriseEnCharge> comparaisonSheet = creationListeAnnonceWithParam(annee, idTiers);
        File fileComparaisonSheet = printList(comparaisonSheet);
        return fileComparaisonSheet;
    }

    private SimpleAnnonceSedexCO persistAnnonce(Class<?>[] addClasses) {
        // Sauvegarde de l'annonce dans la table
        HeaderType header = message.getHeader();
        SimpleAnnonceSedexCO annonceSedexCO = new SimpleAnnonceSedexCO();
        try {
            String idTiersSender = AMSedexRPUtil.getIdTiersFromSedexId(message.getHeader().getSenderId());

            annonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.RECU.getValue());
            annonceSedexCO.setMessageId(header.getMessageId());
            annonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
            annonceSedexCO.setMessageType(header.getMessageType());
            annonceSedexCO.setMessageSubType(header.getSubMessageType());
            annonceSedexCO.setMessageEmetteur(header.getSenderId());
            annonceSedexCO.setMessageRecepteur(header.getRecipientId());
            annonceSedexCO.setIdTiersCM(idTiersSender);
            annonceSedexCO.setDateAnnonce(Date.now().getSwissValue());

            ListOfClaimsGuaranteedAssumptionsType listOfClaimsGuaranteedAssumptionsType = message.getContent()
                    .getListOfClaimsGuaranteedAssumptions();
            statementStartDate = new Date(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getListOfClaimsGuaranteedAssumptions().getStatementStartDate()));

            Date statementDate = new Date(AMSedexRPUtil.getDateXMLToString(listOfClaimsGuaranteedAssumptionsType
                    .getStatementDate()));
            annonceSedexCO.setStatementDate(statementDate.getSwissValue());
            annonceSedexCO.setStatementYear(statementDate.getAnnee());
            annonceSedexCO.setStatementStartDate(AMSedexRPUtil.getDateXMLToString(listOfClaimsGuaranteedAssumptionsType
                    .getStatementStartDate()));
            annonceSedexCO.setStatementEndDate(AMSedexRPUtil.getDateXMLToString(listOfClaimsGuaranteedAssumptionsType
                    .getStatementEndDate()));
            annonceSedexCO = (SimpleAnnonceSedexCO) JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();

            // Sauvegarde du XML dans la base
            saveXml(addClasses, annonceSedexCO);

            // Sauvegarde des débiteurs et personnes assurées
            processDonneesAnnonce(annonceSedexCO);
        } catch (Exception ex) {
            logErrors(
                    "AnnoncesCOReceptionMessage5232_000202_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce des créances avec prise en charge ! (Msg id : "
                            + header.getMessageId() + ") => " + ex.getMessage(), ex);
        }

        return annonceSedexCO;
    }

    private void processDonneesAnnonce(SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        ListOfClaimsGuaranteedAssumptionsType listOfClaimsGuaranteedAssumptions = message.getContent()
                .getListOfClaimsGuaranteedAssumptions();

        for (ClaimDebtorGuaranteedAssumptionType claimDebtorGuaranteedAssumption : listOfClaimsGuaranteedAssumptions
                .getClaimDebtorGuaranteedAssumption()) {
            try {
                DebtorWithClaimType debtorWithClaim = claimDebtorGuaranteedAssumption.getDebtorWithClaim();
                DebtorNPType debtorNP = debtorWithClaim.getDebtor().getDebtorNP();
                List<InsuredPersonWithClaimType> listInsuredPersonsWithClaim = claimDebtorGuaranteedAssumption
                        .getInsuredPersonWithClaim();

                if (debtorNP == null) {
                    JadeThread.logInfo(this.getClass().getName(), "Débiteur non personne physique");
                    continue;
                }

                SimpleAnnonceSedexCODebiteur simpleAnnonceSedexCODebiteur = saveDebiteur(simpleAnnonceSedexCO,
                        debtorWithClaim, listInsuredPersonsWithClaim);
                if (debtorNP.getVn() != null && debtorNP.getVn() > 0) {
                    String strNss = String.valueOf(debtorNP.getVn());
                    FamillePersonneEtendue famillePersonneEtendue = searchPersonne(strNss, listInsuredPersonsWithClaim,
                            simpleAnnonceSedexCODebiteur);

                    if (famillePersonneEtendue == null) {
                        String nss = "";
                        try {
                            nss = formatNSS(debtorNP.getVn());
                        } catch (Exception ex) {
                            nss = String.valueOf(debtorNP.getVn());
                        }
                        personnesNotFound.add("Débiteur " + nss + " - " + debtorNP.getOfficialName() + " "
                                + debtorNP.getFirstName());
                    } else {
                        SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
                        simpleAnnonceSedexCOPersonne.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
                        simpleAnnonceSedexCOPersonne.setIdFamille(famillePersonneEtendue.getSimpleFamille()
                                .getIdFamille());
                        simpleAnnonceSedexCOPersonne.setIdContribuable(famillePersonneEtendue.getSimpleContribuable()
                                .getIdContribuable());
                        try {
                            AmalServiceLocator.getSimpleAnnonceSedexCOPersonneService().create(
                                    simpleAnnonceSedexCOPersonne);
                        } catch (Exception ex) {
                            throw new JadePersistenceException(ex.getMessage());
                        }
                    }
                }

                savePersonneAssuree(listInsuredPersonsWithClaim, simpleAnnonceSedexCODebiteur);
                for (InsuredPersonWithClaimType insuredPersonWithClaim : listInsuredPersonsWithClaim) {
                    try {
                        InsuredPersonType insuredPerson = insuredPersonWithClaim.getInsuredPerson();

                        // FLAG de la personne dans la table MAPERNPP
                        flagPersonne(simpleAnnonceSedexCO, insuredPersonWithClaim);
                        String strNss = String.valueOf(insuredPerson.getVn());
                        FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(strNss, false);

                        if (famillePersonneEtendue == null) {
                            String nss = "";
                            try {
                                nss = formatNSS(insuredPerson.getVn());
                            } catch (Exception ex) {
                                nss = String.valueOf(insuredPerson.getVn());
                            }
                            personnesNotFound.add("Personne " + nss + " - " + insuredPerson.getOfficialName() + " "
                                    + insuredPerson.getFirstName());
                        }

                    } catch (AnnonceSedexCOReceptionException asre) {
                        logErrors(
                                "AnnoncesCOReceptionMessage5232_000202_1.processDonneesAnnonce",
                                "Erreur pendant le traitement d'une personne a ne pas poursuivre : "
                                        + asre.getMessage(), asre);
                    }
                }
            } catch (JadePersistenceException jpe) {
                logErrors("AnnoncesCOReceptionMessage5232_000202_1.processDonneesAnnonce",
                        "Erreur pendant la création du débiteur en DB : " + jpe.getMessage(), jpe);
            }
        }
    }

    private List<ComparaisonAnnonceCreancePriseEnCharge> creationListeAnnonceRecue()
            throws AnnonceSedexCOReceptionException {
        try {
            SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            Date debutPeriode = new Date(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getListOfClaimsGuaranteedAssumptions().getStatementStartDate()));
            simplePersonneANePasPoursuivreSearch.setForAnnee(debutPeriode.getAnnee());
            String idTiersSender = getIdTiersCaisseMaladie();
            simplePersonneANePasPoursuivreSearch.setForIdTiersCM(idTiersSender);
            simplePersonneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService()
                    .search(simplePersonneANePasPoursuivreSearch);

            return generateListe(simplePersonneANePasPoursuivreSearch, false);
        } catch (Exception ex) {
            throw new AnnonceSedexCOReceptionException(ex.getMessage());
        }
    }

    private List<ComparaisonAnnonceCreancePriseEnCharge> creationListeAnnonceWithParam(String annee, String idTiersCM) {
        try {
            SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            simplePersonneANePasPoursuivreSearch.setForAnnee(annee);
            simplePersonneANePasPoursuivreSearch.setForIdTiersCM(idTiersCM);
            simplePersonneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService()
                    .search(simplePersonneANePasPoursuivreSearch);

            return generateListe(simplePersonneANePasPoursuivreSearch, true);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private List<ComparaisonAnnonceCreancePriseEnCharge> generateListe(
            SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch, Boolean wantComplete)
            throws Exception {
        List<ComparaisonAnnonceCreancePriseEnCharge> comparaisonSheet = new ArrayList<ComparaisonAnnonceCreancePriseEnCharge>();

        for (JadeAbstractModel abstractModel : simplePersonneANePasPoursuivreSearch.getSearchResults()) {
            SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre = (SimplePersonneANePasPoursuivre) abstractModel;

            if (!wantComplete && isEnvoyeEtRepondu(simplePersonneANePasPoursuivre)) {
                continue;
            }

            ComparaisonAnnonceCreancePriseEnCharge ligne = new ComparaisonAnnonceCreancePriseEnCharge();

            Contribuable contribuable = AmalImplServiceLocator.getContribuableService().read(
                    simplePersonneANePasPoursuivre.getIdContribuable());
            if (!contribuable.isNew()) {
                ligne.setNoContribuable(contribuable.getPersonneEtendue().getPersonneEtendue()
                        .getNumContribuableActuel());
            }

            String nss = formatNSS(simplePersonneANePasPoursuivre.getNss());
            ligne.setNss(nss);
            ligne.setNomPrenom(simplePersonneANePasPoursuivre.getNomPrenom());
            ligne.setLocalite(simplePersonneANePasPoursuivre.getNpaLocalite());
            ligne.setAnnee(simplePersonneANePasPoursuivre.getAnnee());

            String nomCaisseMaladie = getNomCaisseMaladie(simplePersonneANePasPoursuivre.getIdTiersCM());
            ligne.setAssureur(nomCaisseMaladie);

            Montant montantCreance = new Montant(simplePersonneANePasPoursuivre.getMontantCreance());
            ligne.setMontantCreance(montantCreance);

            if (isNonDemande(simplePersonneANePasPoursuivre)) {
                ligne.addMessage("Contribuable non demandé");
            } else if (isNonRepondu(simplePersonneANePasPoursuivre)) {
                ligne.addMessage("Contribuable non confirmé");
            }

            comparaisonSheet.add(ligne);
        }

        return comparaisonSheet;
    }

    private File printList(List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaisons) {

        Details details = new Details();
        details.add("Reçu le", Date.now().getSwissValue());
        details.newLigne();

        SimpleOutputListBuilder listeComparaisonBuilder = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("Comparaisons").addList(listeComparaisons)
                .classElementList(ComparaisonAnnonceCreancePriseEnCharge.class)
                .addTitle("Liste de comparaison des annonces de prise en charge", Align.LEFT)
                .addSubTitle("Comparaisons").addHeaderDetails(details);

        File fileComparaisonSheet = listeComparaisonBuilder.asXls().build();

        sendMail(fileComparaisonSheet);

        return fileComparaisonSheet;
    }

    private boolean isEnvoyeEtRepondu(SimplePersonneANePasPoursuivre personneANePasPoursuivre) {
        return personneANePasPoursuivre.getFlagEnvoi() && personneANePasPoursuivre.getFlagReponse();
    }

    private boolean isNonDemande(SimplePersonneANePasPoursuivre personneANePasPoursuivre) {
        return !personneANePasPoursuivre.getFlagEnvoi() && personneANePasPoursuivre.getFlagReponse();
    }

    private boolean isNonRepondu(SimplePersonneANePasPoursuivre personneANePasPoursuivre) {
        return personneANePasPoursuivre.getFlagEnvoi() && !personneANePasPoursuivre.getFlagReponse();
    }

    private SimplePersonneANePasPoursuivre flagPersonne(SimpleAnnonceSedexCO annonceSedexCO,
            InsuredPersonWithClaimType personneAssuree) throws AnnonceSedexCOReceptionException {
        try {
            // Récupération de la période (année)
            String anneePeriodeMessage = String.valueOf(message.getContent().getListOfClaimsGuaranteedAssumptions()
                    .getStatementStartDate().getYear());

            Montant totalCreance = new Montant("0");
            if (personneAssuree.getPremium() != null) {
                totalCreance = totalCreance.add(new Montant(personneAssuree.getPremium().getClaimAmount()));
            }

            if (personneAssuree.getCostSharing() != null) {
                totalCreance = totalCreance.add(new Montant(personneAssuree.getCostSharing().getClaimAmount()));
            }

            String npaLocalite = getNPALocalite(personneAssuree.getInsuredPerson().getAddress());

            SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = searchPersonneANePasPoursuivre(
                    personneAssuree, anneePeriodeMessage);
            // S'il existe, on passe le flag reception a true, sinon on créé une ligne
            if (personneANePasPoursuivreSearch.getNbOfResultMatchingQuery() == 1) {
                SimplePersonneANePasPoursuivre personneANePasPoursuivre = (SimplePersonneANePasPoursuivre) personneANePasPoursuivreSearch
                        .getSearchResults()[0];
                personneANePasPoursuivre.setFlagReponse(Boolean.TRUE);
                personneANePasPoursuivre.setNomPrenom(personneAssuree.getInsuredPerson().getOfficialName() + " "
                        + personneAssuree.getInsuredPerson().getFirstName());
                personneANePasPoursuivre.setIdTiersCM(getIdTiersCaisseMaladie());
                personneANePasPoursuivre.setNpaLocalite(npaLocalite);
                personneANePasPoursuivre.setMontantCreance(totalCreance.getValue());

                AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().update(personneANePasPoursuivre);
                checkJadeThreadErrors();

                return personneANePasPoursuivre;
            } else if (personneANePasPoursuivreSearch.getNbOfResultMatchingQuery() == 0) {
                SimplePersonneANePasPoursuivre personneANePasPoursuivreNonExistante = new SimplePersonneANePasPoursuivre();
                personneANePasPoursuivreNonExistante.setFlagEnvoi(Boolean.FALSE);
                personneANePasPoursuivreNonExistante.setFlagReponse(Boolean.TRUE);
                personneANePasPoursuivreNonExistante.setMontantCreance(totalCreance.getValue());
                personneANePasPoursuivreNonExistante.setAnnee(anneePeriodeMessage);
                personneANePasPoursuivreNonExistante.setNss(String.valueOf(personneAssuree.getInsuredPerson().getVn()));
                personneANePasPoursuivreNonExistante.setNomPrenom(personneAssuree.getInsuredPerson().getOfficialName()
                        + " " + personneAssuree.getInsuredPerson().getFirstName());
                personneANePasPoursuivreNonExistante.setNpaLocalite(npaLocalite);
                personneANePasPoursuivreNonExistante.setIdTiersCM(getIdTiersCaisseMaladie());
                personneANePasPoursuivreNonExistante.setIdAnnonceSedex(annonceSedexCO.getIdAnnonceSedexCO());
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

    private SimpleAnnonceSedexCODebiteur saveDebiteur(SimpleAnnonceSedexCO simpleAnnonceSedexCO,
            DebtorWithClaimType debtorWithClaim, List<InsuredPersonWithClaimType> insuredPersonTypes)
            throws JadeNoBusinessLogSessionError, JadePersistenceException {

        if (debtorWithClaim.getDebtor().getDebtorNP() == null) {
            JadeThread.logInfo(this.getClass().getName(), "Débiteur non personne physique");
            return null;
        }
        DebtorNPType debtorNP = debtorWithClaim.getDebtor().getDebtorNP();

        SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur = new SimpleAnnonceSedexCODebiteur();
        annonceSedexCODebiteur.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
        annonceSedexCODebiteur.setNssDebiteur(String.valueOf(debtorNP.getVn()));
        annonceSedexCODebiteur.setNomPrenomDebiteur(debtorNP.getOfficialName() + " " + debtorNP.getFirstName());
        annonceSedexCODebiteur.setNpaLocaliteDebiteur(getNPALocalite(debtorNP.getAddress()));
        annonceSedexCODebiteur.setRueNumeroDebiteur(getRueNumero(debtorNP.getAddress()));
        annonceSedexCODebiteur.setInterets(debtorWithClaim.getClaimDebtor().getInterests().toString());
        annonceSedexCODebiteur.setFrais(debtorWithClaim.getClaimDebtor().getExpenses().toString());
        annonceSedexCODebiteur.setTotal(debtorWithClaim.getClaimDebtor().getTotalClaim().toString());

        if (debtorNP.getVn() != null && debtorNP.getVn() > 0) {
            annonceSedexCODebiteur = findCorrespondanceDebiteur(debtorNP, annonceSedexCODebiteur, insuredPersonTypes);

            if (annonceSedexCODebiteur != null && annonceSedexCODebiteur.getIdFamille() != null) {
                SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
                simpleAnnonceSedexCOPersonne.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getIdAnnonceSedexCO());
                simpleAnnonceSedexCOPersonne.setIdFamille(annonceSedexCODebiteur.getIdFamille());
                simpleAnnonceSedexCOPersonne.setIdContribuable(annonceSedexCODebiteur.getIdContribuable());
                try {
                    AmalServiceLocator.getSimpleAnnonceSedexCOPersonneService().create(simpleAnnonceSedexCOPersonne);
                } catch (Exception ex) {
                    throw new JadePersistenceException(ex.getMessage());
                }
            }
        }
        annonceSedexCODebiteur = (SimpleAnnonceSedexCODebiteur) JadePersistenceManager.add(annonceSedexCODebiteur);
        return annonceSedexCODebiteur;
    }

    private void savePersonneAssuree(List<InsuredPersonWithClaimType> insuredPersonTypes,
            SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur) throws JadePersistenceException {
        for (InsuredPersonWithClaimType insuredPersonWithClaimType : insuredPersonTypes) {
            InsuredPersonType insuredPerson = insuredPersonWithClaimType.getInsuredPerson();

            SimpleAnnonceSedexCOAssure annonceSedexCOAssure = new SimpleAnnonceSedexCOAssure();
            findCorrespondancePersonneAssuree(insuredPerson, annonceSedexCOAssure);
            annonceSedexCOAssure.setIdAnnonceSedexCODebiteur(annonceSedexCODebiteur.getId());

            annonceSedexCOAssure.setNssAssure(String.valueOf(insuredPerson.getVn()));
            annonceSedexCOAssure.setNomPrenomAssure(insuredPerson.getOfficialName() + " "
                    + insuredPerson.getFirstName());
            annonceSedexCOAssure.setNpaLocaliteAssure(getNPALocalite(insuredPerson.getAddress()));
            annonceSedexCOAssure.setRueNumeroAssure(getRueNumero(insuredPerson.getAddress()));

            if (insuredPersonWithClaimType.getPremium() != null) {
                annonceSedexCOAssure.setPrimePeriodeDebut(AMSedexRPUtil.getDateXMLToString(insuredPersonWithClaimType
                        .getPremium().getClaimStartDate()));
                annonceSedexCOAssure.setPrimePeriodeFin(AMSedexRPUtil.getDateXMLToString(insuredPersonWithClaimType
                        .getPremium().getClaimEndDate()));
                annonceSedexCOAssure.setPrimeMontant(insuredPersonWithClaimType.getPremium().getClaimAmount()
                        .toString());
            }

            if (insuredPersonWithClaimType.getCostSharing() != null) {
                annonceSedexCOAssure.setCostSharingPeriodeDebut(AMSedexRPUtil
                        .getDateXMLToString(insuredPersonWithClaimType.getCostSharing().getClaimStartDate()));
                annonceSedexCOAssure.setCostSharingPeriodeFin(AMSedexRPUtil
                        .getDateXMLToString(insuredPersonWithClaimType.getCostSharing().getClaimEndDate()));
                annonceSedexCOAssure.setCostSharingMontant(insuredPersonWithClaimType.getCostSharing().getClaimAmount()
                        .toString());
            }
            JadePersistenceManager.add(annonceSedexCOAssure);
        }
    }

    protected SimpleAnnonceSedexCODebiteur findCorrespondanceDebiteur(DebtorNPType debtorNP,
            SimpleAnnonceSedexCODebiteur annonceSedexCODebiteur, List<InsuredPersonWithClaimType> insuredPersonTypes) {

        if (debtorNP.getVn() == null) {
            throw new IllegalArgumentException("NSS débiteur is null or incorrect ! => " + debtorNP.getVn());
        }

        String strNss = String.valueOf(debtorNP.getVn());
        try {
            FamillePersonneEtendue famillePersonneEtendue = searchPersonne(strNss, insuredPersonTypes,
                    annonceSedexCODebiteur);

            if (famillePersonneEtendue != null) {
                annonceSedexCODebiteur.setIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
                annonceSedexCODebiteur.setIdContribuable(famillePersonneEtendue.getSimpleContribuable()
                        .getIdContribuable());

            } else {
                personnesNotFound.add("Débiteur " + formatNSS(debtorNP.getVn()) + " - " + debtorNP.getOfficialName()
                        + " " + debtorNP.getFirstName());
                annonceSedexCODebiteur.addMessage("Debiteur non retrouvé");
            }
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5232_000202_1.findCorrespondanceDebiteur()",
                    "Erreur lors de la recherche de correspondance du débiteur " + ex.getMessage(), ex);
        }
        return annonceSedexCODebiteur;
    }

    protected SimpleAnnonceSedexCOAssure findCorrespondancePersonneAssuree(InsuredPersonType insuredPerson,
            SimpleAnnonceSedexCOAssure annonceSedexCOAssure) {
        try {
            String strNss = String.valueOf(insuredPerson.getVn());
            FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(strNss, false);

            if (famillePersonneEtendue != null) {
                annonceSedexCOAssure.setIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
                annonceSedexCOAssure.setIdContribuable(famillePersonneEtendue.getSimpleContribuable()
                        .getIdContribuable());

                getInfosSubside(annonceSedexCOAssure, famillePersonneEtendue);
            } else {
                personnesNotFound.add("Personne " + formatNSS(insuredPerson.getVn()) + " - "
                        + insuredPerson.getOfficialName() + " " + insuredPerson.getFirstName());
                annonceSedexCOAssure.addMessage("Personne assurée non retrouvé");
            }
        } catch (Exception ex) {
            logErrors("AnnoncesCOReceptionMessage5232_000202_1.findCorrespondancePersonneAssuree()",
                    "Erreur lors de la recherche de correspondance de la personne assurée " + ex.getMessage(), ex);
        }
        return annonceSedexCOAssure;
    }

    private void getInfosSubside(SimpleAnnonceSedexCOAssure annonceSedexCOAssure,
            FamillePersonneEtendue famillePersonneEtendue) throws JadePersistenceException, DetailFamilleException,
            JadeApplicationServiceNotAvailableException {
        SimpleDetailFamilleSearch simpleDetailFamilleSearch = new SimpleDetailFamilleSearch();
        simpleDetailFamilleSearch.setForIdFamille(famillePersonneEtendue.getSimpleFamille().getIdFamille());
        simpleDetailFamilleSearch.setForAnneeHistorique(statementStartDate.getAnnee());
        simpleDetailFamilleSearch.setForCodeActif(Boolean.TRUE);
        simpleDetailFamilleSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
                simpleDetailFamilleSearch);

        if (simpleDetailFamilleSearch.getNbOfResultMatchingQuery() > 0) {
            SimpleDetailFamille subside = (SimpleDetailFamille) simpleDetailFamilleSearch.getSearchResults()[0];
            annonceSedexCOAssure.setIdDetailFamille(subside.getIdDetailFamille());
            String noCaisseMaladie = subside.getNoCaisseMaladie();
            String idTiersSender = getIdTiersCaisseMaladie();
            if (!noCaisseMaladie.equals(idTiersSender)) {
                annonceSedexCOAssure.addMessage("Caisse maladie différente");
            }

            if (IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("A");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.DEMANDE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("P");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.REPRISE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("R");
            } else if (IAMCodeSysteme.AMTypeDemandeSubside.SOURCE.getValue().equals(subside.getTypeDemande())) {
                annonceSedexCOAssure.setTypeSubside("S");
            }

        }
    }

    protected String getIdTiersCaisseMaladie() {

        if (senderId == null || senderId.isEmpty()) {
            throw new IllegalStateException("Aucun sedex id n'a été défini !");
        }
        try {
            if (idTiersCaisseMaladie == null) {
                idTiersCaisseMaladie = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }

        return idTiersCaisseMaladie;
    }

    private SimplePersonneANePasPoursuivreSearch searchPersonneANePasPoursuivre(
            InsuredPersonWithClaimType personneAssuree, String anneePeriodeMessage) throws JadePersistenceException,
            SimplePersonneANePasPoursuivreException, JadeApplicationServiceNotAvailableException {
        SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
        personneANePasPoursuivreSearch.setForNSS(String.valueOf(personneAssuree.getInsuredPerson().getVn()));
        personneANePasPoursuivreSearch.setForAnnee(anneePeriodeMessage);
        personneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().search(
                personneANePasPoursuivreSearch);
        return personneANePasPoursuivreSearch;
    }

    // private void sendMail(File file) {
    // String subject = "Contentieux Amal : réception des annonces de prise en charge effectuée avec succès !";
    // StringBuilder body = new StringBuilder();
    //
    // if (!personnesNotFound.isEmpty()) {
    // if (personnesNotFound.size() > 1) {
    // subject = "Contentieux Amal : " + personnesNotFound.size()
    // + " personnes non connues détectées lors de la réception des annonces de prise en charge !";
    // } else {
    // subject =
    // "Contentieux Amal : 1 personne non connue détectée lors de la réception des annonces de prise en charge !";
    // }
    // body.append("Liste des débiteurs / personnes non trouvées :\n");
    //
    // for (String personne : personnesNotFound) {
    // body.append("   -" + personne + "\n");
    // }
    //
    // if (!errors.isEmpty()) {
    // body.append("\nErreur(s) rencontrée(s) ");
    // for (String erreur : errors) {
    // body.append("   -" + erreur + "\n");
    // }
    // }
    // }
    //
    // String[] files = new String[1];
    // if (file != null) {
    // files[0] = file.getPath();
    // }
    //
    // try {
    // JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
    // body.toString(), files);
    // } catch (Exception e) {
    // JadeThread.logError(this.getClass().getName(), "Erreur lors de l'envoi du mail ! => " + e.getMessage());
    // }
    // }
}
