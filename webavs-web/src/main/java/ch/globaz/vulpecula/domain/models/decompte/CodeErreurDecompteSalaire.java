package ch.globaz.vulpecula.domain.models.decompte;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * Code système représentant un code erreur pour un décompte salaire.
 * 
 */
public class CodeErreurDecompteSalaire implements DomainEntity {
    private String id;
    private CodeErreur code;
    private String spy;

    public CodeErreurDecompteSalaire() {
	}
    
    public CodeErreurDecompteSalaire(CodeErreur codeErreur) {
    	code = codeErreur;
	}

	public CodeErreur getCodeErreur() {
        return code;
    }

    public String getCodeErreurAsValue() {
        if (code != null) {
            return code.getValue();
        }
        return null;
    }

    public void setCodeErreur(CodeErreur type) {
        this.code = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
    	this.id = id;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
