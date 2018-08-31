package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class CotisationAssociationProfessionnelleSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forLibelleLike;
    private String forLibelleUpperLike;
    private String forFacturerDefaut;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForLibelleLike() {
        return forLibelleLike;
    }

    public void setForLibelleLike(String forLibelleLike) {
        this.forLibelleLike = forLibelleLike;
    }

    public String getForLibelleUpperLike() {
        return forLibelleUpperLike;
    }

    public void setForLibelleUpperLike(String forLibelleUpperLike) {
        this.forLibelleUpperLike = forLibelleUpperLike;
    }

    /**
     * @return the forFacturerDefaut
     */
    public String getForFacturerDefaut() {
        return forFacturerDefaut;
    }

    /**
     * @param forFacturerDefaut the forFacturerDefaut to set
     */
    public void setForFacturerDefaut(String forFacturerDefaut) {
        this.forFacturerDefaut = forFacturerDefaut;
    }

    @Override
    public Class<CotisationAssociationProfessionnelleSimpleModel> whichModelClass() {
        return CotisationAssociationProfessionnelleSimpleModel.class;
    }

}
