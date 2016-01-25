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
