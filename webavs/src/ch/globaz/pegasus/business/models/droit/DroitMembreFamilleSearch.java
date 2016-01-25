/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * @author BSC
 * 
 */
public class DroitMembreFamilleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List forCsRoletMembreFamilleIn = null;
    private String forIdDroit = null;
    private String forIdDroitMembreFamille = null;
    private List forInIdDroit = null;

    /**
     * @return the forCsRoletMembreFamilleIn
     */
    public List getForCsRoletMembreFamilleIn() {
        return forCsRoletMembreFamilleIn;
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

    public List getForInIdDroit() {
        return forInIdDroit;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forCsRoletMembreFamilleIn
     *            the forCsRoletMembreFamilleIn to set
     */
    public void setForCsRoletMembreFamilleIn(List forCsRoletMembreFamilleIn) {
        this.forCsRoletMembreFamilleIn = forCsRoletMembreFamilleIn;
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

    public void setForInIdDroit(List forInIdDroit) {
        this.forInIdDroit = forInIdDroit;
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
        return DroitMembreFamille.class;
    }

}
