package ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;

public class IjsAi extends DonneesFinancieresList<IjAi, IjsAi> {

    public IjsAi() {
        super(IjsAi.class);
    }

    public Montant sumRevenuAnnuel(final int nbDayInYear) {
        return sum(new Each<IjAi>() {

            @Override
            public Montant getMontant(IjAi donnneeFianciere) {
                return donnneeFianciere.computeRevenuAnnuel(nbDayInYear);
            }
        });
    }
}
