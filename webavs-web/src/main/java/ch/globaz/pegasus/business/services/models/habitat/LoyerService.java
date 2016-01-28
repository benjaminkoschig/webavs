package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface LoyerService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AbstractDonneeFinanciereSearchModel search) throws LoyerException, JadePersistenceException;

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
    public Loyer create(Loyer loyer) throws LoyerException, DonneeFinanciereException, JadePersistenceException;

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
     * @throws DonneeFinanciereException
     */
    public Loyer delete(Loyer loyer) throws LoyerException, JadePersistenceException, DonneeFinanciereException;

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
    public Loyer read(String idLoyer) throws LoyerException, JadePersistenceException;

    /**
     * Chargement d'un Loyer via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    public Loyer readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws LoyerException,
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
    public Loyer update(Loyer loyer) throws LoyerException, DonneeFinanciereException, JadePersistenceException;

}
