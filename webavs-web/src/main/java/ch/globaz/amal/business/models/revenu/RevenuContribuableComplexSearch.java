package ch.globaz.amal.business.models.revenu;

public class RevenuContribuableComplexSearch extends RevenuSearch {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Class whichModelClass() {
        return RevenuContribuableComplex.class;
    }

}
