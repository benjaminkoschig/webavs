/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContrePresation;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContreprestationContainer;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;

/**
 * @author BSC
 * 
 */
public interface DessaisissementFortuneService extends JadeApplicationService {

    /**
     * Permet de caculer la contre prestation
     * 
     * @param DessaisissementFortune
     * 
     * @return calculContrePresation avec les valeurs setter
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CalculContrePresation calculContrePresation(CalculContreprestationContainer calculCPContainer)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Permet de caculer la contre prestation
     * 
     * @param DessaisissementFortune
     * 
     * @return calculContrePresation avec les valeurs setter
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CalculContrePresation calculContrePresation(DessaisissementFortune dessaisissementFortune)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DessaisissementFortuneSearch search) throws DessaisissementFortuneException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � cr�er
     * @return L'entit� b�tail cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DessaisissementFortune create(DessaisissementFortune betail) throws JadePersistenceException,
            DessaisissementFortuneException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � supprimer
     * @return L'entit� b�tail supprim�
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DessaisissementFortune delete(DessaisissementFortune betail) throws DessaisissementFortuneException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� betail
     * 
     * @param idDessaisissementFortune
     *            L'identifiant de l'entit� b�tail � charger en m�moire
     * @return L'entit� b�tail charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DessaisissementFortune read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException;

    /**
     * Chargement d'une DessaisissementFortune via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws DessaisissementFortuneSearch
     *             Exception
     * @throws JadePersistenceException
     */
    public DessaisissementFortune readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws DessaisissementFortuneException, JadePersistenceException;

    /**
     * Permet de chercher des b�tail selon un mod�le de crit�res.
     * 
     * @param betailSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DessaisissementFortuneSearch search(DessaisissementFortuneSearch betailSearch)
            throws JadePersistenceException, DessaisissementFortuneException;

    /**
     * 
     * Permet la mise � jour d'une entit� b�tail
     * 
     * @param betail
     *            L'entit� b�tail � mettre � jour
     * @return L'entit� b�tail mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public DessaisissementFortune update(DessaisissementFortune betail) throws JadePersistenceException,
            DessaisissementFortuneException, DonneeFinanciereException;
}
