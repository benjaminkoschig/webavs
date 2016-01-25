package ch.globaz.common.FusionTiersMultiple.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * @author jwe
 * @since 12.02.14
 */

public class MessageBundleUtil {

    /**
     * @param key
     *            : on passe notre clé de label
     * @param language
     *            : on y passe la langue
     * @return : Nous retourne les labels en fonction de la langue trouvée dans la session.
     */
    public static String getMessage(String key, String language) {
        if (key == null) {
            throw new NullPointerException("Unabled to get the message, Key is null");
        }
        if (language == null) {
            throw new NullPointerException("Unabled to get the message, language is null");
        }
        Locale locale = new Locale(language.toLowerCase());

        return ResourceBundle.getBundle("COMMONLabels", locale).getString(key);
    }
}
