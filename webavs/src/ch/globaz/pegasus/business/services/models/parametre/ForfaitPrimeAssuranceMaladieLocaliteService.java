package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;

/**
 * @author DMA
 * @date 16 nov. 2010
 */
public interface ForfaitPrimeAssuranceMaladieLocaliteService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(ForfaitPrimeAssuranceMaladieLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des ForfaitPrimeAssuranceMaladieLocaliteService selon un modèle de critères.
     * 
     * @param ForfaitPrimeAssuranceMaladieLocaliteService
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ForfaitPrimeAssuranceMaladieLocaliteSearch search(ForfaitPrimeAssuranceMaladieLocaliteSearch search)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;
}
