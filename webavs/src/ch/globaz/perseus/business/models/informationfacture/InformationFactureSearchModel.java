package ch.globaz.perseus.business.models.informationfacture;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class InformationFactureSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String betweenDateDebut = null;
    private String betweenDateFin = null;
    private String forDate = null;
    private String forIdDossier = null;

    public String getBetweenDateDebut() {
        return betweenDateDebut;
    }

    public String getBetweenDateFin() {
        return betweenDateFin;
    }

    public String getForDate() {
        return forDate;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setBetweenDateDebut(String betweenDateDebut) {
        this.betweenDateDebut = betweenDateDebut;
    }

    public void setBetweenDateFin(String betweenDateFin) {
        this.betweenDateFin = betweenDateFin;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return InformationFacture.class;
    }

}
