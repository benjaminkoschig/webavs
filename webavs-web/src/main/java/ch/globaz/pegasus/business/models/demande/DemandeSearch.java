package ch.globaz.pegasus.business.models.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class DemandeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_DATE_DEBUT_DESC = "dateDebutDesc";
    public static final String WITH_DEMANDE_DATE_FIN_NULL = "withDemandeDateFinNull";
    public static final String FOR_DEMANDE_WITH_DATE_DEBUT_MAX_OR_DATE_DEBUT_VIDE = "forDemandeWithDateDebutMax";
    private String forCsEtatDemande = null;
    private String forCsSexe = null;
    private String forDateDepotMax = null;
    private String forDateDepotMin = null;
    private String forDateNaissance = null;
    private String forIdDemande = null;
    private String forIdDemandeNotEquals = null;
    private String forIdDossier = null;

    private String forIdGestionnaire = null;

    private String forIdTiers = null;
    private List<String> inCsEtatDemande = null;
    private String forNotCsEtatDemande = null;

    private String likeNom = null;

    private String likeNss = null;

    private String likePrenom = null;

    /**
     * @return the forCsEtatDemande
     */
    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateDepotMax() {
        return forDateDepotMax;
    }

    public String getForDateDepotMin() {
        return forDateDepotMin;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDemandeNotEquals() {
        return forIdDemandeNotEquals;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdGestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    public List<String> getInCsEtatDemande() {
        return inCsEtatDemande;
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

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forCsEtatDemande
     *            the forCsEtatDemande to set
     */
    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateDepotMax(String forDateDepotMax) {
        this.forDateDepotMax = forDateDepotMax;
    }

    public void setForDateDepotMin(String forDateDepotMin) {
        this.forDateDepotMin = forDateDepotMin;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDemandeNotEquals(String forIdDemandeNotEquals) {
        this.forIdDemandeNotEquals = forIdDemandeNotEquals;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdGestionnaire
     *            the forIdGestionnaire to set
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setInCsEtatDemande(List<String> inCsEtatDemande) {
        this.inCsEtatDemande = inCsEtatDemande;
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

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    public String getForNotCsEtatDemande() {
        return forNotCsEtatDemande;
    }

    public void setForNotCsEtatDemande(String forNotCsEtatDemande) {
        this.forNotCsEtatDemande = forNotCsEtatDemande;
    }

    @Override
    public Class whichModelClass() {
        return Demande.class;
    }

}
