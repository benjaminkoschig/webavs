package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RenteMembreFamilleCalculeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_DROIT = "idDroit";
    public final static String WITH_DATE_VALABLE = "withDateValable";

    private String forCsEtatVersionDroit;
    private String forDate;
    private String forDateFin;
    private boolean forIsPlanRetenu = false;

    public String getForCsEtatVersionDroit() {
        return forCsEtatVersionDroit;
    }

    public String getForDate() {
        return forDate;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public void setForCsEtatVersionDroit(String forCsEtatVersionDroit) {
        this.forCsEtatVersionDroit = forCsEtatVersionDroit;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIsPlanRetenu(boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    @Override
    public Class<RenteMembreFamilleCalcule> whichModelClass() {
        return RenteMembreFamilleCalcule.class;
    }

}
