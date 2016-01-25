package ch.globaz.pegasus.business.domaine.donneeFinanciere.regime;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class Regime extends DonneeFinanciere implements Revenu {

    private Montant montant;
    private String sousTypeRegime;

    public Regime(Montant montant, String sousTypeRegime, DonneeFinanciere df) {
        super(df);
        this.montant = montant.addMensuelPeriodicity();
        this.sousTypeRegime = sousTypeRegime;
    }

    public Regime() {

    }

    public Montant getMontant() {
        return montant;
    }

    public String getSousTypeRegime() {
        return sousTypeRegime;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.UNDIFINED;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.annualise();
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant.annualise();
    }

}
