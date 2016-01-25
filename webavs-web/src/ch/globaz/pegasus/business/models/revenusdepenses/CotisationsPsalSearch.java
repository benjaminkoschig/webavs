package ch.globaz.pegasus.business.models.revenusdepenses;

import java.util.List;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class CotisationsPsalSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFin = null;
    private String forIdCotisationsPsal = null;
    private String forIdEntity = null;
    private List<String> forInIdDroitMembreFamille = null;
    private String forMontantCotisationsAnnuelles = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdCotisationsPsal() {
        return forIdCotisationsPsal;
    }

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    public List<String> getForInIdDroitMembreFamille() {
        return forInIdDroitMembreFamille;
    }

    public String getForMontantCotisationsAnnuelles() {
        return forMontantCotisationsAnnuelles;
    }

    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    /**
     * @return the idDroitMembreFamille
     */
    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdCotisationsPsal(String forIdCotisationsPsal) {
        this.forIdCotisationsPsal = forIdCotisationsPsal;
    }

    /**
     * @param forIdEntity the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    public void setForInIdDroitMembreFamille(List<String> forInIdDroitMembreFamille) {
        this.forInIdDroitMembreFamille = forInIdDroitMembreFamille;
    }

    public void setForMontantCotisationsAnnuelles(String forMontantCotisationsAnnuelles) {
        this.forMontantCotisationsAnnuelles = forMontantCotisationsAnnuelles;
    }

    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    /**
     * @param idDroitMembreFamille the idDroitMembreFamille to set
     */
    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return CotisationsPsal.class;
    }

}
