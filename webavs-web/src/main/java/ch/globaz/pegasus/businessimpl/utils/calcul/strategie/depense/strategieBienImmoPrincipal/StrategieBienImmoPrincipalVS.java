/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoPrincipal;

import static ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport.*;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
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
public class StrategieBienImmoPrincipalVS extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // dans tous les cas de propriété sauf nu-
        if (!isNuProprietaire(donnee.getBienImmoPrincipalCSPropriete())) {

            // verification integrité données, si la personne a un habitat cheu un prop -> location --> problème
            // integrité
            if (context.contains(Attribut.HAS_HABITAT_CHEZ_PROPRIETAIRE)) {
                throw new CalculBusinessException("pegasus.calcul.habitat.integrity");
            }
            context.put(Attribut.HAS_BIEN_IMMO_PRINCIPAL, true);

            // tuple pour bien immo principal, on set la notion de propriété
            TupleDonneeRapport tupleHabitatPrincipal = this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE, 0.0f);

            // L'habitation principale à t'elle moins de 10 ans
            boolean isConstructionMoins10Ans = donnee.getIsBienImmoPrincipalDeMoinsDe10Ans();

            this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_MOINS_DE_10_ANS,
                    isConstructionMoins10Ans);

            // taux frais entretirn en fonction de l'age du batiment
            float tauxFraisEntretien = getTauxFraisEntretien(isConstructionMoins10Ans, context);

            // Cs type propriété
            tupleHabitatPrincipal.setLegende(donnee.getBienImmoPrincipalCSPropriete());

            // creation valeur locative habitation
            this.getOrCreateChild(tupleHabitatPrincipal,
                    IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_VALEUR_LOCATIVE_APP_HABITE,
                    checkAmoutAndParseAsFloat(donnee.getBienImmoPrincipalMontantValeurLocative()));

            TupleDonneeRapport nodeRolePropBishp = this.getOrCreateChild(tupleHabitatPrincipal,
                    IPCValeursPlanCalcul.CLE_INTER_BISHP_ROLE_PROPRIETAIRE, donnee.getCsRoleFamille());
            nodeRolePropBishp.setLegende(donnee.getCsRoleFamille());

            // nbre personnes dans le batiment
            this.getOrCreateChild(tupleHabitatPrincipal,
                    IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE_NBPERSONNES,
                    donnee.getBienImmoPrincipalNombrePersonnes());

            // Gestion des intérêts hypothécaire, sauf droit d'habitation
            if (!isDroitHabitation(donnee.getBienImmoPrincipalCSPropriete())) {

                // montant des frais d'entretien --> valeur locative % imputation selon age (+-10 ans) biens immo
                float partPropriete = checkAmountAndParseAsFloat(donnee.getBienImmoPrincipalPartNumerateur())
                        / checkAmountAndParseAsFloat(donnee.getBienImmoPrincipalPartDenominateur());

                // montant des intérêts avec fraction liés à la part saisie
                float montantInteret = checkAmoutAndParseAsFloat(donnee
                        .getBienImmoPrincipalMontantInteretHypothecaire()) * partPropriete;

                float montantFraisEntretien = arronditValeur(checkAmountAndParseAsFloat(donnee
                        .getBienImmoPrincipalMontantValeurLocative()) * tauxFraisEntretien * partPropriete);

                // taux d'imputation de la valeur locative pour le calcul des frais a prendre en compte
                float montantTauxFractionValeurlocativeBrut = (isConstructionMoins10Ans) ? Float
                        .parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE_M10)).getValeurCourante())
                        : Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE)).getValeurCourante());

                float montantValeurLocativeNette = checkAmountAndParseAsFloat(donnee
                        .getBienImmoPrincipalMontantValeurLocative()) * montantTauxFractionValeurlocativeBrut;

                float loyerEffectifRealisable = arronditValeur(montantValeurLocativeNette
                        * Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.TAUX_BIEN_IMMO_FRACTION_LOYER_EFFECTIF)).getValeurCourante()));

                float loyerEffectifRealisablePart = loyerEffectifRealisable * partPropriete;

                float sommeRevenusLocations = (checkAmountAndParseAsFloat(donnee
                        .getBienImmoPrincipalMontantLoyersEncaisses()) + checkAmountAndParseAsFloat(donnee
                        .getBienImmoPrincipalMontantSousLocation()))
                        * partPropriete;

                float plafondInteretsHypothecaire = loyerEffectifRealisablePart - montantFraisEntretien
                        + sommeRevenusLocations;

                float montanAdmis;

                if (montantInteret > plafondInteretsHypothecaire) {
                    montanAdmis = plafondInteretsHypothecaire;
                } else {
                    montanAdmis = montantInteret;
                }

                // ajout de sfrais d'entretin
                this.getOrCreateChild(resultatExistant,
                        IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE, montantFraisEntretien);

                // ajout des interet
                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
                        montantInteret);

                // ajout des montantadmis
                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE,
                        montanAdmis + montantFraisEntretien);

            }

        }

        return resultatExistant;
    }

}
