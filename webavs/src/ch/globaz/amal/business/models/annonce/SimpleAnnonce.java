/**
 * 
 */
package ch.globaz.amal.business.models.annonce;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleAnnonce extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String ancienMontantContributionAnnuelle = null;
    private String anneeHistorique = null;
    private String anneeRecalcul = null;
    private Boolean annonceCaisseMaladie = null;
    private Boolean codeActif = null;
    private Boolean codeForcer = null;
    private String codeTraitement = null;
    private String codeTraitementDossier = null;
    private String dateAvisRIP = null;
    private String dateEnvoiAnnonce = null;
    private String dateModification = null;
    private String dateReceptionDemande = null;
    private String debutDroit = null;
    private String finDroit = null;
    private String idDetailAnnonce = null;
    private String idDetailFamille = null;
    private String montantContribution = null;
    private String montantContributionAnnuelle = null;
    private String montantExact = null;
    private String montantPrime = null;
    private String noAssure = null;
    private String noCaisseMaladie = null;
    private String noModele = null;
    private String numeroLot = null;
    private Boolean refuse = null;
    private String supplementExtraordinaire = null;
    private String tauxEnfantCharge = null;
    private String typeAvisRIP = null;
    private String typeDemande = null;

    /**
     * @return the ancienMontantContributionAnnuelle
     */
    public String getAncienMontantContributionAnnuelle() {
        return ancienMontantContributionAnnuelle;
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the anneeRecalcul
     */
    public String getAnneeRecalcul() {
        return anneeRecalcul;
    }

    /**
     * @return the annonceCaisseMaladie
     */
    public Boolean getAnnonceCaisseMaladie() {
        return annonceCaisseMaladie;
    }

    /**
     * @return the codeActif
     */
    public Boolean getCodeActif() {
        return codeActif;
    }

    /**
     * @return the codeForcer
     */
    public Boolean getCodeForcer() {
        return codeForcer;
    }

    /**
     * @return the codeTraitement
     */
    public String getCodeTraitement() {
        return codeTraitement;
    }

    /**
     * @return the codeTraitementDossier
     */
    public String getCodeTraitementDossier() {
        return codeTraitementDossier;
    }

    /**
     * @return the dateAvisRIP
     */
    public String getDateAvisRIP() {
        return dateAvisRIP;
    }

    /**
     * @return the dateEnvoiAnnonce
     */
    public String getDateEnvoiAnnonce() {
        return dateEnvoiAnnonce;
    }

    /**
     * @return the dateModification
     */
    public String getDateModification() {
        return dateModification;
    }

    /**
     * @return the dateReceptionDemande
     */
    public String getDateReceptionDemande() {
        return dateReceptionDemande;
    }

    /**
     * @return the debutDroit
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * @return the finDroit
     */
    public String getFinDroit() {
        return finDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDetailAnnonce;
    }

    /**
     * @return the idDetailAnnonce
     */
    public String getIdDetailAnnonce() {
        return idDetailAnnonce;
    }

    /**
     * @return the idDetailFamille
     */
    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    /**
     * @return the montantContribution
     */
    public String getMontantContribution() {
        return montantContribution;
    }

    /**
     * @return the montantContributionAnnuelle
     */
    public String getMontantContributionAnnuelle() {
        return montantContributionAnnuelle;
    }

    /**
     * @return the montantExact
     */
    public String getMontantExact() {
        return montantExact;
    }

    /**
     * @return the montantPrime
     */
    public String getMontantPrime() {
        return montantPrime;
    }

    /**
     * @return the noAssure
     */
    public String getNoAssure() {
        return noAssure;
    }

    /**
     * @return the noCaisseMaladie
     */
    public String getNoCaisseMaladie() {
        return noCaisseMaladie;
    }

    /**
     * @return the noModele
     */
    public String getNoModele() {
        return noModele;
    }

    /**
     * @return the numeroLot
     */
    public String getNumeroLot() {
        return numeroLot;
    }

    /**
     * @return the refuse
     */
    public Boolean getRefuse() {
        return refuse;
    }

    /**
     * @return the supplementExtraordinaire
     */
    public String getSupplementExtraordinaire() {
        return supplementExtraordinaire;
    }

    /**
     * @return the tauxEnfantCharge
     */
    public String getTauxEnfantCharge() {
        return tauxEnfantCharge;
    }

    /**
     * @return the typeAvisRIP
     */
    public String getTypeAvisRIP() {
        return typeAvisRIP;
    }

    /**
     * @return the typeDemande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * @param ancienMontantContributionAnnuelle
     *            the ancienMontantContributionAnnuelle to set
     */
    public void setAncienMontantContributionAnnuelle(String ancienMontantContributionAnnuelle) {
        this.ancienMontantContributionAnnuelle = ancienMontantContributionAnnuelle;
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param anneeRecalcul
     *            the anneeRecalcul to set
     */
    public void setAnneeRecalcul(String anneeRecalcul) {
        this.anneeRecalcul = anneeRecalcul;
    }

    /**
     * @param annonceCaisseMaladie
     *            the annonceCaisseMaladie to set
     */
    public void setAnnonceCaisseMaladie(Boolean annonceCaisseMaladie) {
        this.annonceCaisseMaladie = annonceCaisseMaladie;
    }

    /**
     * @param codeActif
     *            the codeActif to set
     */
    public void setCodeActif(Boolean codeActif) {
        this.codeActif = codeActif;
    }

    /**
     * @param codeForcer
     *            the codeForcer to set
     */
    public void setCodeForcer(Boolean codeForcer) {
        this.codeForcer = codeForcer;
    }

    /**
     * @param codeTraitement
     *            the codeTraitement to set
     */
    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    /**
     * @param codeTraitementDossier
     *            the codeTraitementDossier to set
     */
    public void setCodeTraitementDossier(String codeTraitementDossier) {
        this.codeTraitementDossier = codeTraitementDossier;
    }

    /**
     * @param dateAvisRIP
     *            the dateAvisRIP to set
     */
    public void setDateAvisRIP(String dateAvisRIP) {
        this.dateAvisRIP = dateAvisRIP;
    }

    /**
     * @param dateEnvoiAnnonce
     *            the dateEnvoiAnnonce to set
     */
    public void setDateEnvoiAnnonce(String dateEnvoiAnnonce) {
        this.dateEnvoiAnnonce = dateEnvoiAnnonce;
    }

    /**
     * @param dateModification
     *            the dateModification to set
     */
    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    /**
     * @param dateReceptionDemande
     *            the dateReceptionDemande to set
     */
    public void setDateReceptionDemande(String dateReceptionDemande) {
        this.dateReceptionDemande = dateReceptionDemande;
    }

    /**
     * @param debutDroit
     *            the debutDroit to set
     */
    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    /**
     * @param finDroit
     *            the finDroit to set
     */
    public void setFinDroit(String finDroit) {
        this.finDroit = finDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDetailAnnonce = id;
    }

    /**
     * @param idDetailAnnonce
     *            the idDetailAnnonce to set
     */
    public void setIdDetailAnnonce(String idDetailAnnonce) {
        this.idDetailAnnonce = idDetailAnnonce;
    }

    /**
     * @param idDetailFamille
     *            the idDetailFamille to set
     */
    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

    /**
     * @param montantContribution
     *            the montantContribution to set
     */
    public void setMontantContribution(String montantContribution) {
        this.montantContribution = montantContribution;
    }

    /**
     * @param montantContributionAnnuelle
     *            the montantContributionAnnuelle to set
     */
    public void setMontantContributionAnnuelle(String montantContributionAnnuelle) {
        this.montantContributionAnnuelle = montantContributionAnnuelle;
    }

    /**
     * @param montantExact
     *            the montantExact to set
     */
    public void setMontantExact(String montantExact) {
        this.montantExact = montantExact;
    }

    /**
     * @param montantPrime
     *            the montantPrime to set
     */
    public void setMontantPrime(String montantPrime) {
        this.montantPrime = montantPrime;
    }

    /**
     * @param noAssure
     *            the noAssure to set
     */
    public void setNoAssure(String noAssure) {
        this.noAssure = noAssure;
    }

    /**
     * @param noCaisseMaladie
     *            the noCaisseMaladie to set
     */
    public void setNoCaisseMaladie(String noCaisseMaladie) {
        this.noCaisseMaladie = noCaisseMaladie;
    }

    /**
     * @param noModele
     *            the noModele to set
     */
    public void setNoModele(String noModele) {
        this.noModele = noModele;
    }

    /**
     * @param numeroLot
     *            the numeroLot to set
     */
    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    /**
     * @param refuse
     *            the refuse to set
     */
    public void setRefuse(Boolean refuse) {
        this.refuse = refuse;
    }

    /**
     * @param supplementExtraordinaire
     *            the supplementExtraordinaire to set
     */
    public void setSupplementExtraordinaire(String supplementExtraordinaire) {
        this.supplementExtraordinaire = supplementExtraordinaire;
    }

    /**
     * @param tauxEnfantCharge
     *            the tauxEnfantCharge to set
     */
    public void setTauxEnfantCharge(String tauxEnfantCharge) {
        this.tauxEnfantCharge = tauxEnfantCharge;
    }

    /**
     * @param typeAvisRIP
     *            the typeAvisRIP to set
     */
    public void setTypeAvisRIP(String typeAvisRIP) {
        this.typeAvisRIP = typeAvisRIP;
    }

    /**
     * @param typeDemande
     *            the typeDemande to set
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
