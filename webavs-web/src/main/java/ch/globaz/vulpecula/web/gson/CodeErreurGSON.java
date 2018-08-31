package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class CodeErreurGSON implements Serializable {
	private static final long serialVersionUID = 843184074527721893L;
	public String id;
    public String codeErreur;

    /**
	 * @return the codeErreur
	 */
	public String getCodeErreur() {
		return codeErreur;
	}

	/**
	 * @param codeErreur the codeErreur to set
	 */
	public void setCodeErreur(String codeErreur) {
		this.codeErreur = codeErreur;
	}

	public CodeErreurDecompteSalaire convertToDomain(final DecompteSalaire decompteSalaire) {
        CodeErreurDecompteSalaire codeErreurDS = new CodeErreurDecompteSalaire();

        codeErreurDS.setCodeErreur(CodeErreur.fromValue(codeErreur));

        return codeErreurDS;
    }
}
