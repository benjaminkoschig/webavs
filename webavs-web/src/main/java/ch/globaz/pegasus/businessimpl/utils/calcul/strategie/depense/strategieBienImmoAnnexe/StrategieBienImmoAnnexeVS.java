/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoAnnexe;

import static ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport.*;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.StrategieCalculDepense;

/**
 * @author ECO
 * 
 */
public class StrategieBienImmoAnnexeVS extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // Calcul du montant des intérêts --> avec fraction lié à la part
        if (!isNuProprietaire(donnee.getBienImmoAnnexeCsTypePropriete())) {

            // part de propriété
            float fractionPart = checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartNumerateur())
                    / checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexePartDenominateur());

            // L'habitation principale à t'elle moins de 10 ans
            boolean isConstructionMoins10Ans = donnee.getIsBienImmoAnnexeDeMoinsDe10Ans();

            // montant des intérêts avec fraction liés à la part saisie
            float montantInteret = checkAmoutAndParseAsFloat(donnee.getBienImmoAnnexeMontantInteretHypothecaire())
                    * fractionPart;

            // taux d'imputation de la valeur locative pour le calcul des frais a prendre en compte
            float montantTauxFractionValeurlocativeBrut = (isConstructionMoins10Ans) ? Float
                    .parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE_M10)).getValeurCourante())
                    : Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE)).getValeurCourante());

            float montantValeurLocativeNette = checkAmountAndParseAsFloat(donnee
                    .getBienImmoAnnexeMontantValeurLocative()) * montantTauxFractionValeurlocativeBrut;

            float loyerEffectifRealisable = arronditValeur(montantValeurLocativeNette
                    * Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.TAUX_BIEN_IMMO_FRACTION_LOYER_EFFECTIF)).getValeurCourante()));

            float sommeRevenusLocations = checkAmountAndParseAsFloat(donnee.getBienImmoAnnexeMontantLoyersEncaisses())
                    + checkAmountAndParseAsFloat(donnee.getBienImmoAnnexeMontantSousLocation());

            float plafondInteretsHypothecaire = loyerEffectifRealisable + sommeRevenusLocations;

            float montanAdmis;

            if (montantInteret > plafondInteretsHypothecaire) {
                montanAdmis = plafondInteretsHypothecaire;
            } else {
                montanAdmis = montantInteret;
            }

            // ajout des interet
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                    montantInteret);

            // ajout des montantadmis
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE, montanAdmis);

        }

        return resultatExistant;
    }
}
