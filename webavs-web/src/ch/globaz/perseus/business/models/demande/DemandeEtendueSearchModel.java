package ch.globaz.perseus.business.models.demande;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DemandeEtendueSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String ORDER_BY_DATETIME_DECISION = "dateTimeDecision";

    private String forCsEtat = null;
    private String forIdDossier = null;
    private Boolean forIsDemandeIp = null;

    /**
     * @return the forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return DemandeEtendue.class;
    }

    public void setForIsDemandeIp(Boolean forIsDemandeIp) {
        this.forIsDemandeIp = forIsDemandeIp;
    }

    public Boolean getForIsDemandeIp() {
        return forIsDemandeIp;
    }

}
