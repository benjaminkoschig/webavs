package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyer;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyerSearch;

public interface SimpleLoyerService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param renteAvsAi
     *            La renteAvsAi � cr�er
     * @return le loyer cr��
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLoyer create(SimpleLoyer simpleLoyer) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            le loyer � supprimer
     * @return le loyer supprim�
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLoyer delete(SimpleLoyer simpleLoyer) throws LoyerException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleLoyer en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleLoyer
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idrenteAvsAi
     *            L'identifiant de le loyere � charger en m�moire
     * @return le loyer charg� en m�moire
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLoyer read(String idSimpleLoyer) throws LoyerException, JadePersistenceException;

    /**
     * Permet la recherche d'apr�s les param�tres de recherche
     * 
     * @param renteAvsAiSearch
     * @return La recherche effectu�
     * @throws LoyerException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public SimpleLoyerSearch search(SimpleLoyerSearch loyerSearch) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param renteAvsAi
     *            le loyer � mettre � jour
     * @return le loyer mis � jour
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLoyer update(SimpleLoyer simpleLoyer) throws LoyerException, DonneeFinanciereException,
            JadePersistenceException;
}
