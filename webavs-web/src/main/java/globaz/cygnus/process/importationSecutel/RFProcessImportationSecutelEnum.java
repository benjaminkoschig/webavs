package globaz.cygnus.process.importationSecutel;

import globaz.jade.context.JadeThread;

/**
 * 
 * @author jje
 * 
 */
public enum RFProcessImportationSecutelEnum {

    CODE_ERREUR("cygnus.processImportationSecutel.code.erreur"),
    EMAIL("cygnus.processImportationSecutel.file.email"),
    FILE_PATH_FOR_POPULATION(
    // Ce nom correspond à une constante de JadeProcessPopulationByFileCsv
            "cygnus.processImportationSecutel.file.path"),
    GESTIONNAIRE("cygnus.processImportationSecutel.gestionnaire"),
    ID_DEMANDE("cygnus.processImportationSecutel.id.demande"),
    MESSAGES_ERREUR_IMPORTATION("cygnus.processImportationSecutel.process.messages.erreur.importation"),
    NUMERO_AF("cygnus.processImportationSecutel.numero.af"),
    PATH_FILE_USER("cygnus.processImportationSecutel.pathFileUser"),
    PROCESS_KEY_IMPORTATION_SECUTEL("cygnus.processImportationSecutel.process.key");

    private final String idLabel;

    RFProcessImportationSecutelEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
