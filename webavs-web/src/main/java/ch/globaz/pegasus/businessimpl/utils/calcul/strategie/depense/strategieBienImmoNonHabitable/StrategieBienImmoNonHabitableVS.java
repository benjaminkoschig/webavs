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
public class StrategieBienImmoNonHabitableVS extends StrategieCalculDepense {

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
            float fractionPart = Float.parseFloat(donnee.getBienImmoNonHabitablePartNumerateur())
                    / Float.parseFloat(donnee.getBienImmoNonHabitablePartDenominateur());

            float montantRendements = checkAmountAndParseAsFloat(donnee.getBienImmoNonHabitableMontantRendement());

            float montantsInteretsHypothecaires = checkAmountAndParseAsFloat(donnee
                    .getBienImmoNonHabitableMontantInteretHypothecaire()) * fractionPart;

            float montanAdmis;

            if (montantsInteretsHypothecaires > montantRendements) {
                montanAdmis = montantRendements;
            } else {
                montanAdmis = montantsInteretsHypothecaires;
            }

            // ajout des interet
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                    montantsInteretsHypothecaires);

            // ajout des montantadmis
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE, montanAdmis);

        }

        return resultatExistant;
    }

}
