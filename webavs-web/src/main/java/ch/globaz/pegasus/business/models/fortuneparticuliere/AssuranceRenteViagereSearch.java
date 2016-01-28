/**
 * 
 */
package ch.globaz.pegasus.business.models.fortuneparticuliere;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

/**
 * @author BSC
 * 
 */
public class AssuranceRenteViagereSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAssuranceRenteViagere = null;
    private String forIdEntity = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    /**
     * @return the forIdAssuranceRenteViagere
     */
    public String getForIdAssuranceRenteViagere() {
        return forIdAssuranceRenteViagere;
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
     * @param forIdAssuranceRenteViagere
     *            the forIdAssuranceRenteViagere to set
     */
    public void setForIdAssuranceRenteViagere(String forIdAssuranceRenteViagere) {
        this.forIdAssuranceRenteViagere = forIdAssuranceRenteViagere;
    }

    /**
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forNumeroVersion
     *            the forNumeroVersion to set
     */
    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    /**
     * @param idDroitMembreFamille
     *            the idDroitMembreFamille to set
     */
    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    /**
     * @param orderBy
     *            the orderBy to set
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
        return AssuranceRenteViagere.class;
    }

}
