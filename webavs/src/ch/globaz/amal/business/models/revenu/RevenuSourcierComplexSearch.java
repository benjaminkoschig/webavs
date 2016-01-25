package ch.globaz.amal.business.models.revenu;

public class RevenuSourcierComplexSearch extends RevenuSearch {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class whichModelClass() {
        return RevenuSourcierComplex.class;
    }

}
