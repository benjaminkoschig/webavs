package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuImmobiliereSeul implements StrategieCalculFinalisation {
    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        float somme = 0;
        for (String champ : StrategieFinalRevenuImmobiliereSeul.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL, somme));

    }

}
