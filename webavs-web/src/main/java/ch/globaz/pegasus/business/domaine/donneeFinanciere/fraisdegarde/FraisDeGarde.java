package ch.globaz.pegasus.business.domaine.donneeFinanciere.fraisdegarde;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class FraisDeGarde extends DonneeFinanciere implements Depense {
    private final Montant montant;

    public FraisDeGarde(Montant montant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        // Si envoi montant annuel
        this.montant = montant.getMontantAbsolu().addMensuelPeriodicity();
        // Si envoi montant mensuel
        // this.montant = montant.getMontantAbsolu();
    }
    @Override
    public Montant computeDepense() {
        return montant;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.FRAIS_GARDE;
    }

    public Montant getMontant() {
        return montant;
    }
}
