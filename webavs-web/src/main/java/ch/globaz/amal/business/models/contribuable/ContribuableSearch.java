package ch.globaz.amal.business.models.contribuable;

/**
 * @author CBU
 * 
 */
public class ContribuableSearch extends ContribuableOnlySearch {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDetailFamille = null;

    public ContribuableSearch() {
        super();
        forIdDetailFamille = new String();
    }

    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.models.dossier.DossierSearch#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Contribuable.class;
    }
}
