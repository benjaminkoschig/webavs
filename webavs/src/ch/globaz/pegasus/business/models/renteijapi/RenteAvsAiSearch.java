package ch.globaz.pegasus.business.models.renteijapi;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

/**
 * Modèle de recherche pour le modèle SimpleRenteAvsAi
 * 
 * @author SCE
 * 
 */
public class RenteAvsAiSearch extends AbstractDonneeFinanciereSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFin = null;
    private String forIdEntity = null;
    private String forIdRenteAvsAi = null;
    private String forNumeroVersion = null;
    private String idDroitMembreFamille = null;

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forIdEntity
     */
    public String getForIdEntity() {
        return forIdEntity;
    }

    /**
     * @return the forIdrenteAvsAi
     */
    public String getForIdRenteAvsAi() {
        return forIdRenteAvsAi;
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

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forIdEntity the forIdEntity to set
     */
    public void setForIdEntity(String forIdEntity) {
        this.forIdEntity = forIdEntity;
    }

    /**
     * @param forIdrenteAvsAi the forIdrenteAvsAi to set
     */
    public void setForIdRenteAvsAi(String forIdrenteAvsAi) {
        forIdRenteAvsAi = forIdrenteAvsAi;
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
     * Retourne la classe métier lié au modèle de recherche
     */
    @Override
    public Class<RenteAvsAi> whichModelClass() {
        return RenteAvsAi.class;
    }

}
