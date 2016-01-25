package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleLibelleContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;

public interface SimpleLibelleContratEntretienViagerService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleLibelleContratEntretienViager
     * 
     * @param SimpleLibelleContratEntretienViager
     *            L'entit� SimpleLibelleContratEntretienViager � cr�er
     * @return L'entit� SimpleLibelleContratEntretienViager cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleLibelleContratEntretienViager create(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) throws JadePersistenceException,
            SimpleLibelleContratEntretienViagerException;

    /**
     * Permet la suppression d'une entit� SimpleLibelleContratEntretienViager
     * 
     * @param SimpleLibelleContratEntretienViager
     *            L'entit� SimpleLibelleContratEntretienViager � supprimer
     * @return L'entit� SimpleLibelleContratEntretienViager supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws SimpleLibelleContratEntretienViagerException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLibelleContratEntretienViager delete(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleLibelleContratEntretienViager
     * 
     * @param idLibelleContratEntretienViager
     *            L'identifiant de l'entit� SimpleLibelleContratEntretienViager � charger en m�moire
     * @return L'entit� SimpleLibelleContratEntretienViager charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleLibelleContratEntretienViager read(String idLibelleContratEntretienViager)
            throws JadePersistenceException, SimpleLibelleContratEntretienViagerException;

    /**
     * 
     * Permet la rechecher des entit�s SimpleLibelleContratEntretienViager
     * 
     * @param libelleContratEntretienViagerSearch
     * @return
     * @throws SimpleLibelleContratEntretienViagerException
     * @throws JadePersistenceException
     */
    public SimpleLibelleContratEntretienViagerSearch search(
            SimpleLibelleContratEntretienViagerSearch libelleContratEntretienViagerSearch)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleLibelleContratEntretienViager
     * 
     * @param SimpleLibelleContratEntretienViager
     *            L'entit� SimpleLibelleContratEntretienViager � mettre � jour
     * @return L'entit� SimpleLibelleContratEntretienViager mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleLibelleContratEntretienViager update(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) throws JadePersistenceException,
            SimpleLibelleContratEntretienViagerException;
}
