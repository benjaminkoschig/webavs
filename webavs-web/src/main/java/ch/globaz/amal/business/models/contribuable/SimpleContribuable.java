package ch.globaz.amal.business.models.contribuable;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleContribuable extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idContribuable = null;
    private String idContribuableFusion = null;
    private String idTier = null;
    private Boolean isContribuableActif = null;
    private String noContribuable = null;
    private String numeroInternePersonnel = null;
    private String typeNoPersonne = null;
    private String zoneCommuneContribuable = null;
    private String zoneCommuneDateNaissance = null;
    private String zoneCommuneNoContrFormate = null;

    @Override
    public String getId() {
        return idContribuable;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    public String getIdContribuableFusion() {
        return idContribuableFusion;
    }

    public String getIdTier() {
        return idTier;
    }

    public Boolean getIsContribuableActif() {
        return isContribuableActif;
    }

    /**
     * @return the noContribuable
     */
    public String getNoContribuable() {
        return noContribuable;
    }

    public String getNumeroInternePersonnel() {
        return numeroInternePersonnel;
    }

    public String getTypeNoPersonne() {
        return typeNoPersonne;
    }

    /**
     * @return the zoneCommuneContribuable
     */
    public String getZoneCommuneContribuable() {
        return zoneCommuneContribuable;
    }

    /**
     * @return the zoneCommuneDateNaissance
     */
    public String getZoneCommuneDateNaissance() {
        return zoneCommuneDateNaissance;
    }

    /**
     * @return the zoneCommuneNoContrFormate
     */
    public String getZoneCommuneNoContrFormate() {
        return zoneCommuneNoContrFormate;
    }

    @Override
    public void setId(String id) {
        idContribuable = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public void setIdContribuableFusion(String idContribuableFusion) {
        this.idContribuableFusion = idContribuableFusion;
    }

    public void setIdTier(String idTier) {
        this.idTier = idTier;
    }

    public void setIsContribuableActif(Boolean isContribuableActif) {
        this.isContribuableActif = isContribuableActif;
    }

    /**
     * @param noContribuable
     *            the noContribuable to set
     */
    public void setNoContribuable(String noContribuable) {
        this.noContribuable = noContribuable;
    }

    public void setNumeroInternePersonnel(String numeroInternePersonnel) {
        this.numeroInternePersonnel = numeroInternePersonnel;
    }

    public void setTypeNoPersonne(String typeNoPersonne) {
        this.typeNoPersonne = typeNoPersonne;
    }

    /**
     * @param zoneCommuneContribuable
     *            the zoneCommuneContribuable to set
     */
    public void setZoneCommuneContribuable(String zoneCommuneContribuable) {
        this.zoneCommuneContribuable = zoneCommuneContribuable;
    }

    /**
     * @param zoneCommuneDateNaissance
     *            the zoneCommuneDateNaissance to set
     */
    public void setZoneCommuneDateNaissance(String zoneCommuneDateNaissance) {
        this.zoneCommuneDateNaissance = zoneCommuneDateNaissance;
    }

    /**
     * @param zoneCommuneNoContrFormate
     *            the zoneCommuneNoContrFormate to set
     */
    public void setZoneCommuneNoContrFormate(String zoneCommuneNoContrFormate) {
        this.zoneCommuneNoContrFormate = zoneCommuneNoContrFormate;
    }

}
