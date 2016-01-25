package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;

public class DecompteComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -747365266047375732L;

    private DecompteSimpleModel decompteSimpleModel;
    private EmployeurComplexModel employeurComplexModel;

    public DecompteComplexModel() {
        decompteSimpleModel = new DecompteSimpleModel();
        employeurComplexModel = new EmployeurComplexModel();
    }

    public DecompteSimpleModel getDecompteSimpleModel() {
        return decompteSimpleModel;
    }

    public void setDecompteSimpleModel(DecompteSimpleModel decompteSimpleModel) {
        this.decompteSimpleModel = decompteSimpleModel;
    }

    public EmployeurComplexModel getEmployeurComplexModel() {
        return employeurComplexModel;
    }

    public void setEmployeurComplexModel(EmployeurComplexModel employeurComplexModel) {
        this.employeurComplexModel = employeurComplexModel;
    }

    @Override
    public String getId() {
        return decompteSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return decompteSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        decompteSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        decompteSimpleModel.setSpy(spy);
    }
}
