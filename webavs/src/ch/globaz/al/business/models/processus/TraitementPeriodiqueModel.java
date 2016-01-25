package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle représentant un traitement périodique
 * 
 * @author GMO
 * 
 */
public class TraitementPeriodiqueModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * date d'exécution du traitement
     */
    private String dateExecution = null;
    /**
     * état du traitement
     */
    private String etat = null;
    /**
     * Heure d'exécution du traitement
     */
    private String heureExecution = null;
    /**
     * id du processus périodique lié
     */
    private String idProcessusPeriodique = null;
    /**
     * id du traitement périodique (clé primaire)
     */
    private String idTraitementPeriodique = null;
    /**
     * indique si le traitement effectue des modifications DB ou non
     */
    private Boolean readOnly = null;
    /**
     * libellé du traitement (CS)
     */
    private String traitementLibelle = null;

    /**
     * utilisateur exécutant le traitement
     */
    private String userExecution = null;

    /**
     * @return dateExecution
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @return etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @return heureExecution
     */
    public String getHeureExecution() {
        return heureExecution;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idTraitementPeriodique;
    }

    /**
     * @return idProcessusPeriodique
     */
    public String getIdProcessusPeriodique() {
        return idProcessusPeriodique;
    }

    /**
     * @return idTraitementPeriodique
     */
    public String getIdTraitementPeriodique() {
        return idTraitementPeriodique;
    }

    /**
     * @return readOnly
     */
    public Boolean getReadOnly() {
        return readOnly;
    }

    /**
     * 
     * @return traitementLibelle (CS)
     */
    public String getTraitementLibelle() {
        return traitementLibelle;
    }

    /**
     * 
     * @return userExecution
     */
    public String getUserExecution() {
        return userExecution;
    }

    /**
     * @param dateExecution
     *            date d'exécution
     */
    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

    /**
     * @param etat
     *            l'état du traitement
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * @param heureExecution
     *            heure d'exécution
     */
    public void setHeureExecution(String heureExecution) {
        this.heureExecution = heureExecution;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idTraitementPeriodique = id;

    }

    /**
     * @param idProcessusPeriodique
     *            id du processus périodique
     */
    public void setIdProcessusPeriodique(String idProcessusPeriodique) {
        this.idProcessusPeriodique = idProcessusPeriodique;
    }

    /**
     * @param idTraitementPeriodique
     *            id du traitement périodique
     */
    public void setIdTraitementPeriodique(String idTraitementPeriodique) {
        this.idTraitementPeriodique = idTraitementPeriodique;
    }

    /**
     * 
     * @param readOnly
     *            modification en DB ou non
     */
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @param traitementLibelle
     *            libellé du traitement
     */
    public void setTraitementLibelle(String traitementLibelle) {
        this.traitementLibelle = traitementLibelle;
    }

    /**
     * @param userExecution
     *            utilisateur ayant exécuté le traitement la dernière fois
     */
    public void setUserExecution(String userExecution) {
        this.userExecution = userExecution;
    }

}
