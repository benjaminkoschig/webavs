package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre;

public interface SimpleTitreService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleTitre
     * 
     * @param simpleTitre
     *            L'entit� simpleTitre � cr�er
     * @return L'entit� simpleTitre cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTitre create(SimpleTitre simpleTitre) throws JadePersistenceException, TitreException;

    /**
     * Permet la suppression d'une entit� SimpleTitre
     * 
     * @param SimpleTitre
     *            L'entit� SimpleTitre � supprimer
     * @return L'entit� SimpleTitre supprim�
     * @throws SimpleTitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTitre delete(SimpleTitre simpleTitre) throws TitreException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleTitre
     * 
     * @param idTitre
     *            L'identifiant de l'entit� SimpleTitre � charger en m�moire
     * @return L'entit� SimpleTitre charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTitre read(String idTitre) throws JadePersistenceException, TitreException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleTitre
     * 
     * @param SimpleTitre
     *            L'entit� SimpleTitre � mettre � jour
     * @return L'entit� SimpleTitre mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleTitre update(SimpleTitre simpleTitre) throws JadePersistenceException, TitreException;

}