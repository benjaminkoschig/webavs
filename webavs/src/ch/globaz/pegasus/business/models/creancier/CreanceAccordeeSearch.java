package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CreanceAccordeeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCreancier = null;
    private String forIdDemande = null;
    private String forIdDroit = null;
    private String forIdPCAccordee = null;
    private String forIdVersionDroit = null;

    public String getForIdCreancier() {
        return forIdCreancier;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdCreancier(String forIdCreancier) {
        this.forIdCreancier = forIdCreancier;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    @Override
    public Class<CreanceAccordee> whichModelClass() {
        return CreanceAccordee.class;
    }

}
