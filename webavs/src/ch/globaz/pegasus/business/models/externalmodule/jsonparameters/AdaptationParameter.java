package ch.globaz.pegasus.business.models.externalmodule.jsonparameters;

public class AdaptationParameter implements ExternalModuleParameters<AdaptationParameter> {

    private String idExecutionProcess = null;

    public AdaptationParameter(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

}
