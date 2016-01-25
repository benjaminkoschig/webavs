package ch.globaz.pegasus.business.models.process.statistiquesofas;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RenteApiIjSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String FOR_PC_ACCORDEE_WITH_DATE_VALABLE = "forIdPCAccordeeAndDateValable";

    private String forCsRoleFamille = null;
    private String forDateValable = null;
    private String forIdDroit = null;
    private String forIdPCAccordee = null;
    private Boolean forIsSupprime = null;

    public String getForCsRoleFamille() {
        return forCsRoleFamille;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public Boolean getForIsSupprime() {
        return forIsSupprime;
    }

    public void setForCsRoleFamille(String forCsRoleFamille) {
        this.forCsRoleFamille = forCsRoleFamille;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setForIsSupprime(Boolean forIsSupprime) {
        this.forIsSupprime = forIsSupprime;
    }

    @Override
    public Class<RenteApiIj> whichModelClass() {
        return RenteApiIj.class;
    }

}
