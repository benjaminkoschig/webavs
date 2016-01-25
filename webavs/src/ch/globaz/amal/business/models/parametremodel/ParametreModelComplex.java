/**
 * 
 */
package ch.globaz.amal.business.models.parametremodel;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;

/**
 * @author CBU
 * 
 */
public class ParametreModelComplex extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FormuleList formuleList = null;
    private SimpleParametreModel simpleParametreModel = null;

    public ParametreModelComplex() {
        super();
        formuleList = new FormuleList();
        simpleParametreModel = new SimpleParametreModel();
    }

    public ParametreModelComplex(FormuleList formuleList) {
        this();
        this.formuleList = formuleList;
        simpleParametreModel = new SimpleParametreModel();
    }

    public ParametreModelComplex(SimpleParametreModel simpleParametreModel) {
        this();
        this.simpleParametreModel = simpleParametreModel;
        formuleList = new FormuleList();
    }

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
        return getFormuleList().getId();
    }

    public SimpleParametreModel getSimpleParametreModel() {
        return simpleParametreModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleParametreModel.getSpy();
    }

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
        getFormuleList().setId(id);
    }

    public void setSimpleParametreModel(SimpleParametreModel simpleParametreModel) {
        this.simpleParametreModel = simpleParametreModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleParametreModel.setSpy(spy);
    }

}
