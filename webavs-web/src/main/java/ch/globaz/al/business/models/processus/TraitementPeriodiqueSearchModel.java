package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche pour TraitementPeriodique
 * 
 * @author GMO
 * 
 */
public class TraitementPeriodiqueSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Critère CS du traitement
     */
    private String forCSTraitement = null;

    /**
     * Critère id processus périodique
     */
    private String forIdProcessusPeriodique = null;

    /**
     * Constructeur du modèle de recherche
     */
    public TraitementPeriodiqueSearchModel() {
        super();
    }

    /**
     * 
     * @return forCSTraitement
     */
    public String getForCSTraitement() {
        return forCSTraitement;
    }

    /**
     * 
     * @return
     */
    public String getForIdProcessusPeriodique() {
        return forIdProcessusPeriodique;
    }

    public void setForCSTraitement(String forCSTraitement) {
        this.forCSTraitement = forCSTraitement;
    }

    /**
     * 
     * @param forIdProcessusPeriodique
     */
    public void setForIdProcessusPeriodique(String forIdProcessusPeriodique) {
        this.forIdProcessusPeriodique = forIdProcessusPeriodique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return TraitementPeriodiqueModel.class;
    }

}
