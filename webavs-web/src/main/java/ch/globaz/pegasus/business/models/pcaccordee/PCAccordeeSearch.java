package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class PCAccordeeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Défint qu'il faut retourner que les pc accordee de la version version atuelle ( du jour)
     */
    public static final String FOR_CURRENT_VERSIONED = "forCurrentVersioned";
    public static final String FOR_CURRENT_VERSIONED_WITHOUT_COPIE = "forCurrentVersionedWithoutCopie";
    public static final String FOR_DATE_VALABLE = "withDateValable";
    public static final String FOR_PCA_REPLACEC_BY_DECISION_SUPPRESSION = "forPcaReplacedByDecisionsSuppression";
    public static final String FOR_PCA_REPLACEC_BY_DECISION_SUPPRESSION_ANNULATION = "forPcaReplacedByDecisionsSuppressionAnnulation";
    public static final String FOR_REPLACE_BY_NEW_PCA = "forReplaceByAnewPCA";
    public static final String FOR_SEARCH_WITH_DATE = "forSearchWithDate";
    public static final String ORDER_BY_DATE_DEBUT = "byDateDebut";
    public static final String WITH_DATE_FIN_NULL = "withDateFinIsNull";
    public static final String FOR_CURRENT_VERSIONED_WITH_DATE_FIN_FORCE = "forCurrentVersionedWithDateFinForce";
    public static final String FOR_PCA_WITH_DATE_MAX = "forPcaWithDateMax";

    private String forCsEtat = null;
    private String forCsEtatPca = null;
    private String forCsSexe = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateNaissance = null;
    private String forDateSuppression = null;
    private String forDateValable = null;
    private String forExcludePcaNonValidate = null;
    private String forIdDemande = null;
    private String forIdDroit = null;
    private String forIdPCAccordee = null;
    private String forIdPcaParent = null;
    private String forIdTiers = null;
    private boolean forIsDeleted = false;
    private String forNonHistorise = null;
    private String forNoVersionDroit = null;
    private String forVersionDroit = null;
    private List<String> inIdEntity = null;
    private List<String> inIdPCAccordee = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    /**
     * @return th forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsEtatPca() {
        return forCsEtatPca;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDateSuppression() {
        return forDateSuppression;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForExcludePcaNonValidate() {
        return forExcludePcaNonValidate;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdDroit
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

    public String getForIdPcaParent() {
        return forIdPcaParent;
    }

    /**
     * @return th forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    public boolean getForIsDeleted() {
        return forIsDeleted;
    }

    public String getForNonHistorise() {
        return forNonHistorise;
    }

    public String getForNoVersionDroit() {
        return forNoVersionDroit;
    }

    public String getForVersionDroit() {
        return forVersionDroit;
    }

    public List<String> getInIdEntity() {
        return inIdEntity;
    }

    public List<String> getInIdPCAccordee() {
        return inIdPCAccordee;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtatPca(String forCsEtatPca) {
        this.forCsEtatPca = forCsEtatPca;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDateSuppression(String forDateSuppression) {
        this.forDateSuppression = forDateSuppression;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForExcludePcaNonValidate(String forExcludePcaNonValidate) {
        this.forExcludePcaNonValidate = forExcludePcaNonValidate;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
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

    public void setForIdPcaParent(String forIdPcaParent) {
        this.forIdPcaParent = forIdPcaParent;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIsDeleted(boolean forIsDeleted) {
        this.forIsDeleted = forIsDeleted;
    }

    public void setForNonHistorise(String forNonHistorise) {
        this.forNonHistorise = forNonHistorise;
    }

    public void setForNoVersionDroit(String forNoVersionDroit) {
        this.forNoVersionDroit = forNoVersionDroit;
    }

    public void setForVersionDroit(String forVersionDroit) {
        this.forVersionDroit = forVersionDroit;
    }

    public void setInIdEntity(List<String> inIdEntity) {
        this.inIdEntity = inIdEntity;
    }

    public void setInIdPCAccordee(List<String> inIdPCAccordee) {
        this.inIdPCAccordee = inIdPCAccordee;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<PCAccordee> whichModelClass() {
        return PCAccordee.class;
    }

}
