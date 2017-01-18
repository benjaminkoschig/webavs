package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.amal.process.AMALabstractProcess;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationException;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;
import java.io.IOException;
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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
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
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTypeDemandeSubside;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.amal.web.application.AMApplication;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnoncesCOEnvoiMessage5222_000201_1 extends AMALabstractProcess {
    private static final String PACKAGE_CLASS_FOR_SEDEX_LISTE_PERSONNE_NE_PAS_POURSUIVRE = "ch.gdk_cds.xmlns.da_64a_5222_000201._1";
    private static final BigInteger VERSION = new BigInteger("1");
    private String noGroupeCaisse = null;
    private List<String> selectedIdCaisses = null;
    static BSession session = null;
    private ObjectFactory objectFactory = null;

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

    // A supprimer lorsque le cablage aura été fait sur un écran
    public static void main(String[] args) throws Exception {
        AnnoncesCOEnvoiMessage5222_000201_1 process = new AnnoncesCOEnvoiMessage5222_000201_1();
        BSession session = initSession();
        process.setSession(session);
        process.run();
        System.exit(0);
    }

    // A supprimer lorsque le cablage aura été fait sur un écran
    public static BSession initSession() throws Exception {
        if (session == null) {
            session = (BSession) GlobazSystem.getApplication(AMApplication.DEFAULT_APPLICATION_AMAL).newSession(
                    "ccjuglo", "glob4az");
        }

        return session;
    }

    @Override
    protected void process() {

        try {

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
                        throw new AnnonceSedexException("Erreur récupération des idCaisse depuis le groupe "
                                + noGroupeCaisse, ex);
                    }
                }
            }

            // Récupération des assurées qui ont un subside ASSISTE ou PC active au moment du traitement
            FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
            List<String> typesDemande = new ArrayList<String>();
            typesDemande.add(AMTypeDemandeSubside.ASSISTE.getValue());
            typesDemande.add(AMTypeDemandeSubside.PC.getValue());
            familleContribuableSearch.setInTypeDemande(typesDemande);
            familleContribuableSearch.setForFinDefinitive("0");
            familleContribuableSearch.setForAnneeHistorique("2016");
            familleContribuableSearch.setForFinDroit("0");
            familleContribuableSearch.setIsOnListePersonneNePasPoursuivre(Boolean.FALSE);
            familleContribuableSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            try {
                familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        familleContribuableSearch);
            } catch (Exception e) {
                throw new AnnonceSedexException("Erreur pendant la recherche des annonces SEDEX CO", e);
            }

            Map<String, List<FamilleContribuable>> mapSubsideParAssurance = new HashMap<String, List<FamilleContribuable>>();
            if (familleContribuableSearch.getNbOfResultMatchingQuery() > 0) {
                mapSubsideParAssurance = groupSubsidesParAssurance(familleContribuableSearch);
            }

            ArrayList<SimpleSedexMessage> messagesToSend = new ArrayList<SimpleSedexMessage>();

            for (Entry<String, List<FamilleContribuable>> entry : mapSubsideParAssurance.entrySet()) {
                Message message = objectFactory.createMessage();
                String idCaisseMaladie = entry.getKey();
                String recipientId = AMSedexRPUtil.getSedexIdFromIdTiers(idCaisseMaladie);

                HeaderType header = generateHeader(recipientId);
                ContentType content = createContent(entry);
                message.setHeader(header);
                message.setContent(content);
                message.setMinorVersion(VERSION);

                String messageFile = JAXBServices.getInstance().marshal(message, true, false,
                        new Class<?>[] { ch.gdk_cds.xmlns.da_64a_5222_000201._1.Message.class });

                // Ajout du message dans la liste de messages à envoyer
                SimpleSedexMessage simpleSedexMessage = new SimpleSedexMessage();
                simpleSedexMessage.fileLocation = messageFile;
                messagesToSend.add(simpleSedexMessage);

                // Préparer l'enveloppe sedex (ech-0090)
                String envelopeFile = generateEnveloppe(recipientId);

                JadeSedexService.getInstance().sendGroupedMessage(envelopeFile, messagesToSend);
            }

        } catch (AnnonceSedexException e) {
            JadeThread.logError("AnnoncesCOEnvoiMessage5222_000201_1.process()", e.getMessage());
            e.printStackTrace();
        } catch (JAXBValidationException e) {
            JadeThread.logError("Erreur de validation du message", e.getMessage());
            e.printStackTrace();
        } catch (JadeSedexMessageNotSentException e) {
            JadeThread.logError("Erreur lors de l'envoi du message", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JadeThread.logError("Erreur technique", e.getMessage());
            e.printStackTrace();
        }

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
        enveloppe.setVersion(VERSION.toString());
        enveloppe.getRecipientId().add(recipientId);
        JAXBElement<EnvelopeType> element = of0090.createEnvelope(enveloppe);
        String envelopeFile = JAXBServices.getInstance().marshal(element, false, false, new Class<?>[] {});
        return envelopeFile;
    }

    private ContentType createContent(Entry<String, List<FamilleContribuable>> entry)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        ContentType content = objectFactory.createContentType();
        ListOfGuaranteedAssumptionsType listOfGuaranteedAssumptionsType = new ListOfGuaranteedAssumptionsType();
        content.setListOfGuaranteedAssumptions(listOfGuaranteedAssumptionsType);

        for (FamilleContribuable familleContribuable : entry.getValue()) {

            InsuredPersonType insuredPersonType = new InsuredPersonType();

            Long vn = Long.parseLong(JadeStringUtil.removeChar(familleContribuable.getPersonneEtendue()
                    .getPersonneEtendue().getNumAvsActuel(), '.'));
            insuredPersonType.setVn(vn);

            String nom = familleContribuable.getPersonneEtendue().getTiers().getDesignation1();
            insuredPersonType.setOfficialName(nom);

            String prenom = familleContribuable.getPersonneEtendue().getTiers().getDesignation2();
            insuredPersonType.setFirstName(prenom);

            String sexe = null;
            if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(familleContribuable.getPersonneEtendue().getPersonne()
                    .getSexe())) {
                sexe = "1";
            } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(familleContribuable.getPersonneEtendue().getPersonne()
                    .getSexe())) {
                sexe = "2";
            }
            insuredPersonType.setSex(sexe);

            String dateNaissance = familleContribuable.getPersonneEtendue().getPersonne().getDateNaissance();
            insuredPersonType.setDateOfBirth(toXmlDate(dateNaissance, false));
            String tiersLanguage = familleContribuable.getPersonneEtendue().getTiers().getLangue();
            if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(tiersLanguage)) {
                insuredPersonType.setLanguage("fr");
            } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(tiersLanguage)) {
                insuredPersonType.setLanguage("de");
            } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(tiersLanguage)) {
                insuredPersonType.setLanguage("it");
            } else if (IConstantes.CS_TIERS_LANGUE_ANGLAIS.equals(tiersLanguage)) {
                insuredPersonType.setLanguage("en");
            }

            AddressType addressType = getAddress(familleContribuable);
            insuredPersonType.setAddress(addressType);

            content.getListOfGuaranteedAssumptions().getInsuredPerson().add(insuredPersonType);
        }
        return content;
    }

    private AddressType getAddress(FamilleContribuable familleContribuable)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());

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
            addressType.setStreet(currentAdresseStandardDomicile.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
            addressType.setHouseNumber(currentAdresseStandardDomicile.getFields().get(
                    AdresseTiersDetail.ADRESSE_VAR_NUMERO));
            addressType
                    .setTown(currentAdresseStandardDomicile.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
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
            throw new AnnonceSedexException("Adress not found ! idTiers : "
                    + familleContribuable.getPersonneEtendue().getTiers().getIdTiers() + " NNSS : "
                    + familleContribuable.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
        }
    }

    protected XMLGregorianCalendar toXmlDate(String dateJJsMMsAAAA, boolean YYYYMMFormat) {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
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

}
