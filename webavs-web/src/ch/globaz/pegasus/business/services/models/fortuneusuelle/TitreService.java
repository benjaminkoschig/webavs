package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.models.fortuneusuelle.TitreSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface TitreService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(TitreSearch search) throws TitreException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� Titre
     * 
     * @param Titre
     *            L'entit� Titre � cr�er
     * @return L'entit� Titre cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Titre create(Titre titre) throws JadePersistenceException, TitreException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� Titre
     * 
     * @param Titre
     *            L'entit� Titre � supprimer
     * @return L'entit� Titre supprim�
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Titre delete(Titre titre) throws TitreException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� Titre
     * 
     * @param idTitre
     *            L'identifiant de l'entit� Titre � charger en m�moire
     * @return L'entit� Titre charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Titre read(String idTitre) throws JadePersistenceException, TitreException;

    /**
     * Chargement d'un Titre via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws TitreException
     * @throws JadePersistenceException
     */
    public Titre readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws TitreException,
            JadePersistenceException;

    /**
     * Permet de chercher des Titre selon un mod�le de crit�res.
     * 
     * @param TitreSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TitreSearch search(TitreSearch titreSearch) throws JadePersistenceException, TitreException;

    /**
     * 
     * Permet la mise � jour d'une entit� Titre
     * 
     * @param Titre
     *            L'entit� Titre � mettre � jour
     * @return L'entit� Titre mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TitreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public Titre update(Titre titre) throws JadePersistenceException, TitreException, DonneeFinanciereException;
}