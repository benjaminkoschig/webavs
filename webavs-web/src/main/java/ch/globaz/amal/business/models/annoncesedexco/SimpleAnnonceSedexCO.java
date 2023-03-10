/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedexCO;

public class SimpleAnnonceSedexCO extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;
    private String idAnnonceSedexCO = null;
    private String idTiersCM = null;
    private String messageType = null;
    private String messageSubType = null;
    private String messageEmetteur = null;
    private String messageRecepteur = null;
    private String status = null;
    private String messageId = null;
    private String businessProcessId = null;
    private String dateAnnonce = null;
    private String statementStartDate = null;
    private String statementEndDate = null;
    private String statementDate = null;
    private String statementYear = null;
    private String totalAnnulation = null;
    private String totalArrivalPV = null;
    private String totalArrivalDebtor = null;
    private String totalClaim = null;

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    public String getStatementStartDate() {
        return statementStartDate;
    }

    public void setStatementStartDate(String statementStartDate) {
        this.statementStartDate = statementStartDate;
    }

    public String getStatementEndDate() {
        return statementEndDate;
    }

    public void setStatementEndDate(String statementEndDate) {
        this.statementEndDate = statementEndDate;
    }

    public String getStatementYear() {
        return statementYear;
    }

    public void setStatementYear(String statementYear) {
        this.statementYear = statementYear;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdAnnonceSedexCO();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnonceSedexCO = id;
    }

    public String getIdAnnonceSedexCO() {
        return idAnnonceSedexCO;
    }

    public void setIdAnnonceSedexCO(String idAnnonceSedexCO) {
        this.idAnnonceSedexCO = idAnnonceSedexCO;
    }

    public String getIdTiersCM() {
        return idTiersCM;
    }

    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    public String getMessageEmetteur() {
        return messageEmetteur;
    }

    public void setMessageEmetteur(String messageEmetteur) {
        this.messageEmetteur = messageEmetteur;
    }

    public String getMessageRecepteur() {
        return messageRecepteur;
    }

    public void setMessageRecepteur(String messageRecepteur) {
        this.messageRecepteur = messageRecepteur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageSubType() {
        return messageSubType;
    }

    public void setMessageSubType(String messageSubType) {
        this.messageSubType = messageSubType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBusinessProcessId() {
        return businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public String getTotalAnnulation() {
        return totalAnnulation;
    }

    public void setTotalAnnulation(String totalAnnulation) {
        this.totalAnnulation = totalAnnulation;
    }

    public String getTotalArrivalPV() {
        return totalArrivalPV;
    }

    public void setTotalArrivalPV(String totalArrivalPV) {
        this.totalArrivalPV = totalArrivalPV;
    }

    public String getTotalArrivalDebtor() {
        return totalArrivalDebtor;
    }

    public void setTotalArrivalDebtor(String totalArrivalDebtor) {
        this.totalArrivalDebtor = totalArrivalDebtor;
    }

    public String getTotalClaim() {
        return totalClaim;
    }

    public void setTotalClaim(String totalClaim) {
        this.totalClaim = totalClaim;
    }

    /**
     * @return libelle based on Enum : ch.globaz.amal.business.constantes.AMMessageSubTypesAnnonceSedexCO
     */
    public String getMessageSubTypeLibelle() {
        return AMMessagesSubTypesAnnonceSedexCO.getSubTypeCSLibelle(messageSubType);
    }
}