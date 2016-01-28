package ch.globaz.common.business.language;

import globaz.globall.db.BSession;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.constantes.CommonConstLangue;
import ch.globaz.jade.business.models.Langues;

/**
 * Cette classe permet de résoudre la langue d'un tiers dans un format spécifique.
 * 
 * Exemple : code langue = 503001 -> FR, Français
 * 
 * @author jwe
 * 
 */
public class LanguageResolver {

    /**
     * Cette méthode utilitaire permet de contrôler si le tiers a bien une langue.
     * Dans le cas ou le tiers a une langue, on transfère l'id associé
     * 
     * @param tiersLanguage
     * @return
     */
    public static String resolveTiersLanguage(String tiersLanguage) {
        if (tiersLanguage == null) {
            throw new CommonTechnicalException("the tiers must have a language set");
        }

        if (CommonConstLangue.LANGUE_SYSTEM_CODE_FR.equalsIgnoreCase(tiersLanguage)
                || CommonConstLangue.LANGUE_ISO_FRANCAIS.equalsIgnoreCase(tiersLanguage)
                || CommonConstLangue.LANGUE_ID_FRANCAIS.equalsIgnoreCase(tiersLanguage)) {
            tiersLanguage = CommonConstLangue.LANGUE_ID_FRANCAIS;
        }

        else if (CommonConstLangue.LANGUE_SYSTEM_CODE_DE.equalsIgnoreCase(tiersLanguage)
                || CommonConstLangue.LANGUE_ISO_ALLEMAND.equalsIgnoreCase(tiersLanguage)
                || CommonConstLangue.LANGUE_ID_ALLEMAND.equalsIgnoreCase(tiersLanguage)) {

            tiersLanguage = CommonConstLangue.LANGUE_ID_ALLEMAND;
        } else if (CommonConstLangue.LANGUE_SYSTEM_CODE_IT.equalsIgnoreCase(tiersLanguage)
                || CommonConstLangue.LANGUE_ISO_ITALIEN.equalsIgnoreCase(tiersLanguage)
                || CommonConstLangue.LANGUE_ID_ITALIEN.equalsIgnoreCase(tiersLanguage)) {

            tiersLanguage = CommonConstLangue.LANGUE_ID_ITALIEN;
        } else {
            throw new CommonTechnicalException("The language setted is either false or not actualy supported :"
                    + tiersLanguage);
        }

        return tiersLanguage;
    }

    /**
     * Retourne une énumération de langue en fonction de la langue du tiers passée en paramètre.
     * 
     * @param tiersCodeISOLanguage
     * @return Langues -> fr, it, de
     */
    public static Langues resolveISOCode(String language) {
        Langues langueToReturn = null;

        if (language == null) {
            throw new CommonTechnicalException("the tiers must have a language set");
        }

        if (CommonConstLangue.LANGUE_SYSTEM_CODE_FR.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ISO_FRANCAIS.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ID_FRANCAIS.equalsIgnoreCase(language)) {
            langueToReturn = Langues.Francais;
        }

        else if (CommonConstLangue.LANGUE_SYSTEM_CODE_DE.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ISO_ALLEMAND.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ID_ALLEMAND.equalsIgnoreCase(language)) {
            langueToReturn = Langues.Allemand;
        } else if (CommonConstLangue.LANGUE_SYSTEM_CODE_IT.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ISO_ITALIEN.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ID_ITALIEN.equalsIgnoreCase(language)) {
            langueToReturn = Langues.Italien;
        } else {
            throw new CommonTechnicalException("The language setted is either false or not actualy supported :"
                    + language);
        }

        return langueToReturn;

    }

    /**
     * Retourne une String ISO de langue en fonction de la langue du tiers passée en paramètre.
     * 
     * @param tiersCodeISOLanguage
     * @return String -> fr, it, de
     */
    public static String resolveISOCodeToString(String language) {
        Langues languageToReturn = resolveISOCode(language);
        return languageToReturn.getCodeIso();

    }

    /**
     * Retourne le code système de la langue en fonction de l'id ou du code iso passée en paramètre.
     * 
     * @param language
     * @return une String de type CS -> 503001 (fr) || 503002 (de)
     */
    public static String resolveCodeSystemFromLanguage(String language) {

        String csToReturn = null;

        if (language == null) {
            throw new CommonTechnicalException("the tiers must have a language set");
        }

        if (CommonConstLangue.LANGUE_SYSTEM_CODE_FR.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ISO_FRANCAIS.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ID_FRANCAIS.equalsIgnoreCase(language)) {
            csToReturn = CommonConstLangue.LANGUE_SYSTEM_CODE_FR;
        }

        else if (CommonConstLangue.LANGUE_SYSTEM_CODE_DE.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ISO_ALLEMAND.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ID_ALLEMAND.equalsIgnoreCase(language)) {
            csToReturn = CommonConstLangue.LANGUE_SYSTEM_CODE_DE;
        } else if (CommonConstLangue.LANGUE_SYSTEM_CODE_IT.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ISO_ITALIEN.equalsIgnoreCase(language)
                || CommonConstLangue.LANGUE_ID_ITALIEN.equalsIgnoreCase(language)) {
            csToReturn = CommonConstLangue.LANGUE_SYSTEM_CODE_IT;
        } else {
            throw new CommonTechnicalException("The language setted is either false or not actualy supported :"
                    + language);
        }

        return csToReturn;

    }

    /**
     * Cette méthode permet de traduire un libellé en fonction de la langue passée en paramètre
     * 
     * @param language
     * @param label
     * @return
     */
    public static String resolveLibelleFromLabel(String language, String label, BSession session) {
        // si la langue n'est pas settée par défaut, on récupère celle de la session.
        if (language == null) {
            language = session.getIdLangue();
        }

        language = resolveISOCodeToString(language);
        String labelToReturn = null;
        try {
            labelToReturn = session.getApplication().getLabel(label, language);
        } catch (Exception e) {
            throw new CommonTechnicalException(
                    "An error happened while trying to load the following application label : " + label, e);
        }

        if (labelToReturn == null) {
            throw new CommonTechnicalException("The following label does not exists : " + label);
        }

        return labelToReturn;

    }

}
