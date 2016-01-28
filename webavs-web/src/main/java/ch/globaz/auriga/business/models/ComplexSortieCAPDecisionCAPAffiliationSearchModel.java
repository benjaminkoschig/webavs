package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ComplexSortieCAPDecisionCAPAffiliationSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtat = null;
    private String forIdAffiliation = null;
    private String forIdDecision = null;
    private String forIdPassageFacturation = null;
    private String likeNumeroAffilie = null;

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForIdPassageFacturation() {
        return forIdPassageFacturation;
    }

    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForIdPassageFacturation(String forIdPassageFacturation) {
        this.forIdPassageFacturation = forIdPassageFacturation;
    }

    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    @Override
    public Class whichModelClass() {
        return ComplexSortieCAPDecisionCAPAffiliation.class;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

}
