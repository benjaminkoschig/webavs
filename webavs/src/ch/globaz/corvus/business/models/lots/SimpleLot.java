package ch.globaz.corvus.business.models.lots;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleLot extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtat = null;
    private String csProprietaire = null;
    private String csTypeLot = null;
    private String dateCreation = null;
    private String dateEnvoi = null;
    private String description = null;
    private String idJournalCA = null;
    private String idLot = null;

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsProprietaire() {
        return csProprietaire;
    }

    public String getCsTypeLot() {
        return csTypeLot;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idLot;
    }

    public String getIdJournalCA() {
        return idJournalCA;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsProprietaire(String csProprietaire) {
        this.csProprietaire = csProprietaire;
    }

    public void setCsTypeLot(String csTypeLot) {
        this.csTypeLot = csTypeLot;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idLot = id;
    }

    public void setIdJournalCA(String idJournalCA) {
        this.idJournalCA = idJournalCA;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

}
