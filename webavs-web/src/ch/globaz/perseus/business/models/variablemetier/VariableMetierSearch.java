package ch.globaz.perseus.business.models.variablemetier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class VariableMetierSearch extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1L;
    public static String WITH_DATE_VALABLE = "withDateValable";
    private String forCsTypeVariableMetier = null;

    private String forDateValable = null;

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

    @Override
    public Class<VariableMetier> whichModelClass() {
        return VariableMetier.class;
    }

}
