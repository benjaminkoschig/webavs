package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CasMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.UtilStrategieBienImmobillier;

public class StrategieFinalRevenuImmobiliere extends UtilStrategieBienImmobillier implements
        StrategieCalculFinalisation {
    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES

    };

    private final static String[] champWithActiviteLucrativeIndependanteAgricole = {
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        float somme = 0;

        String[] champATraiter = StrategieFinalRevenuImmobiliere.champs;
        float revenuAgricole = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE);

        /* Gestion revenu agricole indpendant, on redlfinit les champs a traiter pour le calcul du solde */
        if ((revenuAgricole != 0f)) {
            champATraiter = StrategieFinalRevenuImmobiliere.champWithActiviteLucrativeIndependanteAgricole;
            context.getCasMetier().add(CasMetier.REVENU_INDEPENDANT_AGRICOLE);
        }
        // Si home et propriété droit d'habitation
        // nombre de homes
        Float sommeHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);
        TupleDonneeRapport bishp = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);

        // Si home et droit habitation...
        if (!isHomeEtDroitHabitation(sommeHomes, bishp)) {
            for (String champ : champATraiter) {
                somme += donnee.getValeurEnfant(champ);
            }
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL, somme));

    }

}
