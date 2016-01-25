package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 4 mars 2014
 * 
 */
public class CotisationParametreSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 5522882620712294668L;

    private String forId = "";

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    private String forIdCotisation = "";

    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public void setForIdCotisation(String forIdCotisation) {
        this.forIdCotisation = forIdCotisation;
    }

    @Override
    public Class<CotisationParametreComplexModel> whichModelClass() {
        return CotisationParametreComplexModel.class;
    }

}
