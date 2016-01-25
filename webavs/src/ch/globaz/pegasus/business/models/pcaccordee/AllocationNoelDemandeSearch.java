package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AllocationNoelDemandeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forIdDemande = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    @Override
    public Class<AllocationNoelDemande> whichModelClass() {
        return AllocationNoelDemande.class;
    }

}
