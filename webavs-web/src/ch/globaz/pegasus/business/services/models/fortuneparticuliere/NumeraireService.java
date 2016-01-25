/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.NumeraireSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface NumeraireService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(NumeraireSearch search) throws NumeraireException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� num�raire
     * 
     * @param numeraire
     *            L'entit� num�raire � cr�er
     * @return L'entit� num�raire cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Numeraire create(Numeraire numeraire) throws JadePersistenceException, NumeraireException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� num�raire
     * 
     * @param numeraire
     *            L'entit� num�raire � supprimer
     * @return L'entit� num�raire supprim�
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Numeraire delete(Numeraire numeraire) throws NumeraireException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� numeraire
     * 
     * @param idNumeraire
     *            L'identifiant de l'entit� num�raire � charger en m�moire
     * @return L'entit� num�raire charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Numeraire read(String idNumeraire) throws JadePersistenceException, NumeraireException;

    /**
     * Chargement d'un Numeraire via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws NumeraireException
     * @throws JadePersistenceException
     */
    public Numeraire readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws NumeraireException,
            JadePersistenceException;

    /**
     * Permet de chercher des num�raire selon un mod�le de crit�res.
     * 
     * @param numeraireSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public NumeraireSearch search(NumeraireSearch numeraireSearch) throws JadePersistenceException, NumeraireException;

    /**
     * 
     * Permet la mise � jour d'une entit� num�raire
     * 
     * @param numeraire
     *            L'entit� num�raire � mettre � jour
     * @return L'entit� num�raire mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public Numeraire update(Numeraire numeraire) throws JadePersistenceException, NumeraireException,
            DonneeFinanciereException;
}
