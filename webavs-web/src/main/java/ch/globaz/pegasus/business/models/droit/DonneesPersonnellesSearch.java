/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author BSC
 */
public class DonneesPersonnellesSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FOR_DROIT = "forDroit";
    private String forCsRoleFamillePC = null;
    private String forIdDroit = null;
    private String forIdDroitMembreFamille = null;
    private String forIdDonneesPersonnelles = null;

    public String getForCsRoleFamillePC() {
        return forCsRoleFamillePC;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdDroitMembreFamille
     */
    public String getForIdDroitMembreFamille() {
        return forIdDroitMembreFamille;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForCsRoleFamillePC(String forCsRoleFamillePC) {
        this.forCsRoleFamillePC = forCsRoleFamillePC;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdDroitMembreFamille
     *            the forIdDroitMembreFamille to set
     */
    public void setForIdDroitMembreFamille(String forIdDroitMembreFamille) {
        this.forIdDroitMembreFamille = forIdDroitMembreFamille;
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
    public Class<DonneesPersonnelles> whichModelClass() {
        return DonneesPersonnelles.class;
    }
    public String getForIdDonneesPersonnelles() {
        return forIdDonneesPersonnelles;
    }

    public void setForIdDonneesPersonnelles(String foridDonneesPersonnelles) {
        this.forIdDonneesPersonnelles = foridDonneesPersonnelles;
    }
}
