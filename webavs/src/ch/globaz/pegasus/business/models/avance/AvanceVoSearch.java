package ch.globaz.pegasus.business.models.avance;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class AvanceVoSearch extends JadeAbstractSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemande = null;
    private String idTiers = null;

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    @Override
    public Class<AvanceVo> whichModelClass() {
        return AvanceVo.class;
    }

}
