/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.fortune;

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
public class StrategieBienImmoNonHabitable extends StrategieCalculFortune implements IStrategieDessaisissable {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeFortune
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isUsufruit(donnee.getBienImmoNonHabitableCsTypePropriete())
                && !isNuProprietaire(donnee.getBienImmoNonHabitableCsTypePropriete())) {
            float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitablePartNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitablePartDenominateur());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantValeurVenale()) * fraction);
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantDetteHypothecaire()) * fraction);
            if(context.contains(CalculContext.Attribut.REFORME)) {
//                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED,
//                        checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantDetteHypothecaire()) * fraction);

                // Si la valeur du bien est inférieur à sa valeur hypothécaire, on ne tient pas compte de la valeur hypothécaire.
                // Exigence 4.15
                if (checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantDetteHypothecaire()) * fraction < checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantValeurVenale()) * fraction) {
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED, checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantDetteHypothecaire()) * fraction);
                } else {
                    this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED,
                            0.0f);
                }
            }
        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitablePartNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitablePartDenominateur());

        return (checkAmoutAndParseAsFloat(donnee.getBienImmoNonHabitableMontantValeurVenale()) - checkAmoutAndParseAsFloat(donnee
                .getBienImmoNonHabitableMontantDetteHypothecaire())) * fraction;
    }
}
