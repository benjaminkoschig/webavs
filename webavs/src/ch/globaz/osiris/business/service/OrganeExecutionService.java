package ch.globaz.osiris.business.service;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.osiris.business.model.OrganeExecutionSimpleModel;
import ch.globaz.osiris.business.model.OrganeExecutionSimpleModelSearch;

public interface OrganeExecutionService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws OrganeExecution
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(OrganeExecutionSimpleModelSearch search) throws JadePersistenceException;

    public OrganeExecutionSimpleModel read(String idOrganeExecution) throws JadePersistenceException;

    /**
     * Permet de chercher des OrganeExecution selon un mod�le de crit�res.
     * 
     * @param nameParamSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws OrganeExecution
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public OrganeExecutionSimpleModelSearch search(OrganeExecutionSimpleModelSearch nameParamSearch)
            throws JadePersistenceException;

}
