package globaz.pyxis.web.DTO;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.pyxis.domaine.EtatCivil;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.pyxis.domaine.Titre;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.web.exceptions.PYBadRequestException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PYValidateDTO {

    // TODO: Implement "validators" for other fields in PYTiersDTO
    private static final List<String> validLanguage = Arrays.asList(
        CodeLangue.FR.getValue(),
        CodeLangue.DE.getValue(),
        CodeLangue.IT.getValue(),
        CodeLangue.RM.getValue(),
        CodeLangue.EN.getValue()
    );

    public static Boolean isValid(PYTiersDTO dto) {
        // TODO: Implement validation on PYTiersDTO's fields for page 2, etc. (maybe in other methods ?)

        getTitleAsSystemCode(dto); // TODO: Check title better than that
        checkNSS(dto);
        checkDates(dto);
        checkAndSetSexAsSystemCode(dto);
        checkAndSetCivilStatusAsSystemCode(dto);
        checkAndSetLanguageAsSystemCode(dto);
        getCountryAsSystemCode(dto); // TODO: Check country better than that

        return true;
    }

    /**
     * Lis title pour retourner le code système associé.
     *
     * @param dto
     */
    private static final void getTitleAsSystemCode(PYTiersDTO dto) {
        if (dto.getTitle() == null) {
            dto.setTitle(Titre.UNDEFINED.getCodeSysteme().toString());
        }

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
     * Si les dates ne sont pas renseignées ou mises à 0, on les set à 0. Sinon, on vérifie si les dates sont au format dd.mm.aaaa et on lance une exception si besoin
     *
     * @param dto
     */
    private static final void checkDates(PYTiersDTO dto) {
        String pattern = "[0-3]\\d\\.[0-1]\\d\\.\\d{4}";

        String birthDate = dto.getBirthDate();
        if (birthDate == "0" || birthDate == null || birthDate == ""){
            dto.setBirthDate("0");
        } else {
            if (!Pattern.matches(pattern, birthDate)) {
                System.err.println("Erreur lors de la validation de la date de naissance du tiers.");
                throw new PYBadRequestException("Erreur lors de la validation de la date de naissance du tiers.");
            }
        }

        String deathDate = dto.getDeathDate();
        if (deathDate == "0" || deathDate == null || deathDate == ""){
            dto.setDeathDate("0");
        } else {
            if (!Pattern.matches(pattern, deathDate)) {
                System.err.println("Erreur lors de la validation de la date de décès du tiers.");
                throw new PYBadRequestException("Erreur lors de la validation de la date de décès du tiers.");
            }
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
                if (!PYValidateDTO.validLanguage.contains(dto.getLanguage()))
                    System.err.println("Erreur lors de l'assignation de la langue du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation de la langue du tiers.");
        }
    }

    /**
     * Méthode pour vérifier que country ressemble à un code système
     *
     * @param dto
     */
    private static final void getCountryAsSystemCode(PYTiersDTO dto) {
        //TODO get codeSystem from enum
        if (dto.getCountry() != null && dto.getCountry() != "") {
            CodeIsoPays codeIsoPays = CodeIsoPays.parse(dto.getCountry());
            if (codeIsoPays != CodeIsoPays.INCONNU) {
                dto.setCountry("100");
            } else {
                System.err.println("Erreur lors de l'assignation du pays");
                throw new PYBadRequestException("Erreur lors de l'assignation du pays");
            }
        }
    }

    /**
     * Méthode pour vérifier que code ressemble à un code système
     *
     * @param code le code système de la requête
     * @return true si ça ressemble à un code système
     */
    private static final boolean isSystemCode(String code) {
        int codeAsInt;
        try {
            codeAsInt = Integer.parseInt(code);
            if (codeAsInt < 0){ // TODO: This if is bad. We need to check if the system code actually exists (custom or generic)
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
