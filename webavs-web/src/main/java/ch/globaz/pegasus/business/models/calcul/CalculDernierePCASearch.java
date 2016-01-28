package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CalculDernierePCASearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroit = null;
    private String forNoVersionDroit = null;

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForNoVersionDroit() {
        return forNoVersionDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForNoVersionDroit(String forNoVersionDroit) {
        this.forNoVersionDroit = forNoVersionDroit;
    }

    @Override
    public Class whichModelClass() {
        return CalculDernierePCA.class;
    }

}
