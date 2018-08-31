package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;

public class DecompteComptableComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -1025546784075067435L;

    private DecompteSimpleModel decompteSimpleModel;
    private EmployeurComplexModel employeurComplexModel;
    private PassageModel passageModel;

    public DecompteComptableComplexModel() {
        decompteSimpleModel = new DecompteSimpleModel();
        employeurComplexModel = new EmployeurComplexModel();
        passageModel = new PassageModel();
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

    public PassageModel getPassageModel() {
        return passageModel;
    }

    public void setPassageModel(PassageModel passageModel) {
        this.passageModel = passageModel;
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
