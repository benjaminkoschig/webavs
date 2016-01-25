package ch.globaz.amal.process.repriseRecalculs;

import globaz.jade.context.JadeThread;

public enum AMProcessRepriseRecalculsTaxationsEnum {
    YEAR_TAXATION("amal.process.repriseDecisionsTaxations.yearTaxation");

    private final String idLabel;

    AMProcessRepriseRecalculsTaxationsEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }

}
