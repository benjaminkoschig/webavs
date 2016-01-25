package ch.globaz.pegasus.business.models.blocage;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PcaBloqueConjointSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande;
    private String forIdPca;

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdPca() {
        return forIdPca;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdPca(String forIdPca) {
        this.forIdPca = forIdPca;
    }

    @Override
    public Class<PcaBloqueConjoint> whichModelClass() {
        return PcaBloqueConjoint.class;
    }
}
