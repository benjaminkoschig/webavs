package ch.globaz.pegasus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;

public interface PrestationService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(PrestationSearch search) throws PrestationException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une prestattion
     * 
     * @param idPrestation
     *            L'identifiant de la prestation � charger en m�moire
     * @return La prestation charg�e en m�moire
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Prestation read(String idPrestation) throws JadePersistenceException, PrestationException;

    /**
     * Permet de chercher des prestations selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrestationSearch search(PrestationSearch search) throws JadePersistenceException, PrestationException;

}
