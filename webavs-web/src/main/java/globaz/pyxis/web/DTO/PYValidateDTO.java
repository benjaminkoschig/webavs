package globaz.pyxis.web.DTO;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.pyxis.domaine.CodesSysPays;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.domaine.StatusPaymentAddress;
import ch.globaz.pyxis.domaine.Titre;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYInternalException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PYValidateDTO {

    private static final List<String> validLanguage = Arrays.asList(
        CodeLangue.FR.getValue(),
        CodeLangue.DE.getValue(),
        CodeLangue.IT.getValue(),
        CodeLangue.RM.getValue(),
        CodeLangue.EN.getValue()
    );

    /**
     * Méthode pour s'assurer de la validité des données du DTO pour création.
     *
     * @param dto
     * @return true si toutes les vérifications passent sans encombres
     */
    public static Boolean isValidForCreation(PYTiersDTO dto) {
        // TODO: Implement validation on PYTiersDTO's fields for page 2, etc. (maybe in other methods ?)

        checkValidity(dto);

        return true;
    }

    /**
     * Méthode pour s'assurer de la validité des données du DTO pour modification.
     *
     * @param dto
     * @return true si toutes les vérifications passent sans encombres
     */
    public static Boolean isValidForUpdate(PYTiersUpdateDTO dto) throws PYBadRequestException {
        // TODO: Implement validation on PYTiersDTO's fields for page 2, etc. (maybe in other methods ?)

        checkValidity(dto);
        if (dto.getModificationDate() != null) {
            checkModificationDate(dto.getModificationDate());
        }

        if (!dto.getIsPhysicalPerson()) {
            // Set those fields to "" since they are not possible for a legal person. They will be reseted to a default value in PRTiersHelper
            dto.setNss("");
            dto.setBirthDate("");
            dto.setDeathDate("");
            dto.setSex(Sexe.UNDEFINDED.getCodeSysteme().toString()); // This one needs to be reseted directly to "0"
            dto.setCivilStatus("");
            dto.setCountry("");
        }

        return true;
    }

    /**
     * Méthode pour s'assurer de la validité des données du DTO.
     * Attention: Certains check peuvent set des données du DTO à des valeurs par défaut et des codes systèmes !
     *
     * Comme un field à null indique l'absence de modification (et pas la modification pour mettre à la valeur null),
     * on vérifie que les fields ne soient pas à null avant d'appeller les méthodes de check.
     *
     * @param dto
     */
    private static void checkValidity(PYTiersDTO dto) {
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
                throw new PYBadRequestException("Le NSS ne doit pas être renseigné pour une personne morale.");
            if (dto.getBirthDate() != null)
                throw new PYBadRequestException("La date de naissance ne doit pas être renseignée pour une personne morale.");
            if (dto.getDeathDate() != null)
                throw new PYBadRequestException("La date de décès ne doit pas être renseignée pour une personne morale.");
            if (dto.getSex() != null)
                throw new PYBadRequestException("Le sexe ne doit pas être renseigné pour une personne morale.");
            if (dto.getCivilStatus() != null)
                throw new PYBadRequestException("L'état civil ne doit pas être renseigné pour une personne morale.");
            if (dto.getNationality() != null)
                throw new PYBadRequestException("La nationalité ne doit pas être renseignée pour une personne morale.");
        }

        if (dto.getCcpNumber() != null) {
            checkCCP(dto.getCcpNumber());
        }
        if (dto.getStatus() != null) {
            checkStatusPaymentAddress(dto.getStatus());
        }
    }

    /**
     * Lis title pour retourner le code système associé.
     *
     * @param dto
     */
    private static final void getTitleAsSystemCode(PYTiersDTO dto) throws PYBadRequestException {
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
     * Si le dto représente une personne morale, set le NSS à "". Sinon, vérifier la validité du NSS et lancer une erreur s'il faut
     *
     * @param dto
     */
    private static final void checkNSS(PYTiersDTO dto) {
        if (!dto.getIsPhysicalPerson()) {
            dto.setNss("");
        } else {
            if (!NSSUtils.checkNSS(dto.getNss())) {
                System.err.println("Erreur dans le format du NSS");
                throw new PYBadRequestException("Erreur dans le format du NSS");
            }
        }
    }

    /**
     * Méthode vérifiant le format de birthdate.
     * Si birthdate n'est pas renseignée ou mis à 0, on la set à 0.
     *
     * @param dto
     */
    private static final void checkBirthdate(PYTiersDTO dto) {
        String birthDate = dto.getBirthDate();
        if (birthDate == "0" || birthDate == null || birthDate == ""){
            dto.setBirthDate("0");
        } else {
            try {
                checkDate(birthDate);
            }
            catch (PYInternalException e) {
                throw new PYInternalException("Erreur lors de la validation de la date de naissance du tiers.", e);
            }
        }
    }

    /**
     * Méthode vérifiant le format de deathDate.
     * Si deathDate n'est pas renseignée ou mis à 0, on la set à 0.
     *
     * @param dto
     */
    private static final void checkDeathdate(PYTiersDTO dto) {
        String deathDate = dto.getDeathDate();
        if (deathDate == "0" || deathDate == null || deathDate == ""){
            dto.setDeathDate("0");
        } else {
            try {
                checkDate(deathDate);
            }
            catch (PYInternalException e) {
                throw new PYInternalException("Erreur lors de la validation de la date de décès du tiers.", e);
            }
        }
    }

    /**
     * Méthode qui vérifie si date est au format dd.mm.yyyy et lance une exception si besoin
     *
     * @param date
     */
    private static final void checkDate(String date) throws PYBadRequestException {
        String pattern = "(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}";
        if (!Pattern.matches(pattern, date)) {
            System.err.println("Erreur lors de la validation d'une date du tiers. Elle doit être au format dd.mm.yyyy.");
            throw new PYBadRequestException("Erreur lors de la validation d'une date du tiers. Elle doit être au format dd.mm.yyyy.");
        }
    }

    /**
     * Méthode qui vérifie si date est au format ddmmyyyy et lance une exception si besoin
     *
     * @param date
     */
    private static final void checkModificationDate(String date) throws PYBadRequestException {
        String pattern = "(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])\\d{4}";
        if (!Pattern.matches(pattern, date)) {
            System.err.println("Erreur lors de la validation de la date de modification. Elle doit être au format ddmmyyyy.");
            throw new PYBadRequestException("Erreur lors de la validation de la date de modification. Elle doit être au format ddmmyyyy.");
        }
    }

    /**
     * Si la valeur pour le sexe dans dto est une formule "standard", change cette valeur pour le code système.
     * Sinon, si la valeur est numérique, on vérifie que le code système existe et désigne un code système pour le sexe.
     * Lance une exception sinon.
     *
     * @param dto
     */
    private static final void checkAndSetSexAsSystemCode(PYTiersDTO dto) {
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
                }
                catch (IllegalArgumentException e) {
                    System.err.println("Erreur lors de l'assignation du sexe du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation du sexe du tiers.", e);
                }
                dto.setSex(Sexe.UNDEFINDED.getCodeSysteme().toString());
        }
    }

    /**
     * Méthode pour vérifier que civilStatus est un code système
     *
     * @param dto
     */
    private static final void checkAndSetCivilStatusAsSystemCode(PYTiersDTO dto) {
        try {
            if (dto.getIsPhysicalPerson()) {
                EtatCivil.parse(dto.getCivilStatus()); // If this goes through without error, civilStatus is a valid civil status
            } else {
                dto.setCivilStatus("0");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors de l'assignation de l'état civil du tiers.");
            throw new PYBadRequestException("Erreur lors de l'assignation de l'état civil du tiers.", e);
        }
    }

    /**
     * Méthode pour vérifier que status est un code système valide
     *
     * @param status
     */
    private static final void checkStatusPaymentAddress(String status) {
        try {
            StatusPaymentAddress.parse(status); // If this goes through without error, status has a valid value
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur dans le statut de l'addresse de paiement.");
            throw new PYBadRequestException("Erreur dans le statut de l'addresse de paiement.", e);
        }
    }

    /**
     * Si la langue est fournie avec un mot ("English", "FR", "rUmAntScH", etc.), le map vers le bon code système.
     * Sinon, on vérifie que c'est un code système valide et on lève une erreur si besoin.
     *
     * @param dto
     */
    private static final void checkAndSetLanguageAsSystemCode(PYTiersDTO dto) {
        switch ((dto.getLanguage() != null) ? JadeStringUtil.toLowerCase(dto.getLanguage()) : "") {
            case "fr":
            case "français":
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
                    System.err.println("Erreur lors de l'assignation de la langue du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation de la langue du tiers.");
                }
        }
    }

    /**
     * Méthode pour vérifier que country soit un code système valide désignant un pays
     *
     * @param dto
     */
    private static final void getNationalityAsSystemCode(PYTiersDTO dto) {
        if (dto.getCountry() != null && dto.getCountry() != "") {
            CodesSysPays codeSystemPays = CodesSysPays.parse(dto.getCountry());
            if (codeSystemPays != CodesSysPays.NATIONALITÉINCONNUE) {
                dto.setCountry(codeSystemPays.getCodeSystem().substring(codeSystemPays.getCodeSystem().length() - 3));
            } else {
                System.err.println("Erreur lors de l'assignation du pays");
                throw new PYBadRequestException("Erreur lors de l'assignation du pays");
            }
        }
    }

    /**
     * Méthode qui vérifie si date est au format dd.mm.yyyy et lance une exception si besoin
     *
     * @param ccp
     */
    private static final void checkCCP(String ccp) throws PYBadRequestException {
        String pattern = "\\d{2}\\-\\d{6}\\-\\d{1}";
        if (!Pattern.matches(pattern, ccp)) {
            System.err.println("Erreur lors de la validation du CCP. Elle doit être au format xx-xxxxxx-x.");
            throw new PYBadRequestException("Erreur lors de la validation du CCP. Elle doit être au format xx-xxxxxx-x.");
        }
    }
}
