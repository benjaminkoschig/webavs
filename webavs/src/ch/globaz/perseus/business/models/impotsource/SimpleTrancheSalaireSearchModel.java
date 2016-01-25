package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleTrancheSalaireSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnnee = null;

    private String forIdTrancheSalaire = null;

    private String forSalaireBrutInferieur = null;

    private String forSalaireBrutSuperieur = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdTrancheSalaire() {
        return forIdTrancheSalaire;
    }

    public String getForSalaireBrutInferieur() {
        return forSalaireBrutInferieur;
    }

    public String getForSalaireBrutSuperieur() {
        return forSalaireBrutSuperieur;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdTrancheSalaire(String forIdTrancheSalaire) {
        this.forIdTrancheSalaire = forIdTrancheSalaire;
    }

    public void setForSalaireBrutInferieur(String forSalaireBrutInferieur) {
        this.forSalaireBrutInferieur = forSalaireBrutInferieur;
    }

    public void setForSalaireBrutSuperieur(String forSalaireBrutSuperieur) {
        this.forSalaireBrutSuperieur = forSalaireBrutSuperieur;
    }

    @Override
    public Class whichModelClass() {
        return SimpleTrancheSalaire.class;
    }

}
