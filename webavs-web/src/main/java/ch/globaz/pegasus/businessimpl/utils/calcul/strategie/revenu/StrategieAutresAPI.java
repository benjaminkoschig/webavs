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
            addIsAPI_EncodeForAutresAPI(resultatExistant);
        } else {
            String cleRevenuAutresApi = null;

            if (IPCAutresAPI.CS_TYPE_AUTRES_API_ACCIDENT.equals(csType)) {
                cleRevenuAutresApi = IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAA;
                addIsAPI_EncodeForAutresAPI(resultatExistant);
            } else if (IPCAutresAPI.CS_TYPE_AUTRES_API_MILITAIRE.equals(csType)) {
                cleRevenuAutresApi = IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAM;
                addIsAPI_EncodeForAutresAPI(resultatExistant);
            } else {
                throw new CalculException("Invalid csTypeRevenu of AllocationPourImpotant : " + csType);
            }

            if (determineIfApiPrisEnCompte(donnee, context)) {
                this.getOrCreateChild(resultatExistant, cleRevenuAutresApi, montant);
            }
        }
        return resultatExistant;
    }

    /***
     * K160212_001 : ajout les "autres API" pour le calcul de la déduction sur les biens immobiliers
     * 
     * @param resultatExistant
     */
    private void addIsAPI_EncodeForAutresAPI(TupleDonneeRapport resultatExistant) {
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_IS_API_ENCODE, 1f);
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
