package ch.globaz.vulpecula.business.services.compteur;

import java.io.Serializable;

/**
 * Données serialisables et nécessaire pour l'écran decompte salaire
 * 
 * @since Web@BMS 0.01.02
 */
public class CompteurGSON implements Serializable {

    public double getMontantCompteur() {
        return montantCompteur;
    }

    public void setMontantCompteur(double montantCompteur) {
        this.montantCompteur = montantCompteur;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(double montantRestant) {
        this.montantRestant = montantRestant;
    }

    private double montantCompteur;
    private int annee;
    private double montantRestant;

}
