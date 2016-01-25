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
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws LoyerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(TaxeJournaliereHomeEtenduSearch search) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un versionDroit
     * 
     * @param idTaxeJournaliereHome
     *            L'identifiant de le taxeJournaliereHomee � charger en m�moire
     * @return le taxeJournaliereHome charg� en m�moire
     * @throws TaxeJournaliereHomeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TaxeJournaliereHomeEtendu read(String idTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet la recherche d'apr�s les param�tres de recherche
     * 
     * @param taxeJournaliereHomeSearch
     * @return La recherche effectu�
     * @throws TaxeJournaliereHomeException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public TaxeJournaliereHomeEtenduSearch search(TaxeJournaliereHomeEtenduSearch taxeJournaliereHomeSearch)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException;

}
