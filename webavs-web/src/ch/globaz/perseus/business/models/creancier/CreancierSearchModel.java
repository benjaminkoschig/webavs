/**
 * 
 */
package ch.globaz.perseus.business.models.creancier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author MBO
 * 
 */
public class CreancierSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCreancier = null;
    private String forIdDemande = null;
    private String forIdTiers = null;
    private String forNotIdCreancier = null;

    public String getForIdCreancier() {
        return forIdCreancier;
    }

    public String getForIdDemande() {
        return forIdDemande;
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

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
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
    public Class<Creancier> whichModelClass() {
        return Creancier.class;
    }

}
