package ch.globaz.amal.business.models.revenu;

public class RevenuContribuableComplex extends Revenu {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleRevenuContribuable simpleRevenuContribuable = null;

    public RevenuContribuableComplex() {
        super();
        simpleRevenuContribuable = new SimpleRevenuContribuable();
    }

    public SimpleRevenuContribuable getSimpleRevenuContribuable() {
        return simpleRevenuContribuable;
    }

    public void setSimpleRevenuContribuable(SimpleRevenuContribuable simpleRevenuContribuable) {
        this.simpleRevenuContribuable = simpleRevenuContribuable;
    }
}
