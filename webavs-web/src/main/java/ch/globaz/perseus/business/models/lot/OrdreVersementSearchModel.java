package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * 
 * @author MBO
 * 
 */
public class OrdreVersementSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = null;
    private String forIdPrestation = null;

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<OrdreVersement> whichModelClass() {
        return OrdreVersement.class;
    }

}
