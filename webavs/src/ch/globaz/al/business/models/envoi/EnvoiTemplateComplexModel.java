/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;

/**
 * @author dhi
 * 
 */
public class EnvoiTemplateComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private EnvoiTemplateSimpleModel envoiTemplateSimpleModel = null;

    private FormuleList formuleList = null;

    /**
	 * 
	 */
    public EnvoiTemplateComplexModel() {
        formuleList = new FormuleList();
        envoiTemplateSimpleModel = new EnvoiTemplateSimpleModel();
    }

    /**
     * @return the envoiTemplateSimpleModel
     */
    public EnvoiTemplateSimpleModel getEnvoiTemplateSimpleModel() {
        return envoiTemplateSimpleModel;
    }

    /**
     * @return the formuleList
     */
    public FormuleList getFormuleList() {
        return formuleList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return envoiTemplateSimpleModel.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return envoiTemplateSimpleModel.getSpy();
    }

    /**
     * @param envoiTemplateSimpleModel
     *            the envoiTemplateSimpleModel to set
     */
    public void setEnvoiTemplateSimpleModel(EnvoiTemplateSimpleModel envoiTemplateSimpleModel) {
        this.envoiTemplateSimpleModel = envoiTemplateSimpleModel;
    }

    /**
     * @param formuleList
     *            the formuleList to set
     */
    public void setFormuleList(FormuleList formuleList) {
        this.formuleList = formuleList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        envoiTemplateSimpleModel.setId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        envoiTemplateSimpleModel.setSpy(spy);
    }

}
