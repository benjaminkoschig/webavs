package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePersonneDansPlanCalcul extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDroitMembreFamille = null;
    private String idPersonneDansPlanCalcul = null;
    private String idPlanDeCalcul = null;
    private Boolean isComprisDansCalcul = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPersonneDansPlanCalcul;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public String getIdPersonneDansPlanCalcul() {
        return idPersonneDansPlanCalcul;
    }

    public String getIdPlanDeCalcul() {
        return idPlanDeCalcul;
    }

    public Boolean getIsComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPersonneDansPlanCalcul = id;
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public void setIdPersonneDansPlanCalcul(String idPersonneDansPlanCalcul) {
        this.idPersonneDansPlanCalcul = idPersonneDansPlanCalcul;
    }

    public void setIdPlanDeCalcul(String idPlanDeCalcul) {
        this.idPlanDeCalcul = idPlanDeCalcul;
    }

    public void setIsComprisDansCalcul(Boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

}
