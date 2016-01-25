package ch.globaz.pegasus.business.domaine.donneeFinanciere.taxeJournalierHome;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class TaxeJournaliereHome extends DonneeFinanciere {
    private final Montant montantJournalierLca;
    private final Montant primeAPayer;
    private final boolean participationLca;
    private final Date dateEntreeHome;

    public TaxeJournaliereHome(Montant montantJournalierLca, Montant primeAPayer, boolean participationLca,
            Date dateEntreeHome, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.dateEntreeHome = dateEntreeHome;
        this.participationLca = participationLca;

        this.montantJournalierLca = montantJournalierLca.addJournalierPeriodicity();
        this.primeAPayer = primeAPayer.addMensuelPeriodicity();
    }

    public Montant getMontantJournalierLca() {
        return montantJournalierLca;
    }

    public Montant getPrimeAPayer() {
        return primeAPayer;
    }

    public boolean isParticipationLca() {
        return participationLca;
    }

    public Date getDateEntreeHome() {
        return dateEntreeHome;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.TAXE_JOURNALIERE_HOME;
    }

    @Override
    public String toString() {
        return "TaxeJournaliereHome [montantJournalierLca=" + montantJournalierLca + ", primeAPayer=" + primeAPayer
                + ", participationLca=" + participationLca + ", dateEntreeHome=" + dateEntreeHome + ", parent="
                + super.toString() + "]";
    }

}
