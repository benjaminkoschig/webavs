package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 */
public class CodeErreurDecompteSalaireComplexModel extends JadeComplexModel {
	private static final long serialVersionUID = -9180311529290532857L;
	
	private CodeErreurDecompteSalaireSimpleModel codeErreurDecompteSalaireSimpleModel;

    public CodeErreurDecompteSalaireComplexModel() {
    	codeErreurDecompteSalaireSimpleModel = new CodeErreurDecompteSalaireSimpleModel();
    }

    public CodeErreurDecompteSalaireSimpleModel getCodeErreurDecompteSalaireSimpleModel() {
        return codeErreurDecompteSalaireSimpleModel;
    }

    public void setCodeErreurDecompteSalaireSimpleModel(CodeErreurDecompteSalaireSimpleModel codeErreurDecompteSalaireSimpleModel) {
        this.codeErreurDecompteSalaireSimpleModel = codeErreurDecompteSalaireSimpleModel;
    }

    @Override
    public String getId() {
        return codeErreurDecompteSalaireSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return codeErreurDecompteSalaireSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
    	codeErreurDecompteSalaireSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
    	codeErreurDecompteSalaireSimpleModel.setSpy(spy);
    }
}
