package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDonneesPersonnelles extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String communeOrigine = null;
    private String communeOrigineCodeOfs = null;
    private String csLienRepondant = null;
    private String csPermis = null;
    private String csStatusRefugieApatride = null;
    private String idDernierDomicileLegale = null;
    private String idDonneesPersonnelles = null;
    private String idLocaliteOrigine = null;
    private String idTiersRepondant = null;
    private Boolean isEnfant = false;
    private Boolean isRepresentantLegal = false;
    private String noCaisseAvs = null;
    private String noOCC = null;
    private String noOfficeAi = null;

    public String getCommuneOrigine() {
        return communeOrigine;
    }

    public String getCommuneOrigineCodeOfs() {
        return communeOrigineCodeOfs;
    }

    public String getCsLienRepondant() {
        return csLienRepondant;
    }

    public String getCsPermis() {
        return csPermis;
    }

    /**
     * @return the csStatusRefugieApatride
     */
    public String getCsStatusRefugieApatride() {
        return csStatusRefugieApatride;
    }

    @Override
    public String getId() {
        return idDonneesPersonnelles;
    }

    /**
     * @return the idDernierDomicileLegale
     */
    public String getIdDernierDomicileLegale() {
        return idDernierDomicileLegale;
    }

    /**
     * @return the idDonneesPersonnelles
     */
    public String getIdDonneesPersonnelles() {
        return idDonneesPersonnelles;
    }

    public String getIdLocaliteOrigine() {
        return idLocaliteOrigine;
    }

    public String getIdTiersRepondant() {
        return idTiersRepondant;
    }

    public Boolean getIsEnfant() {
        return isEnfant;
    }

    public Boolean getIsRepresentantLegal() {
        return isRepresentantLegal;
    }

    /**
     * @return the noCaisseAvs
     */
    public String getNoCaisseAvs() {
        return noCaisseAvs;
    }

    /**
     * @return the noOCC
     */
    public String getNoOCC() {
        return noOCC;
    }

    /**
     * @return the noOfficeAi
     */
    public String getNoOfficeAi() {
        return noOfficeAi;
    }

    public void setCommuneOrigine(String communeOrigine) {
        this.communeOrigine = communeOrigine;
    }

    public void setCommuneOrigineCodeOfs(String communeOrigineCodeOfs) {
        this.communeOrigineCodeOfs = communeOrigineCodeOfs;
    }

    public void setCsLienRepondant(String csLienRepondant) {
        this.csLienRepondant = csLienRepondant;
    }

    public void setCsPermis(String csPermis) {
        this.csPermis = csPermis;
    }

    /**
     * @param csStatusRefugieApatride
     *            the csStatusRefugieApatride to set
     */
    public void setCsStatusRefugieApatride(String csStatusRefugieApatride) {
        this.csStatusRefugieApatride = csStatusRefugieApatride;
    }

    @Override
    public void setId(String id) {
        idDonneesPersonnelles = id;
    }

    /**
     * @param idDernierDomicileLegale
     *            the idDernierDomicileLegale to set
     */
    public void setIdDernierDomicileLegale(String idDernierDomicileLegale) {
        this.idDernierDomicileLegale = idDernierDomicileLegale;
    }

    /**
     * @param idDonneesPersonnelles
     *            the idDonneesPersonnelles to set
     */
    public void setIdDonneesPersonnelles(String idDonneesPersonnelles) {
        this.idDonneesPersonnelles = idDonneesPersonnelles;
    }

    public void setIdLocaliteOrigine(String idLocaliteOrigine) {
        this.idLocaliteOrigine = idLocaliteOrigine;
    }

    public void setIdTiersRepondant(String idTiersRepondant) {
        this.idTiersRepondant = idTiersRepondant;
    }

    public void setIsEnfant(Boolean isEnfant) {
        this.isEnfant = isEnfant;
    }

    public void setIsRepresentantLegal(Boolean isRepresentantLegal) {
        this.isRepresentantLegal = isRepresentantLegal;
    }

    /**
     * @param noCaisseAvs
     *            the noCaisseAvs to set
     */
    public void setNoCaisseAvs(String noCaisseAvs) {
        this.noCaisseAvs = noCaisseAvs;
    }

    /**
     * @param noOCC
     *            the noOCC to set
     */
    public void setNoOCC(String noOCC) {
        this.noOCC = noOCC;
    }

    /**
     * @param noOfficeAi
     *            the noOfficeAi to set
     */
    public void setNoOfficeAi(String noOfficeAi) {
        this.noOfficeAi = noOfficeAi;
    }

}
