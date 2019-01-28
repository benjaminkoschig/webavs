/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import java.util.Arrays;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 * 
 */
public class StrategieRenteAVSAI extends StrategieCalculRevenu {

    private final List<String> rentesCoupleDomicile = Arrays.asList(IPCRenteAvsAi.CS_TYPE_RENTE_10,
            IPCRenteAvsAi.CS_TYPE_RENTE_13, IPCRenteAvsAi.CS_TYPE_RENTE_20, IPCRenteAvsAi.CS_TYPE_RENTE_23,
            IPCRenteAvsAi.CS_TYPE_RENTE_50, IPCRenteAvsAi.CS_TYPE_RENTE_70);

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        float montantAnnuel = checkAmoutAndParseAsFloat(donnee.getRenteAVSAIMontant()) * 12;

        this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL, montantAnnuel);

        // collecte d'informations pour d�terminer la d�duction l�gale sur la
        // fortune nette
        String role = donnee.getCsRoleFamille();

        String clePersonneRente = null;

        if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(role)) {
            clePersonneRente = IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT;
        } else if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(role)) {
            clePersonneRente = IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_CONJOINT;
        } else if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(role)) {
            clePersonneRente = IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_ENFANT;
        }

        if (rentesCoupleDomicile.contains(donnee.getRenteAVSAICsType())) {
            TupleDonneeRapport nbRentesPrincADomTuple = this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_NB_RENTES_PRINCIPALES_COUPLE_A_DOM, 1f);
            this.getOrCreateChild(nbRentesPrincADomTuple, donnee.getCsRoleFamille(), 1f);
        }

        if (clePersonneRente != null) {
            String csTypeRente = donnee.getRenteAVSAICsType();
            if (IPCRenteAvsAi.CS_TYPE_SANS_RENTE.equals(csTypeRente)) {
                csTypeRente = donnee.getRenteAVSAICsTypeSansRente();
            }

            TupleDonneeRapport tuple = this.getOrCreateChild(resultatExistant, clePersonneRente, 0f);

            // ajout du type de la rente du requ�rant dans le contexte de calcul afin de le propager dans les start�gies
            // finales
            if (clePersonneRente.equals(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)) {
                context.put(Attribut.TYPE_RENTE_REQUERANT, csTypeRente);
            }

            // le code system est mis dans la l�gende car le montant �tant un flottant, il se produit une perte de
            // pr�cision qui change le code system
            tuple.setLegende(csTypeRente);
            
            if (IPCRenteAvsAi.CS_TYPE_RENTE_13.equals(csTypeRente)) {
                TupleDonneeRapport tupleImputationFortuneNette = new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL, 0.0f, donnee.getImputationFortune());
                resultatExistant.addEnfantTuple(tupleImputationFortuneNette);
            }   
            
        }
        

        return resultatExistant;
    }
    
}
