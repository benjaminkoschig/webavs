package ch.globaz.prestation.business.services.models.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestation;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestationSearch;

public interface SimpleDemandePrestationService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws DemandePrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDemandePrestationSearch search) throws DemandePrestationException, JadePersistenceException;

    /**
     * 
     * @param demandePrestation
     * @return
     * @throws DemandePrestationException
     * @throws JadePersistenceException
     */
    public SimpleDemandePrestation create(SimpleDemandePrestation demandePrestation) throws DemandePrestationException,
            JadePersistenceException;

    /**
     * 
     * @param demandePrestation
     * @return
     * @throws DemandePrestationException
     * @throws JadePersistenceException
     */
    public SimpleDemandePrestation delete(SimpleDemandePrestation demandePrestation) throws DemandePrestationException,
            JadePersistenceException;

    /**
     * 
     * @param idDemandePrestation
     * @return
     * @throws DemandePrestationException
     * @throws JadePersistenceException
     */
    public SimpleDemandePrestation read(String idDemandePrestation) throws DemandePrestationException,
            JadePersistenceException;

    /**
     * 
     * @param demandePrestation
     * @return
     * @throws DemandePrestationException
     * @throws JadePersistenceException
     */
    public SimpleDemandePrestation update(SimpleDemandePrestation demandePrestation) throws DemandePrestationException,
            JadePersistenceException;
}
