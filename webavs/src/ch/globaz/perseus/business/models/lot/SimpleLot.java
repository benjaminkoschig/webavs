package ch.globaz.perseus.business.models.lot;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleLot extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateCreation = null;
    private String dateEnvoi = null;
    private String description = null;
    private String etatCs = null;
    private String idJournal = null;
    private String idLot = null;
    private String proprietaireLot = null;
    private String typeLot = null;

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDescription() {
        return description;
    }

    public String getEtatCs() {
        return etatCs;
    }

    @Override
    public String getId() {
        return idLot;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getProprietaireLot() {
        return proprietaireLot;
    }

    public String getTypeLot() {
        return typeLot;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEtatCs(String etatCs) {
        this.etatCs = etatCs;
    }

    @Override
    public void setId(String id) {
        idLot = id;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setProprietaireLot(String proprietaireLot) {
        this.proprietaireLot = proprietaireLot;
    }

    public void setTypeLot(String typeLot) {
        this.typeLot = typeLot;
    }

}
