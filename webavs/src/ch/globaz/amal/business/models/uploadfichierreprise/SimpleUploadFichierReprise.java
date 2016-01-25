package ch.globaz.amal.business.models.uploadfichierreprise;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleUploadFichierReprise extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String customValue = null;
    private String idJob = null;
    private String idUploadFichierReprise = null;
    private Boolean isValid = false;
    private String msgNoValid = null;
    private String noContribuable = null;
    private String nomPrenom = null;
    private String typeReprise = null;
    private String xmlLignes = null;

    public String getCustomValue() {
        return customValue;
    }

    @Override
    public String getId() {
        return idUploadFichierReprise;
    }

    public String getIdJob() {
        return idJob;
    }

    public String getIdUploadFichierReprise() {
        return idUploadFichierReprise;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public String getMsgNoValid() {
        return msgNoValid;
    }

    public String getNoContribuable() {
        return noContribuable;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getTypeReprise() {
        return typeReprise;
    }

    public String getXmlLignes() {
        return xmlLignes;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    @Override
    public void setId(String id) {
        idUploadFichierReprise = id;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public void setIdUploadFichierReprise(String idUploadFichierReprise) {
        this.idUploadFichierReprise = idUploadFichierReprise;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public void setMsgNoValid(String msgNoValid) {
        this.msgNoValid = msgNoValid;
    }

    public void setNoContribuable(String noContribuable) {
        this.noContribuable = noContribuable;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setTypeReprise(String typeReprise) {
        this.typeReprise = typeReprise;
    }

    public void setXmlLignes(String xmlLignes) {
        this.xmlLignes = xmlLignes;
    }

}
