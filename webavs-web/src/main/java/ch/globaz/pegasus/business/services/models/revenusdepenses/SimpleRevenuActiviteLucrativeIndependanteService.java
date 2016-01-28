package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependante;

public interface SimpleRevenuActiviteLucrativeIndependanteService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param SimpleRevenuActiviteLucrativeIndependante
     *            L'entit� SimpleRevenuActiviteLucrativeIndependante � cr�er
     * @return L'entit� SimpleRevenuActiviteLucrativeIndependante cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuActiviteLucrativeIndependante create(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * Permet la suppression d'une entit� SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param SimpleRevenuActiviteLucrativeIndependante
     *            L'entit� SimpleRevenuActiviteLucrativeIndependante � supprimer
     * @return L'entit� SimpleRevenuActiviteLucrativeIndependante supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRevenuActiviteLucrativeIndependante delete(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param idRevenuActiviteLucrativeIndependante
     *            L'identifiant de l'entit� SimpleRevenuActiviteLucrativeIndependante � charger en m�moire
     * @return L'entit� SimpleRevenuActiviteLucrativeIndependante charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuActiviteLucrativeIndependante read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param SimpleRevenuActiviteLucrativeIndependante
     *            L'entit� SimpleRevenuActiviteLucrativeIndependante � mettre � jour
     * @return L'entit� SimpleRevenuActiviteLucrativeIndependante mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleRevenuActiviteLucrativeIndependante update(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;
}
