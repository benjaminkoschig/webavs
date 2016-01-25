package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtenduSearch;

public interface RevenuActiviteLucrativeIndependanteEtenduService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RevenuActiviteLucrativeIndependanteEtenduSearch search)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité RevenuActiviteLucrativeIndependante
     * 
     * @param idRevenuActiviteLucrativeIndependante
     *            L'identifiant de l'entité RevenuActiviteLucrativeIndependante à charger en mémoire
     * @return L'entité RevenuActiviteLucrativeIndependante chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependanteEtendu read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeIndependante selon un modèle de critères.
     * 
     * @param RevenuActiviteLucrativeIndependanteSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeIndependanteEtenduSearch search(
            RevenuActiviteLucrativeIndependanteEtenduSearch revenuActiviteLucrativeIndependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

}