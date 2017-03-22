package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.amal.process.AMALabstractProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationException;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.constantes.IConstantes;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import FamilleException.SimplePersonneANePasPoursuivreException;
import ch.ech.xmlns.ech_0058._4.SendingApplicationType;
import ch.ech.xmlns.ech_0090._1.EnvelopeType;
import ch.gdk_cds.xmlns.da_64a_5222_000201._1.ContentType;
import ch.gdk_cds.xmlns.da_64a_5222_000201._1.HeaderType;
import ch.gdk_cds.xmlns.da_64a_5222_000201._1.Message;
import ch.gdk_cds.xmlns.da_64a_5222_000201._1.ObjectFactory;
import ch.gdk_cds.xmlns.da_64a_common._1.AddressType;
import ch.gdk_cds.xmlns.da_64a_common._1.ContactInformationType;
import ch.gdk_cds.xmlns.da_64a_common._1.ExtensionType;
import ch.gdk_cds.xmlns.da_64a_common._1.InsuredPersonType;
import ch.gdk_cds.xmlns.da_64a_common._1.ListOfGuaranteedAssumptionsType;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTypeDemandeSubside;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOXML;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexCO.listes.Simulation_5222_201_1;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AnnoncesCOEnvoiMessage5222_000201_1 extends AMALabstractProcess {
    private static final String ANNEE_A_TRAITER = "2016";
    private static final String MM_YYYY = "MM.yyyy";
    private static final String DD_MM_YYYY = "dd.MM.yyyy";
    private static final String PACKAGE_CLASS_FOR_SEDEX_LISTE_PERSONNE_NE_PAS_POURSUIVRE = "ch.gdk_cds.xmlns.da_64a_5222_000201._1";
    private static final BigInteger VERSION = new BigInteger("1");
    private String noGroupeCaisse = null;
    private List<String> selectedIdCaisses = null;
    static BSession session = null;
    private ObjectFactory objectFactory = null;
    private List<String> errors = null;
    private Boolean simulation = false;

    public AnnoncesCOEnvoiMessage5222_000201_1() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
    }

    @Override
    public String getDescription() {
        return "Génération des annonces SEDEX \"Listes des personnes ne devant pas êtres poursuivies\"";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {

        errors = new ArrayList<String>();
        File fichierSimulation = null;
        try {

            Map<String, List<FamilleContribuable>> mapSubsideParAssurance = getPersonnesToSend();

            if (simulation) {
                List<Simulation_5222_201_1> listSimulationsFiles = new ArrayList<Simulation_5222_201_1>();

                for (Entry<String, List<FamilleContribuable>> entry : mapSubsideParAssurance.entrySet()) {
                    String idCaisseMaladie = entry.getKey();

                    CaisseMaladie caisseMaladie = new CaisseMaladie();
                    try {
                        caisseMaladie = AmalServiceLocator.getCaisseMaladieService().read(idCaisseMaladie);

                        List<FamilleContribuable> famillesContribuables = entry.getValue();

                        for (FamilleContribuable familleContribuable : famillesContribuables) {
                            Simulation_5222_201_1 simulationFile = new Simulation_5222_201_1();
                            simulationFile.setNomCaisse(caisseMaladie.getNomCaisse());

                            simulationFile.setNssPersonne(familleContribuable.getPersonneEtendue().getPersonneEtendue()
                                    .getNumAvsActuel());
                            simulationFile.setNomPrenomPersonne(familleContribuable.getPersonneEtendue().getTiers()
                                    .getDesignation1()
                                    + " " + familleContribuable.getPersonneEtendue().getTiers().getDesignation2());
                            simulationFile.setAnnee(familleContribuable.getSimpleDetailFamille().getAnneeHistorique());
                            listSimulationsFiles.add(simulationFile);
                        }
                    } catch (Exception e) {
                        errors.add("Erreur lors de la création du fichier de simulation : " + e.getMessage());
                        JadeThread.logError("Erreur lors de la création du fichier de simulation", e.getMessage());
                    }
                }

                fichierSimulation = printSimulation(listSimulationsFiles);
            } else {
                // AnneeToDelete doit provenir d'un écran
                String anneeToDelete = ANNEE_A_TRAITER;

                for (Entry<String, List<FamilleContribuable>> entry : mapSubsideParAssurance.entrySet()) {
                    int incr = 1;
                    Message message = objectFactory.createMessage();
                    String idCaisseMaladie = entry.getKey();
                    try {
                        ArrayList<SimpleSedexMessage> messagesToSend = new ArrayList<SimpleSedexMessage>();
                        String recipientId = AMSedexRPUtil.getSedexIdFromIdTiers(idCaisseMaladie);

                        dropPersonneANePasPoursuivre(anneeToDelete, idCaisseMaladie);

                        HeaderType header = generateHeader(recipientId);
                        SimpleAnnonceSedexCO simpleAnnonceSedexCO = creerAnnonce(header, idCaisseMaladie);
                        ContentType content = generateContent(entry, simpleAnnonceSedexCO);
                        message.setHeader(header);
                        message.setContent(content);
                        message.setMinorVersion(VERSION);

                        // TODO Validation ne fonctionne pas ????
                        Class<?>[] classes = new Class<?>[] {};
                        String messageFile = JAXBServices.getInstance().marshal(message, false, false, classes);
                        saveXml(classes, message);

                        // Ajout du message dans la liste de messages à envoyer
                        SimpleSedexMessage simpleSedexMessage = new SimpleSedexMessage();
                        simpleSedexMessage.fileLocation = messageFile;
                        simpleSedexMessage.increment = JadeStringUtil.fillWithZeroes(String.valueOf(incr), 5);
                        messagesToSend.add(simpleSedexMessage);

                        // Préparer l'enveloppe sedex (ech-0090)
                        String envelopeFile = generateEnveloppe(recipientId);

                        JadeSedexService.getInstance().sendGroupedMessage(envelopeFile, messagesToSend);

                        incr++;
                    } catch (AnnonceSedexException ase) {
                        errors.add("Erreur SEDEX " + ase.getMessage());
                        ase.printStackTrace();
                    } catch (JAXBValidationException e) {
                        String events = "";
                        if (e.getEvents() != null && !e.getEvents().isEmpty()) {
                            for (ValidationEvent event : e.getEvents()) {
                                errors.add("Erreur validation " + event.getMessage());
                            }
                        }
                        JadeThread.logError("Erreur de validation du message => " + events, e.getMessage());
                        e.printStackTrace();
                    } catch (JadeSedexMessageNotSentException e) {
                        errors.add("Erreur lors de l'envoi : " + e.getMessage());
                        JadeThread.logError("Erreur lors de l'envoi du message", e.getMessage());
                        e.printStackTrace();
                    } catch (Exception e) {
                        errors.add("Erreur technique (" + idCaisseMaladie + ") " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

        } catch (AnnonceSedexCOException e) {
            errors.add("Annonce SEDEX Exception : " + e.getMessage());
            JadeThread.logError("AnnoncesCOEnvoiMessage5222_000201_1.process()", e.getMessage());
            e.printStackTrace();
        } finally {
            createMail(fichierSimulation);

            try {
                // ------------------------------------------------------------------------
                // 5) Remise des status des jobs annonces à SENT
                // ------------------------------------------------------------------------
                JadeThread.logClear();
                setJobStatusSent();
            } catch (Exception ex) {
                JadeThread.logError("Erreur technique lors de le remise a zero des jobs", ex.getMessage());
            }
        }

    }

    private File printSimulation(List<Simulation_5222_201_1> listSimulationsFiles) {
        Details details = new Details();
        details.add("Reçu le", Date.now().getSwissValue());
        details.newLigne();
        Configuration config = Configurations.buildeDefault();
        SimpleOutputListBuilder builder = SimpleOutputListBuilderJade.newInstance()
                .outputNameAndAddPath("SimulationEnvoi").addList(listSimulationsFiles)
                .addTitle("Simulation", Align.LEFT).addSubTitle("Simulation").configure(config)
                .addHeaderDetails(details);

        return builder.asXls().build();
    }

    private SimpleAnnonceSedexCO creerAnnonce(HeaderType header, String idTiersCM) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexCOException, DetailFamilleException {
        SimpleAnnonceSedexCO simpleAnnonceSedexCO = new SimpleAnnonceSedexCO();
        simpleAnnonceSedexCO.setMessageType(header.getMessageType());
        simpleAnnonceSedexCO.setMessageSubType(header.getSubMessageType());
        simpleAnnonceSedexCO.setMessageEmetteur(header.getSenderId());
        simpleAnnonceSedexCO.setMessageRecepteur(header.getRecipientId());
        simpleAnnonceSedexCO.setBusinessProcessId(header.getBusinessProcessId());
        simpleAnnonceSedexCO.setMessageId(header.getMessageId());
        simpleAnnonceSedexCO.setStatus(IAMCodeSysteme.AMStatutAnnonceSedex.ENVOYE.getValue());
        simpleAnnonceSedexCO.setDateAnnonce(Date.now().getSwissValue());
        simpleAnnonceSedexCO.setIdTiersCM(idTiersCM);
        simpleAnnonceSedexCO = AmalServiceLocator.getSimpleAnnonceSedexCOService().create(simpleAnnonceSedexCO);

        return simpleAnnonceSedexCO;
    }

    private SimpleAnnonceSedexCOXML saveXml(Class<?>[] addClasses, Message message) throws JAXBException, SAXException,
            IOException, JAXBValidationError, JAXBValidationWarning, JadePersistenceException {
        StringWriter sw = new StringWriter();
        JAXBServices.getInstance().marshal(message, sw, false, true, addClasses);
        SimpleAnnonceSedexCOXML annonceSedexCOXML = new SimpleAnnonceSedexCOXML();
        annonceSedexCOXML.setMessageId(message.getHeader().getMessageId());
        annonceSedexCOXML.setXml(sw.toString());
        JadePersistenceManager.add(annonceSedexCOXML);
        return annonceSedexCOXML;
    }

    /**
     * Création du mail à envoyer à l'utilisateur
     * 
     * @param fichierSimulation
     * 
     * @param typeMessage
     * 
     * @param idTiersCMInput
     * @param idTiersGroupeInput
     */
    private void createMail(File fichierSimulation) {
        // --------------------------------------------------
        // 1) Préparation du message (body and subject)
        // --------------------------------------------------
        String subject = "";
        String message = "";
        String etatProcess = "terminé";
        boolean onError = !errors.isEmpty();

        if (onError) {
            etatProcess = "en erreur";
        }

        String typeProcess = "Processus";
        if (simulation) {
            typeProcess = "Simulation";
        }

        try {
            subject += "Web@Lamal : " + typeProcess + " SEDEX CO 'Liste des personnes ne devant pas être poursuivies' "
                    + etatProcess;
            message += typeProcess + " de création et d'envoi d'annonces SEDEX CO terminé.\n\n";

            if (onError) {
                if (!errors.isEmpty()) {
                    message += "\nErreurs : ";
                    message += String.valueOf(errors.size());
                    for (String errMsg : errors) {
                        message += "\n" + errMsg;
                    }
                }
            }

            if (JadeStringUtil.isBlankOrZero(noGroupeCaisse)) {
                message += "\n\nCaisse(s) : \n";
                for (String idCaisse : selectedIdCaisses) {
                    AdministrationComplexModel admc = TIBusinessServiceLocator.getAdministrationService()
                            .read(idCaisse);
                    if (!admc.isNew() && !JadeStringUtil.isEmpty(admc.getTiers().getDesignation1())) {
                        message += "   " + admc.getTiers().getDesignation1() + "\n";
                    } else {
                        message += "   " + admc.getAdmin().getCodeAdministration() + "\n";
                    }
                }
            } else {
                String currentGroupe = noGroupeCaisse;
                String currentNoGroupe = "";
                String currentNomGroupe = "";
                CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                cmSearch.setForIdTiersGroupe(currentGroupe);
                cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                cmSearch.setWhereKey("rcListeOnlyActifs");
                cmSearch.setDefinedSearchSize(0);
                try {
                    cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                    if (cmSearch.getSize() > 0) {
                        CaisseMaladie currentCMGroupe = (CaisseMaladie) cmSearch.getSearchResults()[0];
                        currentNoGroupe = currentCMGroupe.getNumGroupe();
                        currentNomGroupe = currentCMGroupe.getNomGroupe();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (JadeStringUtil.isEmpty(currentNomGroupe) && JadeStringUtil.isEmpty(currentNoGroupe)) {
                    message += "Groupe sélectionné : " + currentGroupe + "\n    ";
                } else {
                    message += "Groupe sélectionné : " + JadeStringUtil.fillWithZeroes(currentNoGroupe, 4) + " "
                            + currentNomGroupe + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] files = new String[1];
        if (fichierSimulation != null) {
            files[0] = fichierSimulation.getPath();
        }
        // if (mapReturn.containsKey(AnnonceSedexProcess.ATTACHED_FILES)) {
        // files = (String[]) mapReturn.get(AnnonceSedexProcess.ATTACHED_FILES);
        // }

        // --------------------------------------------------
        // 2) Envoi du message
        // --------------------------------------------------
        try {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    message, files);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Map<String, List<FamilleContribuable>> getPersonnesToSend() throws AnnonceSedexCOException {
        if (selectedIdCaisses == null) {
            if (!JadeStringUtil.isBlankOrZero(noGroupeCaisse)) {
                selectedIdCaisses = new ArrayList<String>();
                CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                cmSearch.setForIdTiersGroupe(noGroupeCaisse);
                cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                cmSearch.setWhereKey("rcListeOnlyActifs");
                cmSearch.setDefinedSearchSize(0);
                try {
                    cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                    for (int iCm = 0; iCm < cmSearch.getSize(); iCm++) {
                        CaisseMaladie currentCM = (CaisseMaladie) cmSearch.getSearchResults()[iCm];
                        if (!selectedIdCaisses.contains(currentCM.getIdTiersCaisse())) {
                            selectedIdCaisses.add(currentCM.getIdTiersCaisse());
                        }
                    }
                } catch (Exception ex) {
                    throw new AnnonceSedexCOException("Erreur récupération des idCaisse depuis le groupe "
                            + noGroupeCaisse, ex);
                }
            }
        }

        String today = Date.now().getSwissMonthValue();
        String yearToday = Date.now().getAnnee();
        // Récupération des assurées qui ont un subside ASSISTE ou PC active au moment du traitement
        FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
        List<String> typesDemande = new ArrayList<String>();
        typesDemande.add(AMTypeDemandeSubside.ASSISTE.getValue());
        typesDemande.add(AMTypeDemandeSubside.PC.getValue());
        familleContribuableSearch.setInTypeDemande(typesDemande);
        familleContribuableSearch.setForFinDefinitive("0");
        familleContribuableSearch.setForAnneeHistorique(ANNEE_A_TRAITER);
        familleContribuableSearch.setForCodeActif(Boolean.TRUE);
        // Date de fin du subside = 0 ou plus grand que date du jour
        familleContribuableSearch.setForDroitActifFromToday(today);
        familleContribuableSearch.setInNoCaisseMaladie(selectedIdCaisses);
        familleContribuableSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        familleContribuableSearch.setOrderKey("sedexco");
        try {
            familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                    familleContribuableSearch);
        } catch (Exception e) {
            throw new AnnonceSedexCOException("Erreur pendant la recherche des annonces SEDEX CO", e);
        }

        Map<String, List<FamilleContribuable>> mapSubsideParAssurance = new HashMap<String, List<FamilleContribuable>>();
        if (familleContribuableSearch.getNbOfResultMatchingQuery() > 0) {
            mapSubsideParAssurance = groupSubsidesParAssurance(familleContribuableSearch);
        }
        return mapSubsideParAssurance;
    }

    private String generateEnveloppe(String recipientId) throws MalformedURLException, JAXBValidationError,
            JAXBValidationWarning, JAXBException, SAXException, IOException, DatatypeConfigurationException,
            JadeSedexDirectoryInitializationException {
        String envelopeMessageId = JadeUUIDGenerator.createLongUID().toString();
        ch.ech.xmlns.ech_0090._1.ObjectFactory of0090 = new ch.ech.xmlns.ech_0090._1.ObjectFactory();
        EnvelopeType enveloppe = of0090.createEnvelopeType();
        enveloppe.setEventDate(JAXBUtil.getXmlCalendarTimestamp());
        enveloppe.setMessageDate(JAXBUtil.getXmlCalendarTimestamp());
        enveloppe.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        enveloppe.setMessageId(envelopeMessageId);
        enveloppe.setMessageType(Integer.parseInt(IAMSedex.MESSAGE_LIST_GUARANTED_TYPE));
        enveloppe.setVersion("1.0");
        enveloppe.getRecipientId().add(recipientId);
        JAXBElement<EnvelopeType> element = of0090.createEnvelope(enveloppe);
        String envelopeFile = JAXBServices.getInstance().marshal(element, true, false, new Class<?>[] {});
        return envelopeFile;
    }

    private ContentType generateContent(Entry<String, List<FamilleContribuable>> entry,
            SimpleAnnonceSedexCO simpleAnnonceSedexCO) {
        ContentType content = objectFactory.createContentType();
        ListOfGuaranteedAssumptionsType listOfGuaranteedAssumptionsType = new ListOfGuaranteedAssumptionsType();
        content.setListOfGuaranteedAssumptions(listOfGuaranteedAssumptionsType);

        for (FamilleContribuable familleContribuable : entry.getValue()) {

            try {
                InsuredPersonType insuredPersonType = new InsuredPersonType();

                String nom = familleContribuable.getPersonneEtendue().getTiers().getDesignation1();
                insuredPersonType.setOfficialName(nom);

                String prenom = familleContribuable.getPersonneEtendue().getTiers().getDesignation2();
                insuredPersonType.setFirstName(prenom);

                String vn = JadeStringUtil.removeChar(familleContribuable.getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel(), '.');
                if (JadeStringUtil.isNull(vn) || vn.isEmpty()) {
                    String caisseMaladie = "";
                    try {
                        String noCama = familleContribuable.getSimpleDetailFamille().getNoCaisseMaladie();
                        TiersSimpleModel tiersSimpleModel = TIBusinessServiceLocator.getTiersService().read(noCama);
                        caisseMaladie = tiersSimpleModel.getDesignation1();
                    } catch (Exception e) {
                        // Si une exception pète, pas grave, on n'affiche simplement pas la caisse maladie
                        caisseMaladie = "Non trouvée";
                    }
                    throw new AnnonceSedexException("Le contribuable \"" + nom + " " + prenom
                            + "\" de la caisse maladie \"" + caisseMaladie + "\""
                            + "\" a été ignoré car il ne possède pas de NSS");
                }
                Long vnNonFormate = Long.parseLong(JadeStringUtil.removeChar(vn, '.'));
                insuredPersonType.setVn(vnNonFormate);

                String sexe = null;
                if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(familleContribuable.getPersonneEtendue().getPersonne()
                        .getSexe())) {
                    sexe = "1";
                } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(familleContribuable.getPersonneEtendue()
                        .getPersonne().getSexe())) {
                    sexe = "2";
                }
                insuredPersonType.setSex(sexe);

                String dateNaissance = familleContribuable.getPersonneEtendue().getPersonne().getDateNaissance();
                insuredPersonType.setDateOfBirth(toXmlDate(dateNaissance, false));

                String language = getLanguage(familleContribuable);
                insuredPersonType.setLanguage(language);

                AddressType addressType = getAddress(familleContribuable);
                insuredPersonType.setAddress(addressType);

                content.getListOfGuaranteedAssumptions().getInsuredPerson().add(insuredPersonType);

                // Insertion de la persone dans la table des personnes a ne pas poursuivre
                flagPersonne(vn, familleContribuable);
                // Création d'une entrée pour chaque membre
                SimpleAnnonceSedexCOPersonne sedexCOPersonne = new SimpleAnnonceSedexCOPersonne();
                sedexCOPersonne.setIdAnnonceSedexCO(simpleAnnonceSedexCO.getId());
                sedexCOPersonne.setIdContribuable(familleContribuable.getSimpleContribuable().getIdContribuable());
                sedexCOPersonne.setIdFamille(familleContribuable.getSimpleFamille().getIdFamille());
                JadePersistenceManager.add(sedexCOPersonne);

            } catch (Exception ase) {
                errors.add(ase.getMessage());
            }
        }
        return content;
    }

    private String getLanguage(FamilleContribuable familleContribuable) {
        String tiersLanguage = familleContribuable.getPersonneEtendue().getTiers().getLangue();
        String language = "";
        if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(tiersLanguage)) {
            language = "fr";
        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(tiersLanguage)) {
            language = "de";
        } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(tiersLanguage)) {
            language = "it";
        } else if (IConstantes.CS_TIERS_LANGUE_ANGLAIS.equals(tiersLanguage)) {
            language = "en";
        }
        return language;
    }

    private void dropPersonneANePasPoursuivre(String annee, String idTiersCM) throws AnnonceSedexCOException {
        try {
            SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
            personneANePasPoursuivreSearch.setForAnnee(annee);
            personneANePasPoursuivreSearch.setForIdTiersCM(idTiersCM);
            personneANePasPoursuivreSearch = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().search(
                    personneANePasPoursuivreSearch);

            for (JadeAbstractModel model_personneANePasPoursuivre : personneANePasPoursuivreSearch.getSearchResults()) {
                SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre = (SimplePersonneANePasPoursuivre) model_personneANePasPoursuivre;
                AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService()
                        .delete(simplePersonneANePasPoursuivre);
            }
        } catch (Exception e) {
            throw new AnnonceSedexCOException("Erreur lors du vidage de la table des personne a ne pas poursuivre", e);
        }

    }

    private void flagPersonne(String nss, FamilleContribuable familleContribuable) throws JadePersistenceException,
            SimplePersonneANePasPoursuivreException, JadeApplicationServiceNotAvailableException {
        SimplePersonneANePasPoursuivreSearch personneANePasPoursuivreSearch = new SimplePersonneANePasPoursuivreSearch();
        personneANePasPoursuivreSearch.setForNSS(nss);
        personneANePasPoursuivreSearch.setForAnnee(familleContribuable.getSimpleDetailFamille().getAnneeHistorique());
        int count = AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().count(
                personneANePasPoursuivreSearch);

        if (count == 0) {
            SimplePersonneANePasPoursuivre personneANePasPoursuivre = new SimplePersonneANePasPoursuivre();
            personneANePasPoursuivre.setNss(nss);
            personneANePasPoursuivre.setAnnee(familleContribuable.getSimpleDetailFamille().getAnneeHistorique());
            personneANePasPoursuivre.setIdTiersCM(familleContribuable.getSimpleDetailFamille().getNoCaisseMaladie());
            personneANePasPoursuivre.setIdFamille(familleContribuable.getSimpleFamille().getIdFamille());
            personneANePasPoursuivre.setFlagEnvoi(true);
            personneANePasPoursuivre.setFlagReponse(false);
            AmalImplServiceLocator.getSimplePersonneANePasPoursuivreService().create(personneANePasPoursuivre);
        }
    }

    private AddressType getAddress(FamilleContribuable familleContribuable) throws AnnonceSedexException {

        try {
            String dateToday = Date.now().getSwissValue();

            // Recherche de l'adresse en utilisant l'id tiers du contribuable principal
            AdresseTiersDetail currentAdresseStandardDomicile = TIBusinessServiceLocator.getAdresseService()
                    .getAdresseTiers(familleContribuable.getSimpleContribuable().getIdTier(), true, dateToday,
                            AMGestionTiers.CS_DOMAINE_AMAL, AMGestionTiers.CS_TYPE_DOMICILE, null);

            AddressType addressType = new AddressType();
            if (!(currentAdresseStandardDomicile.getFields() == null)) {
                addressType.setAddressLine1(currentAdresseStandardDomicile.getFields().get(
                        AdresseTiersDetail.ADRESSE_VAR_D3));
                addressType.setAddressLine2(currentAdresseStandardDomicile.getFields().get(
                        AdresseTiersDetail.ADRESSE_VAR_D4));
                addressType.setStreet(currentAdresseStandardDomicile.getFields()
                        .get(AdresseTiersDetail.ADRESSE_VAR_RUE));
                addressType.setHouseNumber(currentAdresseStandardDomicile.getFields().get(
                        AdresseTiersDetail.ADRESSE_VAR_NUMERO));
                addressType.setTown(currentAdresseStandardDomicile.getFields().get(
                        AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
                addressType.setMunicipalityName(currentAdresseStandardDomicile.getFields().get(
                        AdresseTiersDetail.ADRESSE_VAR_LOCALITE));

                try {
                    String paysIso = currentAdresseStandardDomicile.getFields()
                            .get(AdresseTiersDetail.ADRESSE_VAR_PAYS_ISO).toUpperCase();

                    if ("CH".equals(paysIso)) {
                        addressType.setCountry(8100);
                        addressType.setSwissZipCode(Long.parseLong(currentAdresseStandardDomicile.getFields().get(
                                AdresseTiersDetail.ADRESSE_VAR_NPA)));
                    } else {
                        try {
                            addressType.setForeignZipCode(currentAdresseStandardDomicile.getFields().get(
                                    AdresseTiersDetail.ADRESSE_VAR_NPA));
                            PaysSearchSimpleModel paysSearchSimpleModel = new PaysSearchSimpleModel();
                            paysSearchSimpleModel.setForCodeIso(paysIso);
                            paysSearchSimpleModel = TIBusinessServiceLocator.getAdresseService().findPays(
                                    paysSearchSimpleModel);
                            if (paysSearchSimpleModel.getSize() > 0) {
                                PaysSimpleModel paysSimpleModel = (PaysSimpleModel) paysSearchSimpleModel
                                        .getSearchResults()[0];
                                String country = paysSimpleModel.getCodeCentrale();
                                addressType.setCountry(Integer.parseInt("8" + country));
                            }
                        } catch (Exception e) {
                            addressType.setCountry(8100);
                        }
                    }
                } catch (Exception e) {
                    addressType.setCountry(8100);
                }

                return addressType;

            } else {
                throw new AnnonceSedexException("Adress non trouvée ! idTiers : "
                        + familleContribuable.getPersonneEtendue().getTiers().getIdTiers() + " NNSS : "
                        + familleContribuable.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
            }
        } catch (Exception ex) {
            throw new AnnonceSedexException(
                    "Erreur technique pendant la récupération de l'adresse du tiers ! idTiers : "
                            + familleContribuable.getPersonneEtendue().getTiers().getIdTiers() + " NNSS : "
                            + familleContribuable.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel() + " ==> "
                            + ex.getMessage());
        }
    }

    private Map<String, List<FamilleContribuable>> groupSubsidesParAssurance(
            FamilleContribuableSearch familleContribuableSearch) {
        Map<String, List<FamilleContribuable>> mapSubsideParAssurance = new HashMap<String, List<FamilleContribuable>>();

        for (JadeAbstractModel modelFamilleContribuable : familleContribuableSearch.getSearchResults()) {
            FamilleContribuable familleContribuable = (FamilleContribuable) modelFamilleContribuable;
            String noCaisseMaladie = familleContribuable.getSimpleDetailFamille().getNoCaisseMaladie();

            if (mapSubsideParAssurance.containsKey(noCaisseMaladie)) {
                mapSubsideParAssurance.get(noCaisseMaladie).add(familleContribuable);
            } else {
                List<FamilleContribuable> familleContribuables = new ArrayList<FamilleContribuable>();
                familleContribuables.add(familleContribuable);
                mapSubsideParAssurance.put(noCaisseMaladie, familleContribuables);
            }
        }

        return mapSubsideParAssurance;
    }

    /**
     * Génère l'en-tête de l'annonce
     * 
     * @param message
     * 
     * @param recipientId
     * 
     * @return l'en-tête initialisée
     * @throws AnnonceSedexException
     * 
     * @throws JadeSedexDirectoryInitializationException
     *             Exception levée si dépôt Sedex ne peut être trouvé
     * @throws DatatypeConfigurationException
     *             Exception levée si l'en-tête ne peut être initialisée
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ALAnnonceRafamException
     *             Exception levée si une erreur métier se produit
     */
    public HeaderType generateHeader(String recipientId) throws JadeSedexDirectoryInitializationException,
            DatatypeConfigurationException {

        HeaderType header = objectFactory.createHeaderType();

        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());

        header.setRecipientId(recipientId);
        header.setMessageId(JadeUUIDGenerator.createStringUUID());

        header.setBusinessProcessId(JadeUUIDGenerator.createLongUID().toString());

        header.setMessageType(IAMSedex.MESSAGE_LIST_GUARANTED_TYPE);
        header.setSubMessageType(IAMSedex.MESSAGE_LIST_GUARANTED_SUBTYPE);

        header.setSendingApplication(getSendingApplicationType());
        header.setSubject(IAMSedex.MESSAGE_LIST_GUARANTED_SUBJECT);

        // Date du jour
        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar nowDateTime;
        nowDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        header.setMessageDate(nowDateTime);

        header.setAction(IAMSedex.MESSAGE_ACTION_NOUVEAU);

        Boolean testDeliveryFlag = JadeSedexService.getInstance().getTestDeliveryFlag();
        if (testDeliveryFlag) {
            header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());
        }
        header.setResponseExpected(Boolean.FALSE);
        header.setBusinessCaseClosed(Boolean.TRUE);

        header = setContactInformation(header);

        return header;
    }

    private SendingApplicationType getSendingApplicationType() {
        SendingApplicationType sat = new SendingApplicationType();
        sat.setManufacturer(IAMSedex.APPLICATION_TYPE_MANUFACTURER);
        sat.setProduct(IAMSedex.APPLICATION_TYPE_PRODUCT_NAME);
        sat.setProductVersion(IAMSedex.APPLICATION_TYPE_PRODUCT_VERSION);

        return sat;
    }

    private HeaderType setContactInformation(HeaderType header) {
        ContactInformationType contactInformationType = new ContactInformationType();

        contactInformationType.setName(BSessionUtil.getSessionFromThreadContext().getUserFullName());
        String phone = JadeStringUtil.removeChar(BSessionUtil.getSessionFromThreadContext().getUserInfo().getPhone(),
                ' ');
        // Si aucun no de téléphone n'est défini, on set un no bidon pour éviter une erreur de la validation XSD
        if (JadeStringUtil.isEmpty(phone)) {
            phone = "0000000000";
        }
        contactInformationType.setPhone(phone);
        contactInformationType.setEmail(BSessionUtil.getSessionFromThreadContext().getUserEMail());

        ExtensionType extensionType = new ExtensionType();
        extensionType.setContactInformation(contactInformationType);
        header.setExtension(extensionType);

        return header;
    }

    /**
     * Reset du status du job en cours à sent
     */
    private void setJobStatusSent() {
        SimpleControleurJobSearch currentJobSearch = new SimpleControleurJobSearch();
        currentJobSearch.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
        currentJobSearch.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
        currentJobSearch.setDefinedSearchSize(0);
        try {
            currentJobSearch = AmalServiceLocator.getControleurEnvoiService().search(currentJobSearch);
            for (int iJob = 0; iJob < currentJobSearch.getSize(); iJob++) {
                SimpleControleurJob currentJob = (SimpleControleurJob) currentJobSearch.getSearchResults()[iJob];
                currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
                currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
            }
        } catch (Exception ex) {
            JadeLogger.error(null, "Error reseting status job annonce à SENT :" + ex.toString());
            ex.printStackTrace();
        }
        try {
            JadeThread.commitSession();
        } catch (Exception ex) {
            JadeLogger.error(null, "Error commiting session when reseting status job annonce à SENT :" + ex.toString());
            ex.printStackTrace();
        }

    }

    protected XMLGregorianCalendar toXmlDate(String dateJJsMMsAAAA, boolean YYYYMMFormat) {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            DateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            if (YYYYMMFormat) {
                if (!JadeDateUtil.isGlobazDateMonthYear(dateJJsMMsAAAA)) {
                    dateJJsMMsAAAA = dateJJsMMsAAAA.substring(3);
                }

                df = new SimpleDateFormat("MM.yyyy");
            }
            cal.setTime(df.parse(dateJJsMMsAAAA));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if (YYYYMMFormat) {
                day = DatatypeConstants.FIELD_UNDEFINED;
            }

            DatatypeFactory factory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlCal = factory.newXMLGregorianCalendarDate(year, month, day,
                    DatatypeConstants.FIELD_UNDEFINED);
            return xmlCal;
        } catch (Exception pe) {
            return null;
        }
    }

    public String getNoGroupeCaisse() {
        return noGroupeCaisse;
    }

    public void setNoGroupeCaisse(String noGroupeCaisse) {
        this.noGroupeCaisse = noGroupeCaisse;
    }

    public List<String> getSelectedIdCaisses() {
        return selectedIdCaisses;
    }

    public void setSelectedIdCaisses(List<String> selectedIdCaisses) {
        this.selectedIdCaisses = selectedIdCaisses;
    }

    public Boolean getSimulation() {
        return simulation;
    }

    public void setSimulation(Boolean simulation) {
        this.simulation = simulation;
    }

}
