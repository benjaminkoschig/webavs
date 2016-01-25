package ch.globaz.lyra.business.models.echeance;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class LYSimpleEcheanceSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDomaineApplicatif;
    private String forIdEcheance;

    public LYSimpleEcheanceSearchModel() {
        super();

        forCsDomaineApplicatif = "";
        forIdEcheance = "";
    }

    public String getForCsDomaineApplicatif() {
        return forCsDomaineApplicatif;
    }

    public String getForIdEcheance() {
        return forIdEcheance;
    }

    public void setForCsDomaineApplicatif(String forCsDomaineApplicatif) {
        this.forCsDomaineApplicatif = forCsDomaineApplicatif;
    }

    public void setForIdEcheance(String forIdEcheance) {
        this.forIdEcheance = forIdEcheance;
    }

    @Override
    public Class<LYSimpleEcheance> whichModelClass() {
        return LYSimpleEcheance.class;
    }
}
