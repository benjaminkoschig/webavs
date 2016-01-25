package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AdhesionSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adhesionId;
    private String affiliationId;
    private String dateDebut;
    private String dateFin;
    private String idTiers;
    private String planCaisseId;
    private String typeAdhesion;

    public String getAdhesionId() {
        return adhesionId;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return getAdhesionId();
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getPlanCaisseId() {
        return planCaisseId;
    }

    public String getTypeAdhesion() {
        return typeAdhesion;
    }

    public void setAdhesionId(String adhesionId) {
        this.adhesionId = adhesionId;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        setAdhesionId(id);
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setPlanCaisseId(String planCaisseId) {
        this.planCaisseId = planCaisseId;
    }

    public void setTypeAdhesion(String typeAdhesion) {
        this.typeAdhesion = typeAdhesion;
    }

}
