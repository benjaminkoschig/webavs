package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CalculVariableMetierSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class<CalculVariableMetier> whichModelClass() {
        return CalculVariableMetier.class;
    }

}
