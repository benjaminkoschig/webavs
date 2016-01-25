package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche sur les configurations processus
 * 
 * @author GMO
 * 
 */
public class ConfigProcessusSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère de recherche processus
     */
    private String forBusinessProcessus = null;
    /**
     * Critère de recherche idconfig
     */
    private String forIdConfig = null;
    /**
     * Critère de recherche template
     */
    private String forTemplate = null;

    /**
     * Constructeur du modèle de recherche
     */
    public ConfigProcessusSearchModel() {
        super();
    }

    /**
     * 
     * @return Valeur du critère de recherche processus
     */
    public String getForBusinessProcessus() {
        return forBusinessProcessus;
    }

    /**
     * 
     * @return Valeur du critère de recherche sur l'id
     */
    public String getForIdConfig() {
        return forIdConfig;
    }

    /**
     * 
     * @return Valeur du critère de recherche sur la template
     */
    public String getForTemplate() {
        return forTemplate;
    }

    /**
     * 
     * @param forBusinessProcessus
     *            Valeur du critère de recherche processus
     */
    public void setForBusinessProcessus(String forBusinessProcessus) {
        this.forBusinessProcessus = forBusinessProcessus;
    }

    /**
     * 
     * @param forIdConfig
     *            Valeur du critère de recherche sur l'id
     */
    public void setForIdConfig(String forIdConfig) {
        this.forIdConfig = forIdConfig;
    }

    /**
     * 
     * @param forTemplate
     *            Valeur du critère de recherche sur la template
     */
    public void setForTemplate(String forTemplate) {
        this.forTemplate = forTemplate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<ConfigProcessusModel> whichModelClass() {
        return ConfigProcessusModel.class;
    }

}
