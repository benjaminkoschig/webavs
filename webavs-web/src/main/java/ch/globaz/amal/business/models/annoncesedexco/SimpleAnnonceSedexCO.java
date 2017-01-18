/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceSedexCO extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;
    private String idAnnonceSedexCO = null;
    private String idContribuable = null;
    private String idFamille = null;
    private String dateAnnonce = null;
    private String messageType = null;
    private String messageSubType = null;
    private String messageEmetteur = null;
    private String messageRecepteur = null;
    private String periodeDebut = null;
    private String periodeFin = null;
    private String messageId = null;
    private String businessProcessId = null;
    private String annulation = null;
    private String rpRetro = null;
    private String pmtDebiteur = null;
    private String totalCreance = null;
    private String frais = null;
    private String interets = null;

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

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public String getIdAnnonceSedexCO() {
        return idAnnonceSedexCO;
    }

    public void setIdAnnonceSedexCO(String idAnnonceSedexCO) {
        this.idAnnonceSedexCO = idAnnonceSedexCO;
    }

    public String getIdContribuable() {
        return idContribuable;
    }

    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public String getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
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

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
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

    public String getAnnulation() {
        return annulation;
    }

    public void setAnnulation(String annulation) {
        this.annulation = annulation;
    }

    public String getRpRetro() {
        return rpRetro;
    }

    public void setRpRetro(String rpRetro) {
        this.rpRetro = rpRetro;
    }

    public String getPmtDebiteur() {
        return pmtDebiteur;
    }

    public void setPmtDebiteur(String pmtDebiteur) {
        this.pmtDebiteur = pmtDebiteur;
    }

    public String getTotalCreance() {
        return totalCreance;
    }

    public void setTotalCreance(String totalCreance) {
        this.totalCreance = totalCreance;
    }

    public String getFrais() {
        return frais;
    }

    public void setFrais(String frais) {
        this.frais = frais;
    }

    public String getInterets() {
        return interets;
    }

    public void setInterets(String interets) {
        this.interets = interets;
    }

}
