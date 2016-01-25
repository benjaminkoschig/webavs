package ch.globaz.pegasus.business.vo.lot;

import ch.globaz.pegasus.business.constantes.EPCPMutationPassage;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;

public class MutationPcaVo implements Cloneable {
    private RecapDomainePca codeCategroriePca;
    private RecapDomainePca codeCategroriePcaPrecedante;
    private String csTypeDecision = null;
    private String csTypePreparationDecision;
    private String dateDebutPcaActuel;
    private String dateDebutPcaPrecedant;
    private String dateFinPcaActuel;
    private boolean hasDiminutation = false;
    private String idPca;
    private boolean isAugementationFutur = false;
    private boolean isCurrent = false;
    private String montantActuel;
    private String montantAllocationNoel;
    private String montantJourAppointActuel;
    private String montantJourAppointPrecedant;
    private String montantPrecedant;
    private String montantRetro;
    private String nom;
    private String noVersion;
    private String nss;
    private EPCPMutationPassage passage;
    private String prenom;
    private boolean purDiminution = false;
    private boolean purRetro = false;

    @Override
    public MutationPcaVo clone() {
        MutationPcaVo o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la
            // méthode super.clone()
            o = (MutationPcaVo) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

    public RecapDomainePca getCodeCategroriePca() {
        return codeCategroriePca;
    }

    public RecapDomainePca getCodeCategroriePcaPrecedante() {
        return codeCategroriePcaPrecedante;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getCsTypePreparationDecision() {
        return csTypePreparationDecision;
    }

    public String getDateDebutPcaActuel() {
        return dateDebutPcaActuel;
    }

    public String getDateDebutPcaPrecedant() {
        return dateDebutPcaPrecedant;
    }

    public String getDateFinPcaActuel() {
        return dateFinPcaActuel;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getMontantActuel() {
        return montantActuel;
    }

    public String getMontantAllocationNoel() {
        return montantAllocationNoel;
    }

    public String getMontantJourAppointActuel() {
        return montantJourAppointActuel;
    }

    public String getMontantJourAppointPrecedant() {
        return montantJourAppointPrecedant;
    }

    public String getMontantPrecedant() {
        return montantPrecedant;
    }

    public String getMontantRetro() {
        return montantRetro;
    }

    public String getNom() {
        return nom;
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getNss() {
        return nss;
    }

    public EPCPMutationPassage getPassage() {
        return passage;
    }

    public String getPrenom() {
        return prenom;
    }

    public boolean isAugementationFutur() {
        return isAugementationFutur;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public boolean isHasDiminutation() {
        return hasDiminutation;
    }

    public boolean isPurDiminution() {
        return purDiminution;
    }

    public boolean isPurRetro() {
        return purRetro;
    }

    public void setAugementationFutur(boolean isAugementationFutur) {
        this.isAugementationFutur = isAugementationFutur;
    }

    public void setCodeCategroriePca(RecapDomainePca codeCategroriePca) {
        this.codeCategroriePca = codeCategroriePca;
    }

    public void setCodeCategroriePcaPrecedante(RecapDomainePca codeCategroriePcaPrecedante) {
        this.codeCategroriePcaPrecedante = codeCategroriePcaPrecedante;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setCsTypePreparationDecision(String csTypePreparationDecision) {
        this.csTypePreparationDecision = csTypePreparationDecision;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public void setDateDebutPcaActuel(String dateDebutPcaActuel) {
        this.dateDebutPcaActuel = dateDebutPcaActuel;
    }

    public void setDateDebutPcaPrecedant(String dateDebutPcaPrecedant) {
        this.dateDebutPcaPrecedant = dateDebutPcaPrecedant;
    }

    public void setDateFinPcaActuel(String dateFinPcaActuel) {
        this.dateFinPcaActuel = dateFinPcaActuel;
    }

    public void setHasDiminutation(boolean hasDiminutation) {
        this.hasDiminutation = hasDiminutation;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setMontantActuel(String montantActuel) {
        this.montantActuel = montantActuel;
    }

    public void setMontantAllocationNoel(String montantAllocationNoel) {
        this.montantAllocationNoel = montantAllocationNoel;
    }

    public void setMontantJourAppointActuel(String montantJourAppointActuel) {
        this.montantJourAppointActuel = montantJourAppointActuel;
    }

    public void setMontantJourAppointPrecedant(String montantJourAppointPrecedant) {
        this.montantJourAppointPrecedant = montantJourAppointPrecedant;
    }

    public void setMontantPrecedant(String ancienMontant) {
        montantPrecedant = ancienMontant;
    }

    public void setMontantRetro(String montantRetro) {
        this.montantRetro = montantRetro;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPassage(EPCPMutationPassage passage) {
        this.passage = passage;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPurDiminution(boolean purDiminution) {
        this.purDiminution = purDiminution;
    }

    public void setPurRetro(boolean purRetro) {
        this.purRetro = purRetro;
    }

    @Override
    public String toString() {
        return "No Version: " + noVersion + ", montantActeul: " + montantActuel + " periode: " + dateDebutPcaActuel
                + "-" + dateFinPcaActuel;
    }

}
