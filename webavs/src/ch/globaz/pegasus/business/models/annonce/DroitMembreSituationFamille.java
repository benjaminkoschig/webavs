package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;

public class DroitMembreSituationFamille extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csRoleFamille = null;
    private String idDroitMembreFamille;
    private String idTiers = null;

    public String getCsRoleFamille() {
        return csRoleFamille;
    }

    @Override
    public String getId() {
        return idDroitMembreFamille;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCsRoleFamille(String csRoleFamille) {
        this.csRoleFamille = csRoleFamille;
    }

    @Override
    public void setId(String id) {
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Override
    public void setSpy(String spy) {
    }

}
