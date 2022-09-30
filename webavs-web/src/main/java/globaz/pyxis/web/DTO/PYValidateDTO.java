package globaz.pyxis.web.DTO;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.domaine.*;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.naos.web.exceptions.AFInternalException;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYInternalException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PYValidateDTO {

    private static final Logger logger = LoggerFactory.getLogger(PYValidateDTO.class);
    private static final List<String> validLanguage = Arrays.asList(
            CodeLangue.FR.getValue(),
            CodeLangue.DE.getValue(),
            CodeLangue.IT.getValue(),
            CodeLangue.RM.getValue(),
            CodeLangue.EN.getValue()
    );

    /**
     * M�thode pour s'assurer de la validit� des donn�es du DTO pour cr�ation.
     *
     * @param dto
     * @return true si toutes les v�rifications passent sans encombres
     */
    public static Boolean isValidForCreation(PYTiersDTO dto) {
        checkValidity(dto);

        return true;
    }

    /**
     * M�thode pour s'assurer de la validit� des donn�es du DTO pour modification.
     *
     * @param dto
     * @return true si toutes les v�rifications passent sans encombres
     */
    public static Boolean isValidForUpdate(PYTiersDTO dto) throws PYBadRequestException {
        // TODO: Implement validation on PYTiersDTO's fields for page 2, etc. (maybe in other methods ?)

        checkValidity(dto);
        if (dto.getModificationDate() != null) {
            checkModificationDate(dto.getModificationDate());
        }

        if (Boolean.FALSE.equals(dto.getIsPhysicalPerson())) {
            // Set those fields to "" since they are not possible for a legal person. They will be reseted to a default value in PRTiersHelper
            dto.setNss("");
            dto.setBirthDate("");
            dto.setDeathDate("");
            dto.setSex(Sexe.UNDEFINDED.getCodeSysteme().toString()); // This one needs to be reseted directly to "0"
            dto.setCivilStatus("");
            dto.setNationality("");
        }

        return true;
    }

    public static Boolean isValidForAddress(PYAddressDTO pyAddressDTO) throws PYBadRequestException {
        if (pyAddressDTO.getDomainAddress() != null)
            getDomainAddressAsSystemCode(pyAddressDTO);
        if (pyAddressDTO.getTypeAddress() != null)
            getTypeAddressAsSystemCode(pyAddressDTO);
        if (pyAddressDTO.getCountry() != null)
            getCountryAsSystemCode(pyAddressDTO);

        return true;
    }

    public static Boolean isValidPage1(PYTiersPage1DTO dto) {
        checkValidityPage1(dto);

        return true;
    }


    /**
     * M�thode pour s'assurer de la validit� des donn�es du DTO.
     * Attention: Certains check peuvent set des donn�es du DTO � des valeurs par d�faut et des codes syst�mes !
     * <p>
     * Comme un field � null indique l'absence de modification (et pas la modification pour mettre � la valeur null),
     * on v�rifie que les fields ne soient pas � null avant d'appeller les m�thodes de check.
     *
     * @param dto
     */
    private static void checkValidity(PYTiersDTO dto) {
        checkValidityPage1(dto);
        checkValidityAddresses(dto);
        checkValidityContacts(dto);
    }

    /**
     * @param dto
     */
    public static void checkValidityPage1(PYTiersPage1DTO dto) {
        if (dto.getTitle() != null)
            getTitleAsSystemCode(dto);
        if (dto.getLanguage() != null)
            checkAndSetLanguageAsSystemCode(dto);
        if (Boolean.TRUE.equals(dto.getIsPhysicalPerson())) {
            if (dto.getNss() != null)
                checkNSS(dto);
            if (dto.getBirthDate() != null)
                checkBirthdate(dto);
            if (dto.getDeathDate() != null)
                checkDeathdate(dto);
            if (dto.getSex() != null)
                checkAndSetSexAsSystemCode(dto);
            if (dto.getCivilStatus() != null)
                checkAndSetCivilStatusAsSystemCode(dto);
            if (dto.getNationality() != null)
                getNationalityAsSystemCode(dto);
        } else if (Boolean.FALSE.equals(dto.getIsPhysicalPerson())) { // If it's a legal person, make sure the user knows what they're doing by making sure they're not modifying impossible fields
            if (dto.getNss() != null)
                throw new PYBadRequestException("Le NSS ne doit pas �tre renseign� pour une personne morale.");
            if (dto.getBirthDate() != null)
                throw new PYBadRequestException("La date de naissance ne doit pas �tre renseign�e pour une personne morale.");
            if (dto.getDeathDate() != null)
                throw new PYBadRequestException("La date de d�c�s ne doit pas �tre renseign�e pour une personne morale.");
            if (dto.getSex() != null)
                throw new PYBadRequestException("Le sexe ne doit pas �tre renseign� pour une personne morale.");
            if (dto.getCivilStatus() != null)
                throw new PYBadRequestException("L'�tat civil ne doit pas �tre renseign� pour une personne morale.");
            if (dto.getMaidenName() != null)
                throw new PYBadRequestException("Le nom de jeune fille ne doit pas �tre renseign� pour une personne morale.");
            if (dto.getNationality() != null)
                throw new PYBadRequestException("La nationalit� ne doit pas �tre renseign�e pour une personne morale.");

            // (Re)set those fields to 0 for a legal person
            resetFieldsForLegalPerson(dto);
        }
    }

    /**
     * M�thode pour reset les champs pour une personne morale.
     * <p>Le changement d'une personne physique � personne morale n�cessite de reset certains champs.</p>
     *
     * @param dto
     */
    public static void resetFieldsForLegalPerson(PYTiersPage1DTO dto) {
        dto.setNss("");
        dto.setBirthDate("");
        dto.setDeathDate("");
        dto.setSex("0");
        dto.setCivilStatus("");
        dto.setMaidenName("");
        //bypass Pyxis auto set nationality for non physical person
        dto.setNationality("0");
    }

    /**
     * @param dto
     */
    private static void checkValidityAddresses(PYTiersDTO dto) {
        for (PYAddressDTO addressDTO : dto.getAddresses()) {
            if (addressDTO.getDomainAddress() != null)
                getDomainAddressAsSystemCode(addressDTO);
            if (addressDTO.getTypeAddress() != null)
                getTypeAddressAsSystemCode(addressDTO);
            if (addressDTO.getCountry() != null)
                getCountryAsSystemCode(addressDTO);
        }

        for (PYPaymentAddressDTO paymentAddressDTO : dto.getPaymentAddress()) {
            if (paymentAddressDTO.getCcpNumber() != null)
                checkCCP(paymentAddressDTO.getCcpNumber());
        }
    }

    /**
     * Contr�le la validit� des moyens de communication dans les contacts
     *
     * @param dto
     */
    private static void checkValidityContacts(PYTiersDTO dto) {
        for (PYContactCreateDTO contactDTO : dto.getContacts()) {
            for (PYMeanOfCommunicationDTO meanDTO : contactDTO.getMeansOfCommunication()) {
                if (meanDTO.getApplicationDomain() != null)
                    checkApplicationDomain(meanDTO.getApplicationDomain());
                if (meanDTO.getMeanOfCommunicationType() != null)
                    checkMeanOfCommuncationType(meanDTO.getMeanOfCommunicationType());
            }
        }
    }

    /**
     * Lis title pour retourner le code syst�me associ�.
     *
     * @param dto
     */
    private static final void getTitleAsSystemCode(PYTiersPage1DTO dto) throws PYBadRequestException {
        switch ((dto.getTitle() != null) ? JadeStringUtil.toLowerCase(dto.getTitle()) : "") {
            case "monsieur":
            case "m":
                dto.setTitle(ITITiers.CS_MONSIEUR);
                break;
            case "madame":
            case "mme":
                dto.setTitle(ITITiers.CS_MADAME);
                break;
            case "madame, monsieur":
                dto.setTitle(ITITiers.CS_HORIE);
                break;
            case "":
                dto.setTitle(Titre.UNDEFINED.getCodeSysteme().toString());
                break;
            default: // If the title isn't anything standard, check that it's a valid system code
                try {
                    Titre.parse(dto.getTitle());
                } catch (IllegalArgumentException e) {
                    throw new PYBadRequestException("Erreur lors de l'assignation du titre du tiers.", e);
                }
        }
    }

    /**
     * Si le dto repr�sente une personne morale, set le NSS � "". Sinon, v�rifier la validit� du NSS et lancer une erreur s'il faut
     *
     * @param dto
     */
    private static final void checkNSS(PYTiersPage1DTO dto) {
        if (Boolean.FALSE.equals(dto.getIsPhysicalPerson())) {
            dto.setNss("");
        } else {
            if (!NSSUtils.checkNSS(dto.getNss())) {
                logger.error("Erreur dans le format du NSS");
                throw new PYBadRequestException("Erreur dans le format du NSS");
            }
        }
    }

    /**
     * M�thode v�rifiant le format de birthdate.
     * Si birthdate n'est pas renseign�e ou mis � 0, on la set � 0.
     *
     * @param dto
     */
    private static final void checkBirthdate(PYTiersPage1DTO dto) {
        String birthDate = dto.getBirthDate();
        if (JadeStringUtil.isBlankOrZero(birthDate)) {
            dto.setBirthDate("0");
        } else {
            try {
                checkDate(birthDate);
            } catch (PYInternalException e) {
                throw new PYInternalException("Erreur lors de la validation de la date de naissance du tiers.", e);
            }
        }
    }

    /**
     * M�thode v�rifiant le format de deathDate.
     * Si deathDate n'est pas renseign�e ou mis � 0, on la set � 0.
     *
     * @param dto
     */
    private static final void checkDeathdate(PYTiersPage1DTO dto) {
        String deathDate = dto.getDeathDate();
        if (JadeStringUtil.isBlankOrZero(deathDate)) {
            dto.setDeathDate("0");
        } else {
            try {
                checkDate(deathDate);
            } catch (PYInternalException e) {
                throw new PYInternalException("Erreur lors de la validation de la date de d�c�s du tiers.", e);
            }
        }
    }

    /**
     * M�thode qui v�rifie si date est au format dd.mm.yyyy et lance une exception si besoin
     *
     * @param date
     */
    private static final void checkDate(String date) throws PYBadRequestException {
        String pattern = "(0[1-9]|[12]\\d|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}";
        if (!Pattern.matches(pattern, date)) {
            logger.error("Erreur lors de la validation d'une date du tiers. Elle doit �tre au format dd.mm.yyyy.");
            throw new PYBadRequestException("Erreur lors de la validation d'une date du tiers. Elle doit �tre au format dd.mm.yyyy.");
        }
    }

    /**
     * M�thode qui v�rifie si date est au format ddmmyyyy et lance une exception si besoin
     *
     * @param date
     */
    private static final void checkModificationDate(String date) throws PYBadRequestException {
        String pattern = "(0[1-9]|[12]\\d|3[01])(0[1-9]|1[0-2])\\d{4}";
        if (!Pattern.matches(pattern, date)) {
            logger.error("Erreur lors de la validation de la date de modification. Elle doit �tre au format ddmmyyyy.");
            throw new PYBadRequestException("Erreur lors de la validation de la date de modification. Elle doit �tre au format ddmmyyyy.");
        }
    }

    /**
     * Si la valeur pour le sexe dans dto est une formule "standard", change cette valeur pour le code syst�me.
     * Sinon, si la valeur est num�rique, on v�rifie que le code syst�me existe et d�signe un code syst�me pour le sexe.
     * Lance une exception sinon.
     *
     * @param dto
     */
    private static final void checkAndSetSexAsSystemCode(PYTiersPage1DTO dto) {
        switch ((dto.getSex() != null) ? JadeStringUtil.toLowerCase(dto.getSex()) : "") {
            case "m":
            case "homme":
            case "male":
                dto.setSex(Sexe.HOMME.getCodeSysteme().toString());
                break;
            case "f":
            case "femme":
            case "female":
                dto.setSex(Sexe.FEMME.getCodeSysteme().toString());
                break;
            case "":
                dto.setSex(Sexe.UNDEFINDED.getCodeSysteme().toString());
                break;
            default: // If sex isn't anything standard, check that it's a valid system code
                try {
                    Sexe.parse(dto.getSex()); // If this goes through without error, sex is a valid sex
                } catch (IllegalArgumentException e) {
                    logger.error("Erreur lors de l'assignation du sexe du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation du sexe du tiers.", e);
                }
                dto.setSex(Sexe.UNDEFINDED.getCodeSysteme().toString());
        }
    }

    /**
     * M�thode pour v�rifier que civilStatus est un code syst�me
     *
     * @param dto
     */
    private static final void checkAndSetCivilStatusAsSystemCode(PYTiersPage1DTO dto) {
        try {
            if (Boolean.TRUE.equals(dto.getIsPhysicalPerson())) {
                EtatCivil.parse(dto.getCivilStatus()); // If this goes through without error, civilStatus is a valid civil status
            } else {
                dto.setCivilStatus("0");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de l'assignation de l'�tat civil du tiers.");
            throw new PYBadRequestException("Erreur lors de l'assignation de l'�tat civil du tiers.", e);
        }
    }

    /**
     * M�thode pour v�rifier que status est un code syst�me valide
     * TODO : Est'il dispensable ?
     *
     * @param status
     */
    private static final void checkStatusPaymentAddress(String status) {
        try {
            StatusPaymentAddress.parse(status); // If this goes through without error, status has a valid value
        } catch (IllegalArgumentException e) {
            logger.error("Erreur dans le statut de l'addresse de paiement.");
            throw new PYBadRequestException("Erreur dans le statut de l'addresse de paiement.", e);
        }
    }

    /**
     * M�thode pour v�rifier que type est un code syst�me valide
     *
     * @param type
     */
    private static final void checkMeanOfCommuncationType(String type) {
        if (!TypeContact.isValid(type)) {
            logger.error("Erreur dans le type de moyen de communication lors de l'ajout d'un contact.");
            throw new PYBadRequestException("Erreur dans le type de moyen de communication lors de l'ajout d'un contact.");
        }
    }

    /**
     * M�thode pour v�rifier que applicationDomain est un code syst�me valide
     *
     * @param applicationDomain
     */
    private static final void checkApplicationDomain(String applicationDomain) {
        try {
            DomaineApplication.parse(applicationDomain); // If this goes through without error, applicationDomain has a valid value
        } catch (IllegalArgumentException e) {
            logger.error("Erreur dans le domaine d'application lors de l'ajout d'un contact.");
            throw new PYBadRequestException("Erreur dans le domaine d'application lors de l'ajout d'un contact.", e);
        }
    }

    /**
     * Si la langue est fournie avec un mot ("English", "FR", "rUmAntScH", etc.), le map vers le bon code syst�me.
     * Sinon, on v�rifie que c'est un code syst�me valide et on l�ve une erreur si besoin.
     *
     * @param dto
     */
    private static final void checkAndSetLanguageAsSystemCode(PYTiersPage1DTO dto) {
        switch ((dto.getLanguage() != null) ? JadeStringUtil.toLowerCase(dto.getLanguage()) : "") {
            case "fr":
            case "fran�ais":
                dto.setLanguage(CodeLangue.FR.getValue());
                break;
            case "de":
            case "deutsch":
                dto.setLanguage(CodeLangue.DE.getValue());
                break;
            case "en":
            case "english":
                dto.setLanguage(CodeLangue.EN.getValue());
                break;
            case "it":
            case "italiano":
                dto.setLanguage(CodeLangue.IT.getValue());
                break;
            case "rm":
            case "rumantsch":
                dto.setLanguage(CodeLangue.RM.getValue());
                break;
            default: // If the language isn't one of those, check if it's a valid system code and throw an error if needed
                if (!PYValidateDTO.validLanguage.contains(dto.getLanguage())) {
                    logger.error("Erreur lors de l'assignation de la langue du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation de la langue du tiers.");
                }
        }
    }


    /**
     * M�thode pour v�rifier que domainAddress soit un code syst�me valide d�signant un domaine
     *
     * @param dto
     */
    private static final void getDomainAddressAsSystemCode(PYAddressDTO dto) {
        if (!JadeStringUtil.isBlankOrZero(dto.getDomainAddress())) {
            dto.setDomainAddress(DomaineApplication.parse(dto.getDomainAddress()).getSystemCode().toString());
        } else {
            logger.error("Erreur lors de l'assignation du domaine");
            throw new PYBadRequestException("Erreur lors de l'assignation du domaine");
        }
    }

    /**
     * M�thode pour v�rifier que typeAddress soit un code syst�me valide d�signant un type d'adresse
     *
     * @param dto
     */
    private static final void getTypeAddressAsSystemCode(PYAddressDTO dto) {
        if (!JadeStringUtil.isBlankOrZero(dto.getTypeAddress())) {
            if (dto.getTypeAddress().equals(AdresseService.CS_TYPE_COURRIER) || dto.getTypeAddress().equals(AdresseService.CS_TYPE_DOMICILE))
                dto.setTypeAddress(dto.getTypeAddress());
        } else {
            logger.error("Erreur lors de l'assignation du type d'adresse");
            throw new PYBadRequestException("Erreur lors du type d'adresse");
        }
    }

    /**
     * M�thode pour v�rifier que nationality soit un code syst�me valide d�signant un pays
     *
     * @param dto
     */
    private static final void getNationalityAsSystemCode(PYTiersPage1DTO dto) {
        if (!JadeStringUtil.isBlankOrZero(dto.getNationality())) {
            CodesSysPays codeSystemPays = CodesSysPays.parse(dto.getNationality());
            if (codeSystemPays != CodesSysPays.NATIONALIT�INCONNUE) {
                dto.setNationality(codeSystemPays.getCodeSystem().substring(codeSystemPays.getCodeSystem().length() - 3));
            } else {
                logger.error("Erreur lors de l'assignation de la nationalit�");
                throw new PYBadRequestException("Erreur lors de l'assignation de la nationalit�");
            }
        }
    }

    /**
     * M�thode pour v�rifier que country soit un code syst�me valide d�signant un pays
     *
     * @param dto
     */
    private static final void getCountryAsSystemCode(PYAddressDTO dto) {
        if (!JadeStringUtil.isBlankOrZero(dto.getCountry())) {
            CodesSysPays codeSystemPays = CodesSysPays.parse(dto.getCountry());
            if (codeSystemPays != CodesSysPays.NATIONALIT�INCONNUE) {
                dto.setCountry(codeSystemPays.getCodeSystem().substring(codeSystemPays.getCodeSystem().length() - 3));
            } else {
                logger.error("Erreur lors de l'assignation du pays");
                throw new PYBadRequestException("Erreur lors de l'assignation du pays");
            }
        }
    }

    /**
     * M�thode qui v�rifie si ccp est au format xx-xxxxxx-x et lance une exception si besoin
     *
     * @param ccp
     */
    private static final void checkCCP(String ccp) throws PYBadRequestException {
        String pattern = "\\d{2}\\-\\d{6}\\-\\d{1}";
        if (!Pattern.matches(pattern, ccp)) {
            logger.error("Erreur lors de la validation du CCP. Elle doit �tre au format xx-xxxxxx-x.");
            throw new PYBadRequestException("Erreur lors de la validation du CCP. Elle doit �tre au format xx-xxxxxx-x.");
        }
    }


    public static void checkIfExist(PYLienEntreTiersDTO pyLienEntreTiersDTO) {
        TICompositionTiersManager manager = new TICompositionTiersManager();
        manager.setForIdTiersParent(pyLienEntreTiersDTO.getIdTiersPrincipal());
        manager.setForIdTiersEnfant(pyLienEntreTiersDTO.getIdTiersSecondaire());
        try {
            manager.find(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(), 10, true);
        } catch (Exception e) {
            logger.error(MessageFormat.format("PYValidateDTO.checkIfExist() : {0}", e.getMessage()));
            throw new PYInternalException(e);
        }
        if (!manager.isEmpty()) {
            logger.error("Lien avec le conjoint existant");
            throw new PYInternalException("Lien avec le conjoint existant");
        }
    }

    public static void checkIfEmpty(Map<String, String> mapForValidator) {
        List<String> listEmptyValuesMandatory = mapForValidator
                .entrySet()
                .stream()
                .filter(sets -> JadeStringUtil.isEmpty(sets.getValue().trim()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!listEmptyValuesMandatory.isEmpty()) {
            throw new PYBadRequestException("Champs manquant(s) - " + StringUtils.join(listEmptyValuesMandatory, ","));
        }
    }

    public static void verifyCodeSystem(String idCodeSystem, String famille) {
        if (!JadeStringUtil.isEmpty(idCodeSystem)) {
            try {
                //Validation avec MMO et EVID le 23.09.2022 : la m�thode getFamilleCodeSystem ne va remonter que les
                // codes actifs ! Ce qui fait que si on passe un code inactif (FWCOSP.PCODFI = 1), il ne sera pas
                // retrouv� et une exception sera remont�e.
                JadeBusinessServiceLocator.getCodeSystemeService()
                        .getFamilleCodeSysteme(famille).stream()
                        .filter(cs -> idCodeSystem.equals(cs.getIdCodeSysteme()))
                        .findFirst()
                        .orElseThrow(() -> new AFBadRequestException("Le code \"" + idCodeSystem + "\" ne fait pas partie de la famille \"" + famille + "\""));
            } catch (JadePersistenceException e) {
                throw new AFInternalException("Erreur lors de la v�rification du code system \" " + idCodeSystem + " \" => ", e);
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new AFInternalException("Erreur lors de la v�rification du code system \" " + idCodeSystem + " \" => ", e);
            }
        }
    }
}
