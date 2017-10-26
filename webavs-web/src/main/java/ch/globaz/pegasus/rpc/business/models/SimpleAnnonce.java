package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonce extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String idLot;
    private String idDossier;
    private String idDemande;
    private String idVersionDroit;
    private String csEtat;
    private String csType;
    private String csCodeTraitement;

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public String getCsCodeTraitement() {
        return csCodeTraitement;
    }

    public void setCsCodeTraitement(String csCodeTraitement) {
        this.csCodeTraitement = csCodeTraitement;
    }

    public String getCsType() {
        return csType;
    }

    public void setCsType(String csType) {
        this.csType = csType;
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
        return "SimpleAnnonce [id=" + id + ", idLot=" + idLot + ", idDossier=" + idDossier + ", idDemande=" + idDemande
                + ", idVersionDroit=" + idVersionDroit + ", csEtat=" + csEtat + ", csType=" + csType
                + ", csCodeTraitement=" + csCodeTraitement + "]";
    }

}
