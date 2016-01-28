package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le repr�sentant un processus p�riodique
 * 
 * @author GMO
 * 
 */
public class ProcessusPeriodiqueModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * �tat du processus
     */
    private String etat = null;

    /**
     * id de la configuration li� au processus p�riodique
     */
    private String idConfig = null;
    /**
     * indique le passage facturation li� au processus
     */
    private String idPassageFactu = null;
    /**
     * id de la p�riode li� au processus p�riodique
     */
    private String idPeriode = null;
    /**
     * id du processus
     */
    private String idProcessusPeriodique = null;

    /**
     * Indique si il s'agit du processus partiel ou non
     */
    private Boolean isPartiel = null;

    /**
     * @return etat
     */
    public String getEtat() {
        return etat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idProcessusPeriodique;
    }

    /**
     * @return idConfig
     */
    public String getIdConfig() {
        return idConfig;
    }

    /**
     * 
     * @return idPassageFactu
     */
    public String getIdPassageFactu() {
        return idPassageFactu;
    }

    /**
     * @return idPeriode
     */
    public String getIdPeriode() {
        return idPeriode;
    }

    /**
     * @return idProcessusPeriodique
     */
    public String getIdProcessusPeriodique() {
        return idProcessusPeriodique;
    }

    /**
     * @return isPartiel
     */
    public Boolean getIsPartiel() {
        return isPartiel;
    }

    /**
     * @param etat
     *            �tat du processus
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idProcessusPeriodique = id;

    }

    /**
     * @param idConfig
     *            identifiant de la config
     */
    public void setIdConfig(String idConfig) {
        this.idConfig = idConfig;
    }

    /**
     * 
     * @param idPassageFactu
     *            - id du passage de facturation li� au processus
     */
    public void setIdPassageFactu(String idPassageFactu) {
        this.idPassageFactu = idPassageFactu;
    }

    /**
     * @param idPeriode
     *            id de la p�riode
     */
    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    /**
     * @param idProcessusPeriodique
     *            id du processus
     */
    public void setIdProcessusPeriodique(String idProcessusPeriodique) {
        this.idProcessusPeriodique = idProcessusPeriodique;
    }

    /**
     * 
     * @param isPartiel
     *            - indique si le processus p�riodique est partiel ou principal
     */
    public void setIsPartiel(Boolean isPartiel) {
        this.isPartiel = isPartiel;
    }

}
