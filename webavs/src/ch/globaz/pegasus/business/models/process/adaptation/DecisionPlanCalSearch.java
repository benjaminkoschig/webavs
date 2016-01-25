package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DecisionPlanCalSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsMotif = null;
    private String forDateDebut = null;
    private String nss = null;

    public String getForCsMotif() {
        return forCsMotif;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getNss() {
        return nss;
    }

    public void setForCsMotif(String forCsMotif) {
        this.forCsMotif = forCsMotif;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Override
    public Class<DecisionPlanCal> whichModelClass() {
        return DecisionPlanCal.class;
    }

}
