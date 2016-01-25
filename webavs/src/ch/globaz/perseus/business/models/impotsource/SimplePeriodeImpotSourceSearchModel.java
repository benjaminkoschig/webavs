package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimplePeriodeImpotSourceSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutCheckPeriode = null;
    private String forDateFinCheckPeriode = null;
    private String forIdPeriodeImpotSource = null;

    public String getForDateDebutCheckPeriode() {
        return forDateDebutCheckPeriode;
    }

    public String getForDateFinCheckPeriode() {
        return forDateFinCheckPeriode;
    }

    public void setForDateDebutCheckPeriode(String forDateDebutCheckPeriode) {
        this.forDateDebutCheckPeriode = forDateDebutCheckPeriode;
    }

    public void setForDateFinCheckPeriode(String forDateFinCheckPeriode) {
        this.forDateFinCheckPeriode = forDateFinCheckPeriode;
    }

    @Override
    public Class whichModelClass() {
        return SimplePeriodeImpotSource.class;
    }

    public void setForIdPeriodeImpotSource(String forIdPeriodeImpotSource) {
        this.forIdPeriodeImpotSource = forIdPeriodeImpotSource;
    }

    public String getForIdPeriodeImpotSource() {
        return forIdPeriodeImpotSource;
    }

}
