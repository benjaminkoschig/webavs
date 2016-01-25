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
public class StrategieVehicule extends StrategieCalculFortune implements IStrategieDessaisissable {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeFortune
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeFortune(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        if (!isUsufruit(donnee.getVehiculeCsTypePropriete()) && !isNuProprietaire(donnee.getVehiculeCsTypePropriete())) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_VEHICULES,
                    getMontant(donnee));
        }

        return resultatExistant;
    }

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontant(donnee);
    }

    public Float getMontant(CalculDonneesCC donnee) throws CalculException {
        float fraction = checkAmoutAndParseAsFloat(donnee.getVehiculeFractionNumerateur())
                / checkAmoutAndParseAsFloat(donnee.getVehiculeFractionDenominateur());

        return checkAmoutAndParseAsFloat(donnee.getVehiculeMontant()) * fraction;
    }

}
