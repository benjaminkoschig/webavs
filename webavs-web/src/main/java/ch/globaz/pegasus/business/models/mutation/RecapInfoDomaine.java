package ch.globaz.pegasus.business.models.mutation;

import java.math.BigDecimal;

/**
 * Donne tout le résumé d'un domaine
 * 
 * @author dma
 * 
 */
public class RecapInfoDomaine {

    private BigDecimal adaptation = new BigDecimal(0);
    private BigDecimal augmentation = new BigDecimal(0);
    private BigDecimal augmentationFuture = new BigDecimal(0);
    private BigDecimal difference = new BigDecimal(0);
    private BigDecimal diminutationFuture = new BigDecimal(0);
    private BigDecimal diminution = new BigDecimal(0);
    private int nbDossierAncien;
    private int nbDossierNouveau;
    private BigDecimal presationAncien = new BigDecimal(0);
    private BigDecimal presationNouveau = new BigDecimal(0);
    private BigDecimal retro = new BigDecimal(0);
    private BigDecimal sousTotalDimAug = new BigDecimal(0);
    private BigDecimal totalAllocationNoel = new BigDecimal(0);
    private BigDecimal totalJoursAppoint = new BigDecimal(0);
    private BigDecimal totalMutation = new BigDecimal(0);
    private BigDecimal totalPaiement = new BigDecimal(0);
    private BigDecimal versementAncien = new BigDecimal(0);
    private BigDecimal versementNouveau = new BigDecimal(0);

    public BigDecimal getAdaptation() {
        return adaptation;
    }

    public BigDecimal getAugmentation() {
        return augmentation;
    }

    public BigDecimal getAugmentationFuture() {
        return augmentationFuture;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public BigDecimal getDiminutationFuture() {
        return diminutationFuture;
    }

    public BigDecimal getDiminution() {
        return diminution;
    }

    public int getNbDossierAncien() {
        return nbDossierAncien;
    }

    public int getNbDossierNouveau() {
        return nbDossierNouveau;
    }

    public BigDecimal getPresationAncien() {
        return presationAncien;
    }

    public BigDecimal getPresationNouveau() {
        return presationNouveau;
    }

    public BigDecimal getRetro() {
        return retro;
    }

    public BigDecimal getSousTotalDimAug() {
        return sousTotalDimAug;
    }

    public BigDecimal getTotalAllocationNoel() {
        return totalAllocationNoel;
    }

    public BigDecimal getTotalJoursAppoint() {
        return totalJoursAppoint;
    }

    public BigDecimal getTotalMutation() {
        return totalMutation;
    }

    public BigDecimal getTotalPaiement() {
        return totalPaiement;
    }

    public BigDecimal getVersementAncien() {
        return versementAncien;
    }

    public BigDecimal getVersementNouveau() {
        return versementNouveau;
    }

    public void setAdaptation(BigDecimal adaptation) {
        this.adaptation = adaptation;
    }

    public void setAugmentation(BigDecimal augmentation) {
        this.augmentation = augmentation;
    }

    public void setAugmentationFuture(BigDecimal augmentationFuture) {
        this.augmentationFuture = augmentationFuture;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public void setDiminutationFuture(BigDecimal diminutationFuture) {
        this.diminutationFuture = diminutationFuture;
    }

    public void setDiminution(BigDecimal diminution) {
        this.diminution = diminution;
    }

    public void setNbDossierAncien(int nbDossierAncien) {
        this.nbDossierAncien = nbDossierAncien;
    }

    public void setNbDossierNouveau(int nbDossierNouveau) {
        this.nbDossierNouveau = nbDossierNouveau;
    }

    public void setPresationAncien(BigDecimal presationAncien) {
        this.presationAncien = presationAncien;
    }

    public void setPresationNouveau(BigDecimal presationNouveau) {
        this.presationNouveau = presationNouveau;
    }

    public void setRetro(BigDecimal retro) {
        this.retro = retro;
    }

    public void setSousTotalDimAug(BigDecimal sousTotalDimAug) {
        this.sousTotalDimAug = sousTotalDimAug;
    }

    public void setTotalAllocationNoel(BigDecimal totalAllocationNoel) {
        this.totalAllocationNoel = totalAllocationNoel;
    }

    public void setTotalJoursAppoint(BigDecimal totalJoursAppoint) {
        this.totalJoursAppoint = totalJoursAppoint;
    }

    public void setTotalMutation(BigDecimal totalMutation) {
        this.totalMutation = totalMutation;
    }

    public void setTotalPaiement(BigDecimal totalPaiement) {
        this.totalPaiement = totalPaiement;
    }

    public void setVersementAncien(BigDecimal versementAncien) {
        this.versementAncien = versementAncien;
    }

    public void setVersementNouveau(BigDecimal versementNouveau) {
        this.versementNouveau = versementNouveau;
    }
}
