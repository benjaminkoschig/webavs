package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAi;

/**
 * Interface pour le service des simple Rente avsai AI 6.2010
 * 
 * @author SCE
 * 
 */
public interface SimpleRenteAvsAiService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� renteAvsAi.
     * 
     * @param renteAvsAi
     *            La renteAvsAi � cr�er
     * @return La renteAvsAi cr��
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAvsAi create(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� renteAvsAi
     * 
     * @param renteAvsAi
     *            La renteAvsAi � supprimer
     * @return La renteAvsAi supprim�
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAvsAi delete(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la suppression r�ele de la donn�e financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une renteAvsAi
     * 
     * @param idRenteAvsAi
     *            L'identifiant de la renteAvsAi � charger en m�moire
     * @return La renteAvsAi charg� en m�moire
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAvsAi read(String idRenteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� renteAvsAi
     * 
     * @param renteAvsAi
     *            La renteAvsAi � mettre � jour
     * @return La renteAvsAi mis � jour
     * @throws RenteAvsAiException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleRenteAvsAi update(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;
}
