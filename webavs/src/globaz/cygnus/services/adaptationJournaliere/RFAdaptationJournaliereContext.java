/*
 * Créé le 4 avril 2011
 */
package globaz.cygnus.services.adaptationJournaliere;

import globaz.cygnus.api.adaptationsJournalieres.IRFAdaptationJournaliere;
import globaz.cygnus.services.RFRetrieveInfoDroitPCServiceData;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * author jje
 */
public class RFAdaptationJournaliereContext {

    private String dateDebutDecision = "";
    private String dateDernierPmt = "";
    private String dateFinDecision = "";
    private String etat = IRFAdaptationJournaliere.ETAT_SUCCES;
    private String gestionnaire = "";
    private boolean hasQdsActuelle = false;
    private String idAdaptationJournaliere = "";
    private String idDecisionPc = "";
    private String idPotGrandeQd = "";

    // String[id Rfm Prestation Accordee, id demande]
    private Set<String[]> idsPrestationAccordeeEnCours = new HashSet<String[]>();
    private String IdTiersBeneficiaire = "";
    private RFRetrieveInfoDroitPCServiceData infoDroitPcServiceData = null;
    private String limiteAnnuelleGrandeQd = "";

    private String montantExcedentDeRevenu = "";
    private String nssTiersBeneficiaire = "";
    private String numeroDecisionPc = "";

    private List<RFAdaptationJournalierePeriodeQdData> periodesQdExistantes = new LinkedList<RFAdaptationJournalierePeriodeQdData>();

    private String typeDeDecisionPc = "";
    private String typeRemboursementConjoint = "";

    private String typeRemboursementRequerant = "";

    public String getDateDebutDecision() {
        return dateDebutDecision;
    }

    public String getDateDernierPmt() {
        return dateDernierPmt;
    }

    public String getDateFinDecision() {
        return dateFinDecision;
    }

    public String getEtat() {
        return etat;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    public String getIdAdaptationJournaliere() {
        return idAdaptationJournaliere;
    }

    public String getIdDecisionPc() {
        return idDecisionPc;
    }

    public String getIdPotGrandeQd() {
        return idPotGrandeQd;
    }

    /**
     * @return String[id Rfm Prestation Accordee, id demande]
     */
    public Set<String[]> getIdsPrestationAccordeeEnCours() {
        return idsPrestationAccordeeEnCours;
    }

    public String getIdTiersBeneficiaire() {
        return IdTiersBeneficiaire;
    }

    public RFRetrieveInfoDroitPCServiceData getInfoDroitPcServiceData() {
        return infoDroitPcServiceData;
    }

    public String getLimiteAnnuelleGrandeQd() {
        return limiteAnnuelleGrandeQd;
    }

    public String getMontantExcedentDeRevenu() {
        return montantExcedentDeRevenu;
    }

    public String getNssTiersBeneficiaire() {
        return nssTiersBeneficiaire;
    }

    public String getNumeroDecisionPc() {
        return numeroDecisionPc;
    }

    public List<RFAdaptationJournalierePeriodeQdData> getPeriodesQdExistantes() {
        return periodesQdExistantes;
    }

    public String getTypeDeDecisionPc() {
        return typeDeDecisionPc;
    }

    public String getTypeRemboursementConjoint() {
        return typeRemboursementConjoint;
    }

    public String getTypeRemboursementRequerant() {
        return typeRemboursementRequerant;
    }

    public boolean isHasQdsActuelle() {
        return hasQdsActuelle;
    }

    public void setDateDebutDecision(String dateDebutDecision) {
        this.dateDebutDecision = dateDebutDecision;
    }

    public void setDateDernierPmt(String dateDernierPmt) {
        this.dateDernierPmt = dateDernierPmt;
    }

    public void setDateFinDecision(String dateFinDecision) {
        this.dateFinDecision = dateFinDecision;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public void setHasQdsActuelle(boolean hasQdsActuelle) {
        this.hasQdsActuelle = hasQdsActuelle;
    }

    public void setIdAdaptationJournaliere(String idAdaptationJournaliere) {
        this.idAdaptationJournaliere = idAdaptationJournaliere;
    }

    public void setIdDecisionPc(String idDecisionPc) {
        this.idDecisionPc = idDecisionPc;
    }

    public void setIdPotGrandeQd(String idPotGrandeQd) {
        this.idPotGrandeQd = idPotGrandeQd;
    }

    /**
     * 
     * @param idsPrestationAccordeeEnCours
     *            String[id Rfm Prestation Accordee, id demande]
     */
    public void setIdsPrestationAccordeeEnCours(Set<String[]> idsPrestationAccordeeEnCours) {
        this.idsPrestationAccordeeEnCours = idsPrestationAccordeeEnCours;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        IdTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setInfoDroitPcServiceData(RFRetrieveInfoDroitPCServiceData infoDroitPcServiceData) {
        this.infoDroitPcServiceData = infoDroitPcServiceData;
    }

    public void setLimiteAnnuelleGrandeQd(String limiteAnnuelleGrandeQd) {
        this.limiteAnnuelleGrandeQd = limiteAnnuelleGrandeQd;
    }

    public void setMontantExcedentDeRevenu(String montantExcedentDeRevenu) {
        this.montantExcedentDeRevenu = montantExcedentDeRevenu;
    }

    public void setNssTiersBeneficiaire(String nssTiersBeneficiaire) {
        this.nssTiersBeneficiaire = nssTiersBeneficiaire;
    }

    public void setNumeroDecisionPc(String numeroDecisionPc) {
        this.numeroDecisionPc = numeroDecisionPc;
    }

    public void setPeriodesQdExistantes(List<RFAdaptationJournalierePeriodeQdData> periodesQdExistantes) {
        this.periodesQdExistantes = periodesQdExistantes;
    }

    public void setTypeDeDecisionPc(String typeDeDecisionPc) {
        this.typeDeDecisionPc = typeDeDecisionPc;
    }

    public void setTypeRemboursementConjoint(String typeRemboursementConjoint) {
        this.typeRemboursementConjoint = typeRemboursementConjoint;
    }

    public void setTypeRemboursementRequerant(String typeRemboursementRequerant) {
        this.typeRemboursementRequerant = typeRemboursementRequerant;
    }

}
