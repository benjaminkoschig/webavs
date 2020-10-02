package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

import java.util.Date;

public class StrategieFinalPrimeAssuranceMaladie implements StrategieCalculFinalisation {

    private final static String[] champs = {IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL};

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        float somme = 0;
        for (String champ : StrategieFinalPrimeAssuranceMaladie.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        float primeMoy = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL);
        if (primeMoy < somme) {
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL, primeMoy, Integer.toString((int) primeMoy)));
        } else {

            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL, somme, Integer.toString((int) primeMoy)));
        }
    }

}
