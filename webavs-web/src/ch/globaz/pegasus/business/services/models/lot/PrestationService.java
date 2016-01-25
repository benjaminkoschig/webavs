package ch.globaz.pegasus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;

public interface PrestationService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PrestationSearch search) throws PrestationException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une prestattion
     * 
     * @param idPrestation
     *            L'identifiant de la prestation à charger en mémoire
     * @return La prestation chargée en mémoire
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Prestation read(String idPrestation) throws JadePersistenceException, PrestationException;

    /**
     * Permet de chercher des prestations selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PrestationSearch search(PrestationSearch search) throws JadePersistenceException, PrestationException;

}
