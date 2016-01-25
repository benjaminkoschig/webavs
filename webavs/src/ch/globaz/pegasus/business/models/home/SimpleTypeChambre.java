package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleTypeChambre extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCategorie = null;
    private String designation = null;
    private String idHome = null;
    private String idTiersParticularite = null;
    private String idTypeChambre = null;
    private Boolean isApiFacturee = false;
    private Boolean isParticularite = false;
    private String csCategorieArgentPoche = null;

    public String getCsCategorieArgentPoche() {
        return csCategorieArgentPoche;
    }

    public void setCsCategorieArgentPoche(String csCategorieArgentPoche) {
        this.csCategorieArgentPoche = csCategorieArgentPoche;
    }

    /**
     * @return the csCategorie
     */
    public String getCsCategorie() {
        return csCategorie;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    @Override
    public String getId() {
        return idTypeChambre;
    }

    /**
     * @return the idHome
     */
    public String getIdHome() {
        return idHome;
    }

    /**
     * @return the idTiersParticularite
     */
    public String getIdTiersParticularite() {
        return idTiersParticularite;
    }

    /**
     * @return the idTypeChambre
     */
    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    /**
     * @return the isApiFacturee
     */
    public Boolean getIsApiFacturee() {
        return isApiFacturee;
    }

    /**
     * @return the isParticularite
     */
    public Boolean getIsParticularite() {
        return isParticularite;
    }

    /**
     * @param csCategorie
     *            the csCategorie to set
     */
    public void setCsCategorie(String csCategorie) {
        this.csCategorie = csCategorie;
    }

    /**
     * @param designation
     *            the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public void setId(String id) {
        idTypeChambre = id;
    }

    /**
     * @param idHome
     *            the idHome to set
     */
    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    /**
     * @param idTiersParticularite
     *            the idTiersParticularite to set
     */
    public void setIdTiersParticularite(String idTiersParticularite) {
        if (!"null".equals(idTiersParticularite)) {
            this.idTiersParticularite = idTiersParticularite;
        } else {
            this.idTiersParticularite = null;
        }
    }

    /**
     * @param idTypeChambre
     *            the idTypeChambre to set
     */
    public void setIdTypeChambre(String idTypeChambre) {
        this.idTypeChambre = idTypeChambre;
    }

    /**
     * @param isApiFacturee
     *            the isApiFacturee to set
     */
    public void setIsApiFacturee(Boolean isApiFacturee) {
        this.isApiFacturee = isApiFacturee;
    }

    /**
     * @param isParticularite
     *            the isParticularite to set
     */
    public void setIsParticularite(Boolean isParticularite) {
        this.isParticularite = isParticularite;
    }

}
