package ch.globaz.perseus.business.models.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DemandeSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_BY_DATE_DEBUT_ASC = "dateDebutAsc";
    public final static String ORDER_BY_DATE_DEBUT_ASC_NOM_ASC = "dateDebutAscNomAsc";
    public static String ORDER_BY_DATE_FIN_AND_DATETIME_DECISION_DESC = "dateFinAndDateTimeDecisionDesc";
    public static String ORDER_BY_DATE_FIN_ID_DOSSIER_DESC = "ordreByDateFinAndIdDossierDesc";
    public static String ORDER_BY_DATETIME_DECISION_DESC = "dateTimeDecisionDesc";
    public final static String ORDER_BY_DATE_DEBUT_DESC = "dateDebutDesc";

    public final static String ORDER_BY_NAME = "ordreAlphabetiqueNom";
    public final static String WITH_ANNEE_VALABLE = "withAnneeValable";
    public final static String WITH_DATE_VALABLE_LE = "withDateValableForIdDossier";
    public final static String WITH_ONLY_DATEFIN = "withOnlyDateFin";
    public final static String WITHOUT_DATEFIN = "withoutDateFin";
    public final static String WITH_ANNEE_VALABLE_FOR_ID_DOSSIER = "withAnneeValableForIdDossier";

    public static String getOrderByName() {
        return DemandeSearchModel.ORDER_BY_NAME;
    }

    private String betweenAgeRetraiteConjointDebut = null;
    private String betweenAgeRetraiteConjointFin = null;
    private String betweenAgeRetraiteRequerantDebut = null;
    private String betweenAgeRetraiteRequerantFin = null;
    private String betweenDateDebut = null;
    private String betweenDateDepotDebut = null;
    private String betweenDateDepotFin = null;
    private String betweenDateFin = null;
    private String betweenDateRevisionDebut = null;
    private String betweenDateRevisionFin = null;
    private String forCsCaisse = null;
    private String forCsEtatDemande = null;
    private String forCsSexe = null;
    private String forCsSexeConjoint = null;
    private String forCsSexeRequerant = null;
    private String forCsTypeDemande = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateNaissance = null;
    private String forDateValable = null;
    private String forIdDossier = null;
    private String forIdDossierPcf = null;
    private String forIdGestionnaire = null;
    private String forIdSituationFamiliale = null;
    private Boolean forIsDemandeIp = null;
    private String forNotIdDemande = null;
    private String forNumeroOFS = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    private String notCsEtatDemande = null;

    public String getBetweenAgeRetraiteConjointDebut() {
        return betweenAgeRetraiteConjointDebut;
    }

    public String getBetweenAgeRetraiteConjointFin() {
        return betweenAgeRetraiteConjointFin;
    }

    public String getBetweenAgeRetraiteRequerantDebut() {
        return betweenAgeRetraiteRequerantDebut;
    }

    public String getBetweenAgeRetraiteRequerantFin() {
        return betweenAgeRetraiteRequerantFin;
    }

    public String getBetweenDateDebut() {
        return betweenDateDebut;
    }

    public String getBetweenDateDepotDebut() {
        return betweenDateDepotDebut;
    }

    public String getBetweenDateDepotFin() {
        return betweenDateDepotFin;
    }

    public String getBetweenDateFin() {
        return betweenDateFin;
    }

    public String getBetweenDateRevisionDebut() {
        return betweenDateRevisionDebut;
    }

    public String getBetweenDateRevisionFin() {
        return betweenDateRevisionFin;
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

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

    public String getForCsSexeConjoint() {
        return forCsSexeConjoint;
    }

    public String getForCsSexeRequerant() {
        return forCsSexeRequerant;
    }

    public String getForCsTypeDemande() {
        return forCsTypeDemande;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdDossierPcf
     */
    public String getForIdDossierPcf() {
        return forIdDossierPcf;
    }

    /**
     * @return the forIdGestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    /**
     * @return the forIdSituationFamiliale
     */
    public String getForIdSituationFamiliale() {
        return forIdSituationFamiliale;
    }

    public Boolean getForIsDemandeIp() {
        return forIsDemandeIp;
    }

    /**
     * @return the forNotIdDemande
     */
    public String getForNotIdDemande() {
        return forNotIdDemande;
    }

    public String getForNumeroOFS() {
        return forNumeroOFS;
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

    public String getNotCsEtatDemande() {
        return notCsEtatDemande;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setBetweenAgeRetraiteConjointDebut(String betweenAgeRetraiteConjointDebut) {
        this.betweenAgeRetraiteConjointDebut = betweenAgeRetraiteConjointDebut;
    }

    public void setBetweenAgeRetraiteConjointFin(String betweenAgeRetraiteConjointFin) {
        this.betweenAgeRetraiteConjointFin = betweenAgeRetraiteConjointFin;
    }

    public void setBetweenAgeRetraiteRequerantDebut(String betweenAgeRetraiteRequerantDebut) {
        this.betweenAgeRetraiteRequerantDebut = betweenAgeRetraiteRequerantDebut;
    }

    public void setBetweenAgeRetraiteRequerantFin(String betweenAgeRetraiteRequerantFin) {
        this.betweenAgeRetraiteRequerantFin = betweenAgeRetraiteRequerantFin;
    }

    public void setBetweenDateDebut(String betweenDateDebut) {
        this.betweenDateDebut = betweenDateDebut;
    }

    public void setBetweenDateDepotDebut(String betweenDateDepotDebut) {
        this.betweenDateDepotDebut = betweenDateDepotDebut;
    }

    public void setBetweenDateDepotFin(String betweenDateDepotFin) {
        this.betweenDateDepotFin = betweenDateDepotFin;
    }

    public void setBetweenDateFin(String betweenDateFin) {
        this.betweenDateFin = betweenDateFin;
    }

    public void setBetweenDateRevisionDebut(String betweenDateRevisionDebut) {
        this.betweenDateRevisionDebut = betweenDateRevisionDebut;
    }

    public void setBetweenDateRevisionFin(String betweenDateRevisionFin) {
        this.betweenDateRevisionFin = betweenDateRevisionFin;
    }

    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
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

    public void setForCsSexeConjoint(String forCsSexeConjoint) {
        this.forCsSexeConjoint = forCsSexeConjoint;
    }

    public void setForCsSexeRequerant(String forCsSexeRequerant) {
        this.forCsSexeRequerant = forCsSexeRequerant;
    }

    public void setForCsTypeDemande(String forCsTypeDemande) {
        this.forCsTypeDemande = forCsTypeDemande;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
        if (!JadeStringUtil.isEmpty(forDateValable)) {
            setWhereKey(DemandeSearchModel.WITH_DATE_VALABLE_LE);
        }
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdDossierPcf
     *            the forIdDossierPcf to set
     */
    public void setForIdDossierPcf(String forIdDossierPcf) {
        this.forIdDossierPcf = forIdDossierPcf;
    }

    /**
     * @param forIdGestionnaire
     *            the forIdGestionnaire to set
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    /**
     * @param forIdSituationFamiliale
     *            the forIdSituationFamiliale to set
     */
    public void setForIdSituationFamiliale(String forIdSituationFamiliale) {
        this.forIdSituationFamiliale = forIdSituationFamiliale;
    }

    public void setForIsDemandeIp(Boolean forIsDemandeIp) {
        this.forIsDemandeIp = forIsDemandeIp;
    }

    /**
     * @param forNotIdDemande
     *            the forNotIdDemande to set
     */
    public void setForNotIdDemande(String forNotIdDemande) {
        this.forNotIdDemande = forNotIdDemande;
    }

    public void setForNumeroOFS(String forNumeroOFS) {
        this.forNumeroOFS = forNumeroOFS;
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

    public void setNotCsEtatDemande(String notCsEtatDemande) {
        this.notCsEtatDemande = notCsEtatDemande;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class whichModelClass() {
        return Demande.class;
    }

}
