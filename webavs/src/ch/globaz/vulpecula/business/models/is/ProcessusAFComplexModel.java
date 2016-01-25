package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;

public class ProcessusAFComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -8308411598328025641L;

    private ProcessusPeriodiqueModel processusPeriodiqueModel;
    private ConfigProcessusModel configProcessusModel;
    private PassageModel passageModel;
    private JournalSimpleModel journalSimpleModel;
    private PeriodeAFModel periodeAFModel;

    public ProcessusAFComplexModel() {
        processusPeriodiqueModel = new ProcessusPeriodiqueModel();
        configProcessusModel = new ConfigProcessusModel();
        passageModel = new PassageModel();
        journalSimpleModel = new JournalSimpleModel();
        periodeAFModel = new PeriodeAFModel();
    }

    @Override
    public String getId() {
        return processusPeriodiqueModel.getId();
    }

    @Override
    public String getSpy() {
        return configProcessusModel.getSpy();
    }

    @Override
    public void setId(String id) {
        configProcessusModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        configProcessusModel.setSpy(spy);
    }

    public ProcessusPeriodiqueModel getProcessusPeriodiqueModel() {
        return processusPeriodiqueModel;
    }

    public void setProcessusPeriodiqueModel(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        this.processusPeriodiqueModel = processusPeriodiqueModel;
    }

    public ConfigProcessusModel getConfigProcessusModel() {
        return configProcessusModel;
    }

    public void setConfigProcessusModel(ConfigProcessusModel configProcessusModel) {
        this.configProcessusModel = configProcessusModel;
    }

    public PassageModel getPassageModel() {
        return passageModel;
    }

    public void setPassageModel(PassageModel passageModel) {
        this.passageModel = passageModel;
    }

    public PeriodeAFModel getPeriodeAFModel() {
        return periodeAFModel;
    }

    public void setPeriodeAFModel(PeriodeAFModel periodeAFModel) {
        this.periodeAFModel = periodeAFModel;
    }

    public JournalSimpleModel getJournalSimpleModel() {
        return journalSimpleModel;
    }

    public void setJournalSimpleModel(JournalSimpleModel journalSimpleModel) {
        this.journalSimpleModel = journalSimpleModel;
    }
}
