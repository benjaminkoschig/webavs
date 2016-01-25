/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author MBO
 * 
 */
public class CreancierRentePontSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCreancier = null;
    private String forIdRentePont = null;
    private String forIdTiers = null;
    private String forNotIdCreancier = null;

    public String getForIdCreancier() {
        return forIdCreancier;
    }

    /**
     * @return the forIdRentePont
     */
    public String getForIdRentePont() {
        return forIdRentePont;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forNotIdCreancier
     */
    public String getForNotIdCreancier() {
        return forNotIdCreancier;
    }

    public void setForIdCreancier(String forIdCreancier) {
        this.forIdCreancier = forIdCreancier;
    }

    /**
     * @param forIdRentePont
     *            the forIdRentePont to set
     */
    public void setForIdRentePont(String forIdRentePont) {
        this.forIdRentePont = forIdRentePont;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forNotIdCreancier
     *            the forNotIdCreancier to set
     */
    public void setForNotIdCreancier(String forNotIdCreancier) {
        this.forNotIdCreancier = forNotIdCreancier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return CreancierRentePont.class;
    }

}
