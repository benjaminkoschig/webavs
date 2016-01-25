package ch.globaz.vulpecula.business.models.employeur;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class EmployeurParticulariteSearchComplexModel extends JadeSearchComplexModel {
    private String forTypeAffiliation = null;
    private String forParticularite = null;
    private String forAffiliationId = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdConvention = null;
    private String likeConventionDesignation = null;
    private String likeDesignationTiersUpper = null;
    private String likeNumeroAffilie = null;
    private Collection<String> inPeriodicite = null;
    private Boolean isBvr = null;

    /**
     * @return the forAffiliationId
     */
    public String getForAffiliationId() {
        return forAffiliationId;
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forIdConvention
     */
    public String getForIdConvention() {
        return forIdConvention;
    }

    /**
     * @return the likeConventionDesignation
     */
    public String getLikeConventionDesignation() {
        return likeConventionDesignation;
    }

    /**
     * @return the likeDesignationUpper
     */
    public String getLikeDesignationTiersUpper() {
        return likeDesignationTiersUpper;
    }

    /**
     * @return the likeNumeroAffilie
     */
    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    /**
     * @param forAffiliationId
     *            the forAffiliationId to set
     */
    public void setForAffiliationId(String forAffiliationId) {
        this.forAffiliationId = forAffiliationId;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forIdConvention
     *            the forIdConvention to set
     */
    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    /**
     * @param likeConventionDesignation
     *            the likeConventionDesignation to set
     */
    public void setLikeConventionDesignation(String likeConventionDesignation) {
        this.likeConventionDesignation = JadeStringUtil.convertSpecialChars(likeConventionDesignation.toUpperCase());
    }

    /**
     * @param likeDesignationUpper
     *            the likeDesignationUpper to set
     */
    public void setLikeDesignationTiersUpper(String likeDesignationUpper) {
        likeDesignationTiersUpper = JadeStringUtil.convertSpecialChars(likeDesignationUpper.toUpperCase());
    }

    /**
     * @param likeNumeroAffilie
     *            the likeNumeroAffilie to set
     */
    public void setLikeNumeroAffilie(String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

    public Collection<String> getInPeriodicite() {
        return inPeriodicite;
    }

    public void setInPeriodicite(Collection<String> inPeriodicite) {
        this.inPeriodicite = inPeriodicite;
    }

    @Override
    public Class<EmployeurParticulariteComplexModel> whichModelClass() {
        return EmployeurParticulariteComplexModel.class;
    }

    public Boolean getIsBvr() {
        return isBvr;
    }

    public void setIsBvr(Boolean isBvr) {
        this.isBvr = isBvr;
    }

    public String getForParticularite() {
        return forParticularite;
    }

    public void setForParticularite(String forParticularite) {
        this.forParticularite = forParticularite;
    }

    public String getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    public void setForTypeAffiliation(String forTypeAffiliation) {
        this.forTypeAffiliation = forTypeAffiliation;
    }
}
