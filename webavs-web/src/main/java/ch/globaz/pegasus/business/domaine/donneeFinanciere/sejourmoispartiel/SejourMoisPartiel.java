package ch.globaz.pegasus.business.domaine.donneeFinanciere.sejourmoispartiel;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

import java.util.Objects;

public class SejourMoisPartiel extends DonneeFinanciere implements Depense {
    private final Montant prixJournalier;
    private final Montant fraisNourriture;
    private final Montant nombreJours;

    public SejourMoisPartiel(Montant prixJournalier, Montant fraisNourriture, Montant nombreJours, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.prixJournalier = prixJournalier.getMontantAbsolu();
        this.fraisNourriture = fraisNourriture.getMontantAbsolu();
        this.nombreJours = nombreJours.getMontantAbsolu();
    }
    @Override
    public Montant computeDepense() {
        return prixJournalier.substract(fraisNourriture).multiply(nombreJours).addMensuelPeriodicity().annualise();
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.SEJOUR_MOIS_PARTIEL_HOME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SejourMoisPartiel that = (SejourMoisPartiel) o;
        return Objects.equals(prixJournalier, that.prixJournalier) &&
                Objects.equals(fraisNourriture, that.fraisNourriture) &&
                Objects.equals(nombreJours, that.nombreJours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), prixJournalier, fraisNourriture, nombreJours);
    }

    @Override
    public String toString() {
        return "SejourMoisPartiel{" +
                "prixJournalier=" + prixJournalier +
                ", fraisNourriture=" + fraisNourriture +
                ", nombreJours=" + nombreJours +
                '}';
    }
}
