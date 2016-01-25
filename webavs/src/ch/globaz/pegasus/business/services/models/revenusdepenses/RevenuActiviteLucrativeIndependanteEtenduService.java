package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteEtenduSearch;

public interface RevenuActiviteLucrativeIndependanteEtenduService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RevenuActiviteLucrativeIndependanteEtenduSearch search)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� RevenuActiviteLucrativeIndependante
     * 
     * @param idRevenuActiviteLucrativeIndependante
     *            L'identifiant de l'entit� RevenuActiviteLucrativeIndependante � charger en m�moire
     * @return L'entit� RevenuActiviteLucrativeIndependante charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeIndependanteEtendu read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeIndependante selon un mod�le de crit�res.
     * 
     * @param RevenuActiviteLucrativeIndependanteSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeIndependanteEtenduSearch search(
            RevenuActiviteLucrativeIndependanteEtenduSearch revenuActiviteLucrativeIndependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

}