/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 *         Classe du modèle de données Envoi Item >> table ALTEMENV
 * 
 */
public class EnvoiItemSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String envoiError = null;

    private String envoiFileName = null;

    private String envoiNoGroupe = null;

    private String envoiStatus = null;

    private String envoiType = null;

    private String idEnvoi = null;

    private String idExternalLink = null;
    private String idFormule = null;

    private String idJob = null;

    /**
     * @return the envoiError
     */
    public String getEnvoiError() {
        return envoiError;
    }

    /**
     * @return the envoiFileName
     */
    public String getEnvoiFileName() {
        return envoiFileName;
    }

    /**
     * @return the envoiNoGroupe
     */
    public String getEnvoiNoGroupe() {
        return envoiNoGroupe;
    }

    /**
     * @return the envoiStatus
     */
    public String getEnvoiStatus() {
        return envoiStatus;
    }

    /**
     * @return the envoiType
     */
    public String getEnvoiType() {
        return envoiType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdEnvoi();
    }

    /**
     * @return the idEnvoi
     */
    public String getIdEnvoi() {
        return idEnvoi;
    }

    /**
     * @return the idExternalLink
     */
    public String getIdExternalLink() {
        return idExternalLink;
    }

    /**
     * @return the idFormule
     */
    public String getIdFormule() {
        return idFormule;
    }

    /**
     * @return the idJob
     */
    public String getIdJob() {
        return idJob;
    }

    /**
     * @param envoiError
     *            the envoiError to set
     */
    public void setEnvoiError(String envoiError) {
        this.envoiError = envoiError;
    }

    /**
     * @param envoiFileName
     *            the envoiFileName to set
     */
    public void setEnvoiFileName(String envoiFileName) {
        this.envoiFileName = envoiFileName;
    }

    /**
     * @param envoiNoGroupe
     *            the envoiNoGroupe to set
     */
    public void setEnvoiNoGroupe(String envoiNoGroupe) {
        this.envoiNoGroupe = envoiNoGroupe;
    }

    /**
     * @param envoiStatus
     *            the envoiStatus to set
     */
    public void setEnvoiStatus(String envoiStatus) {
        this.envoiStatus = envoiStatus;
    }

    /**
     * @param envoiType
     *            the envoiType to set
     */
    public void setEnvoiType(String envoiType) {
        this.envoiType = envoiType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdEnvoi(id);
    }

    /**
     * @param idEnvoi
     *            the idEnvoi to set
     */
    public void setIdEnvoi(String idEnvoi) {
        this.idEnvoi = idEnvoi;
    }

    /**
     * @param idExternalLink
     *            the idExternalLink to set
     */
    public void setIdExternalLink(String idExternalLink) {
        this.idExternalLink = idExternalLink;
    }

    /**
     * @param idFormule
     *            the idFormule to set
     */
    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    /**
     * @param idJob
     *            the idJob to set
     */
    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

}
