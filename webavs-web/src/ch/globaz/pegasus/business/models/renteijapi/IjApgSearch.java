package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class IjApgSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEntity = null;
    private String forIdIjApg = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdAutrerENTE
     */
    public String getForIdIjApg() {
        return forIdIjApg;
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
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdAutrerENTE
     *            the forIdAutrerENTE to set
     */
    public void setForIdIjApg(String forIdIjApg) {
        this.forIdIjApg = forIdIjApg;
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
        return IjApg.class;
    }
}
