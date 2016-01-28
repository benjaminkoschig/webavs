package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le simple repr�sentant un passage de facturation
 * 
 * Sert uniquement � afficher les donn�es via le mod�le complexe <code>PassageModuleComplexModel</code>
 * 
 */
public class PassageModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * la date de facturation
     */
    private String dateFacturation = null;
    /**
     * la p�riode de facturation
     */
    private String datePeriode = null;
    /**
     * l'id du passage
     */
    private String idPassage = null;
    /**
     * le libell� du passage de facturation
     * 
     */
    private String libellePassage = null;
    /**
     * Le statut / �tat du passage
     */
    private String status = null;
    /**
     * le type de facturation (interne,externe,p�riodique)
     */
    private String typeFacturation = null;
    
    private String idPlanFacturation = null;

    private String idJournal = null;

    public String getIdPlanFacturation() {
        return idPlanFacturation;
    }

    public void setIdPlanFacturation(String idPlanFacturation) {
        this.idPlanFacturation = idPlanFacturation;
    }

    /**
     * 
     * @return dateFacturation
     */
    public String getDateFacturation() {
        return dateFacturation;
    }

    /**
     * 
     * @return datePeriode
     */
    public String getDatePeriode() {
        return datePeriode;
    }

    @Override
    public String getId() {
        return idPassage;
    }

    /**
     * 
     * @return idPassage
     */
    public String getIdPassage() {
        return idPassage;
    }

    /**
     * 
     * @return libellePassage
     */
    public String getLibellePassage() {
        return libellePassage;
    }

    /**
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @return typeFacturation
     */
    public String getTypeFacturation() {
        return typeFacturation;
    }

    /**
     * 
     * @param dateFacturation
     *            la date du facturation
     */
    public void setDateFacturation(String dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    /**
     * 
     * @param datePeriode
     *            la p�riode de facturation
     */
    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    @Override
    public void setId(String id) {
        idPassage = id;

    }

    /**
     * 
     * @param idPassage
     *            l'identifiant du passage
     */
    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    /**
     * 
     * @param libellePassage
     *            la description du passage
     */
    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    /**
     * 
     * @param status
     *            le statut / �tat (ouvert,traitement,comptabilis�)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @param typeFacturation
     *            le type de facturation(interne,externe,p�riodique)
     */
    public void setTypeFacturation(String typeFacturation) {
        this.typeFacturation = typeFacturation;
    }
    
    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }
}
