/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoAnnexe;

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
public class StrategieBienImmoAnnexe extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getBienImmoAnnexeCsTypePropriete())) {
            float fraction = Float.parseFloat(donnee.getBienImmoAnnexePartNumerateur())
                    / Float.parseFloat(donnee.getBienImmoAnnexePartDenominateur());

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantInteretHypothecaire()) * fraction);

        }

        return resultatExistant;
    }

}
