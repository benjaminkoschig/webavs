/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleControleurEnvoiStatusSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnonce = null;
    private String forIdEnvoi = null;
    private String forIdJob = null;
    private String forIdStatus = null;
    private String forNoGroupe = null;
    private String forNotStatusEnvoi = null;
    private String forStatusEnvoi = null;
    private String forTypeEnvoi = null;

    /**
     * @return the forIdAnnonce
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * @return the forIdEnvoi
     */
    public String getForIdEnvoi() {
        return forIdEnvoi;
    }

    /**
     * @return the forIdJob
     */
    public String getForIdJob() {
        return forIdJob;
    }

    /**
     * @return the forIdStatus
     */
    public String getForIdStatus() {
        return forIdStatus;
    }

    /**
     * @return the forNoGroupe
     */
    public String getForNoGroupe() {
        return forNoGroupe;
    }

    /**
     * @return the forNotStatusEnvoi
     */
    public String getForNotStatusEnvoi() {
        return forNotStatusEnvoi;
    }

    /**
     * @return the forStatusEnvoi
     */
    public String getForStatusEnvoi() {
        return forStatusEnvoi;
    }

    /**
     * @return the forTypeEnvoi
     */
    public String getForTypeEnvoi() {
        return forTypeEnvoi;
    }

    /**
     * @param forIdAnnonce
     *            the forIdAnnonce to set
     */
    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    /**
     * @param forIdEnvoi
     *            the forIdEnvoi to set
     */
    public void setForIdEnvoi(String forIdEnvoi) {
        this.forIdEnvoi = forIdEnvoi;
    }

    /**
     * @param forIdJob
     *            the forIdJob to set
     */
    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    /**
     * @param forIdStatus
     *            the forIdStatus to set
     */
    public void setForIdStatus(String forIdStatus) {
        this.forIdStatus = forIdStatus;
    }

    /**
     * @param forNoGroupe
     *            the forNoGroupe to set
     */
    public void setForNoGroupe(String forNoGroupe) {
        this.forNoGroupe = forNoGroupe;
    }

    /**
     * @param forNotStatusEnvoi
     *            the forNotStatusEnvoi to set
     */
    public void setForNotStatusEnvoi(String forNotStatusEnvoi) {
        this.forNotStatusEnvoi = forNotStatusEnvoi;
    }

    /**
     * @param forStatusEnvoi
     *            the forStatusEnvoi to set
     */
    public void setForStatusEnvoi(String forStatusEnvoi) {
        this.forStatusEnvoi = forStatusEnvoi;
    }

    /**
     * @param forTypeEnvoi
     *            the forTypeEnvoi to set
     */
    public void setForTypeEnvoi(String forTypeEnvoi) {
        this.forTypeEnvoi = forTypeEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleControleurEnvoiStatus.class;
    }

}
