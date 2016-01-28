package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleCotisationsPsal;

public interface SimpleCotisationsPsalService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleCotisationsPsal
     * 
     * @param SimpleCotisationsPsal
     *            L'entit� SimpleCotisationsPsal � cr�er
     * @return L'entit� SimpleCotisationsPsal cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCotisationsPsal create(SimpleCotisationsPsal simpleCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException;

    /**
     * Permet la suppression d'une entit� SimpleCotisationsPsal
     * 
     * @param SimpleCotisationsPsal
     *            L'entit� SimpleCotisationsPsal � supprimer
     * @return L'entit� SimpleCotisationsPsal supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCotisationsPsal delete(SimpleCotisationsPsal simpleCotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleCotisationsPsal
     * 
     * @param idCotisationsPsal
     *            L'identifiant de l'entit� SimpleCotisationsPsal � charger en m�moire
     * @return L'entit� SimpleCotisationsPsal charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCotisationsPsal read(String idCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleCotisationsPsal
     * 
     * @param SimpleCotisationsPsal
     *            L'entit� SimpleCotisationsPsal � mettre � jour
     * @return L'entit� SimpleCotisationsPsal mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCotisationsPsal update(SimpleCotisationsPsal simpleCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException;

}
