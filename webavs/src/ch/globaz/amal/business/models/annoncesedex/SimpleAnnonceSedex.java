/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedex;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleAnnonceSedex extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean ajax_isConfirmed = false;
    private String ajax_libelleAnnonce = "";
    private String ajax_libelleStatus = "";
    private String ajax_nomCM = "";
    private String dateMessage = null;
    private String idAnnonceSedex = null;
    private String idContribuable = null;
    private String idDetailFamille = null;
    private String idTiersCM = null;
    private String messageContent = null;
    private String messageEmetteur = null;
    private String messageHeader = null;
    private String messageId = null;
    private String messageRecepteur = null;
    private String messageSubType = null;
    private String messageType = null;
    private String numeroCourant = null;
    private String numeroDecision = null;
    private String referenceIdMessage = null;
    private String status = null;
    private String traitement = null;
    private String versionXSD = null;

    /**
     * Default constructor
     */
    public SimpleAnnonceSedex() {
    }

    public Boolean getAjax_isConfirmed() {
        return ajax_isConfirmed;
    }

    public String getAjax_libelleAnnonce() {
        return ajax_libelleAnnonce;
    }

    public String getAjax_libelleStatus() {
        return ajax_libelleStatus;
    }

    public String getAjax_nomCM() {
        return ajax_nomCM;
    }

    /**
     * @return the dateMessage
     */
    public String getDateMessage() {
        return dateMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdAnnonceSedex();
    }

    /**
     * @return the idAnnonceSedex
     */
    public String getIdAnnonceSedex() {
        return idAnnonceSedex;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idDetailFamille
     */
    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    /**
     * @return the idTiersCM
     */
    public String getIdTiersCM() {
        return idTiersCM;
    }

    /**
     * @return the messageContent
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @return the messageEmetteur
     */
    public String getMessageEmetteur() {
        return messageEmetteur;
    }

    /**
     * @return the messageHeader
     */
    public String getMessageHeader() {
        return messageHeader;
    }

    public String getMessageId() {
        return messageId;
    }

    /**
     * @return the messageRecepteur
     */
    public String getMessageRecepteur() {
        return messageRecepteur;
    }

    /**
     * @return the messageSubType
     */
    public String getMessageSubType() {
        return messageSubType;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @return the numeroCourant
     */
    public String getNumeroCourant() {
        return numeroCourant;
    }

    /**
     * @return the numeroDecision
     */
    public String getNumeroDecision() {
        return numeroDecision;
    }

    public String getReferenceIdMessage() {
        return referenceIdMessage;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the traitement
     */
    public String getTraitement() {
        return traitement;
    }

    /**
     * @return the versionXSD
     */
    public String getVersionXSD() {
        return versionXSD;
    }

    public void setAjax_isConfirmed(Boolean ajax_isConfirmed) {
        this.ajax_isConfirmed = ajax_isConfirmed;
    }

    public void setAjax_libelleAnnonce(String ajax_libelleAnnonce) {
        this.ajax_libelleAnnonce = ajax_libelleAnnonce;
    }

    public void setAjax_libelleStatus(String ajax_libelleStatus) {
        this.ajax_libelleStatus = ajax_libelleStatus;
    }

    public void setAjax_nomCM(String ajax_nomCM) {
        this.ajax_nomCM = ajax_nomCM;
    }

    /**
     * @param dateMessage
     *            the dateMessage to set
     */
    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnonceSedex = id;
    }

    /**
     * @param idAnnonceSedex
     *            the idAnnonceSedex to set
     */
    public void setIdAnnonceSedex(String idAnnonceSedex) {
        this.idAnnonceSedex = idAnnonceSedex;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idDetailFamille
     *            the idDetailFamille to set
     */
    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

    /**
     * @param idTiersCM
     *            the idTiersCM to set
     */
    public void setIdTiersCM(String idTiersCM) {
        this.idTiersCM = idTiersCM;
    }

    /**
     * @param messageContent
     *            the messageContent to set
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * @param messageEmetteur
     *            the messageEmetteur to set
     */
    public void setMessageEmetteur(String messageEmetteur) {
        this.messageEmetteur = messageEmetteur;
    }

    /**
     * @param messageHeader
     *            the messageHeader to set
     */
    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @param messageRecepteur
     *            the messageRecepteur to set
     */
    public void setMessageRecepteur(String messageRecepteur) {
        this.messageRecepteur = messageRecepteur;
    }

    /**
     * @param messageSubType
     *            the messageSubType to set
     */
    public void setMessageSubType(String messageSubType) {
        this.messageSubType = messageSubType;
    }

    /**
     * @param messageType
     *            the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @param numeroCourant
     *            the numeroCourant to set
     */
    public void setNumeroCourant(String numeroCourant) {
        this.numeroCourant = numeroCourant;
    }

    /**
     * @param numeroDecision
     *            the numeroDecision to set
     */
    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setReferenceIdMessage(String referenceIdMessage) {
        this.referenceIdMessage = referenceIdMessage;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @param traitement
     *            the traitement to set
     */
    public void setTraitement(String traitement) {
        this.traitement = traitement;
    }

    /**
     * @param versionXSD
     *            the versionXSD to set
     */
    public void setVersionXSD(String versionXSD) {
        this.versionXSD = versionXSD;
    }

}
