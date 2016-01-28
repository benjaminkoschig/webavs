package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.AutreRenteSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AutreRenteService extends JadeApplicationService, AbstractDonneeFinanciereService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AutreRenteSearch search) throws AutreRenteException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� autreRente
     * 
     * @param autreRente
     *            L' autre rente � cr�er
     * @return L' autre rente cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutreRente create(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� autreRente
     * 
     * @param autreRente
     *            L' autre rente � supprimer
     * @return L' autre rente supprim�
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public AutreRente delete(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException;

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
    public AutreRente read(String idAutreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * Chargement d'une AutreRente via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreRenteException
     * @throws JadePersistenceException
     */
    public AutreRente readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutreRenteException,
            JadePersistenceException;

    public AutreRenteSearch search(AutreRenteSearch autreRenteSearch) throws JadePersistenceException,
            AutreRenteException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param autreRente
     *            L'autre rente � mettre � jour
     * @return L'autre rente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutreRente update(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException;
}
