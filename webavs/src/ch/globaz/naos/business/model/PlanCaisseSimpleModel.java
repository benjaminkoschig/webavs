package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class PlanCaisseSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idTiers;

    private String libelle;

    private String planCaisseId;

    private String typeAffiliation;

    @Override
    public String getId() {
        return getPlanCaisseId();
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getPlanCaisseId() {
        return planCaisseId;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    @Override
    public void setId(String id) {
        setPlanCaisseId(id);
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setPlanCaisseId(String planCaisseId) {
        this.planCaisseId = planCaisseId;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }
}
