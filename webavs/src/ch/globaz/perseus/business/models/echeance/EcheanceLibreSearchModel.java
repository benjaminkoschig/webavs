package ch.globaz.perseus.business.models.echeance;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class EcheanceLibreSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String betweenDateButoireDebut = null;
    private String betweenDateButoireFin = null;
    private String forDateButoire = null;
    private String forIdDossier = null;

    public String getBetweenDateButoireDebut() {
        return betweenDateButoireDebut;
    }

    public String getBetweenDateButoireFin() {
        return betweenDateButoireFin;
    }

    public String getForDateButoire() {
        return forDateButoire;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setBetweenDateButoireDebut(String betweenDateButoireDebut) {
        this.betweenDateButoireDebut = betweenDateButoireDebut;
    }

    public void setBetweenDateButoireFin(String betweenDateButoireFin) {
        this.betweenDateButoireFin = betweenDateButoireFin;
    }

    public void setForDateButoire(String forDateButoire) {
        this.forDateButoire = forDateButoire;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return EcheanceLibre.class;
    }

}
