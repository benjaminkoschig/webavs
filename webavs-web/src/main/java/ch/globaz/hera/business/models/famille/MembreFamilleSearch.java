package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class MembreFamilleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMembreFamille = null;
    private List<String> forIdMembreFamilleIn = null;
    private String forNss = null;
    private String forIdTiers = null;

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public String getForNss() {
        return forNss;
    }

    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    /**
     * @return the forIdMembreFamille
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return the forIdMembreFamilleIn
     */
    public List<String> getForIdMembreFamilleIn() {
        return forIdMembreFamilleIn;
    }

    /**
     * @param forIdMembreFamille
     *            the forIdMembreFamille to set
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    /**
     * @param forIdMembreFamilleIn
     *            the forIdMembreFamilleIn to set
     */
    public void setForIdMembreFamilleIn(List<String> forIdMembreFamilleIn) {
        this.forIdMembreFamilleIn = forIdMembreFamilleIn;
    }

    @Override
    public Class<MembreFamille> whichModelClass() {
        return MembreFamille.class;
    }

}
