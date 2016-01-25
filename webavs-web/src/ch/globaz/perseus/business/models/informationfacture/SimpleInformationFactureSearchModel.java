package ch.globaz.perseus.business.models.informationfacture;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleInformationFactureSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDossier = null;

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return SimpleInformationFactureSearchModel.class;
    }

}
