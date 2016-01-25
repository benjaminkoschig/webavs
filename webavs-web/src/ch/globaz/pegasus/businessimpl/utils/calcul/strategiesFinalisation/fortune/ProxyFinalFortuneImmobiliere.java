package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune;

import java.util.Date;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.ProxyCalculDates;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere.StrategieFinalFortuneImmobiliere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere.StrategieFinalFortuneImmobiliere201101;

public class ProxyFinalFortuneImmobiliere implements StrategieCalculFinalisation {

    /**
     * Aiguilleur sur la bonne strategie des biens Immobiliiers en fonction de la date
     */
    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        if (dateDebut.getTime() >= ProxyCalculDates.FORTUNE_IMMOBILIERE_SWITCH_STRATEGY_DATE.timestamp) {
            new StrategieFinalFortuneImmobiliere201101().calcule(donnee, context, dateDebut);
        } else {
            new StrategieFinalFortuneImmobiliere().calcule(donnee, context, dateDebut);
        }
    }

}
