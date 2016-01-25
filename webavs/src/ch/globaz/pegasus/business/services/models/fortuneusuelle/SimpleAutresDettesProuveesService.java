package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuvees;

public interface SimpleAutresDettesProuveesService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleAutresDettesProuvees
     * 
     * @param simpleAssuranceVie
     *            L'entit� simpleAutresDettesProuvees � cr�er
     * @return L'entit� simpleAutresDettesProuvees cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutresDettesProuvees create(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws JadePersistenceException, AutresDettesProuveesException;

    /**
     * Permet la suppression d'une entit� SimpleAutresDettesProuvees
     * 
     * @param SimpleAutresDettesProuvees
     *            L'entit� SimpleAutresDettesProuvees � supprimer
     * @return L'entit� SimpleAutresDettesProuvees supprim�
     * @throws SimpleAutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAutresDettesProuvees delete(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleAutresDettesProuvees
     * 
     * @param idAutresDettesProuvees
     *            L'identifiant de l'entit� SimpleAutresDettesProuvees � charger en m�moire
     * @return L'entit� SimpleAutresDettesProuvees charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutresDettesProuvees read(String idAutresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleAutresDettesProuvees
     * 
     * @param SimpleAutresDettesProuvees
     *            L'entit� SimpleAutresDettesProuvees � mettre � jour
     * @return L'entit� SimpleAutresDettesProuvees mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutresDettesProuvees update(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws JadePersistenceException, AutresDettesProuveesException;

}
