package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;

/**
 * Modèle complexe d'un template configuration des processus et traitements
 * 
 * @author GMO
 * 
 */
public class TemplateTraitementListComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Modèle de la configuration des processus
     */
    private ConfigProcessusModel configProcessusModel = null;
    /**
     * Modèle de la période liée au traitement
     */
    private PeriodeAFModel periodeAFModel = null;
    /**
     * Modèle des processus périodiques générés en fonction du template
     */
    private ProcessusPeriodiqueModel processusPeriodiqueModel = null;

    /**
     * Modèle des traitements périodiques générés en fonction des processus périodiques
     */
    private TraitementPeriodiqueModel traitementPeriodiqueModel = null;

    /**
     * Constructeur du modèle
     */
    public TemplateTraitementListComplexModel() {
        super();
        configProcessusModel = new ConfigProcessusModel();
        processusPeriodiqueModel = new ProcessusPeriodiqueModel();
        traitementPeriodiqueModel = new TraitementPeriodiqueModel();
        periodeAFModel = new PeriodeAFModel();
    }

    /**
     * @return configProcessusModel
     */
    public ConfigProcessusModel getConfigProcessusModel() {
        return configProcessusModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return configProcessusModel.getId();
    }

    /**
     * @return periodeAFModel
     */
    public PeriodeAFModel getPeriodeAFModel() {
        return periodeAFModel;
    }

    /**
     * @return processusPeriodiqueModel
     */
    public ProcessusPeriodiqueModel getProcessusPeriodiqueModel() {
        return processusPeriodiqueModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return configProcessusModel.getSpy();
    }

    /**
     * @return traitementPeriodiqueModel
     */
    public TraitementPeriodiqueModel getTraitementPeriodiqueModel() {
        return traitementPeriodiqueModel;
    }

    /**
     * 
     * @param configProcessusModel
     *            le modèle de configuration
     */
    public void setConfigProcessusModel(ConfigProcessusModel configProcessusModel) {
        this.configProcessusModel = configProcessusModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        configProcessusModel.setId(id);

    }

    /**
     * @param periodeAFModel
     *            le modèle de la période
     */
    public void setPeriodeAFModel(PeriodeAFModel periodeAFModel) {
        this.periodeAFModel = periodeAFModel;
    }

    /**
     * @param processusPeriodiqueModel
     *            le modèle du processus périodique
     */
    public void setProcessusPeriodiqueModel(ProcessusPeriodiqueModel processusPeriodiqueModel) {
        this.processusPeriodiqueModel = processusPeriodiqueModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        configProcessusModel.setSpy(spy);

    }

    /**
     * @param traitementPeriodiqueModel
     *            le modèle du traitement périodique
     */
    public void setTraitementPeriodiqueModel(TraitementPeriodiqueModel traitementPeriodiqueModel) {
        this.traitementPeriodiqueModel = traitementPeriodiqueModel;
    }

}
