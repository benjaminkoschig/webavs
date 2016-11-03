/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoPrincipal;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieCalculDepense;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 * 
 */
public class StrategieBienImmoPrincipal extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getBienImmoPrincipalCSPropriete())) {
            // verification integrité données
            if (context.contains(Attribut.HAS_HABITAT_CHEZ_PROPRIETAIRE)) {
                throw new CalculBusinessException("pegasus.calcul.habitat.integrity");
            }
            context.put(Attribut.HAS_BIEN_IMMO_PRINCIPAL, true);

            // tuple pour bien immo principal
            TupleDonneeRapport tupleHabitatPrincipal = this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE, 0.0f);

            // L'habitation principale à t'elle moins de 10 ans
            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_MOINS_DE_10_ANS,
                    donnee.getIsBienImmoPrincipalDeMoinsDe10Ans());

            float fraction = checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalPartNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalPartDenominateur());

            // Cs type propriété
            tupleHabitatPrincipal.setLegende(donnee.getBienImmoPrincipalCSPropriete());

            // creation valeur locative habitation
            this.getOrCreateChild(tupleHabitatPrincipal,
                    IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurLocative()));// *
                                                                                                   // fraction);//Pas

            TupleDonneeRapport nodeRolePropBishp = this.getOrCreateChild(tupleHabitatPrincipal,
                    IPCValeursPlanCalcul.CLE_INTER_BISHP_ROLE_PROPRIETAIRE, donnee.getCsRoleFamille());
            nodeRolePropBishp.setLegende(donnee.getCsRoleFamille());

            // de fraction
            // dans ce cas, uniquement
            // multiplication prorata
            // personne
            this.getOrCreateChild(tupleHabitatPrincipal,
                    IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE_NBPERSONNES,
                    donnee.getBienImmoPrincipalNombrePersonnes());

            // Gestion des intérêts hypothécaire, sauf droit d'habitation
            if (!isDroitHabitation(donnee.getBienImmoPrincipalCSPropriete())) {
                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                        checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantInteretHypothecaire()) * fraction);
            }

        }

        return resultatExistant;
    }
}
