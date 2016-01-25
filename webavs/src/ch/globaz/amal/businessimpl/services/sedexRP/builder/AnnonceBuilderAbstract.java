package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.constantes.IConstantes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import ch.ech.xmlns.ech_0058._4.SendingApplicationType;
import ch.gdk_cds.xmlns.pv_common._3.AddressType;
import ch.gdk_cds.xmlns.pv_common._3.DeclarationLocalReferenceType;
import ch.gdk_cds.xmlns.pv_common._3.PersonType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.AMGestionTiers;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Class de base des annonces à créer
 * 
 * @author cbu
 * 
 */
public abstract class AnnonceBuilderAbstract {
    public static boolean isAnnonceConfirmed(SimpleAnnonceSedex simpleAnnonceSedex) {
        if ((simpleAnnonceSedex == null) || simpleAnnonceSedex.isNew()) {
            return false;
        }
        if (JadeStringUtil.isBlankOrZero(simpleAnnonceSedex.getNumeroDecision())) {
            return false;
        }
        if (!AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue().equals(simpleAnnonceSedex.getMessageSubType())
                && !AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue().equals(
                        simpleAnnonceSedex.getMessageSubType())) {
            return false;
        }
        try {

            SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();
            simpleAnnonceSedexSearch.setForIdDetailFamille(simpleAnnonceSedex.getIdDetailFamille());
            simpleAnnonceSedexSearch.setForNumeroDecision(simpleAnnonceSedex.getNumeroDecision());
            simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(
                    simpleAnnonceSedexSearch);

            boolean isConfirmed = false;
            boolean isStopped = false;
            for (JadeAbstractModel abstractAnnonce : simpleAnnonceSedexSearch.getSearchResults()) {
                SimpleAnnonceSedex annonce = (SimpleAnnonceSedex) abstractAnnonce;

                // On va regarder si l'annonce a eu une confirmation de décision ET qu'elle n'a pas eu de confirmation
                // d'interruption
                if (AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue().equals(annonce.getMessageSubType())) {
                    isConfirmed = true;
                }
                if (AMMessagesSubTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue().equals(
                        annonce.getMessageSubType())) {
                    isStopped = true;
                }
            }

            if (isConfirmed && !isStopped) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    protected ArrayList<String> arrayErrors = new ArrayList<String>();
    protected SimpleAnnonceSedex simpleAnnonceSedex = null;
    protected SimpleDetailFamille simpleDetailFamille = null;

    public abstract Object createMessage(Object annonceHeader, Object annonceContent);

    public abstract Object generateContent() throws AnnonceSedexException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException;

    protected String generateDecreeId() {
        return JadeUUIDGenerator.createLongUID().toString();
    }

    public abstract Object generateHeader() throws AnnonceSedexException, JadeSedexDirectoryInitializationException,
            DatatypeConfigurationException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Retourne l'action de l'annonce
     * 
     * @param message
     * @return l'action
     */
    public abstract String getAction(Object message);

    public ArrayList<String> getArrayErrors() {
        return arrayErrors;
    }

    public abstract String getBusinessProcessId(Object message);

    public abstract ByteArrayOutputStream getContentMessage(Object message) throws MalformedURLException,
            JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException;

    protected DeclarationLocalReferenceType getDeclarationLocalReferenceType() {
        DeclarationLocalReferenceType declarationLocalReferenceType = new DeclarationLocalReferenceType();

        declarationLocalReferenceType.setName(BSessionUtil.getSessionFromThreadContext().getUserFullName());
        String phone = JadeStringUtil.removeChar(BSessionUtil.getSessionFromThreadContext().getUserInfo().getPhone(),
                ' ');
        // Si aucun no de téléphone n'est défini, on set un no bidon pour éviter une erreur de la validation XSD
        if (JadeStringUtil.isEmpty(phone)) {
            phone = "0000000000";
        }
        declarationLocalReferenceType.setPhone(phone);
        declarationLocalReferenceType.setEmail(BSessionUtil.getSessionFromThreadContext().getUserEMail());
        return declarationLocalReferenceType;
    }

    public abstract String getDecreeId(Object message);

    public abstract ByteArrayOutputStream getHeaderMessage(Object message) throws MalformedURLException,
            JAXBValidationError, JAXBValidationWarning, JAXBException, SAXException, IOException;

    public abstract String getMessageId(Object message);

    public abstract String getMinorVersion(Object message);

    protected BigInteger getNextBusinessProcessId() throws JadePersistenceException, AnnonceSedexException,
            JadeApplicationServiceNotAvailableException {
        SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();
        // BZ9214 - Utilisation de l'idTiers de l'annonce et non celui de la caisse
        simpleAnnonceSedexSearch.setForIdTiersCM(simpleAnnonceSedex.getIdTiersCM());
        simpleAnnonceSedexSearch.setOrderKey("orderByNoCourantDesc");
        // BZ9214 - Utilisation de l'idTiers de l'annonce et non celui de la caisse
        simpleAnnonceSedexSearch.setDefinedSearchSize(1);
        simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(
                simpleAnnonceSedexSearch);

        BigInteger nextNoCourant = null;
        if (simpleAnnonceSedexSearch.getSize() == 0) {
            nextNoCourant = new BigInteger("1");
        } else {
            SimpleAnnonceSedex annonceSedex = (SimpleAnnonceSedex) simpleAnnonceSedexSearch.getSearchResults()[0];
            nextNoCourant = new BigInteger(annonceSedex.getNumeroCourant()).add(new BigInteger("1"));
        }

        return nextNoCourant;
    }

    protected PersonType getPersonType() throws JadePersistenceException, JadeApplicationException {

        if (JadeStringUtil.isBlankOrZero(simpleAnnonceSedex.getIdDetailFamille())) {
            throw new AnnonceSedexException("idDetailFamille can't be null ! AnnonceSedexId : "
                    + simpleAnnonceSedex.getIdAnnonceSedex());
        }

        // Recherche du subside pour avoir l'id famille et trouver la personne...
        SimpleDetailFamille simpleDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                simpleAnnonceSedex.getIdDetailFamille());
        if ((simpleDetailFamille == null) || simpleDetailFamille.isNew()) {
            throw new AnnonceSedexException("SimpleDetailFamille not found with idDetailFamille : "
                    + simpleAnnonceSedex.getIdDetailFamille());
        }

        // Recherche du membre famille
        SimpleFamille simpleFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                simpleDetailFamille.getIdFamille());
        if ((simpleFamille == null) || simpleFamille.isNew()) {
            throw new AnnonceSedexException("SimpleFamille not found with idFamille : "
                    + simpleDetailFamille.getIdFamille());
        }

        if (JadeStringUtil.isBlankOrZero(simpleFamille.getIdTier())) {
            throw new AnnonceSedexException("SimpleFamille.idTiers mandatory : " + simpleDetailFamille.getIdFamille());
        }

        // Recherche du tiers
        PersonneEtendueComplexModel personneEtendueComplexModel = TIBusinessServiceLocator.getPersonneEtendueService()
                .read(simpleFamille.getIdTier());
        if ((personneEtendueComplexModel == null) || personneEtendueComplexModel.isNew()) {
            throw new AnnonceSedexException("PersonneEtendueComplexModel not found with idTier : "
                    + simpleFamille.getIdTier());
        }

        String idCont = simpleAnnonceSedex.getIdContribuable();
        if (JadeStringUtil.isBlankOrZero(idCont)) {
            idCont = this.simpleDetailFamille.getIdContribuable();
        }

        Contribuable cont = null;
        if (!JadeStringUtil.isBlankOrZero(idCont)) {
            cont = AmalServiceLocator.getContribuableService().read(idCont);
        }

        if ((cont == null) || cont.isNew()) {
            throw new AnnonceSedexException("Contribuable can't be null ! AnnonceSedexId : "
                    + simpleAnnonceSedex.getIdAnnonceSedex());
        }

        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());

        PersonType personType = new PersonType();

        if (!JadeStringUtil.isEmpty(personneEtendueComplexModel.getTiers().getDesignation1())) {
            personType.setOfficialName(personneEtendueComplexModel.getTiers().getDesignation1());
        } else {
            throw new AnnonceSedexException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "amal.sedex.personType.officialName.mandatory")
                    + " NNSS : " + personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel());
        }

        if (!JadeStringUtil.isEmpty(personneEtendueComplexModel.getTiers().getDesignation2())) {
            personType.setFirstName(personneEtendueComplexModel.getTiers().getDesignation2());
        } else {
            throw new AnnonceSedexException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "amal.sedex.personType.firstName.mandatory")
                    + " NNSS : " + personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel());
        }

        if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(personneEtendueComplexModel.getPersonne().getSexe())) {
            personType.setSex("1");
        } else if (IConstantes.CS_PERSONNE_SEXE_FEMME.equals(personneEtendueComplexModel.getPersonne().getSexe())) {
            personType.setSex("2");
        } else {
            throw new AnnonceSedexException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "amal.sedex.personType.sexe.mandatory")
                    + " NNSS : " + personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel());
        }

        if (!JadeStringUtil.isBlankOrZero(personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel())) {
            Long l_nAvs = Long.parseLong(JadeStringUtil.removeChar(personneEtendueComplexModel.getPersonneEtendue()
                    .getNumAvsActuel(), '.'));
            personType.setVn(l_nAvs);
        } else {
            throw new AnnonceSedexException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "amal.sedex.personType.nss.mandatory"));
        }

        if (!JadeStringUtil.isBlankOrZero(personneEtendueComplexModel.getPersonne().getDateNaissance())
                && JadeDateUtil.isGlobazDate(personneEtendueComplexModel.getPersonne().getDateNaissance())) {
            XMLGregorianCalendar dob = toXmlDate(personneEtendueComplexModel.getPersonne().getDateNaissance(), false);
            personType.setDateOfBirth(dob);
        } else {
            throw new AnnonceSedexException(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "amal.sedex.personType.dateOfBirth.mandatory")
                    + " ==> "
                    + personneEtendueComplexModel.getPersonne().getDateNaissance()
                    + " NNSS : "
                    + personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel());
        }

        // Recherche de l'adresse en utilisant l'id tiers du contribuable principal
        AdresseTiersDetail currentAdresseStandardDomicile = TIBusinessServiceLocator.getAdresseService()
                .getAdresseTiers(cont.getPersonneEtendue().getTiers().getIdTiers(), true, dateToday,
                        AMGestionTiers.CS_DOMAINE_AMAL, AMGestionTiers.CS_TYPE_DOMICILE, null);

        AddressType addressType = new AddressType();
        if (!(currentAdresseStandardDomicile.getFields() == null)) {
            addressType.setStreet(currentAdresseStandardDomicile.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
            addressType.setHouseNumber(currentAdresseStandardDomicile.getFields().get(
                    AdresseTiersDetail.ADRESSE_VAR_NUMERO));
            addressType
                    .setTown(currentAdresseStandardDomicile.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));

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

            personType.setAddress(addressType);

        } else {
            arrayErrors.add("Adress not found ! idTiers : " + personneEtendueComplexModel.getTiers().getIdTiers()
                    + " NNSS : " + personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel());
            throw new AnnonceSedexException("Adress not found ! idTiers : "
                    + personneEtendueComplexModel.getTiers().getIdTiers() + " NNSS : "
                    + personneEtendueComplexModel.getPersonneEtendue().getNumAvsActuel());
        }

        return personType;
    }

    public abstract String getRecipientId(Object message);

    public abstract String getSenderId(Object message);

    protected SendingApplicationType getSendingApplicationType() {
        SendingApplicationType sat = new SendingApplicationType();
        sat.setManufacturer(IAMSedex.APPLICATION_TYPE_MANUFACTURER);
        sat.setProduct(IAMSedex.APPLICATION_TYPE_PRODUCT_NAME);
        sat.setProductVersion(IAMSedex.APPLICATION_TYPE_PRODUCT_VERSION);

        return sat;
    }

    public SimpleDetailFamille getSimpleDetailFamille() {
        return simpleDetailFamille;
    }

    /**
     * Va chercher le subside associé à l'ID qu'on a dans la table MAMSGSDX
     * 
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AnnonceSedexException
     */
    public SimpleDetailFamille getSubside() throws DetailFamilleException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, AnnonceSedexException {
        SimpleDetailFamille simpleDetailFamille = new SimpleDetailFamille();

        if (!JadeStringUtil.isBlankOrZero(simpleAnnonceSedex.getIdDetailFamille())) {
            simpleDetailFamille = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    simpleAnnonceSedex.getIdDetailFamille());

            if (!simpleDetailFamille.isNew()) {
                // this.simpleAnnonceSedex.setIdTiersCM(simpleDetailFamille.getAdministration().getTiers().getIdTiers());
                return simpleDetailFamille;
            } else {
                throw new AnnonceSedexException("Id subside not found with idDetailFamille '"
                        + simpleAnnonceSedex.getIdDetailFamille() + "'!");
            }
        } else {
            throw new AnnonceSedexException("Subside can't be null or blank ! AnnonceSedexId : "
                    + simpleAnnonceSedex.getIdAnnonceSedex());
        }

    }

    public abstract Object send(Object header, Object content) throws JadeApplicationException,
            JadePersistenceException;

    public void setSimpleDetailFamille(SimpleDetailFamille simpleDetailFamille) {
        this.simpleDetailFamille = simpleDetailFamille;
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

    /**
     * Class utilisée par l'annonce
     * 
     * @return la class de l'annonce
     */
    public abstract Class<?> whichClass();

}
