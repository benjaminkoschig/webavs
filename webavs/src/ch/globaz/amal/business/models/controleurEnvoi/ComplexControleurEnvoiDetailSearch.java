/**
 * 
 */
package ch.globaz.amal.business.models.controleurEnvoi;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * @author DHI
 * 
 */
public class ComplexControleurEnvoiDetailSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private String forIdDetailFamille = null;
    private String forIdFamille = null;
    private String forIdJob = null;
    private String forIdStatus = null;
    private String forStatusEnvoi = null;
    private String forUserJob = null;
    private Collection<String> inStatusEnvoi = null;

    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    /**
     * @return the forIdDetailFamille
     */
    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    public String getForIdFamille() {
        return forIdFamille;
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
     * @return the forStatusEnvoi
     */
    public String getForStatusEnvoi() {
        return forStatusEnvoi;
    }

    /**
     * @return the forUserJob
     */
    public String getForUserJob() {
        return forUserJob;
    }

    public Collection<String> getInStatusEnvoi() {
        return inStatusEnvoi;
    }

    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    /**
     * @param forIdDetailFamille
     *            the forIdDetailFamille to set
     */
    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    public void setForIdFamille(String forIdFamille) {
        this.forIdFamille = forIdFamille;
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
     * @param forStatusEnvoi
     *            the forStatusEnvoi to set
     */
    public void setForStatusEnvoi(String forStatusEnvoi) {
        this.forStatusEnvoi = forStatusEnvoi;
    }

    /**
     * @param forUserJob
     *            the forUserJob to set
     */
    public void setForUserJob(String forUserJob) {
        this.forUserJob = forUserJob;
    }

    public void setInStatusEnvoi(Collection<String> inStatusEnvoi) {
        this.inStatusEnvoi = inStatusEnvoi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ComplexControleurEnvoiDetail.class;
    }

}
