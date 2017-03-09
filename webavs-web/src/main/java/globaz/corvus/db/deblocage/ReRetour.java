package globaz.corvus.db.deblocage;

import ch.globaz.common.domaine.Montant;

public class ReRetour {

    private String idTiers;
    private String idRetour;
    private String libelle;
    private Montant montant;
    private String descriptionTiers;

    public String getDescriptionTiers() {
        return descriptionTiers;
    }

    public void setDescriptionTiers(String descriptionTiers) {
        this.descriptionTiers = descriptionTiers;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    @Override
    public String toString() {
        return "ReRetour [idTiers=" + idTiers + ", idRetour=" + idRetour + ", libelle=" + libelle + ", montant="
                + montant + ", descriptionTiers=" + descriptionTiers + "]";
    }

}
