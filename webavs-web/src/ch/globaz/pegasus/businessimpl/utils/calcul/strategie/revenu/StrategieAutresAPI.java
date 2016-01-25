/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCAutresAPI;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieAutresAPI extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getAutresAPIMontant());
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String montant = checkAmount(donnee.getAutresAPIMontant());
        String csType = donnee.getAutresAPICsTypeMontant();

        if (IPCAutresAPI.CS_TYPE_AUTRES_API_AUTRE.equals(csType)) {
            if (determineIfApiPrisEnCompte(donnee, context)) {
                dealTypeApiAutre(donnee, resultatExistant, montant);
            }
        } else {
            String cleRevenuAutresApi = null;

            if (IPCAutresAPI.CS_TYPE_AUTRES_API_ACCIDENT.equals(csType)) {
                cleRevenuAutresApi = IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAA;
            } else if (IPCAutresAPI.CS_TYPE_AUTRES_API_MILITAIRE.equals(csType)) {
                cleRevenuAutresApi = IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAM;
            } else {
                throw new CalculException("Invalid csTypeRevenu of AllocationPourImpotant : " + csType);
            }

            if (determineIfApiPrisEnCompte(donnee, context)) {
                this.getOrCreateChild(resultatExistant, cleRevenuAutresApi, montant);
            }

        }
        return resultatExistant;
    }

    /**
     * Gestion des api de type autre standard
     * 
     */
    private void dealTypeApiAutre(CalculDonneesCC donnee, TupleDonneeRapport resultatExistant, String montant)
            throws CalculException {
        String legende = donnee.getAutresApiAutre();

        if (legende == null) {
            throw new CalculException("Field AutresApiAutre should be not null!");
        }

        TupleDonneeRapport tupleAutresAPI = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AUTRE_API, 0f);
        TupleDonneeRapport tuple = this.getOrCreateChild(tupleAutresAPI, String.valueOf(legende.hashCode()), montant);
        tuple.setLegende(legende);
    }

}
