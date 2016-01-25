package ch.globaz.vulpecula.business.models.employeur;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import globaz.pavo.util.CIAffilie;
import java.util.Collection;

public class EmployeurSearchComplexModel extends JadeSearchComplexModel {
    private static final long serialVersionUID = -2897148419721954304L;

    public static final String ORDER_BY_RAISON_SOCIAL_ASC = "raisonSocialeAsc";

    private String forAffiliationId = null;
    private String forNumeroAffilie = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdConvention = null;
    private String likeConventionDesignation = null;
    private String likeDesignationTiersUpper = null;
    private String likeRaisonSocialeUpper = null;
    private String likeNumeroAffilie = null;
    private Collection<String> inPeriodicite = null;
    private String typeAffiliation = CIAffilie.CS_EMPLOYEUR;
    private Boolean isBvr = null;
    private Boolean isEditerSansTravailleur = null;

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

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public String getLikeRaisonSocialeUpper() {
        return likeRaisonSocialeUpper;
    }

    public void setLikeRaisonSocialeUpper(String likeRaisonSocialeUpper) {
        this.likeRaisonSocialeUpper = "%" + JadeStringUtil.convertSpecialChars(likeRaisonSocialeUpper.toUpperCase());
    }

    @Override
    public Class<EmployeurComplexModel> whichModelClass() {
        return EmployeurComplexModel.class;
    }

    public Boolean getIsBvr() {
        return isBvr;
    }

    public void setIsBvr(Boolean isBvr) {
        this.isBvr = isBvr;
    }

    public Boolean getIsEditerSansTravailleur() {
        return isEditerSansTravailleur;
    }

    public void setIsEditerSansTravailleur(Boolean isEditerSansTravailleur) {
        this.isEditerSansTravailleur = isEditerSansTravailleur;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }
}
