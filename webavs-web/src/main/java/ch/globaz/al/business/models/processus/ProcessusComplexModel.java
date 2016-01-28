package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;

public class ProcessusComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Configuration liée au processus périodique
     */
    private ConfigProcessusModel configProcessusModel = null;
    private PeriodeAFModel periodeModel = null;

    /**
     * Processus périodique (root-model)
     */
    private ProcessusPeriodiqueModel processusPeriodiqueModel = null;

    /**
     * 
     * @return configProcessusModel
     */
    public ConfigProcessusModel getConfigProcessusModel() {
        return configProcessusModel;
    }

    @Override
    public String getId() {
        return processusPeriodiqueModel.getId();
    }

    /**
     * 
     * @return periodeModel
     */
    public PeriodeAFModel getPeriodeModel() {
        return periodeModel;
    }

    /**
     * 
     * @return processusPeriodiqueModel
     */
    public ProcessusPeriodiqueModel getProcessusPeriodiqueModel() {
        return processusPeriodiqueModel;
    }

    @Override
    public String getSpy() {
        return processusPeriodiqueModel.getSpy();
    }

    /**
     * 
     * @param configProcessusModel
     */
    public void setConfigProcessusModel(ConfigProcessusModel configProcessusModel) {
        this.configProcessusModel = configProcessusModel;
    }

    @Override
    public void setId(String id) {
        processusPeriodiqueModel.setId(id);

    }

    /**
     * 
     * @param periodeModel
     */
    public void setPeriodeModel(PeriodeAFModel periodeModel) {
        this.periodeModel = periodeModel;
    }

    /**
     * 
     * @param processusPeriodiqueModel
     */
    public void setProcessusPeriodiqueModel(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        this.processusPeriodiqueModel = processusPeriodiqueModel;
    }

    @Override
    public void setSpy(String spy) {
        processusPeriodiqueModel.setSpy(spy);

    }

}
