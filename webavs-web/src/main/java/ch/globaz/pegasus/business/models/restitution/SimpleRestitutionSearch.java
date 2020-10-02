package ch.globaz.pegasus.business.models.restitution;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class SimpleRestitutionSearch extends JadeAbstractSearchModel {

    private String forIdDossier;

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return SimpleRestitution.class;
    }
}
