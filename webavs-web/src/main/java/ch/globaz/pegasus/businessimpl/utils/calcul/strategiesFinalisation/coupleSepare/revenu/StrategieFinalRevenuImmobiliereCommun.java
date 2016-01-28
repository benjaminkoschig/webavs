package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuImmobiliereCommun implements StrategieCalculFinalisation {
    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES

    };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        float somme = 0;
        for (String champ : StrategieFinalRevenuImmobiliereCommun.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL, somme));

    }

}
