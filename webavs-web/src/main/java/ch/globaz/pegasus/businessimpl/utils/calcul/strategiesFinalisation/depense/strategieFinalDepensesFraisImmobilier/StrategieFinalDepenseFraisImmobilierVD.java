package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategieFinalDepensesFraisImmobilier;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

import java.util.Date;

public class StrategieFinalDepenseFraisImmobilierVD implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut)
            throws NumberFormatException, CalculException {

        float somme = 0;
        float revenusBruts = 0;
        float revenusActiviteAgricole = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE);

        //

        // Si pas de revenu activité lucrative indépendante agricole
        if ((revenusActiviteAgricole) == 0) {
            somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE);
            revenusBruts = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE)
                    + donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS)
                    + donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS);
        }

        float plafondRevenus = revenusBruts;
        plafondRevenus += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_REVENU, plafondRevenus));
        String typeHabitationCS = donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);

        // Si type de propriété n'est pas droit d'habitation
        if ((typeHabitationCS == null)
                || ((donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE) != null) && !donnee
                        .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE).equals(
                                IPCBienImmoPrincipal.CS_TYPE_DROIT_HABITATION))) {

            // Récupération du flag booléen pour savoir si le bine à moins de 10ans
            boolean isConstructionMoinsDixAns = false;

            // Récupération du flag booléen pour savoir si le bine à plus de 20ans
            boolean isConstructionPlusVingtAnsPrincipale = false;
            boolean isConstructionPlusVingtAnsAnnexe = false;

            if (donnee.getEnfants().get(
                    IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_MOINS_DE_10_ANS) != null) {
                float floatAsBoolean = donnee.getEnfants()
                        .get(IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_MOINS_DE_10_ANS)
                        .getValeur();
                isConstructionMoinsDixAns = TupleDonneeRapport.readBoolean(floatAsBoolean);
            }

            if (donnee.getEnfants().get(
                    IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_PLUS_DE_20_ANS) != null) {
                float floatAsBoolean = donnee.getEnfants()
                        .get(IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_PRINCIPALE_PLUS_DE_20_ANS)
                        .getValeur();
                isConstructionPlusVingtAnsPrincipale = TupleDonneeRapport.readBoolean(floatAsBoolean);
            }

            if (donnee.getEnfants().get(
                    IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_ANNEXE_PLUS_DE_20_ANS) != null) {
                float floatAsBoolean = donnee.getEnfants()
                        .get(IPCValeursPlanCalcul.CLE_INTER_BIEN_IMMOBILIER_HABITATION_ANNEXE_PLUS_DE_20_ANS)
                        .getValeur();
                isConstructionPlusVingtAnsAnnexe = TupleDonneeRapport.readBoolean(floatAsBoolean);
            }

            Attribut attribut = null;

            if (isConstructionMoinsDixAns) {
                attribut = Attribut.FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS;
            } else if (isConstructionPlusVingtAnsPrincipale) {
                attribut = Attribut.FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_PRINCIPALE;
            } else if (isConstructionPlusVingtAnsAnnexe) {
                attribut = Attribut.FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_ANNEXE;
            } else {
                attribut = Attribut.FRAIS_ENTRETIEN_IMMEUBLE;
            }

            String legende = ((ControlleurVariablesMetier) context.get(attribut)).getLegendeCourante();

            // Récupération des frais d'entretien calculé précédement
            float fraisEntretien = donnee
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE);

            // On l'ajoute aux frais calculés

            donnee.addEnfantTuple(new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE, fraisEntretien, legende));
            somme += fraisEntretien;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL, somme));

    }

}
