package globaz.prestation.acor.web.ws;

import acor.xsd.standard.error.OriginType;
import acor.xsd.standard.error.StandardError;
import com.google.common.base.Throwables;

public final class TokenStandardErrorUtil {

    public static final String ERROR_TOKEN_INVALID = "error.token.invalid";
    public static final String ERROR_ACOR_IMPORT = "error.acor.import";
    public static final String ERROR_ACOR_EXPORT = "error.acor.export";
    public static final String ERROR_ACOR_IMPORT_SUBJECT = "error.acor.import.subject";
    public static final String ERROR_ACOR_EXPORT_SUBJECT = "error.acor.export.subject";
    public static final String ERROR_ACOR_GLOBAL = "error.acor.global";
    public static final String ERROR_ES_TOKEN = "error.es.token";
    public static final String ERROR_ES_RETRIEVE = "error.es.retrieve";
    public static final String TOKEN_INVALIDE = "Token invalide.";

    private TokenStandardErrorUtil() {
    }

    /**
     * Méthode permettant de générer des erreurs standards lorsqu'un traitement est en erreur.
     *
     * @param label
     * @param e
     * @param level
     * @param type
     * @return
     */
    public static StandardError getStandardError(String label, Exception e, int level, OriginType type) {
        StandardError error = new StandardError();
        error.setLabelId(label);
        error.setOrigin(type);
        error.setLevel(level);
        error.setType(1);
        if (e != null) {
            error.setLabel(e.getMessage());
            error.setDebug(Throwables.getStackTraceAsString(e));
        }
        return error;
    }

}
