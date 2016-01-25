/**
 * 
 */
package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author MBO
 * 
 */
public class SimpleOrdreVersementSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdOrdreVersement = null;
    private String forIdPrestation = null;

    public String getForIdOrdreVersement() {
        return forIdOrdreVersement;
    }

    /**
     * @return the forIdPrestation
     */
    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public void setForIdOrdreVersement(String forIdOrdreVersement) {
        this.forIdOrdreVersement = forIdOrdreVersement;
    }

    /**
     * @param forIdPrestation
     *            the forIdPrestation to set
     */
    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleOrdreVersement.class;
    }

}
