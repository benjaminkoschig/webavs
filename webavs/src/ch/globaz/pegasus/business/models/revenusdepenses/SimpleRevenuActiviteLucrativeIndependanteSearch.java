/**
 * 
 */
package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author BSC
 * 
 */
public class SimpleRevenuActiviteLucrativeIndependanteSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDonneesPersonnelles = null;
    private List<String> forListIdDonneeFinanciere = null;

    /**
     * @return the forIdDonneesPersonnelles
     */
    public String getForIdDonneesPersonnelles() {
        return forIdDonneesPersonnelles;
    }

    /**
     * @return the forListIdDonneeFinanciere
     */
    public List<String> getForListIdDonneeFinanciere() {
        return forListIdDonneeFinanciere;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forIdDonneesPersonnelles
     *            the forIdDonneesPersonnelles to set
     */
    public void setForIdDonneesPersonnelles(String forIdDonneesPersonnelles) {
        this.forIdDonneesPersonnelles = forIdDonneesPersonnelles;
    }

    /**
     * @param forListIdDonneeFinanciere
     *            the forListIdDonneeFinanciere to set
     */
    public void setForListIdDonneeFinanciere(List<String> forListIdDonneeFinanciere) {
        this.forListIdDonneeFinanciere = forListIdDonneeFinanciere;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleRevenuActiviteLucrativeIndependante.class;
    }

}
