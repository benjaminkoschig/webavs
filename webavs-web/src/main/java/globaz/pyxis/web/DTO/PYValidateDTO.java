package globaz.pyxis.web.DTO;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.pyxis.domaine.CodesSysPays;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Sexe;
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
     * M�thode pour s'assurer de la validit� des donn�es du DTO de cr�ation.
     * Attention: Certains check peuvent set des donn�es du DTO � des valeurs par d�faut et des codes syst�mes !
     *
     * @param dto
     * @return true si toutes les v�rifications passent sans encombres
     */
    public static Boolean isValidForCreation(PYTiersDTO dto) {
        // TODO: Implement validation on PYTiersDTO's fields for page 2, etc. (maybe in other methods ?)

        getTitleAsSystemCode(dto);
        checkNSS(dto);
        checkBirthdate(dto);
        checkDeathdate(dto);
        checkAndSetSexAsSystemCode(dto);
        checkAndSetCivilStatusAsSystemCode(dto);
        checkAndSetLanguageAsSystemCode(dto);
        getCountryAsSystemCode(dto);

        return true;
    }

    /**
     * M�thode pour s'assurer de la validit� des donn�es du DTO pour modification.
     * Comme un field � null indique l'absence de modification (et pas la modification pour mettre � la valeur null),
     * on v�rifie que les fields ne soient pas � null avant d'appeller les m�thodes de check.
     *
     * @param dto
     * @return true si toutes les v�rifications passent sans encombres
     */
    public static Boolean isValidForUpdate(PYTiersDTO dto) {
        // TODO: Implement validation on PYTiersDTO's fields for page 2, etc. (maybe in other methods ?)

        if (dto.getTitle() != null)
            getTitleAsSystemCode(dto);
        if (dto.getLanguage() != null)
            checkAndSetLanguageAsSystemCode(dto);
        if (Boolean.TRUE.equals(dto.getIsPhysicalPerson())) { // TODO: This is bad since that if lets unvalidated data through...
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
            if (dto.getCountry() != null)
                getCountryAsSystemCode(dto);
        }

        return true;
    }

    /**
     * Lis title pour retourner le code syst�me associ�.
     *
     * @param dto
     */
    private static final void getTitleAsSystemCode(PYTiersDTO dto) {
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
     * M�thode v�rifiant le format de birthdate.
     * Si birthdate n'est pas renseign�e ou mis � 0, on la set � 0.
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
     * M�thode v�rifiant le format de deathDate.
     * Si deathDate n'est pas renseign�e ou mis � 0, on la set � 0.
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
                throw new PYInternalException("Erreur lors de la validation de la date de d�c�s du tiers.", e);
            }
        }
    }

    /**
     * M�thode qui v�rifie si date est au format dd.mm.aaaa et lance une exception si besoin
     *
     * @param date
     */
    private static final void checkDate(String date) throws PYBadRequestException{
        String pattern = "[0-3]\\d\\.[0-1]\\d\\.\\d{4}";
        if (!Pattern.matches(pattern, date)) {
            System.err.println("Erreur lors de la validation d'une date du tiers.");
            throw new PYBadRequestException("Erreur lors de la validation d'une date du tiers.");
        }
    }

    /**
     * Si la valeur pour le sexe dans dto est une formule "standard", change cette valeur pour le code syst�me.
     * Sinon, si la valeur est num�rique, on v�rifie que le code syst�me existe et d�signe un code syst�me pour le sexe.
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
     * M�thode pour v�rifier que civilStatus est un code syst�me
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
            System.err.println("Erreur lors de l'assignation de l'�tat civil du tiers.");
            throw new PYBadRequestException("Erreur lors de l'assignation de l'�tat civil du tiers.", e);
        }
    }

    /**
     * Si la langue est fournie avec un mot ("English", "FR", "rUmAntScH", etc.), le map vers le bon code syst�me.
     * Sinon, on v�rifie que c'est un code syst�me valide et on l�ve une erreur si besoin.
     *
     * @param dto
     */
    private static final void checkAndSetLanguageAsSystemCode(PYTiersDTO dto) {
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
                    System.err.println("Erreur lors de l'assignation de la langue du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation de la langue du tiers.");
                }
        }
    }

    /**
     * M�thode pour v�rifier que country ressemble � un code syst�me
     *
     * @param dto
     */
    private static final void getCountryAsSystemCode(PYTiersDTO dto) {
        if (dto.getCountry() != null && dto.getCountry() != "") {
            CodesSysPays codeSystemPays = CodesSysPays.parse(dto.getCountry());
            if (codeSystemPays != CodesSysPays.NATIONALIT�INCONNUE) {
                dto.setCountry(codeSystemPays.getCodeSystem().substring(codeSystemPays.getCodeSystem().length() - 3));
            } else {
                System.err.println("Erreur lors de l'assignation du pays");
                throw new PYBadRequestException("Erreur lors de l'assignation du pays");
            }
        }
    }
}
