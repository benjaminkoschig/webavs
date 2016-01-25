package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocaliteSearch;

public interface SimpleLienZoneLocaliteService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleLienZoneLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleLienZoneLocalite
     * 
     * @param SimpleLienZoneLocalite
     *            La simpleLienZoneLocalite m�tier � cr�er
     * @return simpleLienZoneLocalite cr��
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleLienZoneLocalite create(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� simpleLienZoneLocalite
     * 
     * @param SimpleLienZoneLocalite
     *            La simpleLienZoneLocalite m�tier � supprimer
     * @return supprim�
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLienZoneLocalite delete(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleLienZoneLocalite PC
     * 
     * @param idsimpleLienZoneLocalite
     *            L'identifiant de simpleLienZoneLocalite � charger en m�moire
     * @return simpleLienZoneLocalite charg�e en m�moire
     * @throws exeeForfaitsPrimesAssuranceMaladieExceptioneeee
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLienZoneLocalite read(String idSimpleLienZoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleLienZoneLocalite selon un mod�le de crit�res.
     * 
     * @param simpleLienZoneLocaliteSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLienZoneLocaliteSearch search(SimpleLienZoneLocaliteSearch simpleLienZoneLocaliteSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleLienZoneLocalite
     * 
     * @param SimpleLienZoneLocalite
     *            Le modele � mettre � jour
     * @return simpleLienZoneLocalite mis � jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleLienZoneLocalite update(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}