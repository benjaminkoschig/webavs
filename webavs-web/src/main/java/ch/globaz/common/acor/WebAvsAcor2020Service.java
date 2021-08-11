package ch.globaz.common.acor;


import acor.rentes.xsd.standard.error.OriginType;
import acor.rentes.xsd.standard.error.StandardError;

// TODO : à supprimer sans doute : peu d'intérêt pour cette interface.
public interface WebAvsAcor2020Service {

     String ERROR_ACOR_EXTERN_TOKEN_INVALID = "ERROR.ACOR_EXTERN.TOKEN.INVALID";
     String ERROR_ACOR_EXTERN_IMPORT_UNKOWN = "ERROR.ACOR_EXTERN.IMPORT.UNKOWN";
     String ERROR_ACOR_EXTERN_IMPORT_IN_HOST = "ERROR.ACOR_EXTERN.IMPORT.IN_HOST";
     String ERROR_ACOR_EXTERN_IMPORT_CONVERT = "ERROR.ACOR_EXTERN.IMPORT.CONVERT";
     String ERROR_ACOR_EXTERN_EXPORT_UNKNOWN = "ERROR.ACOR_EXTERN.EXPORT.UNKNOWN";
     String ERROR_ACOR_EXTERN_EXPORT_SPE = "&ERROR.ACOR_EXTERN.EXPORT.SPE";
     String TOKEN_INVALIDE = "Token invalide.";

    /**
     * Méthode permettant de générer des erreurs standards lorsqu'un traitement est en erreur.
     *
     * @param label
     * @param e
     * @param level
     * @param type
     * @return
     */
    static StandardError getStandardError(String label, Exception e, int level, OriginType type) {
        StandardError error = new StandardError();
        error.setLabelId(label);
        error.setOrigin(type);
        error.setLevel(level);
        error.setType(1);
        if (e != null) {
            error.setDebug(e.getStackTrace().toString());
        }
        return error;
    }

}
