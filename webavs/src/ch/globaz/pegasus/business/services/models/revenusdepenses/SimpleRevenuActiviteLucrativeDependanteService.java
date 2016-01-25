package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante;

public interface SimpleRevenuActiviteLucrativeDependanteService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleRevenuActiviteLucrativeDependante
     * 
     * @param SimpleRevenuActiviteLucrativeDependante
     *            L'entit� SimpleRevenuActiviteLucrativeDependante � cr�er
     * @return L'entit� SimpleRevenuActiviteLucrativeDependante cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuActiviteLucrativeDependante create(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * Permet la suppression d'une entit� SimpleRevenuActiviteLucrativeDependante
     * 
     * @param SimpleRevenuActiviteLucrativeDependante
     *            L'entit� SimpleRevenuActiviteLucrativeDependante � supprimer
     * @return L'entit� SimpleRevenuActiviteLucrativeDependante supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRevenuActiviteLucrativeDependante delete(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleRevenuActiviteLucrativeDependante
     * 
     * @param idRevenuActiviteLucrativeDependante
     *            L'identifiant de l'entit� SimpleRevenuActiviteLucrativeDependante � charger en m�moire
     * @return L'entit� SimpleRevenuActiviteLucrativeDependante charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuActiviteLucrativeDependante read(String idRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleRevenuActiviteLucrativeDependante
     * 
     * @param SimpleRevenuActiviteLucrativeDependante
     *            L'entit� SimpleRevenuActiviteLucrativeDependante � mettre � jour
     * @return L'entit� SimpleRevenuActiviteLucrativeDependante mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuActiviteLucrativeDependante update(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

}
