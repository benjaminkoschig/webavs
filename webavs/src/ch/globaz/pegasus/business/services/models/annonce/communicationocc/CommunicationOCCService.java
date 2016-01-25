package ch.globaz.pegasus.business.services.models.annonce.communicationocc;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCCSearch;

public interface CommunicationOCCService extends JadeApplicationService {
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
    public int count(CommunicationOCCSearch search) throws PrestationException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une communication OCC
     * 
     * @param idCommunication
     *            L'identifiant de la communication � charger en m�moire
     * @return La communication charg�e en m�moire
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CommunicationOCC read(String idCommunicationOCC) throws JadePersistenceException, PrestationException;

    /**
     * Permet de chercher des communictions selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CommunicationOCCSearch search(CommunicationOCCSearch search) throws JadePersistenceException,
            PrestationException;
}
