package globaz.osiris.db.ventilation;

import java.math.BigDecimal;

public class CAVPDetailSection {
    private String date = "";
    private String idRubrique = "";
    private String libelle = "";
    private BigDecimal montant;
    private BigDecimal montantEmployeur;
    private BigDecimal montantSalarie;

    private boolean montantSimple = true;
    private boolean ventile = true;

    public CAVPDetailSection() {

    }

    public String getDate() {
        return date;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getLibelle() {
        return libelle;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public BigDecimal getMontantEmployeur() {
        return montantEmployeur;
    }

    public BigDecimal getMontantSalarie() {
        return montantSalarie;
    }

    public boolean isMontantSimple() {
        return montantSimple;
    }

    public boolean isVentile() {
        return ventile;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setMontantEmployeur(BigDecimal montantEmployeur) {
        this.montantEmployeur = montantEmployeur;
    }

    public void setMontantSalarie(BigDecimal montantSalarie) {
        this.montantSalarie = montantSalarie;
    }

    public void setMontantSimple(boolean montantSimple) {
        this.montantSimple = montantSimple;
    }

    public void setVentile(boolean ventile) {
        this.ventile = ventile;
    }
}
