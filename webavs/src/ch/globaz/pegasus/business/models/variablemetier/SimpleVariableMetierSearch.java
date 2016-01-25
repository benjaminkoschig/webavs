package ch.globaz.pegasus.business.models.variablemetier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleVariableMetierSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeVariableMetier = null;
    private String forDateDebutCheckPeriode = null;
    private String forDateFinCheckPeriode = null;
    private String forDateValable = null;
    private String forIdVariableMetier = null;

    /**
     * @return the forCsTypeVariableMetier
     */
    public String getForCsTypeVariableMetier() {
        return forCsTypeVariableMetier;
    }

    /**
     * @return the forDateDebutCheckPeriode
     */
    public String getForDateDebutCheckPeriode() {
        return forDateDebutCheckPeriode;
    }

    /**
     * @return the forDateFinCheckPeriode
     */
    public String getForDateFinCheckPeriode() {
        return forDateFinCheckPeriode;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdVariableMetier
     */
    public String getForIdVariableMetier() {
        return forIdVariableMetier;
    }

    /**
     * @param forDateDebutCheckPeriode
     *            the forDateDebutCheckPeriode to set
     */
    public void setForDateDebutCheckPeriode(String forDateDebutCheckPeriode) {
        this.forDateDebutCheckPeriode = forDateDebutCheckPeriode;
    }

    /**
     * @param forDateFinCheckPeriode
     *            the forDateFinCheckPeriode to set
     */
    public void setForDateFinCheckPeriode(String forDateFinCheckPeriode) {
        this.forDateFinCheckPeriode = forDateFinCheckPeriode;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    /**
     * @param forIdTypeVariableMetier
     *            the forIdTypeVariableMetier to set
     */
    public void setForforCsTypeVariableMetier(String forCsTypeVariableMetier) {
        this.forCsTypeVariableMetier = forCsTypeVariableMetier;
    }

    /**
     * @param forIdVariableMetier
     *            the forIdVariableMetier to set
     */
    public void setForIdVariableMetier(String forIdVariableMetier) {
        this.forIdVariableMetier = forIdVariableMetier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleVariableMetier.class;
    }

}
