package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import java.util.List;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class CotisationDecompteSearchComplexModel extends JadeSearchComplexModel {
    private String forId;
    private String forIdLigneDecompte;
    private String forIdCotisation;
    private Collection<String> forIdIn;

    public String getForId() {
        return forId;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public String getForIdLigneDecompte() {
        return forIdLigneDecompte;
    }

    public void setForIdLigneDecompte(final String forIdLigneDecompte) {
        this.forIdLigneDecompte = forIdLigneDecompte;
    }

    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public void setForIdCotisation(final String forIdCotisation) {
        this.forIdCotisation = forIdCotisation;
    }

    public Collection<String> getForIdIn() {
        return forIdIn;
    }

    public void setForIdIn(final Collection<String> forIdIn) {
        this.forIdIn = forIdIn;
    }

    public void setInId(final List<String> ids) {
        forIdIn = ids;
    }

    @Override
    public Class<CotisationDecompteComplexModel> whichModelClass() {
        return CotisationDecompteComplexModel.class;
    }
}
