package ch.globaz.perseus.business.models.parametres;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleLoyer extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeLoyer = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idLoyer = null;
    private String idZone = null;
    private String montant = null;

    public SimpleLoyer() {
        super();
    }

    public String getCsTypeLoyer() {
        return csTypeLoyer;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idLoyer;
    }

    public String getIdLoyer() {
        return idLoyer;
    }

    public String getIdZone() {
        return idZone;
    }

    public String getMontant() {
        return montant;
    }

    public void setCsTypeLoyer(String csTypeLoyer) {
        this.csTypeLoyer = csTypeLoyer;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idLoyer = id;
    }

    public void setIdLoyer(String idLoyer) {
        this.idLoyer = idLoyer;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
