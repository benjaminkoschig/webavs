package ch.globaz.perseus.business.models.statistique;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class StatistiquesMensuellesDemPcfDecSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = null;
    private String forDateFin = null;

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    @Override
    public Class whichModelClass() {
        return StatistiquesMensuellesDemPcfDec.class;
    }

}
