package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ConfigProcessusSearchModel;

/**
 * Service de gestion de la persistence du modèle de configuration des processus
 * 
 * @author gmo
 * 
 */
public interface ConfigProcessusModelService extends JadeApplicationService {

    /**
     * Lecture d'une configuration processus selon l'id passé en paramètre
     * 
     * @param idConfig
     *            l'id de la configuration à charger
     * @return le modèle de la configuration chargée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ConfigProcessusModel read(String idConfig) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche de configuration selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            le modèle de recherche
     * @return le modèle de recherche avec les résultats correspondants
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ConfigProcessusSearchModel search(ConfigProcessusSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
