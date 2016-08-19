/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul;

/**
 * @author ECO
 * 
 */
public class StrategieAllocationImpotent extends StrategieCalculRevenu {

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // gestion etat api encode
        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_IS_API_ENCODE, 1f);

        final float montant = checkAmoutAndParseAsFloat(donnee.getAPIAVSAIMontant()) * StrategieCalcul.NB_MOIS;

        if (determineIfApiPrisEnCompte(donnee, context)) {
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI, montant);
        }

        // collecte d'informations pour déterminer la déduction légale sur la
        // fortune nette
        final String role = donnee.getCsRoleFamille();

        String clePersonneRente = null;

        if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(role)) {
            clePersonneRente = IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT;
        } else if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(role)) {
            clePersonneRente = IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_CONJOINT;

        }
        TupleDonneeRapport nbRentesPrincADomTuple = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_INTER_NB_RENTES_PRINCIPALES_COUPLE_A_DOM, 1f);
        this.getOrCreateChild(nbRentesPrincADomTuple, donnee.getCsRoleFamille(), 1f);

        if (clePersonneRente != null) {
            String csTypeRente = donnee.getAPIAVSCsType();
            TupleDonneeRapport tuple = this.getOrCreateChild(resultatExistant, clePersonneRente, csTypeRente);

            // ajout du type de la rente du requérant dans le contexte de calcul afin de le propager dans les startégies
            // finales
            if (clePersonneRente.equals(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)) {
                context.put(Attribut.TYPE_RENTE_REQUERANT, csTypeRente);
            }

            tuple.setLegende(csTypeRente);
        }

        return resultatExistant;
    }

}
