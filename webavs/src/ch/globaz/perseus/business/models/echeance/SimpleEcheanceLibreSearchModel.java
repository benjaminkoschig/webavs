package ch.globaz.perseus.business.models.echeance;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleEcheanceLibreSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande = null;

    public String getForIdDemande() {
        return forIdDemande;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    @Override
    public Class whichModelClass() {
        return SimpleEcheanceLibre.class;
    }

}
