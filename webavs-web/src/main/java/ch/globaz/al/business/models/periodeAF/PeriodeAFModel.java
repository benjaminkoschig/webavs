package ch.globaz.al.business.models.periodeAF;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * Modèle d'une période AF
 * 
 * @author GMO
 * 
 */
public class PeriodeAFModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * La date de traitement de la période AF (Exemple 11.2009)
     */
    private String datePeriode = null;
    /**
     * Représente l'état de la période, ouverte ou fermée
     */
    private String etat = null;
    /**
     * Identifiant de la période AF
     */
    private String idPeriodeAF = null;

    /**
     * @return periode - la date de traitement de la période AF
     */
    public String getDatePeriode() {
        return datePeriode;
    }

    /**
     * @return etat - l'état de la période AF
     */
    public String getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return idPeriodeAF;
    }

    /**
     * @return idPeriodeAF
     */
    public String getIdPeriodeAF() {
        return idPeriodeAF;
    }

    /**
     * Définit la date de traitement de la période
     * 
     * @param periode
     *            période de traitement
     */
    public void setDatePeriode(String periode) {
        datePeriode = periode;
    }

    /**
     * Définit l'état de la période AF
     * 
     * @param etat
     *            état de la période AF
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String id) {
        idPeriodeAF = id;

    }

    /**
     * Définit l'id de la période AF
     * 
     * @param idPeriodeAF
     *            id de la période AF
     */
    public void setIdPeriodeAF(String idPeriodeAF) {
        this.idPeriodeAF = idPeriodeAF;
    }

}
