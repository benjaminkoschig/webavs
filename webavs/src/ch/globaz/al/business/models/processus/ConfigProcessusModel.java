package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * Mod�le de la configuration d'un processus m�tier
 * 
 * @author GMO
 * 
 */
public class ConfigProcessusModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * processus li� � la configuration
     */
    private String businessProcessus = null;
    /**
     * id de la configuration
     */
    private String idConfig = null;
    /**
     * template correspondant � cette configuration
     */
    private String template = null;

    /**
     * @return businessProcessus
     */
    public String getBusinessProcessus() {
        return businessProcessus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idConfig;
    }

    /**
     * @return idConfig
     */
    public String getIdConfig() {
        return idConfig;
    }

    /**
     * @return template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param businessProcessus
     *            processus li� � la configuration
     */
    public void setBusinessProcessus(String businessProcessus) {
        this.businessProcessus = businessProcessus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idConfig = id;

    }

    /**
     * @param idConfig
     *            id de la configuration
     */
    public void setIdConfig(String idConfig) {
        this.idConfig = idConfig;
    }

    /**
     * @param template
     *            Template correspondant � cette configuration
     */
    public void setTemplate(String template) {
        this.template = template;
    }

}
