package ch.globaz.pegasus.business.models.assurancemaladie;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class SubsideAssuranceMaladieSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSubsideAssuranceMaladie = null;
    private String idDroitMembreFamille = null;
    private String forIdEntity = null;
    private String forNumeroVersion = null;

    public String getForIdSubsideAssuranceMaladie() {
        return forIdSubsideAssuranceMaladie;
    }


    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForIdSubsideAssuranceMaladie(String forIdSubsideAssuranceMaladie) {
        this.forIdSubsideAssuranceMaladie = forIdSubsideAssuranceMaladie;
    }

    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
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
        return SubsideAssuranceMaladie.class;
    }

    public String getForIdEntity() {
        return forIdEntity;
    }

    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }
}
