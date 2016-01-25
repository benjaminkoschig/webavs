package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DemandePcaPersonneDansCalSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande;
    private String forIdDossier;
    private String forIdDroit;

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    @Override
    public Class<DemandePcaPersonneDansCal> whichModelClass() {
        return DemandePcaPersonneDansCal.class;
    }

}
