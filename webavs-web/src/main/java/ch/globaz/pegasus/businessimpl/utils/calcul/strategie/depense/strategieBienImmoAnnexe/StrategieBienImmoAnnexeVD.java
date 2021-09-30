/**
 *
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoAnnexe;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieCalculDepense;
import ch.globaz.pegasus.utils.PCApplicationUtil;

import static ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport.arronditValeur;

/**
 * @author ECO
 *
 */
public class StrategieBienImmoAnnexeVD extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (!isNuProprietaire(donnee.getBienImmoAnnexeCsTypePropriete())) {
            float fraction = Float.parseFloat(donnee.getBienImmoAnnexePartNumerateur())
                    / Float.parseFloat(donnee.getBienImmoAnnexePartDenominateur());

            // L'habitation principale à t'elle moins de 10 ans
            boolean isConstructionPlus20Ans = donnee.getIsBienImmoAnnexeDePlusDe20Ans();
            boolean isImmeubleCommerciale = donnee.getIsImmeubleCommercialeAnnexe();

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantInteretHypothecaire()) * fraction);

            // L'habitation principale à t'elle plus de 20 ans et Reforme
            if(context.contains(CalculContext.Attribut.REFORME)) {
                this.getOrCreateChild(resultatExistant,
                        IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_ANNEXE_PLUS_DE_20_ANS,
                        isConstructionPlus20Ans);

                this.getOrCreateChild(resultatExistant,
                        IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_COMMERCIALE_ANNEXE,
                        donnee.getIsImmeubleCommercialeAnnexe());
            }

            // taux frais entretien en fonction de l'age du batiment
            float tauxFraisEntretien = getTauxFraisEntretienAnnexe(false, isConstructionPlus20Ans, context);
            float montantFraisEntretien = 0f;

            // Si l'immeuble est utilisé à des fins commerciales, frais entretiens à 0
            if (!isImmeubleCommerciale) {
                montantFraisEntretien = arronditValeur(checkAmountAndParseAsFloat(donnee
                        .getBienImmoAnnexeMontantValeurLocative()) * tauxFraisEntretien * fraction);
            }

            // ajout des frais d'entretien
            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE, montantFraisEntretien);
        }

        return resultatExistant;
    }

}
