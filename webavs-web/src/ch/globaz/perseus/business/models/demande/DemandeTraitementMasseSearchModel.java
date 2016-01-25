package ch.globaz.perseus.business.models.demande;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class DemandeTraitementMasseSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_BY_DATE_DEBUT_DESC = "dateDebutDesc";
    public final static String POPULATION_SANS_DATE_DE_FIN = "populationSansDateDeFin";
    public final static String POPULATION_DATE_DE_FIN_FIN_ANNEE = "populationAvecDateFinFinAnnee";

    public static String getOrderByName() {
        return DemandeTraitementMasseSearchModel.ORDER_BY_DATE_DEBUT_DESC;
    }

    private String forIdDemande = null;

    private String greaterDateDebut = null;
    private String lessDateDebut = null;
    private String betweenDateFin = null;
    private String forCsEtat = null;
    private String forCsTypeDecision = null;
    private String forCsCaisse = null;
    private String forCsEtatDemande = null;
    private String forCsTypeDemande = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateFinMax = null;
    private String forIdDossier = null;
    private String forNotIdDemande = null;
    private Boolean fromRI = null;

    private String notCsEtatDemande = null;

    private List<String> forListCsTypes = null;

    public String getForDateFinMax() {
        return forDateFinMax;
    }

    public void setForDateFinMax(String forDateFinMax) {
        this.forDateFinMax = forDateFinMax;
    }

    public String getForNotIdDemande() {
        return forNotIdDemande;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForNotIdDemande(String forNotIdDemande) {
        this.forNotIdDemande = forNotIdDemande;
    }

    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    public String getBetweenDateFin() {
        return betweenDateFin;
    }

    public Boolean getFromRI() {
        return fromRI;
    }

    public void setFromRI(Boolean fromRI) {
        this.fromRI = fromRI;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

    public String getGreaterDateDebut() {
        return greaterDateDebut;
    }

    public void setGreaterDateDebut(String greaterDateDebut) {
        this.greaterDateDebut = greaterDateDebut;
    }

    public String getLessDateDebut() {
        return lessDateDebut;
    }

    public void setLessDateDebut(String lessDateDebut) {
        this.lessDateDebut = lessDateDebut;
    }

    /**
     * @return the forCsEtatDemande
     */
    public String getForCsEtatDemande() {
        return forCsEtatDemande;
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
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
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

    public void setBetweenDateFin(String betweenDateFin) {
        this.betweenDateFin = betweenDateFin;
    }

    public List<String> getForListCsTypes() {
        return forListCsTypes;
    }

    public void setForListCsTypes(List<String> forListCsTypes) {
        this.forListCsTypes = forListCsTypes;
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
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
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
    public Class<DemandeTraitementMasse> whichModelClass() {
        return DemandeTraitementMasse.class;
    }

}
