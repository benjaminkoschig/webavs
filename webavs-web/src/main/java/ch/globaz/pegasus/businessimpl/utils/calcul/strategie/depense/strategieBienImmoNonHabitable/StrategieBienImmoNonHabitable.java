/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoNonHabitable;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieCalculDepense;

/**
 * @author ECO
 * 
 */
public class StrategieBienImmoNonHabitable extends StrategieCalculDepense {

    /*
     * 
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeDepense
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getBienImmoNonHabitableCsTypePropriete())) {
            float fraction = Float.parseFloat(donnee.getBienImmoNonHabitablePartNumerateur())
                    / Float.parseFloat(donnee.getBienImmoNonHabitablePartDenominateur());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantInteretHypothecaire()) * fraction);
        }

        return resultatExistant;
    }

}
