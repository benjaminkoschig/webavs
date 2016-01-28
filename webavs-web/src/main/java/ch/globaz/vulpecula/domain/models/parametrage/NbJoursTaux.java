package ch.globaz.vulpecula.domain.models.parametrage;

/**
 * Représente le couple nbJours de vacances accordés pour un taux.
 */
public class NbJoursTaux {
    private final int nbJours;
    private final double taux;

    public NbJoursTaux(int nbJours, double taux) {
        this.nbJours = nbJours;
        this.taux = taux;
    }

    public int getNbJours() {
        return nbJours;
    }

    public double getTaux() {
        return taux;
    }
}
