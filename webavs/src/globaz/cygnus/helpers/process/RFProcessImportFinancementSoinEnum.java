package globaz.cygnus.helpers.process;

import globaz.jade.context.JadeThread;

public enum RFProcessImportFinancementSoinEnum {
    EMAIL("cygnus.process.file.email"),
    FILE_PATH_FOR_POPULATION("cygnus.process.file.path"),
    GESTIONNAIRE("cygnus.process.file.gestionnaire");

    private final String idLabel;

    RFProcessImportFinancementSoinEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}
