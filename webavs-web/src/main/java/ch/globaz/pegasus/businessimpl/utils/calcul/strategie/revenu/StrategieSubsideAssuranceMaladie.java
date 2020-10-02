/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 * 
 */
public class StrategieSubsideAssuranceMaladie extends StrategieCalculRevenu {

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if(context.contains(CalculContext.Attribut.REFORME) && !donnee.getSubsideAssuranceMaladieMontant().isEmpty()){
            float montantAnnuel = checkAmoutAndParseAsFloat(donnee.getSubsideAssuranceMaladieMontant()) * 12;
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_SUBSIDE_ASSURANCE_MALADIE_TOTAL, montantAnnuel);
        }

        return resultatExistant;
    }
}
