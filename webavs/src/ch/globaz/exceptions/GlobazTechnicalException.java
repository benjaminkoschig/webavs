package ch.globaz.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Exception lancée lors d'erreur technique dans un <code>Repository</code>.
 * 
 */
@SuppressWarnings("serial")
public class GlobazTechnicalException extends RuntimeException {

    private static final String EXCEPTIONS_BUNDLE_NAME = "ch.globaz.exceptions.Exceptions";

    public GlobazTechnicalException(final ExceptionMessage message) {
        super(message.toString());
    }

    public GlobazTechnicalException(final ExceptionMessage message, final Throwable cause) {
        super(message.toString(), cause);
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(ExceptionLanguage.DEFAULT);
    }

    /**
     * Retourne le message localisé de cette exception.
     * 
     * @param lang
     *            la langue à utiliser pour la localisation du message
     * @return un message String localisé
     */
    public String getLocalizedMessage(final ExceptionLanguage lang) {
        Locale locale = new Locale(lang.toString());
        ResourceBundle message = ResourceBundle.getBundle(EXCEPTIONS_BUNDLE_NAME, locale);

        return message.getString(super.getMessage()); // super.getMessage() retourne le "label" de l'exception
    }
}
