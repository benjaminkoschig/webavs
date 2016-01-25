package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ChambreMedicaliseeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsMedicalise = null;
    private String forCsTypeDonneeFinanciere = null;
    private String forDateDonneeFinanciere = null;
    private String forIdTiers = null;
    private String forIdVersionPcDroit = null;

    public String getForCsMedicalise() {
        return forCsMedicalise;
    }

    public String getForCsTypeDonneeFinanciere() {
        return forCsTypeDonneeFinanciere;
    }

    public String getForDateDonneeFinanciere() {
        return forDateDonneeFinanciere;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdVersionPcDroit() {
        return forIdVersionPcDroit;
    }

    public void setForCsMedicalise(String forCsMedicalise) {
        this.forCsMedicalise = forCsMedicalise;
    }

    public void setForCsTypeDonneeFinanciere(String forCsTypeDonneeFinanciere) {
        this.forCsTypeDonneeFinanciere = forCsTypeDonneeFinanciere;
    }

    public void setForDateDonneeFinanciere(String forDateDonneeFinanciere) {
        this.forDateDonneeFinanciere = forDateDonneeFinanciere;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdVersionPcDroit(String forIdVersionPcDroit) {
        this.forIdVersionPcDroit = forIdVersionPcDroit;
    }

    @Override
    public Class whichModelClass() {
        return ChambreMedicalisee.class;
    }

}
