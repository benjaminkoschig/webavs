package ch.globaz.vulpecula.business.models.controleemployeur;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;

public class ControleEmployeurComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 4803912482871685113L;

    private ControleEmployeurSimpleModel controleEmployeurSimpleModel;
    private EmployeurComplexModel employeurComplexModel;

    public ControleEmployeurComplexModel() {
        controleEmployeurSimpleModel = new ControleEmployeurSimpleModel();
        employeurComplexModel = new EmployeurComplexModel();
    }

    public ControleEmployeurSimpleModel getControleEmployeurSimpleModel() {
        return controleEmployeurSimpleModel;
    }

    public void setControleEmployeurSimpleModel(ControleEmployeurSimpleModel controleEmployeurSimpleModel) {
        this.controleEmployeurSimpleModel = controleEmployeurSimpleModel;
    }

    public EmployeurComplexModel getEmployeurComplexModel() {
        return employeurComplexModel;
    }

    public void setEmployeurComplexModel(EmployeurComplexModel employeurComplexModel) {
        this.employeurComplexModel = employeurComplexModel;
    }

    @Override
    public String getId() {
        return controleEmployeurSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return controleEmployeurSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        controleEmployeurSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        controleEmployeurSimpleModel.setSpy(spy);
    }

}
