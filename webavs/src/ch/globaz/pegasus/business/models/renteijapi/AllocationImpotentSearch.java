package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

/**
 * Modele de recherche pour Allocation Impotent 6.2010
 * 
 * @author SCE
 * 
 */
public class AllocationImpotentSearch extends AbstractDonneeFinanciereSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAllocationImpotent = null;
    private String forIdEntity = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    /**
     * @return the forIdAllocationImpotent
     */
    public String getForIdAllocationImpotent() {
        return forIdAllocationImpotent;
    }

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forNumeroVersion
     */
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

    /**
     * @param forIdAllocationImpotent the forIdAllocationImpotent to set
     */
    public void setForIdAllocationImpotent(String forIdAllocationImpotent) {
        this.forIdAllocationImpotent = forIdAllocationImpotent;
    }

    /**
     * @param forIdEntity the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forNumeroVersion the forNumeroVersion to set
     */
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

    /**
     * Retourne la classe associé au modele de recherche
     */
    @Override
    public Class whichModelClass() {
        return AllocationImpotent.class;

    }

}
