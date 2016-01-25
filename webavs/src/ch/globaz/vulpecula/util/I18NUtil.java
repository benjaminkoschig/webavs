package ch.globaz.vulpecula.util;

import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import java.util.Locale;
import org.apache.commons.lang.LocaleUtils;
import ch.globaz.jade.JadeBusinessServiceLocator;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

/**
 * Utilitaire permettant de g�rer les diff�rentes variables li�es � la localisation dans les applcatios WEBAVS.
 * On retrouve trois classes/�num�rations repr�sentant la langue chez Globaz :
 * <ul>
 * <li>{@link Locale} Classe standard Java permettant de g�rer les diff�rentes locales. Utilis� principalement dans le
 * BMS.
 * <li>{@link CodeLangue} Enum�ration du BMS permettant de g�rer le code syst�me ainsi que le code ISO associ�.
 * <li>{@link Langues} Enum�ration utilis� dans le cadre de la recherche de code syst�me via
 * {@link JadeBusinessServiceLocator#getCodeSystemeService()}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 28 avr. 2014
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
     * Retourne la Locale de l'utilisateur actuellement connect�.
     * 
     * @return Locale de l'utilisateur connect�
     */
    public static Locale getUserLocale() {
        return LocaleUtils.toLocale(JadeThread.currentLanguage());
    }

    /**
     * Retourne la langue de l'utilisateur actuellement connect�
     * 
     * @return Langues de l'utilisateur connect�
     */
    public static Langues getLangues() {
        return Langues.getLangueDepuisCodeIso(JadeThread.currentLanguage());
    }

    /**
     * Retourne la langue du code syst�me pass� en param�tre
     * 
     * @param cs Code syst�me repr�sentant la langue
     * @return Langues selon le code syst�me
     */
    public static Langues getLanguesCS(String cs) {
        return Langues.getLangueDepuisCodeIso(cs);
    }

    /**
     * Retourne la Locale � partir de l'�num�ration CodeLangue qui repr�sente un code syst�me.
     * 
     * @return Locale de la code langue
     */
    public static Locale getLocaleOf(CodeLangue codeLangue) {
        if (codeLangue == null) {
            throw new NullPointerException("Le code langue ne peut �tre null");
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
     * Retourne la classe Langues � partir d'un code syst�me.
     * Dans le cas o� le code syst�me n'est pas valide, la langue par d�faut sera retourn�e.
     * 
     * @param codeSysteme String repr�sentant le code syst�me de la langue
     * @return Langues
     */
    public static Langues getLanguesOf(String codeSysteme) {
        CodeLangue codeLangue = CodeLangue.fromValue(codeSysteme);
        return getLanguesOf(codeLangue);

    }

    /**
     * Retourne la classe Langues � partir d'un CodeLangue
     * 
     * @param codeLangue Code langue � convertir
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
     * Retourne un message provenant de vulpeculabusinessimpl.properties. Ces messages sont la plupart du temps utilis�s
     * pour les processus.
     * 
     * @param messageId Id correspond � l'identifiant du message (sans la langue)
     * @param parametres Tableau de param�tres
     * @return Message
     */
    public static String getMessageFromResource(String messageId, String... parametres) {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), messageId, parametres);
    }
}
