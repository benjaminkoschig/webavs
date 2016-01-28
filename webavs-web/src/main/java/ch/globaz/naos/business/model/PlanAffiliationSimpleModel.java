package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class PlanAffiliationSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String affiliationId = null;

    private String libelle = null;
    private String planAffiliationId = null;

    public String getAffiliationId() {
        return affiliationId;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setPlanAffiliationId(String planAffiliationId) {
        this.planAffiliationId = planAffiliationId;
    }

}
