package ch.globaz.pegasus.businessimpl.utils.calcul;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Collection;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

/**
 * Cette interface d�clare l'entit� d'une condition de partage des p�riodes d'un droit par rapport � sa plage temporelle
 * globale. Ses impl�mentations r�cup�rent des informations d'un cache de donn�es pour d�terminer les dates de
 * s�paration selon leurs r�gles m�tier.<br>
 * Une impl�mentation de condition de partage ne doit id�alement contenir qu'une seule r�gle m�tier, pour garder le plus
 * de claret� et de d�couplage de code possible.
 * 
 * @author ECO
 * 
 */
public interface IConditionPartagePeriode {

    /**
     * Calcule les dates de partages de la plage du droit pour la condition m�tier d�finie dans chaque impl�mentation.
     * 
     * @param dateDebut
     *            Date de d�but de la plage du droit � calculer.
     * @param cacheDonnees
     *            Cache de donn�es r�cup�r�s au pr�alable de la base de donn�es.
     * @return La liste de dates de partage calcul�es.
     * @throws CalculException
     */
    public abstract Collection<String> calculateDates(String dateDebut,
            Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal)
            throws CalculException;

}
