package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimplePrestation extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateFin = null;
    private String datePrestation = null;
    private String etatPrestation = null;
    private String idDecisionPcf = null;
    private String idFacture = null;
    private String idLot = null;
    private String idPrestation = null;
    private String montantMesureCoaching = null;
    private String montantTotal = null;

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

    public String getDatePrestation() {
        return datePrestation;
    }

    public String getEtatPrestation() {
        return etatPrestation;
    }

    @Override
    public String getId() {
        return idPrestation;
    }

    public String getIdDecisionPcf() {
        return idDecisionPcf;
    }

    /**
     * @return the idFacture
     */
    public String getIdFacture() {
        return idFacture;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * @return the montantMesureCoaching
     */
    public String getMontantMesureCoaching() {
        return montantMesureCoaching;
    }

    public String getMontantTotal() {
        return montantTotal;
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

    public void setDatePrestation(String datePrestation) {
        this.datePrestation = datePrestation;
    }

    public void setEtatPrestation(String etatPrestation) {
        this.etatPrestation = etatPrestation;
    }

    @Override
    public void setId(String id) {
        idPrestation = id;

    }

    public void setIdDecisionPcf(String idDecisionPcf) {
        this.idDecisionPcf = idDecisionPcf;
    }

    /**
     * @param idFacture
     *            the idFacture to set
     */
    public void setIdFacture(String idFacture) {
        this.idFacture = idFacture;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    /**
     * @param montantMesureCoaching
     *            the montantMesureCoaching to set
     */
    public void setMontantMesureCoaching(String montantMesureCoaching) {
        this.montantMesureCoaching = montantMesureCoaching;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

}
