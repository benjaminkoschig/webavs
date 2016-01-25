package globaz.naos.util;

import globaz.jade.client.util.JadeStringUtil;
import java.text.SimpleDateFormat;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.axis.types.NonNegativeInteger;
import ch.admin.bit.xmlns.uid_wse_f._3.RegisterDeregisterItem;
import ch.admin.bit.xmlns.uid_wse_shared._1.RegisterDeregisterStatus;
import ch.ech.xmlns.ech_0007_f._6.CantonAbbreviationType;
import ch.ech.xmlns.ech_0010_f._6.AddressInformationType;
import ch.ech.xmlns.ech_0010_f._6.CountryType;
import ch.ech.xmlns.ech_0010_f._6.MailAddressType;
import ch.ech.xmlns.ech_0046_f._3.AddressType;
import ch.ech.xmlns.ech_0046_f._3.ContactType;
import ch.ech.xmlns.ech_0097_f._2.OrganisationIdentificationType;
import ch.ech.xmlns.ech_0097_f._2.UidOrganisationIdCategorieType;
import ch.ech.xmlns.ech_0097_f._2.UidStructureType;
import ch.ech.xmlns.ech_0108_f._3.OrganisationType;
import ch.ech.xmlns.ech_0108_f._3.UidregInformationType;

public class IDEServiceMappingUtil {

    public static final String COUNTRY_CH = "CH";
    public static final String ADRESSE_CATEGORY_MAIN = "main";

    public static final String getNumeroIDE(RegisterDeregisterItem registerDeregisterItem) {
        return getNumeroIDE(registerDeregisterItem.getUid());
    }

    public static final String getNumeroIDE(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getOrganisationIdentification().getUid()
                .getUidOrganisationIdCategorie())
                + String.valueOf(organisationType.getOrganisation().getOrganisationIdentification().getUid()
                        .getUidOrganisationId());
    }

    public static final String getDateJJMMYYYY(XMLGregorianCalendar scheduledDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(scheduledDate.toGregorianCalendar().getTime());
    }

    public static final String getNumeroIDE(UidStructureType uidStruct) {
        return String.valueOf(uidStruct.getUidOrganisationIdCategorie())
                + String.valueOf(uidStruct.getUidOrganisationId());
    }

    public static final String getNumeroIDERemplacement(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {

        UidStructureType uidStructureType = organisationType.getUidregInformation().getUidReplacement();

        if (uidStructureType != null) {
            return String.valueOf(uidStructureType.getUidOrganisationIdCategorie())
                    + String.valueOf(uidStructureType.getUidOrganisationId());
        }

        return "";

    }

    public static final String getCanton(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getCantonAbbreviationMainAddress());
    }

    public static final String getNPA(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getContact().getAddress().get(0).getPostalAddress()
                .getAddressInformation().getForeignZipCodeOrSwissZipCodeIdOrSwissZipCode().get(0).getValue());
    }

    public static final String getLocalite(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getContact().getAddress().get(0).getPostalAddress()
                .getAddressInformation().getTown());
    }

    public static final String getRue(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return organisationType.getOrganisation().getContact().getAddress().get(0).getPostalAddress()
                .getAddressInformation().getStreet();
    }

    public static final String getNumeroRue(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return organisationType.getOrganisation().getContact().getAddress().get(0).getPostalAddress()
                .getAddressInformation().getHouseNumber();
    }

    public static final String getCareOf(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getContact().getAddress().get(0).getPostalAddress()
                .getAddressInformation().getAddressLine1());
    }

    public static final String getLegalForm(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getOrganisationIdentification().getLegalForm());
    }

    public static final String getLangue(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getLanguageOfCorrespondance());
    }

    public static final String getOrganisationType(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getUidregInformation().getUidregOrganisationType());
    }

    public static final String getRaisonSociale(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(organisationType.getOrganisation().getOrganisationIdentification().getOrganisationName());
    }

    public static final String getAdresse(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        AddressInformationType adresseInformation = organisationType.getOrganisation().getContact().getAddress().get(0)
                .getPostalAddress().getAddressInformation();
        String adresse = "";
        if (adresseInformation.getStreet() != null) {
            adresse = adresseInformation.getStreet();
        }
        if (adresseInformation.getHouseNumber() != null) {
            if (!JadeStringUtil.isEmpty(adresse)) {
                adresse = adresse + " ";
            }
            adresse = adresse + adresseInformation.getHouseNumber();
        }
        if (adresseInformation.getForeignZipCodeOrSwissZipCodeIdOrSwissZipCode().get(0).getValue() != null) {
            if (!JadeStringUtil.isEmpty(adresse)) {
                adresse = adresse + " - ";
            }
            adresse = adresse + adresseInformation.getForeignZipCodeOrSwissZipCodeIdOrSwissZipCode().get(0).getValue();
        }
        if (adresseInformation.getTown() != null) {
            if (!JadeStringUtil.isEmpty(adresse)) {
                adresse = adresse + " ";
            }
            adresse = adresse + adresseInformation.getTown();
        }
        return adresse;
    }

    public static final RegisterDeregisterStatus getStatut(RegisterDeregisterItem registerDeregisterItem) {
        return registerDeregisterItem.getStatus();
    }

    public static final String getStatut(ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType) {
        return String.valueOf(AFIDEUtil.translateCodeStatut(Integer.valueOf(organisationType.getUidregInformation()
                .getUidregStatusEnterpriseDetail())));
    }

    public static final OrganisationType getStructureForUpdateEntiteIde(IDEDataBean ideDataBean) {

        OrganisationType organisationType = getStructureCommonForCreateUpdateEntiteIde(ideDataBean);

        UidStructureType uidStruct = new UidStructureType();
        NonNegativeInteger uid = new NonNegativeInteger(AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(ideDataBean
                .getNumeroIDE()));

        uidStruct.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
        uidStruct.setUidOrganisationId(uid);

        organisationType.getOrganisation().getOrganisationIdentification().setUid(uidStruct);

        return organisationType;

    }

    public static final OrganisationType getStructureForRadiationEntiteIde(IDEDataBean ideDataBean) {

        OrganisationType organisationType = new OrganisationType();
        organisationType.setOrganisation(new ch.ech.xmlns.ech_0098_f._3.OrganisationType());

        UidStructureType uidStruct = new UidStructureType();
        NonNegativeInteger uid = new NonNegativeInteger(AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(ideDataBean
                .getNumeroIDE()));

        uidStruct.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
        uidStruct.setUidOrganisationId(uid);
        OrganisationIdentificationType organisationIdentification = new OrganisationIdentificationType();
        organisationIdentification.setUid(uidStruct);

        organisationType.getOrganisation().setOrganisationIdentification(organisationIdentification);

        // Indication du motif de fin
        UidregInformationType uidregInformationType = new UidregInformationType();
        uidregInformationType.setUidregLiquidationReason(AFIDEUtil.translateMotifFinGlobazVersIDE(ideDataBean
                .getMotifFin()));
        organisationType.setUidregInformation(uidregInformationType);

        return organisationType;

    }

    public static final UidStructureType getStructureForReactivateEntiteIde(IDEDataBean ideDataBean) {
        UidStructureType uidStruct = new UidStructureType();

        NonNegativeInteger uid = new NonNegativeInteger(AFIDEUtil.giveMeNumIdeUnformatedWithoutPrefix(ideDataBean
                .getNumeroIDE()));
        uidStruct.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
        uidStruct.setUidOrganisationId(uid);
        return uidStruct;
    }

    private static final OrganisationType getStructureCommonForCreateUpdateEntiteIde(IDEDataBean ideDataBean) {

        OrganisationType organisationType = new OrganisationType();

        // Raison sociale
        OrganisationIdentificationType organisationIdentification = new OrganisationIdentificationType();
        organisationIdentification.setOrganisationName(ideDataBean.getRaisonSociale());

        ch.ech.xmlns.ech_0098_f._3.OrganisationType organisation = new ch.ech.xmlns.ech_0098_f._3.OrganisationType();
        // personnalité juridique
        organisationIdentification.setLegalForm(AFIDEUtil.translatePersJuriVersLegalForm(ideDataBean
                .getPersonnaliteJuridique()));

        // ATTENTION ! ne pas modifier car la méthode appelante getStructureForUpdateEntiteIde se base sur le fait
        // qu'une organisation identification est renseignée
        organisation.setOrganisationIdentification(organisationIdentification);

        // Pays, NPA, Localité, Rue
        AddressInformationType adresseInformation = new AddressInformationType();

        CountryType country = new CountryType();
        country.setCountryIdISO2(COUNTRY_CH);
        country.setCountryNameShort(COUNTRY_CH);

        adresseInformation.setCountry(country);

        if (!JadeStringUtil.isEmpty(ideDataBean.getNpa())) {
            JAXBElement<String> swissZipCodeJaxB = new JAXBElement<String>(new QName(
                    "http://www.ech.ch/xmlns/eCH-0010-f/6", "swissZipCode"), String.class, ideDataBean.getNpa());
            adresseInformation.getForeignZipCodeOrSwissZipCodeIdOrSwissZipCode().add(swissZipCodeJaxB);
        }

        adresseInformation.setTown(ideDataBean.getLocalite());
        adresseInformation.setStreet(ideDataBean.getRue());
        adresseInformation.setHouseNumber(ideDataBean.getNumeroRue());
        adresseInformation.setAddressLine1(ideDataBean.getCareOf());

        MailAddressType postalAdress = new MailAddressType();
        postalAdress.setAddressInformation(adresseInformation);

        AddressType addressType = new AddressType();
        addressType.setPostalAddress(postalAdress);
        addressType.setOtherAddressCategory(ADRESSE_CATEGORY_MAIN);

        ContactType contact = new ContactType();
        contact.getAddress().add(addressType);

        organisation.setContact(contact);

        // Canton
        organisationType.setCantonAbbreviationMainAddress(CantonAbbreviationType.valueOf(ideDataBean.getCanton()));

        // Langue
        organisation.setLanguageOfCorrespondance(ideDataBean.getLangue());

        organisationType.setOrganisation(organisation);

        // Type d'entité IDE
        UidregInformationType uidregInformationType = new UidregInformationType();
        uidregInformationType.setUidregOrganisationType(AFIDEUtil
                .translateTypeEntrepriseVersOrganisationType(ideDataBean.getPersonnaliteJuridique()));

        organisationType.setUidregInformation(uidregInformationType);

        return organisationType;

    }

    public static final OrganisationType getStructureForCreateEntiteIde(IDEDataBean ideDataBean) {

        return getStructureCommonForCreateUpdateEntiteIde(ideDataBean);
    }

    public static final ch.ech.xmlns.ech_0108_f._3.OrganisationType getStructureForSearchByNumeroIDE(String numeroIDE) {
        String numeroIDEWithoutPrefixe = IDEUtils.removePrefixeFromNumeroIDE(numeroIDE);

        UidStructureType uidStruct = new UidStructureType();
        NonNegativeInteger uid = new NonNegativeInteger(numeroIDEWithoutPrefixe);

        uidStruct.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
        uidStruct.setUidOrganisationId(uid);

        OrganisationIdentificationType organisationIdentification = new OrganisationIdentificationType();
        organisationIdentification.setUid(uidStruct);

        ch.ech.xmlns.ech_0098_f._3.OrganisationType organisation = new ch.ech.xmlns.ech_0098_f._3.OrganisationType();
        organisation.setOrganisationIdentification(organisationIdentification);

        ch.ech.xmlns.ech_0108_f._3.OrganisationType organisationType = new ch.ech.xmlns.ech_0108_f._3.OrganisationType();
        organisationType.setOrganisation(organisation);

        return organisationType;
    }

    public static final ch.ech.xmlns.ech_0108_f._3.OrganisationType getStructureForSearch(String forRaisonSociale,
            String forNpa, String forLocalite, String forRue, String forNumeroRue) {

        OrganisationType organisationType = new OrganisationType();

        ch.ech.xmlns.ech_0098_f._3.OrganisationType org = new ch.ech.xmlns.ech_0098_f._3.OrganisationType();

        // Recherche par nom
        OrganisationIdentificationType searchNom = new OrganisationIdentificationType();
        searchNom.setOrganisationName("*");
        if (!JadeStringUtil.isBlankOrZero(forRaisonSociale)) {
            searchNom.setOrganisationName(forRaisonSociale);
        }

        org.setOrganisationIdentification(searchNom);

        AddressInformationType adresseInformation = new AddressInformationType();
        adresseInformation.setTown(forLocalite);
        adresseInformation.setStreet(forRue);
        adresseInformation.setHouseNumber(forNumeroRue);

        // SET du NPA
        if (!JadeStringUtil.isEmpty(forNpa)) {
            JAXBElement<String> swissZipCodeJaxB = new JAXBElement<String>(new QName(
                    "http://www.ech.ch/xmlns/eCH-0010-f/6", "swissZipCode"), String.class, forNpa);
            adresseInformation.getForeignZipCodeOrSwissZipCodeIdOrSwissZipCode().add(swissZipCodeJaxB);
        }

        MailAddressType postalAdress = new MailAddressType();
        postalAdress.setAddressInformation(adresseInformation);

        AddressType addressType = new AddressType();
        addressType.setPostalAddress(postalAdress);
        addressType.setOtherAddressCategory(ADRESSE_CATEGORY_MAIN);

        ContactType contact = new ContactType();
        contact.getAddress().add(addressType);

        org.setContact(contact);

        organisationType.setOrganisation(org);

        return organisationType;
    }
}