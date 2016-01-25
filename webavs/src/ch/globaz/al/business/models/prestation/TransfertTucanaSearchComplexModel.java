package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TransfertTucanaSearchComplexModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateVersCompDebut = null;
    private String forDateVersCompFin = null;
    private String forEtat = null;
    private String forNumBouclement = null;

    public String getForDateVersCompDebut() {
        return forDateVersCompDebut;
    }

    public String getForDateVersCompFin() {
        return forDateVersCompFin;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForNumBouclement() {
        return forNumBouclement;
    }

    public void setForDateVersCompDebut(String forDateVersCompDebut) {
        this.forDateVersCompDebut = forDateVersCompDebut;
    }

    public void setForDateVersCompFin(String forDateVersCompFin) {
        this.forDateVersCompFin = forDateVersCompFin;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForNumBouclement(String forPeriodeBouclement) {
        forNumBouclement = forPeriodeBouclement;
    }

    @Override
    public Class<TransfertTucanaComplexModel> whichModelClass() {
        return TransfertTucanaComplexModel.class;
    }

}
