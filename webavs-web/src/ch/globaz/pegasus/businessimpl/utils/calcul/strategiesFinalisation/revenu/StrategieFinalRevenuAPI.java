package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuAPI implements StrategieCalculFinalisation {
    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI,
            IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAA, IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_LAM

    };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        float somme = 0;

        for (String champ : StrategieFinalRevenuAPI.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        somme += donnee.getSommeEnfants(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AUTRE_API);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_TOTAL, somme));

    }

}
