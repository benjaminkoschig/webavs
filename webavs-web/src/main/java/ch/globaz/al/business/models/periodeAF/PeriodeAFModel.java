package ch.globaz.al.business.models.periodeAF;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * Mod�le d'une p�riode AF
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
     * La date de traitement de la p�riode AF (Exemple 11.2009)
     */
    private String datePeriode = null;
    /**
     * Repr�sente l'�tat de la p�riode, ouverte ou ferm�e
     */
    private String etat = null;
    /**
     * Identifiant de la p�riode AF
     */
    private String idPeriodeAF = null;

    /**
     * @return periode - la date de traitement de la p�riode AF
     */
    public String getDatePeriode() {
        return datePeriode;
    }

    /**
     * @return etat - l'�tat de la p�riode AF
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
     * D�finit la date de traitement de la p�riode
     * 
     * @param periode
     *            p�riode de traitement
     */
    public void setDatePeriode(String periode) {
        datePeriode = periode;
    }

    /**
     * D�finit l'�tat de la p�riode AF
     * 
     * @param etat
     *            �tat de la p�riode AF
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String id) {
        idPeriodeAF = id;

    }

    /**
     * D�finit l'id de la p�riode AF
     * 
     * @param idPeriodeAF
     *            id de la p�riode AF
     */
    public void setIdPeriodeAF(String idPeriodeAF) {
        this.idPeriodeAF = idPeriodeAF;
    }

}
