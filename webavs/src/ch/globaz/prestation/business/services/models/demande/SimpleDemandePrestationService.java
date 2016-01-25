package ch.globaz.prestation.business.services.models.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestation;
import ch.globaz.prestation.business.models.demande.SimpleDemandePrestationSearch;

public interface SimpleDemandePrestationService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws DemandePrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
