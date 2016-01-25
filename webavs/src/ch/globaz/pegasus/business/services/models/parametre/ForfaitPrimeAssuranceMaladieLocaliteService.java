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
    public int count(ForfaitPrimeAssuranceMaladieLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des ForfaitPrimeAssuranceMaladieLocaliteService selon un mod�le de crit�res.
     * 
     * @param ForfaitPrimeAssuranceMaladieLocaliteService
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ForfaitPrimeAssuranceMaladieLocaliteSearch search(ForfaitPrimeAssuranceMaladieLocaliteSearch search)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;
}
