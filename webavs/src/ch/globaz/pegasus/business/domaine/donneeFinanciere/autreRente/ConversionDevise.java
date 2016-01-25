package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;

public class ConversionDevise {

    MonnaiesEtrangere monnaiesEtrangere;

    public ConversionDevise(MonnaiesEtrangere monnaiesEtrangere) {
        this.monnaiesEtrangere = monnaiesEtrangere;
    }

    public Montant compute(AutreRente autreRente, Date dateValidite) {
        if (autreRente.mustConvertToFrancSuisse()) {
            MonnaieEtrangere monnaieEtrangere = resolveMonnaie(autreRente).resolveCourant(dateValidite);
            return compute(autreRente, monnaieEtrangere);
        } else {
            return autreRente.getMontant();
        }
    }

    public Montant compute(AutreRente autreRente) {
        if (autreRente.mustConvertToFrancSuisse()) {
            MonnaieEtrangere monnaieEtrangere = resolveMonnaie(autreRente).resolveMostRecent();
            return compute(autreRente, monnaieEtrangere);
        } else {
            return autreRente.getMontant();
        }
    }

    private MonnaiesEtrangere resolveMonnaie(AutreRente autreRente) {
        return monnaiesEtrangere.getParameters(autreRente.getMonnaieType());
    }

    Montant compute(AutreRente autreRente, MonnaieEtrangere monnaieEtrangere) {
        Montant montant = autreRente.getMontant();
        if (autreRente.mustConvertToFrancSuisse()) {
            montant = montant.multiply(monnaieEtrangere.getTaux().getBigDecimal());
        }
        return montant;
    }

}
