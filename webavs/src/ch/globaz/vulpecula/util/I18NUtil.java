package ch.globaz.vulpecula.util;

import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import java.util.Locale;
import org.apache.commons.lang.LocaleUtils;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * Utilitaire permettant de gérer les différentes variables liées à la localisation dans les applcatios WEBAVS.
 * On retrouve trois classes/énumérations représentant la langue chez Globaz :
 * <ul>
 * <li>{@link Locale} Classe standard Java permettant de gérer les différentes locales. Utilisé principalement dans le
 * BMS.
 * <li>{@link CodeLangue} Enumération du BMS permettant de gérer le code système ainsi que le code ISO associé.
 * <li>{@link Langues} Enumération utilisé dans le cadre de la recherche de code système via
 * {@link JadeBusinessServiceLocator#getCodeSystemeService()}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 28 avr. 2014
 * 
 */
public class I18NUtil {
    private final Locale locale;

    public static I18NUtil getInstance() {
        return new I18NUtil();
    }

    private I18NUtil() {
        locale = getUserLocale();
    }

    protected I18NUtil(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Retourne la Locale de l'utilisateur actuellement connecté.
     * 
     * @return Locale de l'utilisateur connecté
     */
    public static Locale getUserLocale() {
        return LocaleUtils.toLocale(JadeThread.currentLanguage());
    }

    /**
     * Retourne la langue de l'utilisateur actuellement connecté
     * 
     * @return Langues de l'utilisateur connecté
     */
    public static Langues getLangues() {
        return Langues.getLangueDepuisCodeIso(JadeThread.currentLanguage());
    }

    /**
     * Retourne la langue du code système passé en paramètre
     * 
     * @param cs Code système représentant la langue
     * @return Langues selon le code système
     */
    public static Langues getLanguesCS(String cs) {
        return Langues.getLangueDepuisCodeIso(cs);
    }

    /**
     * Retourne la Locale à partir de l'énumération CodeLangue qui représente un code système.
     * 
     * @return Locale de la code langue
     */
    public static Locale getLocaleOf(CodeLangue codeLangue) {
        if (codeLangue == null) {
            throw new NullPointerException("Le code langue ne peut être null");
        }
        switch (codeLangue) {
            case FR:
                return Locale.FRENCH;
            case DE:
                return Locale.GERMAN;
            case IT:
                return Locale.ITALIAN;
            case EN:
                return Locale.ENGLISH;
            default:
                return Locale.FRENCH;
        }
    }

    /**
     * Retourne la classe Langues à partir d'un code système.
     * Dans le cas où le code système n'est pas valide, la langue par défaut sera retournée.
     * 
     * @param codeSysteme String représentant le code système de la langue
     * @return Langues
     */
    public static Langues getLanguesOf(String codeSysteme) {
        CodeLangue codeLangue = CodeLangue.fromValue(codeSysteme);
        return getLanguesOf(codeLangue);

    }

    /**
     * Retourne la classe Langues à partir d'un CodeLangue
     * 
     * @param codeLangue Code langue à convertir
     * @return Langues
     */
    public static Langues getLanguesOf(CodeLangue codeLangue) {
        Langues langues = Langues.getLangueDepuisCodeIso(codeLangue.getCodeIsoLangue());
        return langues;
    }

    public static String getOneLetterLang() {
        return getOneLetterLang(getLangues());
    }

    public static String getOneLetterLang(String cs) {
        return getOneLetterLang(getLanguesCS(cs));
    }

    public static String getOneLetterLang(Langues langues) {
        switch (langues) {
            case Allemand:
                return "D";
            case Italien:
                return "I";
            default:
                return "F";
        }
    }

    /**
     * Retourne un message provenant de vulpeculabusinessimpl.properties. Ces messages sont la plupart du temps utilisés
     * pour les processus.
     * 
     * @param messageId Id correspond à l'identifiant du message (sans la langue)
     * @param parametres Tableau de paramètres
     * @return Message
     */
    public static String getMessageFromResource(String messageId, String... parametres) {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), messageId, parametres);
    }
}
