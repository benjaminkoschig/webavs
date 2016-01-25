package ch.globaz.pegasus.business.models.dessaisissement;

/**
 * Classe container faisant office de mediateur pour le calcul de la contre prestation d'un dessaisissement de fortune.
 * 
 * @author ECO
 * 
 */
public class CalculContreprestationContainer {
    private String charges = null;
    /**
     * Date de début de la donnée financière de fortune
     */
    private String dateDebut = null;
    /**
     * Date de fin de la donnée financière de fortune
     */
    private String dateFin = null;
    /**
     * Date de naissance du proprietaire de la fortune dessaisie. Ce champ est initialisé automatiquement durant le
     * calcul.
     */
    private String dateNaissance = null;
    private String dateValeur = null;

    private String deductionMontantDessaisi = null;

    /**
     * id du droit dont l'objet de fortune fait partie.
     */
    private String idDroit = null;
    private String montantBrut = null;
    private String rendementFortune = null;

    public String getCharges() {
        return charges;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getDeductionMontantDessaisi() {
        return deductionMontantDessaisi;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getMontantBrut() {
        return montantBrut;
    }

    public String getRendementFortune() {
        return rendementFortune;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setDeductionMontantDessaisi(String deductionMontantDessaisi) {
        this.deductionMontantDessaisi = deductionMontantDessaisi;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setRendementFortune(String rendementFortune) {
        this.rendementFortune = rendementFortune;
    }
}
