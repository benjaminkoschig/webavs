package globaz.cygnus.process.importationAvasad;

import globaz.jade.context.JadeThread;

/**
 * 
 * @author jje
 * 
 */
public enum RFProcessImportationAvasadEnum {

    CODE_ERREUR("cygnus.processImportationAvasad.code.erreur"),
    EMAIL("cygnus.processImportationAvasad.file.email"),
    FILE_PATH_FOR_POPULATION(
    // Ce nom correspond à une constante de JadeProcessPopulationByFileCsv
            "cygnus.processImportationAvasad.file.path"),
    GESTIONNAIRE("cygnus.processImportationAvasad.gestionnaire"),
    ID_DEMANDE("cygnus.processImportationAvasad.id.demande"),
    MESSAGES_ERREUR_IMPORTATION("cygnus.processImportationAvasad.process.messages.erreur.importation"),
    NUMERO_AF("cygnus.processImportationAvasad.numero.af"),
    PATH_FILE_USER("cygnus.processImportationAvasad.pathFileUser"),
    PROCESS_KEY_IMPORTATION_AVASAD("cygnus.processImportationAvasad.process.key");

    private final String idLabel;

    RFProcessImportationAvasadEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
