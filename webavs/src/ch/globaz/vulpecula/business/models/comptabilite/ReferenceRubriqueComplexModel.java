package ch.globaz.vulpecula.business.models.comptabilite;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.codesystem.CodeSystemLibelleSimpleModel;
import ch.globaz.vulpecula.business.models.codesystem.CodeSystemSimpleModel;

public class ReferenceRubriqueComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 2948269505398807238L;

    private CodeSystemSimpleModel codeSystemSimpleModel;
    private CodeSystemLibelleSimpleModel codeSystemLibelleSimpleModel;
    private RubriqueSimpleModel rubriqueSimpleModel;
    private ReferenceRubriqueSimpleModel referenceRubriqueSimpleModel;

    public ReferenceRubriqueComplexModel() {
        codeSystemSimpleModel = new CodeSystemSimpleModel();
        codeSystemLibelleSimpleModel = new CodeSystemLibelleSimpleModel();
        rubriqueSimpleModel = new RubriqueSimpleModel();
        referenceRubriqueSimpleModel = new ReferenceRubriqueSimpleModel();
    }

    @Override
    public String getId() {
        return rubriqueSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return rubriqueSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        rubriqueSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        rubriqueSimpleModel.setSpy(spy);
    }

    public CodeSystemSimpleModel getCodeSystemSimpleModel() {
        return codeSystemSimpleModel;
    }

    public void setCodeSystemSimpleModel(CodeSystemSimpleModel codeSystemSimpleModel) {
        this.codeSystemSimpleModel = codeSystemSimpleModel;
    }

    public CodeSystemLibelleSimpleModel getCodeSystemLibelleSimpleModel() {
        return codeSystemLibelleSimpleModel;
    }

    public void setCodeSystemLibelleSimpleModel(CodeSystemLibelleSimpleModel codeSystemLibelleSimpleModel) {
        this.codeSystemLibelleSimpleModel = codeSystemLibelleSimpleModel;
    }

    public RubriqueSimpleModel getRubriqueSimpleModel() {
        return rubriqueSimpleModel;
    }

    public void setRubriqueSimpleModel(RubriqueSimpleModel rubriqueSimpleModel) {
        this.rubriqueSimpleModel = rubriqueSimpleModel;
    }

    public ReferenceRubriqueSimpleModel getReferenceRubriqueSimpleModel() {
        return referenceRubriqueSimpleModel;
    }

    public void setReferenceRubriqueSimpleModel(ReferenceRubriqueSimpleModel referenceRubriqueSimpleModel) {
        this.referenceRubriqueSimpleModel = referenceRubriqueSimpleModel;
    }
}
