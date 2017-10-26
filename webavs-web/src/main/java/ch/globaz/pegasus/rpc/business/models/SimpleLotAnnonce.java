package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleLotAnnonce extends JadeSimpleModel {

    private static final long serialVersionUID = 1L;
    private String id;
    private String idJob;
    private String csEtat;
    private String csType;
    private String dateEnvoi;

    public String getCsEtat() {
        return csEtat;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getCsType() {
        return csType;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleLotAnnonce [id=" + id + ", idJob=" + idJob + ", csEtat=" + csEtat + ", csType=" + csType
                + ", dateEnvoi=" + dateEnvoi + "]";
    }

}
