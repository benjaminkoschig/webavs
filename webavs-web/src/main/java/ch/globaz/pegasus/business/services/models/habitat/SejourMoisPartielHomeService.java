package ch.globaz.pegasus.business.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHomeSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface SejourMoisPartielHomeService extends JadeApplicationService, AbstractDonneeFinanciereService {
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
    int count(SejourMoisPartielHomeSearch search) throws SejourMoisPartielHomeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simpleIndemniteJournaliere.
     * 
     * @param sejourMoisPartielHome
     *            La renteAvsAi � cr�er
     * @return le sejourMoisPartielHome cr��
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SejourMoisPartielHome create(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleIndemniteJournaliere
     * 
     * @param sejourMoisPartielHome
     *            le sejourMoisPartielHome � supprimer
     * @return le sejourMoisPartielHome supprim�
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    SejourMoisPartielHome delete(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException, DonneeFinanciereException;
    
    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idSejourMoisPartielHome
     *            L'identifiant de le sejourMoisPartielHomee � charger en m�moire
     * @return le sejourMoisPartielHome charg� en m�moire
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SejourMoisPartielHome read(String idSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException;

    /**
     * Chargement d'une SejourMoisPartielHome via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws SejourMoisPartielHomeException
     * @throws JadePersistenceException
     */
    SejourMoisPartielHome readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws SejourMoisPartielHomeException, JadePersistenceException;

    /**
     * Permet la recherche d'apr�s les param�tres de recherche
     * 
     * @param sejourMoisPartielHomeSearch
     * @return La recherche effectu�
     * @throws SejourMoisPartielHomeException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    SejourMoisPartielHomeSearch search(SejourMoisPartielHomeSearch sejourMoisPartielHomeSearch)
            throws SejourMoisPartielHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� simpleIndemniteJournaliere
     * 
     * @param sejourMoisPartielHome
     *            le sejourMoisPartielHome � mettre � jour
     * @return le sejourMoisPartielHome mis � jour
     * @throws SejourMoisPartielHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    SejourMoisPartielHome update(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
