package ch.globaz.vulpecula.business.models.association;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;

public class CotisationAssociationProfessionnelleSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = 1L;

    private String forId;
    private String forLibelleLike;
    private String forLibelleUpperLike;
    private String forAssociationUpper1Like;
    private String forAssociationUpper2Like;
    private String forGenre;
    private String forGenreNot;
    private String forIdAssociation;
    private String forCodeAdministration;
    private String forCodeLike;
    private String forFacturerDefaut;

    @Override
    public Class<CotisationAssociationProfessionnelleComplexModel> whichModelClass() {
        return CotisationAssociationProfessionnelleComplexModel.class;
    }

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
        if (!JadeStringUtil.isEmpty(forLibelleLike)) {
            this.forLibelleLike = "%" + forLibelleLike;
        }
    }

    public void setForAssociationLike(String forAssociationLike) {
        setForAssociationUpper1Like(forAssociationLike);
        setForAssociationUpper2Like(forAssociationLike);
    }

    public String getForGenreNot() {
        return forGenreNot;
    }

    public void setForGenreNot(String forGenreNot) {
        this.forGenreNot = forGenreNot;
    }

    public String getForGenre() {
        return forGenre;
    }

    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    public String getForIdAssociation() {
        return forIdAssociation;
    }

    public void setForIdAssociation(String idAssociation) {
        forIdAssociation = idAssociation;
    }

    public void setForGenre(GenreCotisationAssociationProfessionnelle genre) {
        forGenre = genre.getValue();
    }

    public final void setForAssociationUpperLike(String forAssociationUpperLike) {
        setForAssociationUpper1Like(forAssociationUpperLike);
        setForAssociationUpper2Like(forAssociationUpperLike);
    }

    public String getForLibelleUpperLike() {
        return forLibelleUpperLike;
    }

    public void setForLibelleUpperLike(String forLibelleUpperLike) {
        if (!JadeStringUtil.isEmpty(forLibelleUpperLike)) {
            this.forLibelleUpperLike = "%" + JadeStringUtil.toUpperCase(forLibelleUpperLike);
        }
    }

    public final String getForAssociationUpper1Like() {
        return forAssociationUpper1Like;
    }

    public final void setForAssociationUpper1Like(String forAssociationUpper1Like) {
        if (!JadeStringUtil.isEmpty(forAssociationUpper1Like)) {
            this.forAssociationUpper1Like = "%" + JadeStringUtil.toUpperCase(forAssociationUpper1Like);
        }
    }

    public final String getForAssociationUpper2Like() {
        return forAssociationUpper2Like;
    }

    public final void setForAssociationUpper2Like(String forAssociationUpper2Like) {
        if (!JadeStringUtil.isEmpty(forAssociationUpper2Like)) {
            this.forAssociationUpper2Like = "%" + JadeStringUtil.toUpperCase(forAssociationUpper2Like);
        }
    }

    public final String getForCodeAdministration() {
        return forCodeAdministration;
    }

    public final void setForCodeAdministration(String forCodeAdministration) {
        this.forCodeAdministration = forCodeAdministration;
    }

    public final String getForCodeLike() {
        return forCodeLike;
    }

    public final void setForCodeLike(String forCodeLike) {
        if (!JadeStringUtil.isEmpty(forCodeLike)) {
            this.forCodeLike = JadeStringUtil.toUpperCase(forCodeLike);
        }
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
}
