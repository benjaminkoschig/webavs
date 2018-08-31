package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * 
 */
public class CodeErreurDecompteSalaireSearchSimpleModel extends JadeSearchSimpleModel {
	private static final long serialVersionUID = 1L;
	private String forId;
    private String forIdDecompteSalaire;

    public String getForId() {
        return forId;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public String getForIdDecompteSalaire() {
        return forIdDecompteSalaire;
    }

    public void setForIdDecompteSalaire(final String forIdDecompteSalaire) {
        this.forIdDecompteSalaire = forIdDecompteSalaire;
    }

    @Override
    public Class<CodeErreurDecompteSalaireSimpleModel> whichModelClass() {
        return CodeErreurDecompteSalaireSimpleModel.class;
    }

}
