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
public class StrategieAutresRevenus extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getAutresRevenusMontant());
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String legende = donnee.getAutresRevenusLibelle();

        TupleDonneeRapport tupleAutreRevenu = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_REVENUS, 0f);
        TupleDonneeRapport tuple = this.getOrCreateChild(tupleAutreRevenu, String.valueOf(legende.hashCode()),
                donnee.getAutresRevenusMontant());
        tuple.setLegende(legende);
        return resultatExistant;
    }

}
