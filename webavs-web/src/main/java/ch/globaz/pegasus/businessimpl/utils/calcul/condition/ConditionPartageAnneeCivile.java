/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.condition;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

/**
 * Condition pour contrôler si le prochain paiement est sur l'année suivante.
 * Si oui la prochaine date commencera à l'année suivante.
 * 
 * @author est
 * 
 */
public class ConditionPartageAnneeCivile implements IConditionPartagePeriode {

    @Override
    public Collection<String> calculateDates(String dateDebut, Map<String, JadeAbstractSearchModel> cacheDonnees,
            DonneesHorsDroitsProvider containerGlobal) {

        Set<String> dates = new HashSet<String>();
        Calendar dateProchainPaiement = JadeDateUtil.getGlobazCalendar(containerGlobal.getDateProchainPaiement());
        Calendar debutPlage = JadeDateUtil.getGlobazCalendar(dateDebut);
        for (int i = debutPlage.get(Calendar.YEAR) + 1; i <= dateProchainPaiement.get(Calendar.YEAR); i++) {
            dates.add("01.01." + i);
        }

        return dates;
    }
}