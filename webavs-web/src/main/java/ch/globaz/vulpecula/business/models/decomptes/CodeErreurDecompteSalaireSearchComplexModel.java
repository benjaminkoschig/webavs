package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * 
 */
public class CodeErreurDecompteSalaireSearchComplexModel extends JadeSearchComplexModel {
    private String forId;
    private String forIdLigneDecompte;
    private Collection<String> inIdLigneDecompte;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdLigneDecompte() {
        return forIdLigneDecompte;
    }

    public void setForIdLigneDecompte(String forIdLigneDecompte) {
        this.forIdLigneDecompte = forIdLigneDecompte;
    }

    public void setInIdLigneDecompte(Collection<String> ids) {
        inIdLigneDecompte = ids;
    }

    public Collection<String> getInIdLigneDecompte() {
        return inIdLigneDecompte;
    }

    @Override
    public Class<CodeErreurDecompteSalaireComplexModel> whichModelClass() {
        return CodeErreurDecompteSalaireComplexModel.class;
    }
}
