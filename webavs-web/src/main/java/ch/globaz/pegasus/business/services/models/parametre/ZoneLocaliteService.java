package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.ZoneLocaliteSearch;

public interface ZoneLocaliteService extends JadeApplicationService {
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
    public int count(ZoneLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� ZoneLocalite
     * 
     * @param ZoneLocalite
     *            La zoneLocalite � cr�er
     * @return zoneLocalite cr��
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ZoneLocalite create(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� zoneLocalite
     * 
     * @param ZoneLocalite
     *            La zoneLocalite m�tier � supprimer
     * @return supprim�
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ZoneLocalite delete(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une zoneLocalite PC
     * 
     * @param idzoneLocalite
     *            L'identifiant de zoneLocalite � charger en m�moire
     * @return zoneLocalite charg�e en m�moire
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ZoneLocalite read(String idZoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des ZoneLocalite selon un mod�le de crit�res.
     * 
     * @param zoneLocaliteSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ZoneLocaliteSearch search(ZoneLocaliteSearch zoneLocaliteSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� ZoneLocalite
     * 
     * @param ZoneLocalite
     *            Le modele � mettre � jour
     * @return zoneLocalite mis � jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ZoneLocalite update(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
