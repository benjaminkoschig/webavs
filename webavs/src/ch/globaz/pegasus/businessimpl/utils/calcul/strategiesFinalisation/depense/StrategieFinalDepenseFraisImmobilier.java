package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCBienImmoPrincipal;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalDepenseFraisImmobilier implements StrategieCalculFinalisation {

    private static final float taux_frais_entretien = 1f / 5f;

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {

        float somme = 0;
        float revenusBruts = 0;
        float revenusActiviteAgricole = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE);

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
            revenusBruts *= StrategieFinalDepenseFraisImmobilier.taux_frais_entretien;
            donnee.addEnfantTuple(new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE, revenusBruts));
            somme += revenusBruts;
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL, somme));

    }

}
