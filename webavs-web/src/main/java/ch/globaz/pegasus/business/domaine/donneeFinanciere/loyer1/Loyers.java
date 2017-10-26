package ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;

public class Loyers extends DonneesFinancieresList<Loyer, Loyers> {

    public Loyers() {
        super(Loyers.class);
    }

    public Montant sumLoyerBrut(final Montant forfait) {
        return sum(new Each<Loyer>() {

            @Override
            public Montant getMontant(Loyer donnneeFianciere) {
                return donnneeFianciere.getMontant().annualise().add(donnneeFianciere.computeCharge(forfait));
            }
        });
    }

    public Montant sumSousLocation() {
        return sum(new Each<Loyer>() {

            @Override
            public Montant getMontant(Loyer donnneeFianciere) {
                return donnneeFianciere.getSousLocation().annualise();
            }
        });
    }
}
