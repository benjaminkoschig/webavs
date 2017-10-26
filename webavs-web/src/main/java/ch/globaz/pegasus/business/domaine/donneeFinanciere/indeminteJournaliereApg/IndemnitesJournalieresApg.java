package ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;

public class IndemnitesJournalieresApg extends
        DonneesFinancieresList<IndemniteJournaliereApg, IndemnitesJournalieresApg> {

    public IndemnitesJournalieresApg() {
        super(IndemnitesJournalieresApg.class);
    }

    public Montant sumRevenuAnnuel(final int nbDayInYear) {
        return sum(new Each<IndemniteJournaliereApg>() {

            @Override
            public Montant getMontant(IndemniteJournaliereApg donnneeFianciere) {
                return donnneeFianciere.computeRevenuAnnuel(nbDayInYear);
            }
        });
    }
}
