package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuLoyer implements StrategieCalculFinalisation {

    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_LOYER_SOUS_LOCATION_NET };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        float somme = 0;

        for (String champ : StrategieFinalRevenuLoyer.champs) {
            somme += donnee.getValeurEnfant(champ);
        }
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_LOYER_SOUS_LOCATION_NET, somme));

    }

}
