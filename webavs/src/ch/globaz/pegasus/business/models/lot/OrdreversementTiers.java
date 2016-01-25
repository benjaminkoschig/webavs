package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;

public class OrdreversementTiers extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissance;
    private String designation1;
    private String designation2;
    private String idPays;
    private String idTiers;
    private String numAvs;
    private String sexe;

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    @Override
    public String getId() {
        return idTiers;
    }

    public String getIdPays() {
        return idPays;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getSexe() {
        return sexe;
    }

    @Override
    public String getSpy() {
        throw new UnsupportedOperationException("Not used");
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException("Not used");
    }

    public void setIdPays(String idPays) {
        this.idPays = idPays;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Override
    public void setSpy(String spy) {
        throw new UnsupportedOperationException("Not used");
    }
}
