package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class AbsenceSearchSimpleModel extends JadeSearchSimpleModel {
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
    public Class<AbsenceSimpleModel> whichModelClass() {
        return AbsenceSimpleModel.class;
    }

}
