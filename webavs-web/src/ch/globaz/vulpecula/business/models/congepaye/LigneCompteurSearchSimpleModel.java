package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

public class LigneCompteurSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forIdCompteur;
    private String forIdCongePaye;
    private Collection<String> forIdCompteurIn;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdCompteur() {
        return forIdCompteur;
    }

    public void setForIdCompteur(String forIdCompteur) {
        this.forIdCompteur = forIdCompteur;
    }

    public String getForIdCongePaye() {
        return forIdCongePaye;
    }

    public void setForIdCongePaye(String forIdCongePaye) {
        this.forIdCongePaye = forIdCongePaye;
    }

    public Collection<String> getForIdCompteurIn() {
        return forIdCompteurIn;
    }

    public void setForIdCompteurIn(Collection<String> forIdCompteurIn) {
        this.forIdCompteurIn = forIdCompteurIn;
    }

    @Override
    public Class<LigneCompteurSimpleModel> whichModelClass() {
        return LigneCompteurSimpleModel.class;
    }

}
