/**
 *
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 *
 */
public class StrategiePrimeAssuranceMaladie extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
                                                TupleDonneeRapport resultatExistant) throws CalculException {
        if (context.contains(CalculContext.Attribut.REFORME) && !donnee.getPrimeAssuranceMaladieMontant().isEmpty()) {

            float montantAnnuel = checkAmoutAndParseAsFloat(donnee.getPrimeAssuranceMaladieMontant()) * 12;

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL, montantAnnuel);

        }

        return resultatExistant;
    }
}
