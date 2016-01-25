package ch.globaz.pegasus.business.models.externalmodule.jsonparameters;

public class ComptabilisationParameter implements ExternalModuleParameters {

    private String idLot = null;

    public ComptabilisationParameter(String idLot) {
        this.idLot = idLot;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

}
