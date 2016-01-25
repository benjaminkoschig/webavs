package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeEtendu;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeEtenduSearch;

public interface TaxeJournaliereHomeEtenduService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(TaxeJournaliereHomeEtenduSearch search) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idTaxeJournaliereHome
     *            L'identifiant de le taxeJournaliereHomee à charger en mémoire
     * @return le taxeJournaliereHome chargé en mémoire
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TaxeJournaliereHomeEtendu read(String idTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param taxeJournaliereHomeSearch
     * @return La recherche effectué
     * @throws TaxeJournaliereHomeException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public TaxeJournaliereHomeEtenduSearch search(TaxeJournaliereHomeEtenduSearch taxeJournaliereHomeSearch)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException;

}
