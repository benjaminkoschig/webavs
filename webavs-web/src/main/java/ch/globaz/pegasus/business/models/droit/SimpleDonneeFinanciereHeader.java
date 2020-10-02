package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDonneeFinanciereHeader extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeDonneeFinanciere = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idDonneeFinanciereHeader = null;
    private String idDroitMembreFamille = null;
    private String idEntity = null;
    private String idEntityGroup = null;
    private String idVersionDroit = null;
    private Boolean isCopieFromPreviousVersion = Boolean.FALSE;
    private Boolean isDessaisissementFortune = Boolean.FALSE;
    private String typeDessaisissementFortune = null;

    private Boolean isDessaisissementRevenu = Boolean.FALSE;
    private Boolean isPeriodeClose = Boolean.FALSE;

    public Boolean getIsPeriodeClose() {
        return isPeriodeClose;
    }

    public void setIsPeriodeClose(Boolean isPeriodeClose) {
        this.isPeriodeClose = isPeriodeClose;
    }

    private Boolean isSupprime = null;

    /**
     * @return the csTypeDonneeFinanciere
     */
    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idDroitMembreFamille
     */
    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    /**
     * @return the idEntity
     */
    public String getIdEntity() {
        return idEntity;
    }

    /**
     * @return the idEntityGroup
     */
    public String getIdEntityGroup() {
        return idEntityGroup;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsCopieFromPreviousVersion() {
        return isCopieFromPreviousVersion;
    }

    /**
     * @return the isDessaisissementFortune
     */
    public Boolean getIsDessaisissementFortune() {
        return isDessaisissementFortune;
    }

    /**
     * @return the isDessaisissementRevenu
     */
    public Boolean getIsDessaisissementRevenu() {
        return isDessaisissementRevenu;
    }

    /**
     * @return the isSupprime
     */
    public Boolean getIsSupprime() {
        return isSupprime;
    }

    /**
     * @param csTypeDonneeFinanciere
     *            the csTypeDonneeFinanciere to set
     */
    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDonneeFinanciereHeader = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idDroitMembreFamille
     *            the idDroitMembreFamille to set
     */
    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    /**
     * @param idEntity
     *            the idEntity to set
     */
    public void setIdEntity(String idEntity) {
        this.idEntity = idEntity;
    }

    /**
     * @param idEntityGroup
     *            the idEntityGroup to set
     */
    public void setIdEntityGroup(String idEntityGroup) {
        this.idEntityGroup = idEntityGroup;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsCopieFromPreviousVersion(Boolean isCopieFromPreviousVersion) {
        this.isCopieFromPreviousVersion = isCopieFromPreviousVersion;
    }

    /**
     * @param isDessaisissementFortune
     *            the isDessaisissementFortune to set
     */
    public void setIsDessaisissementFortune(Boolean isDessaisissementFortune) {
        this.isDessaisissementFortune = isDessaisissementFortune;
    }

    /**
     * Type de dessaisissement de fortune
     *
     * @return typeDessaisissementFortune
     */
    public String getTypeDessaisissementFortune() {
        return typeDessaisissementFortune;
    }

    public void setTypeDessaisissementFortune(String typeDessaisissementFortune) {
        this.typeDessaisissementFortune = typeDessaisissementFortune;
    }

    /**
     * @param isDessaisissementRevenu
     *            the isDessaisissementRevenu to set
     */
    public void setIsDessaisissementRevenu(Boolean isDessaisissementRevenu) {
        this.isDessaisissementRevenu = isDessaisissementRevenu;
    }

    /**
     * @param isSupprime
     *            the isSupprime to set
     */
    public void setIsSupprime(Boolean isSupprime) {
        this.isSupprime = isSupprime;
    }

}
