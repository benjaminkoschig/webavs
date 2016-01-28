package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class AutreApiSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAutreApi = null;
    private String forIdEntity = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    /**
     * @return the forIdAutrerENTE
     */
    public String getForIdAutreApi() {
        return forIdAutreApi;
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
     * @param forIdAutrerENTE
     *            the forIdAutrerENTE to set
     */
    public void setForIdAutreApi(String forIdAutreApi) {
        this.forIdAutreApi = forIdAutreApi;
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

    @Override
    public Class whichModelClass() {
        return AutreApi.class;
    }
}
