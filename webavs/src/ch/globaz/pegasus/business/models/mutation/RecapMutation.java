package ch.globaz.pegasus.business.models.mutation;

import java.math.BigDecimal;
import ch.globaz.pegasus.business.constantes.EPCPMutationPassage;
import ch.globaz.pegasus.business.models.recap.MutationType;

public class RecapMutation implements Cloneable {
    private BigDecimal allocationNoel = new BigDecimal(0);
    private BigDecimal augmentation;
    private BigDecimal dimminution;
    private BigDecimal joursAppoint = new BigDecimal(0);
    private Boolean newDroit = false;
    private String nom;
    private String nss;
    private EPCPMutationPassage passage;
    private String Periode;
    private String prenom;
    private BigDecimal retro;
    private MutationType typeDeMutation;

    @Override
    public RecapMutation clone() {
        RecapMutation o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la
            // méthode super.clone()
            o = (RecapMutation) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

    public BigDecimal getAllocationNoel() {
        return allocationNoel;
    }

    public BigDecimal getAugmentation() {
        return augmentation;
    }

    public BigDecimal getDimminution() {
        return dimminution;
    }

    public BigDecimal getJoursAppoint() {
        return joursAppoint;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public EPCPMutationPassage getPassage() {
        return passage;
    }

    public String getPeriode() {
        return Periode;
    }

    public String getPrenom() {
        return prenom;
    }

    public BigDecimal getRetro() {
        return retro;
    }

    public MutationType getTypeDeMutation() {
        return typeDeMutation;
    }

    public Boolean isNewDroit() {
        return newDroit;
    }

    public void setAllocationNoel(BigDecimal allocationNoel) {
        this.allocationNoel = allocationNoel;
    }

    public void setAugmentation(BigDecimal augmentation) {
        this.augmentation = augmentation;
    }

    public void setDimminution(BigDecimal dimminution) {
        this.dimminution = dimminution;
    }

    public void setJoursAppoint(BigDecimal joursAppoint) {
        this.joursAppoint = joursAppoint;
    }

    public void setNewDroit(Boolean newDroit) {
        this.newDroit = newDroit;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPassage(EPCPMutationPassage passage) {
        this.passage = passage;
    }

    public void setPeriode(String periode) {
        Periode = periode;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRetro(BigDecimal retro) {
        this.retro = retro;
    }

    public void setTypeDeMutation(MutationType typeDeMutation) {
        this.typeDeMutation = typeDeMutation;
    }
}
