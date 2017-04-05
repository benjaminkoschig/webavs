package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
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
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.webavs.common.CommonNSSFormater;
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
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendue;
import ch.globaz.amal.business.models.famille.FamillePersonneEtendueSearch;
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
    private String idTiersCM = null;
    private List<String> personnesNotFound = null;
    private List<String> errors = null;

    private Message message = null;
    private String idTiersCaisseMaladie = null;

    public AnnoncesCOReceptionMessage5232_000202_1() {
        personnesNotFound = new ArrayList<String>();
        errors = new ArrayList<String>();
    }

    /**
     * Pr�paration des users et mots de passe pour le gestion SEDEX (JadeSedexService.xml)
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
            throw new JadePersistenceException("Erreur(s) pendant la cr�ation de l'annonce : " + strErrors);
        }

    }

    @OnReceive
    public void importMessages(SedexMessage message) throws JadeSedexMessageNotHandledException {
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), getContext());

            if (message instanceof GroupedSedexMessage) {
                // ---------------------------------------------------------
                // Contr�le de la r�ception d'un message group�
                // ---------------------------------------------------------
                GroupedSedexMessage currentGroupedMessage = (GroupedSedexMessage) message;
                Iterator<SimpleSedexMessage> messagesIterator = currentGroupedMessage.iterator();
                while (messagesIterator.hasNext()) {
                    importMessagesSingle(messagesIterator.next());
                }
            } else if (message instanceof SimpleSedexMessage) {
                // ---------------------------------------------------------
                // Contr�le de la r�ception d'un message simple
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
            throw new JadeSedexMessageNotHandledException("Erreur dans la r�ception d'une annonce SEDEX CO: ");
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
     * M�thode de lecture du message sedex en r�ception, et traitement
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
            message = (Message) jaxbs.unmarshal(currentSimpleMessage.fileLocation, false, true, addClasses);
            senderId = message.getHeader().getSenderId();
            idTiersCM = AMSedexRPUtil.getIdTiersFromSedexId(senderId);
            // Sauvegarde de l'annonce dans la table
            SimpleAnnonceSedexCO annonceSedexCO = persistAnnonce(addClasses);

            List<ComparaisonAnnonceCreancePriseEnCharge> comparaisonSheet = creationListeAnnonceRecue();
            File fileComparaisonSheet = printList(comparaisonSheet);

            sendMail(fileComparaisonSheet);
        } catch (Exception e) {
            throw new JadeSedexMessageNotHandledException("Erreur lors du traitement du message => " + e.getMessage());
        }
    }

    protected File createAndPrintList(String annee, String idTiers) {
        List<ComparaisonAnnonceCreancePriseEnCharge> comparaisonSheet = creationListeAnnonceWithParam(annee, idTiers);
        File fileComparaisonSheet = printList(comparaisonSheet);
        return fileComparaisonSheet;
    }

    private SimpleAnnonceSedexCO persistAnnonce(Class<?>[] addClasses) {
        // Sauvegarde de l'annonce dans la table
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

            annonceSedexCO.setStatementDate(AMSedexRPUtil.getDateXMLToString(listOfClaimsGuaranteedAssumptionsType
                    .getStatementDate()));
            annonceSedexCO.setStatementStartDate(AMSedexRPUtil.getDateXMLToString(listOfClaimsGuaranteedAssumptionsType
                    .getStatementStartDate()));
            annonceSedexCO.setStatementEndDate(AMSedexRPUtil.getDateXMLToString(listOfClaimsGuaranteedAssumptionsType
                    .getStatementEndDate()));
            annonceSedexCO = (SimpleAnnonceSedexCO) JadePersistenceManager.add(annonceSedexCO);
            checkJadeThreadErrors();

            // Sauvegarde du XML dans la base
            saveXml(addClasses, annonceSedexCO);

            // Sauvegarde des d�biteurs et personnes assur�es
            processDonneesAnnonce(annonceSedexCO);
        } catch (Exception ex) {
            JadeThread.logError(
                    "AnnoncesCOReceptionMessage5232_000202_1.saveAnnonce()",
                    "Erreur pendant la sauvegarde de l'annonce des cr�ances avec prise en charge ! (Msg id : "
                            + header.getMessageId() + ") => " + ex.getMessage());
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
                    JadeThread.logInfo(this.getClass().getName(), "D�biteur non personne physique");
                    continue;
                }

                if (debtorNP.getVn() != null && debtorNP.getVn() > 0) {
                    String strNss = String.valueOf(debtorNP.getVn());
                    FamillePersonneEtendue famillePersonneEtendue = searchPersonne(strNss, listInsuredPersonsWithClaim);

                    if (famillePersonneEtendue == null) {
                        String nss = "";
                        try {
                            nss = formatNSS(debtorNP.getVn());
                        } catch (Exception ex) {
                            nss = String.valueOf(debtorNP.getVn());
                        }
                        personnesNotFound.add("D�biteur " + nss + " - " + debtorNP.getOfficialName() + " "
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

                for (InsuredPersonWithClaimType insuredPersonWithClaim : listInsuredPersonsWithClaim) {
                    try {
                        InsuredPersonType insuredPerson = insuredPersonWithClaim.getInsuredPerson();

                        // FLAG de la personne dans la table MAPERNPP
                        flagPersonne(simpleAnnonceSedexCO, insuredPersonWithClaim);

                        String strNss = String.valueOf(insuredPerson.getVn());
                        FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(strNss);

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
                        JadeThread.logError(
                                this.getClass().getName(),
                                "Erreur pendant le traitement d'une personne a ne pas poursuivre : "
                                        + asre.getMessage());
                    }
                }
            } catch (JadePersistenceException jpe) {
                JadeThread.logError(this.getClass().getName(),
                        "Erreur pendant la cr�ation du d�biteur en DB : " + jpe.getMessage());
            }
        }
    }

    /**
     * R�gles :
     * Si le d�biteur existe avec le nss pass�, on utilise celui ci
     * Sinon, on prend le premier contribuable actif trouv� d'une personne assur�e
     * Et enfin, on prend un contribuable qui a une fin de droit la plus r�cente et on met un message
     * 
     * @param nss
     * @param personnesAssurees
     * @return
     * @throws JadePersistenceException
     */
    // CODEFAULT
    protected FamillePersonneEtendue searchPersonne(String nss, List<InsuredPersonWithClaimType> personnesAssurees)
            throws JadePersistenceException {

        try {
            FamillePersonneEtendue famillePersonneEtendue = getPersonneEtendue(nss);

            if (famillePersonneEtendue != null) {
                return famillePersonneEtendue;
            }

            // Si on arrive jusqu'ici, c'est qu'on a trouv� aucun membre avec ce nss, on tente de r�cup�rer le 1er
            // contribuable actif qu'on trouve sur une des personnes assur�es...
            // On en profite �galement pour conserver le contribuable avec la fin de droit la plus r�cente, au cas o� on
            // devrait aller � la prochaine �tape...
            FamillePersonneEtendue famillePersonneEtendueMostRecent = null;
            for (InsuredPersonWithClaimType insuredPerson : personnesAssurees) {
                String nssPersonneAssureeFormate = "";
                try {
                    CommonNSSFormater nssFormateur = new CommonNSSFormater();
                    nssPersonneAssureeFormate = nssFormateur.format(String.valueOf(insuredPerson.getInsuredPerson()
                            .getVn()));
                } catch (Exception e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
                FamillePersonneEtendueSearch famillePersonneEtendueSearch = new FamillePersonneEtendueSearch();
                famillePersonneEtendueSearch.setLikeNss(nssPersonneAssureeFormate);
                famillePersonneEtendueSearch.setOrderKey("orderByFinDroitDesc");
                famillePersonneEtendueSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        famillePersonneEtendueSearch);
                for (JadeAbstractModel abstractFamilleContribuable : famillePersonneEtendueSearch.getSearchResults()) {
                    famillePersonneEtendue = (FamillePersonneEtendue) abstractFamilleContribuable;

                    if (JadeStringUtil.isBlankOrZero(famillePersonneEtendue.getSimpleFamille().getFinDefinitive())) {
                        // On retourne le 1er cas sans date de fin qu'on trouve.
                        return famillePersonneEtendue;
                    } else {
                        // Sinon on prend le 1er, qui est le plus r�cent
                        Date dateFinMostRecent = new Date(famillePersonneEtendueMostRecent.getSimpleFamille()
                                .getFinDefinitive());
                        Date dateFinCurrent = new Date(famillePersonneEtendue.getSimpleFamille().getFinDefinitive());
                        if (dateFinCurrent.after(dateFinMostRecent)) {
                            famillePersonneEtendueMostRecent = famillePersonneEtendue;
                        }
                    }
                }
            }

            // Si on arrive la, c'est qu'on a trouv� aucun contribuable actif sur une des personnes assur�es, on
            // retourne donc celui qui a la date de fin la plus r�cente.
            return famillePersonneEtendueMostRecent;
        } catch (Exception ex) {
            throw new JadePersistenceException("Erreur pendant la recherche de la personne " + nss, ex);
        }
    }

    private List<ComparaisonAnnonceCreancePriseEnCharge> creationListeAnnonceRecue() {
        try {
            SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            Date debutPeriode = new Date(AMSedexRPUtil.getDateXMLToString(message.getContent()
                    .getListOfClaimsGuaranteedAssumptions().getStatementStartDate()));
            simplePersonneANePasPoursuivreSearch.setForAnnee(debutPeriode.getAnnee());
            simplePersonneANePasPoursuivreSearch.setForIdTiersCM(idTiersCM);
            simplePersonneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService()
                    .search(simplePersonneANePasPoursuivreSearch);

            return generateListe(simplePersonneANePasPoursuivreSearch, false);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
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
                ligne.setMessage("Contribuable non demand�");
            } else if (isNonRepondu(simplePersonneANePasPoursuivre)) {
                ligne.setMessage("Contribuable non confirm�");
            }

            comparaisonSheet.add(ligne);
        }

        return comparaisonSheet;
    }

    private File printList(List<ComparaisonAnnonceCreancePriseEnCharge> listeComparaisons) {

        Details details = new Details();
        details.add("Re�u le", Date.now().getSwissValue());
        details.newLigne();

        SimpleOutputListBuilder listeComparaisonBuilder = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("Comparaisons").addList(listeComparaisons)
                .classElementList(ComparaisonAnnonceCreancePriseEnCharge.class)
                .addTitle("Liste de comparaison des annonces de prise en charge", Align.LEFT)
                .addSubTitle("Comparaisons").addHeaderDetails(details);

        File fileAnnoncesRecues = listeComparaisonBuilder.asXls().build();

        return fileAnnoncesRecues;
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
            // R�cup�ration de la p�riode (ann�e)
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
            // S'il existe, on passe le flag reception a true, sinon on cr�� une ligne
            if (personneANePasPoursuivreSearch.getNbOfResultMatchingQuery() == 1) {
                SimplePersonneANePasPoursuivre personneANePasPoursuivre = (SimplePersonneANePasPoursuivre) personneANePasPoursuivreSearch
                        .getSearchResults()[0];
                personneANePasPoursuivre.setFlagReponse(Boolean.TRUE);
                personneANePasPoursuivre.setNomPrenom(personneAssuree.getInsuredPerson().getOfficialName() + " "
                        + personneAssuree.getInsuredPerson().getFirstName());
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
                personneANePasPoursuivreNonExistante.setIdTiersCM(idTiersCM);
                personneANePasPoursuivreNonExistante.setIdAnnonceSedex(annonceSedexCO.getIdAnnonceSedexCO());
                AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().create(
                        personneANePasPoursuivreNonExistante);
                checkJadeThreadErrors();

                return personneANePasPoursuivreNonExistante;
            } else {
                throw new AnnonceSedexCOReceptionException(
                        "Plusieurs personne � ne pas poursuivre trouv�e pour l'ann�e " + anneePeriodeMessage
                                + " et le nss " + personneAssuree.getInsuredPerson().getVn());
            }
        } catch (Exception ex) {
            throw new AnnonceSedexCOReceptionException("Erreur pendant l'update de la personne a ne pas poursuivre", ex);
        }
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

    private void sendMail(File file) {
        String subject = "Contentieux Amal : r�ception des annonces de prise en charge effectu�e avec succ�s !";
        StringBuilder body = new StringBuilder();

        if (!personnesNotFound.isEmpty()) {
            if (personnesNotFound.size() > 1) {
                subject = "Contentieux Amal : " + personnesNotFound.size()
                        + " personnes non connues d�tect�es lors de la r�ception des annonces de prise en charge !";
            } else {
                subject = "Contentieux Amal : 1 personne non connue d�tect�e lors de la r�ception des annonces de prise en charge !";
            }
            body.append("Liste des d�biteurs / personnes non trouv�es :\n");

            for (String personne : personnesNotFound) {
                body.append("   -" + personne + "\n");
            }

            if (!errors.isEmpty()) {
                body.append("\nErreur(s) rencontr�e(s) ");
                for (String erreur : errors) {
                    body.append("   -" + erreur + "\n");
                }
            }
        }

        String[] files = new String[1];
        if (file != null) {
            files[0] = file.getPath();
        }

        try {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    body.toString(), files);
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "Erreur lors de l'envoi du mail ! => " + e.getMessage());
        }
    }
}
