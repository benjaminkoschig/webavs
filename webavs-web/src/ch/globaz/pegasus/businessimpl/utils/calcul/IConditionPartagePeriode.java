package ch.globaz.pegasus.businessimpl.utils.calcul;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Collection;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

/**
 * Cette interface déclare l'entité d'une condition de partage des périodes d'un droit par rapport à sa plage temporelle
 * globale. Ses implémentations récupèrent des informations d'un cache de données pour déterminer les dates de
 * séparation selon leurs règles métier.<br>
 * Une implémentation de condition de partage ne doit idéalement contenir qu'une seule règle métier, pour garder le plus
 * de clareté et de découplage de code possible.
 * 
 * @author ECO
 * 
 */
public interface IConditionPartagePeriode {

    /**
     * Calcule les dates de partages de la plage du droit pour la condition métier définie dans chaque implémentation.
     * 
     * @param dateDebut
     *            Date de début de la plage du droit à calculer.
     * @param cacheDonnees
     *            Cache de données récupérés au préalable de la base de données.
     * @return La liste de dates de partage calculées.
     * @throws CalculException
     */
    public abstract Collection<String> calculateDates(String dateDebut,
            Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal)
            throws CalculException;

}
