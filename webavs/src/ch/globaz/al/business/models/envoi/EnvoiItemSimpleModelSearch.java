/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 *         Modèle de recherche pour le modèle simple Envoi Item
 * 
 */
public class EnvoiItemSimpleModelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEnvoiFileName = null;
    private String forEnvoiNoGroupe = null;
    private String forEnvoiStatus = null;
    private String forEnvoiType = null;
    private String forIdEnvoi = null;
    private String forIdExternalLink = null;
    private String forIdFormule = null;
    private String forIdJob = null;
    private String forNotStatusEnvoi = null;

    /**
     * @return the forEnvoiFileName
     */
    public String getForEnvoiFileName() {
        return forEnvoiFileName;
    }

    /**
     * @return the forEnvoiNoGroupe
     */
    public String getForEnvoiNoGroupe() {
        return forEnvoiNoGroupe;
    }

    /**
     * @return the forEnvoiStatus
     */
    public String getForEnvoiStatus() {
        return forEnvoiStatus;
    }

    /**
     * @return the forEnvoiType
     */
    public String getForEnvoiType() {
        return forEnvoiType;
    }

    /**
     * @return the forIdEnvoi
     */
    public String getForIdEnvoi() {
        return forIdEnvoi;
    }

    /**
     * @return the forIdExternalLink
     */
    public String getForIdExternalLink() {
        return forIdExternalLink;
    }

    /**
     * @return the forIdFormule
     */
    public String getForIdFormule() {
        return forIdFormule;
    }

    /**
     * @return the forIdJob
     */
    public String getForIdJob() {
        return forIdJob;
    }

    /**
     * @return the forNotStatusEnvoi
     */
    public String getForNotStatusEnvoi() {
        return forNotStatusEnvoi;
    }

    /**
     * @param forEnvoiFileName
     *            the forEnvoiFileName to set
     */
    public void setForEnvoiFileName(String forEnvoiFileName) {
        this.forEnvoiFileName = forEnvoiFileName;
    }

    /**
     * @param forEnvoiNoGroupe
     *            the forEnvoiNoGroupe to set
     */
    public void setForEnvoiNoGroupe(String forEnvoiNoGroupe) {
        this.forEnvoiNoGroupe = forEnvoiNoGroupe;
    }

    /**
     * @param forEnvoiStatus
     *            the forEnvoiStatus to set
     */
    public void setForEnvoiStatus(String forEnvoiStatus) {
        this.forEnvoiStatus = forEnvoiStatus;
    }

    /**
     * @param forEnvoiType
     *            the forEnvoiType to set
     */
    public void setForEnvoiType(String forEnvoiType) {
        this.forEnvoiType = forEnvoiType;
    }

    /**
     * @param forIdEnvoi
     *            the forIdEnvoi to set
     */
    public void setForIdEnvoi(String forIdEnvoi) {
        this.forIdEnvoi = forIdEnvoi;
    }

    /**
     * @param forIdExternalLink
     *            the forIdExternalLink to set
     */
    public void setForIdExternalLink(String forIdExternalLink) {
        this.forIdExternalLink = forIdExternalLink;
    }

    /**
     * @param forIdFormule
     *            the forIdFormule to set
     */
    public void setForIdFormule(String forIdFormule) {
        this.forIdFormule = forIdFormule;
    }

    /**
     * @param forIdJob
     *            the forIdJob to set
     */
    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    /**
     * @param forNotStatusEnvoi
     *            the forNotStatusEnvoi to set
     */
    public void setForNotStatusEnvoi(String forNotStatusEnvoi) {
        this.forNotStatusEnvoi = forNotStatusEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EnvoiItemSimpleModel> whichModelClass() {
        return EnvoiItemSimpleModel.class;
    }

}
