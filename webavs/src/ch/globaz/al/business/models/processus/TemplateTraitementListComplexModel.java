package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;

/**
 * Mod�le complexe d'un template configuration des processus et traitements
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
     * Mod�le de la configuration des processus
     */
    private ConfigProcessusModel configProcessusModel = null;
    /**
     * Mod�le de la p�riode li�e au traitement
     */
    private PeriodeAFModel periodeAFModel = null;
    /**
     * Mod�le des processus p�riodiques g�n�r�s en fonction du template
     */
    private ProcessusPeriodiqueModel processusPeriodiqueModel = null;

    /**
     * Mod�le des traitements p�riodiques g�n�r�s en fonction des processus p�riodiques
     */
    private TraitementPeriodiqueModel traitementPeriodiqueModel = null;

    /**
     * Constructeur du mod�le
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
     *            le mod�le de configuration
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
     *            le mod�le de la p�riode
     */
    public void setPeriodeAFModel(PeriodeAFModel periodeAFModel) {
        this.periodeAFModel = periodeAFModel;
    }

    /**
     * @param processusPeriodiqueModel
     *            le mod�le du processus p�riodique
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
     *            le mod�le du traitement p�riodique
     */
    public void setTraitementPeriodiqueModel(TraitementPeriodiqueModel traitementPeriodiqueModel) {
        this.traitementPeriodiqueModel = traitementPeriodiqueModel;
    }

}
