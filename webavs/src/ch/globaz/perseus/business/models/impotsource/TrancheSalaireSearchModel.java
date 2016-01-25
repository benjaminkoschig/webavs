package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TrancheSalaireSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forIdTrancheSalaire = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdTrancheSalaire() {
        return forIdTrancheSalaire;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdTrancheSalaire(String forIdTrancheSalaire) {
        this.forIdTrancheSalaire = forIdTrancheSalaire;
    }

    @Override
    public Class whichModelClass() {
        return TrancheSalaire.class;
    }

}
