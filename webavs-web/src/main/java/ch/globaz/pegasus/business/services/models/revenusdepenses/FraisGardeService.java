package ch.globaz.pegasus.business.services.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGardeSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface FraisGardeService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     *
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(FraisGardeSearch search) throws FraisGardeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� FraisGarde
     *
     * @param fraisGarde
     *            L'entit� FraisGarde � cr�er
     * @return L'entit� FraisGarde cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public FraisGarde create(FraisGarde fraisGarde) throws JadePersistenceException, FraisGardeException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� FraisGarde
     *
     * @param autresRevenus
     *            L'entit� FraisGarde � supprimer
     * @return L'entit� FraisGarde supprim�
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public FraisGarde delete(FraisGarde autresRevenus) throws FraisGardeException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� FraisGarde
     *
     * @param idFraisGarde
     *            L'identifiant de l'entit� FraisGarde � charger en m�moire
     * @return L'entit� FraisGarde charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public FraisGarde read(String idFraisGarde) throws JadePersistenceException, FraisGardeException;

    /**
     * Chargement d'un FraisGarde via l'id donnee financiere header
     *
     * @param idDonneeFinanciereHeader
     * @return
     * @throws FraisGardeException
     * @throws JadePersistenceException
     */
    public FraisGarde readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws FraisGardeException,
            JadePersistenceException;

    /**
     * Permet de chercher des FraisGarde selon un mod�le de crit�res.
     *
     * @param fraisGardeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public FraisGardeSearch search(FraisGardeSearch fraisGardeSearch) throws JadePersistenceException,
            FraisGardeException;

    /**
     *
     * Permet la mise � jour d'une entit� FraisGarde
     *
     * @param fraisGarde
     *            L'entit� FraisGarde � mettre � jour
     * @return L'entit� FraisGarde mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FraisGardeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public FraisGarde update(FraisGarde fraisGarde) throws JadePersistenceException, FraisGardeException,
            DonneeFinanciereException;
}
