package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente;

public interface SimpleAutreRenteService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� autreRente
     * 
     * @param simpleAutreRente
     *            L' simpleAutreRente � cr�er
     * @return simpleAutreRente cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreRente create(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� autreRente
     * 
     * @param simpleAutreRente
     *            simpleAutreRente � supprimer
     * @return simpleAutreRente supprim�
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAutreRente delete(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * Permet la suppression r�ele de la donn�e financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une autreRente PC
     * 
     * @param idAutreRente
     *            L'identifiant de la autreRente � charger en m�moire
     * @return L' autre rente charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreRente read(String idSimpleAutreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param simpleAutreRente
     *            L'autre rente � mettre � jour
     * @return simpleAutreRente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAutreRente update(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException;
}
