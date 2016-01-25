package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimplePrestationSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = null;

    public SimplePrestationSearchModel() {
        super();
    }

    /**
     * @return the forIdLot
     */
    public String getForIdLot() {
        return forIdLot;
    }

    /**
     * @param forIdLot
     *            the forIdLot to set
     */
    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    @Override
    public Class whichModelClass() {
        return SimplePrestation.class;
    };

}
