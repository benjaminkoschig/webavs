package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class AbsenceSearchComplexModel extends JadeSearchComplexModel {
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
    public Class<AbsenceComplexModel> whichModelClass() {
        return AbsenceComplexModel.class;
    }
}
