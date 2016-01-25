package ch.globaz.vulpecula.web.views.taxationoffice;

/**
 * Représentation d'une ligne de cotisation pour son affichage dans un écran.
 * 
 * @author age
 * 
 */
public class LigneTaxationView {
    private String id;
    private String cotisation;
    private String masse;
    private String taux;
    private String montant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCotisation() {
        return cotisation;
    }

    public void setCotisation(String cotisation) {
        this.cotisation = cotisation;
    }

    public String getMasse() {
        return masse;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
