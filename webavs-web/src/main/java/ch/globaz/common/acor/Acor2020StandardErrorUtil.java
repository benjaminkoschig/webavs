package ch.globaz.common.acor;

import acor.rentes.xsd.standard.error.OriginType;
import acor.rentes.xsd.standard.error.StandardError;
import com.google.common.base.Throwables;

public final class Acor2020StandardErrorUtil {

    public static final String ERROR_ACOR_EXTERN_TOKEN_INVALID = "ERROR.ACOR_EXTERN.TOKEN.INVALID";
    public static final String ERROR_ACOR_EXTERN_IMPORT_UNKOWN = "ERROR.ACOR_EXTERN.IMPORT.UNKOWN";
    public static final String ERROR_ACOR_EXTERN_IMPORT_IN_HOST = "ERROR.ACOR_EXTERN.IMPORT.IN_HOST";
    public static final String ERROR_ACOR_EXTERN_IMPORT_CONVERT = "ERROR.ACOR_EXTERN.IMPORT.CONVERT";
    public static final String ERROR_ACOR_EXTERN_EXPORT_UNKNOWN = "ERROR.ACOR_EXTERN.EXPORT.UNKNOWN";
    public static final String ERROR_ACOR_EXTERN_EXPORT_SPE = "&ERROR.ACOR_EXTERN.EXPORT.SPE";
    public static final String TOKEN_INVALIDE = "Token invalide.";

    private Acor2020StandardErrorUtil() {
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
