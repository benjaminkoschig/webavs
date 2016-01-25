package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

/**
 * Modele de recherche pour IndemniteJournaliereAi
 * 
 * @author SCE
 * 
 */
public class IndemniteJournaliereAiSearch extends AbstractDonneeFinanciereSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEntity = null;
    private String forIdIndemniteJournaliereAi = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdIndemniteJournaliereAi
     */
    public String getForIdIndemniteJournaliereAi() {
        return forIdIndemniteJournaliereAi;
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
     * @param forIdEntity
     *            the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdIndemniteJournaliereAi
     *            the forIdIndemniteJournaliereAi to set
     */
    public void setForIdIndemniteJournaliereAi(String forIdIndemniteJournaliereAi) {
        this.forIdIndemniteJournaliereAi = forIdIndemniteJournaliereAi;
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

    /**
     * Retourne le modèle métier associé au modèle de recherche
     */
    @Override
    public Class whichModelClass() {

        return IndemniteJournaliereAi.class;
    }

}
