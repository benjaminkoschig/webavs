package ch.globaz.pegasus.business.models.externalmodule;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ExternalModuleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdJob = null;
    private ExternalJobEtat forEtatJob = null;
    private ExternalJobActionSource forSourceActionJob = null;

    public String getForIdJob() {
        return forIdJob;
    }

    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    public ExternalJobEtat getForEtatJob() {
        return forEtatJob;
    }

    public void setForEtatJob(ExternalJobEtat forEtatJob) {
        this.forEtatJob = forEtatJob;
    }

    public ExternalJobActionSource getForSourceActionJob() {
        return forSourceActionJob;
    }

    public void setForSourceActionJob(ExternalJobActionSource forSourceActionJob) {
        this.forSourceActionJob = forSourceActionJob;
    }

    @Override
    public Class whichModelClass() {
        return ExternalModule.class;
    }

}
