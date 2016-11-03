package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoNonHabitable.StrategieBienImmoNonHabitable;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoNonHabitable.StrategieBienImmoNonHabitableVS;
import ch.globaz.pegasus.utils.PCApplicationUtil;

public class ProxyStrategieBienImmoNonHabitable extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (PCApplicationUtil.isCantonVS()) {
            return new StrategieBienImmoNonHabitableVS().calcule(donnee, context, resultatExistant);
        } else {
            return new StrategieBienImmoNonHabitable().calcule(donnee, context, resultatExistant);
        }
    }

}
