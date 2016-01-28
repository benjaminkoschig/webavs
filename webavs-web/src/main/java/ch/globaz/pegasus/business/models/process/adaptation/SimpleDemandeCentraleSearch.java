package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleDemandeCentraleSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class<SimpleDemandeCentrale> whichModelClass() {
        return SimpleDemandeCentrale.class;
    }

}
