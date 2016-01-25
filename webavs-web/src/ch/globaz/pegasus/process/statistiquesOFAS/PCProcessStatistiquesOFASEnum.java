package ch.globaz.pegasus.process.statistiquesOFAS;

import globaz.jade.context.JadeThread;

public enum PCProcessStatistiquesOFASEnum {

    CONTROLE_ONE("pegasus.process.statistiquesofas.controle1"),
    CONTROLE_TWO("pegasus.process.statistiquesofas.controle2"),

    DATE_STATISTIQUE("pegasus.process.statistiquesofas.dateStatistique"),

    OBJET_JSON_STATISTIQUESOFAS("pegasus.process.statistiquesofas.objetjson");

    private final String idLabel;

    PCProcessStatistiquesOFASEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}
