/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleDroitMembreFamilleSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDonneesPersonnelles = null;
    private String forIdDroit = null;
    private String forIdMembreFamilleSF = null;

    /**
     * @return the forIdDonneesPersonnelles
     */
    public String getForIdDonneesPersonnelles() {
        return forIdDonneesPersonnelles;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdMembreFamilleSF
     */
    public String getForIdMembreFamilleSF() {
        return forIdMembreFamilleSF;
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
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdMembreFamilleSF
     *            the forIdMembreFamilleSF to set
     */
    public void setForIdMembreFamilleSF(String forIdMembreFamilleSf) {
        forIdMembreFamilleSF = forIdMembreFamilleSf;
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
        return SimpleDroitMembreFamille.class;
    }

}
