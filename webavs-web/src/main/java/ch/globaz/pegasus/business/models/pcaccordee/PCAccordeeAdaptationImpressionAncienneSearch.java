package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PCAccordeeAdaptationImpressionAncienneSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValidite = null;
    private String forIdDecisionHeader = null;
    /**
     * D�fint qu'il faut retourner que les pc accordee de la version version atuelle ( du jour)
     */

    private String forIdDemande = null;
    private String forIdDroit = null;
    private String forIdVersionDroit = null;

    public String getForDateValidite() {
        return forDateValidite;
    }

    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForDateValidite(String forDateValidite) {
        this.forDateValidite = forDateValidite;
    }

    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return PCAccordeeAdaptationImpressionAncienne.class;
    }

}
