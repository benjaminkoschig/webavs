package ch.globaz.pegasus.business.models.variablemetier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class VariableMetierSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeVariableMetier = null;
    private String forDateValable = null;
    private String forLangue = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forCsTypeVariableMetierIn = null;

    public String getForCsTypeVariableMetierIn() {
        return forCsTypeVariableMetierIn;
    }

    public void setForCsTypeVariableMetierIn(String forCsTypeVariableMetierIn) {
        this.forCsTypeVariableMetierIn = forCsTypeVariableMetierIn;
    }

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

    /**
     * @return the forCsTypeVariableMetier
     */
    public String getForCsTypeVariableMetier() {
        return forCsTypeVariableMetier;
    }

    /**
     * @return the forValableLe
     */
    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @param forCsTypeVariableMetier
     *            the forCsTypeVariableMetier to set
     */
    public void setForCsTypeVariableMetier(String forIdTypeVariableMetier) {
        forCsTypeVariableMetier = forIdTypeVariableMetier;
    }

    /**
     * @param forValableLe
     *            the forValableLe to set
     */
    public void setForDateValable(String forValableLe) {
        forDateValable = forValableLe;
    }

    public String getForLangue() {
        return forLangue;
    }

    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    @Override
    public Class<VariableMetier> whichModelClass() {
        return VariableMetier.class;
    }

}
