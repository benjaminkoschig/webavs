/**
 * 
 */
package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author SCE
 * 
 *         28 sept. 2010
 */
public class ListPCAccordeeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCacherHistorique = null;
    private String forCsEtat = null;
    private String forCsSexe = null;
    private String forDateNaissance = null;
    private String forIdDemande = null;
    private String forIdDossier = null;
    private String forIdDroit = null;
    private String forIdVersionDroit;
    private String forIdPCAccordee = null;
    private String forIdTiers = null;
    private Boolean forIsSupprime = null;
    private String forNoVersion = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public String getForCacherHistorique() {
        return forCacherHistorique;
    }

    /**
     * @return the forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forNoDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdPCAccordee
     */
    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    public Boolean getForIsSupprime() {
        return forIsSupprime;
    }

    /**
     * @return the forNoVersionDroit
     */
    public String getForNoVersion() {
        return forNoVersion;
    }

    /**
     * @return the likeNom
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return the likeNss
     */
    public String getLikeNss() {
        return likeNss;
    }

    /**
     * @return the likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    public void setForCacherHistorique(String forCacherHistorique) {
        this.forCacherHistorique = forCacherHistorique;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forNoDroit
     *            the forNoDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdPCAccordee
     *            the forIdPCAccordee to set
     */
    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIsSupprime(Boolean forIsSupprime) {
        this.forIsSupprime = forIsSupprime;
    }

    /**
     * @param forNoVersionDroit
     *            the forNoVersionDroit to set
     */
    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    /**
     * @param likeNom
     *            the likeNom to set
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    /**
     * @param likeNss
     *            the likeNss to set
     */
    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * @param likePrenom
     *            the likePrenom to set
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<ListPCAccordee> whichModelClass() {
        return ListPCAccordee.class;
    }

}
