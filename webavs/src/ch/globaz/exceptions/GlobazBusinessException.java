package ch.globaz.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Exception lanc�e lorsque surgissent des probl�mes m�tiers, que l'utilisateur
 * peut potentiellement corriger.
 * 
 */
@SuppressWarnings("serial")
public class GlobazBusinessException extends Exception {

    private static final String EXCEPTIONS_BUNDLE_NAME = "ch.globaz.exceptions.Exceptions";

    public GlobazBusinessException(final ExceptionMessage messageKey) {
        super(messageKey.toString());
    }

    public GlobazBusinessException(final ExceptionMessage messageKey, final Throwable cause) {
        super(messageKey.toString(), cause);
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(ExceptionLanguage.DEFAULT);
    }

    /**
     * Retourne le message localis� de cette exception.
     * 
     * @param lang
     *            la langue � utiliser pour la localisation du message
     * @return un message String localis�
     */
    public String getLocalizedMessage(final ExceptionLanguage lang) {
        Locale locale = new Locale(lang.toString());
        ResourceBundle message = ResourceBundle.getBundle(EXCEPTIONS_BUNDLE_NAME, locale);

        return message.getString(super.getMessage());
    }

    @Override
    public String toString() {
        return getMessage();
    }

}
