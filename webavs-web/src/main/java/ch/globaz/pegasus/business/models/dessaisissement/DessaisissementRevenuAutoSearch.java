/**
 * 
 */
package ch.globaz.pegasus.business.models.dessaisissement;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author ECO
 * 
 */
public class DessaisissementRevenuAutoSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forIdDroit = null;

    private String forIdDroitMembreFamille = null;

    private String forIdVersionDroit = null;

    private String forNumeroVersion = null;

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
     * @return the forIdVersionDroit
     */
    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    /**
     * @return the forNumeroVersion
     */
    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
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
     * @param forIdVersionDroit
     *            the forIdVersionDroit to set
     */
    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    /**
     * @param forNumeroVersion
     *            the forNumeroVersion to set
     */
    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
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
        return DessaisissementRevenuAuto.class;
    }

}
