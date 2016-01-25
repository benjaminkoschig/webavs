package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ConfigProcessusSearchModel;

/**
 * Service de gestion de la persistence du mod�le de configuration des processus
 * 
 * @author gmo
 * 
 */
public interface ConfigProcessusModelService extends JadeApplicationService {

    /**
     * Lecture d'une configuration processus selon l'id pass� en param�tre
     * 
     * @param idConfig
     *            l'id de la configuration � charger
     * @return le mod�le de la configuration charg�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ConfigProcessusModel read(String idConfig) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche de configuration selon le mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            le mod�le de recherche
     * @return le mod�le de recherche avec les r�sultats correspondants
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ConfigProcessusSearchModel search(ConfigProcessusSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
