package ch.globaz.perseus.business.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;

/**
 * 
 * @author MBO
 * 
 */
public class DecisionSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_BY_DATE_DEBUT_ASC = "dateDebutAsc";
    public static String ORDER_BY_DATE_FIN_AND_DATE_VALIDATION_AND_NUM_DECISION_DESC = "dateDebutAndDateValidationAndNumDecisionDesc";
    public static String ORDER_BY_DATE_FIN_AND_DATETIME_DECISION_DESC = "dateFinAndDateTimeDecisionDesc";
    public static String ORDER_BY_DATE_FIN_DESC = "dateFinDesc";
    public static String ORDER_BY_DATETIME_DECISION_DESC = "dateTimeDecisionDesc";
    public static String ORDER_BY_ID_DOSSIER = "idDossier";
    public static String ORDER_BY_TYPE_DEMANDE_ASC = "typeDemandetAsc";
    public static String ORDER_BY_DATE_VALIDATION_AND_NUMERO_DESC = "dateValidationAndNumeroDecisionDesc";
    public static String WITH_ANNEE_VALABLE = "withAnneeValable";
    public final static String WITH_DATE_VALABLE_LE = "withDateValableForIdDossier";
    public final static String WITHOUT_DATEFIN = "withoutDateFin";

    public static String getORDER_BY_DATE_DEBUT_ASC() {
        return DecisionSearchModel.ORDER_BY_DATE_DEBUT_ASC;
    }

    public static String getORDER_BY_DATE_FIN_DESC() {
        return DecisionSearchModel.ORDER_BY_DATE_FIN_DESC;
    }

    private String betweenDateDebut = null;
    private String betweenDateFin = null;
    private String betweenDateValidationDebut = null;
    private String betweenDateValidationFin = null;
    private String forCsCaisse = null;
    private String forCsChoix = null;
    private String forCsEtat = null;
    private String forCsSexe = null;
    private String forCsTypeDecision = null;
    private String forDateDebut = null;
    private String forDateDebutDroit = null;
    private String forDateDebutValidation = null;
    private String forDateFin = null;
    private String forDateNaissance = null;
    private String forDatePreparation = null;
    private String forDateValable = null;
    private String forDateValidation = null;
    private String forDernierLot = null;
    private String forIdDecision = null;
    private String forIdDemande = null;
    private String forIdDossier = null;
    private String forIdTiers = null;
    private List<String> forListCsTypes = null;
    private List<String> forListIdDemande = null;
    private String forNotCsTypeDecision = null;
    private String forNotIdDemande = null;
    private String forNumeroDecision = null;
    private String forNumOFS = null;
    private String forUtilisateurPreparation = null;
    private String forUtilisateurValidation = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public DecisionSearchModel() {
        forListIdDemande = new ArrayList<String>();
    }

    public String getBetweenDateDebut() {
        return betweenDateDebut;
    }

    public String getBetweenDateFin() {
        return betweenDateFin;
    }

    public String getBetweenDateValidationDebut() {
        return betweenDateValidationDebut;
    }

    public String getBetweenDateValidationFin() {
        return betweenDateValidationFin;
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

    public String getForCsChoix() {
        return forCsChoix;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateDebutDroit() {
        return forDateDebutDroit;
    }

    public String getForDateDebutValidation() {
        return forDateDebutValidation;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDatePreparation() {
        return forDatePreparation;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    public String getForDateValidation() {
        return forDateValidation;
    }

    public String getForDernierLot() {
        return forDernierLot;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public List<String> getForListCsTypes() {
        return forListCsTypes;
    }

    /**
     * @return the forListIdDemande
     */
    public List<String> getForListIdDemande() {
        return forListIdDemande;
    }

    /**
     * @return the forNotCsTypeDecision
     */
    public String getForNotCsTypeDecision() {
        return forNotCsTypeDecision;
    }

    public String getForNotIdDemande() {
        return forNotIdDemande;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForNumOFS() {
        return forNumOFS;
    }

    public String getForUtilisateurPreparation() {
        return forUtilisateurPreparation;
    }

    public String getForUtilisateurValidation() {
        return forUtilisateurValidation;
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

    public void setBetweenDateDebut(String betweenDateDebut) {
        this.betweenDateDebut = betweenDateDebut;
    }

    public void setBetweenDateFin(String betweenDateFin) {
        this.betweenDateFin = betweenDateFin;
    }

    public void setBetweenDateValidationDebut(String betweenDateValidationDebut) {
        this.betweenDateValidationDebut = betweenDateValidationDebut;
    }

    public void setBetweenDateValidationFin(String betweenDateValidationFin) {
        this.betweenDateValidationFin = betweenDateValidationFin;
    }

    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
    }

    public void setForCsChoix(String forCsChoix) {
        this.forCsChoix = forCsChoix;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateDebutDroit(String forDateDebutDroit) {
        this.forDateDebutDroit = forDateDebutDroit;
    }

    public void setForDateDebutValidation(String forDate) {
        forDateDebutValidation = forDate;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDatePreparation(String forDatePreparation) {
        this.forDatePreparation = forDatePreparation;
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

    public void setForDateValidation(String forDateValidation) {
        this.forDateValidation = forDateValidation;
    }

    public void setForDernierLot(String forDernierLot) {
        this.forDernierLot = forDernierLot;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForListCsTypes(List<String> forListCsTypes) {
        this.forListCsTypes = forListCsTypes;
    }

    /**
     * @param forListIdDemande
     *            the forListIdDemande to set
     */
    public void setForListIdDemande(List<String> forListIdDemande) {
        this.forListIdDemande = forListIdDemande;
    }

    /**
     * @param forNotCsTypeDecision
     *            the forNotCsTypeDecision to set
     */
    public void setForNotCsTypeDecision(String forNotCsTypeDecision) {
        this.forNotCsTypeDecision = forNotCsTypeDecision;
    }

    public void setForNotIdDemande(String forNotIdDemande) {
        this.forNotIdDemande = forNotIdDemande;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForNumOFS(String forNumOFS) {
        this.forNumOFS = forNumOFS;
    }

    public void setForUtilisateurPreparation(String forUtilisateurPreparation) {
        this.forUtilisateurPreparation = forUtilisateurPreparation;
    }

    public void setForUtilisateurValidation(String forUtilisateurValidation) {
        this.forUtilisateurValidation = forUtilisateurValidation;
    }

    public void setLikeNom(String likeNom) {
        if (!JadeStringUtil.isEmpty(likeNom)) {
            this.likeNom = JadeStringUtil.convertSpecialChars(likeNom).toUpperCase();
        }
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public void setLikePrenom(String likePrenom) {
        if (!JadeStringUtil.isEmpty(likePrenom)) {
            this.likePrenom = JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase();
        }
    }

    @Override
    public Class whichModelClass() {
        return Decision.class;
    }

}
