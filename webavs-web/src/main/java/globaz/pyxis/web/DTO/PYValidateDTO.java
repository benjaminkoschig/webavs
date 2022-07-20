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
     * Lis title pour retourner le code syst�me associ�.
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
     * Si les dates ne sont pas renseign�es ou mises � 0, on les set � 0. Sinon, on v�rifie si les dates sont au format dd.mm.aaaa et on lance une exception si besoin
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
                System.err.println("Erreur lors de la validation de la date de d�c�s du tiers.");
                throw new PYBadRequestException("Erreur lors de la validation de la date de d�c�s du tiers.");
            }
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
                if (!PYValidateDTO.validLanguage.contains(dto.getLanguage()))
                    System.err.println("Erreur lors de l'assignation de la langue du tiers.");
                    throw new PYBadRequestException("Erreur lors de l'assignation de la langue du tiers.");
        }
    }

    /**
     * M�thode pour v�rifier que country ressemble � un code syst�me
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
     * M�thode pour v�rifier que code ressemble � un code syst�me
     *
     * @param code le code syst�me de la requ�te
     * @return true si �a ressemble � un code syst�me
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
