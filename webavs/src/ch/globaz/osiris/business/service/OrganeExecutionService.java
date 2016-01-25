package ch.globaz.osiris.business.service;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.osiris.business.model.OrganeExecutionSimpleModel;
import ch.globaz.osiris.business.model.OrganeExecutionSimpleModelSearch;

public interface OrganeExecutionService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws OrganeExecution
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(OrganeExecutionSimpleModelSearch search) throws JadePersistenceException;

    public OrganeExecutionSimpleModel read(String idOrganeExecution) throws JadePersistenceException;

    /**
     * Permet de chercher des OrganeExecution selon un modèle de critères.
     * 
     * @param nameParamSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws OrganeExecution
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public OrganeExecutionSimpleModelSearch search(OrganeExecutionSimpleModelSearch nameParamSearch)
            throws JadePersistenceException;

}
