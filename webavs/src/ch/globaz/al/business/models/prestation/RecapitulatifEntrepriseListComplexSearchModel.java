package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RecapitulatifEntrepriseListComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * crit�re num�ro r�cap
     */
    private String forIdRecap = null;
    /**
     * crit�re num�ro affili�
     */
    private String forNumeroAffilie = null;
    /**
     * crit�re num�ro processus li�
     */
    private String forNumProcessusLie = null;

    /**
     * 
     * @return forIdRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * 
     * @return forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * 
     * @return forNumeroAffilie
     */
    public String getForNumProcessusLie() {
        return forNumProcessusLie;
    }

    /**
     * 
     * @param forIdRecap
     */
    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /**
     * 
     * @param forNumeroAffilie
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * 
     * @param forNumProcessusLie
     */
    public void setForNumProcessusLie(String forNumProcessusLie) {
        this.forNumProcessusLie = forNumProcessusLie;
    }

    @Override
    public Class<RecapitulatifEntrepriseListComplexModel> whichModelClass() {
        return RecapitulatifEntrepriseListComplexModel.class;
    }

}
