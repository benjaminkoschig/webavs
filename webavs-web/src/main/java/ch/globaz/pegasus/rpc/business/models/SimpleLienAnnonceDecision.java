package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleLienAnnonceDecision extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String idAnnonce;
    private String idDecision;
    private String idPlanCalcul;
    private String csRole;

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public String getIdPlanCalcul() {
        return idPlanCalcul;
    }

    public void setIdPlanCalcul(String idPlanCalcul) {
        this.idPlanCalcul = idPlanCalcul;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCsRole() {
        return csRole;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

}
