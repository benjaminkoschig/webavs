package ch.globaz.pegasus.business.models.process.allocationsNoel;

import globaz.jade.persistence.model.JadeComplexModel;

public class CoupleSepareParLaMaladie extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemande;
    private String idDroit;
    private String idPCAccordee;
    private String idVersionDroit;

    @Override
    public String getId() {
        return getIdPCAccordee();
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
        idPCAccordee = id;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    @Override
    public void setSpy(String spy) {
    }

}
