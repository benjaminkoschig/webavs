package ch.globaz.pegasus.business.models.process.allocationsNoel;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AllocationNoelEntitySearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdExecutionProcess;

    public String getForIdExecutionProcess() {
        return forIdExecutionProcess;
    }

    public void setForIdExecutionProcess(String forIdExecutionProcess) {
        this.forIdExecutionProcess = forIdExecutionProcess;
    }

    @Override
    public Class<AllocationNoelEntity> whichModelClass() {
        return AllocationNoelEntity.class;
    }
}
