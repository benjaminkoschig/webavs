/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieBienImmoNonHabitable extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getBienImmoNonHabitableCsTypePropriete())) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES,
                    getMontant(donnee));
        }

        return resultatExistant;
    }

    public float getMontant(CalculDonneesCC donnee) {
        float fraction = Float.parseFloat(donnee.getBienImmoNonHabitablePartNumerateur())
                / Float.parseFloat(donnee.getBienImmoNonHabitablePartDenominateur());
        return checkAmountAndParseAsFloat(donnee.getBienImmoNonHabitableMontantRendement()) * fraction;
    }

}
