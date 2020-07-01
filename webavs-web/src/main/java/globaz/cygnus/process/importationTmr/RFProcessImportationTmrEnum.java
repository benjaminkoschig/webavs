package globaz.cygnus.process.importationTmr;

import globaz.jade.context.JadeThread;

/**
 * 
 * @author jje
 * 
 */
public enum RFProcessImportationTmrEnum {

    CODE_ERREUR("cygnus.processImportationTmr.code.erreur"),
    EMAIL("cygnus.processImportationTmr.file.email"),
    FILE_PATH_FOR_POPULATION(
    // Ce nom correspond à une constante de JadeProcessPopulationByFileCsv
            "cygnus.processImportationTmr.file.path"),
    GESTIONNAIRE("cygnus.processImportationTmr.gestionnaire"),
    ID_DEMANDE("cygnus.processImportationTmr.id.demande"),
    MESSAGES_ERREUR_IMPORTATION("cygnus.processImportationTmr.process.messages.erreur.importation"),
    NUMERO_AF("cygnus.processImportationTmr.numero.af"),
    PATH_FILE_USER("cygnus.processImportationTmr.pathFileUser"),
    PROCESS_KEY_IMPORTATION_TMR("cygnus.processImportationTmr.process.key");

    private final String idLabel;

    RFProcessImportationTmrEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
