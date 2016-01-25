package ch.globaz.amal.process.repriseDecisionsTaxations;

import globaz.jade.context.JadeThread;

public enum AMProcessRepriseDecisionsTaxationsEnum {
    b_getFiles("amal.process.repriseDecisionsTaxations.b_getFiles"),
    DATE_LIMITE_ADRESSE("amal.process.repriseDecisionsTaxations.dateLimiteAdresses"),
    FILENAME("amal.process.repriseDecisionsTaxations.nomFichier1"),
    FILENAME1("amal.process.repriseDecisionsTaxations.nomFichier1"),
    IS_FIN_ANNEE("amal.process.repriseDecisionsTaxations.isFinAnnee"),
    IS_REPRISE_ADRESSE("amal.process.repriseDecisionsTaxations.isRepriseAdresse"),
    mode("amal.process.repriseDecisionsTaxations.mode"),
    nomFichier("amal.process.repriseDecisionsTaxations.nomFichier"),
    upload_done("amal.process.repriseDecisionsTaxations.upload_done"),
    YEAR_SUBSIDE("amal.process.repriseDecisionsTaxations.yearSubside"),
    YEAR_TAXATION("amal.process.repriseDecisionsTaxations.yearTaxation");

    private final String idLabel;

    AMProcessRepriseDecisionsTaxationsEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}
