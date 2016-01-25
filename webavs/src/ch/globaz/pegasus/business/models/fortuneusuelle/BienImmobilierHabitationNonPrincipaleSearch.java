package ch.globaz.pegasus.business.models.fortuneusuelle;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class BienImmobilierHabitationNonPrincipaleSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdBienImmobilierHabitationNonPrincipale = null;
    private String forIdEntity = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    public String getForIdBienImmobilierHabitationNonPrincipale() {
        return forIdBienImmobilierHabitationNonPrincipale;
    }

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
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

    public void setForIdBienImmobilierHabitationNonPrincipale(String forIdBienImmobilierHabitationNonPrincipale) {
        this.forIdBienImmobilierHabitationNonPrincipale = forIdBienImmobilierHabitationNonPrincipale;
    }

    /**
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

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
        return BienImmobilierHabitationNonPrincipale.class;
    }

}
