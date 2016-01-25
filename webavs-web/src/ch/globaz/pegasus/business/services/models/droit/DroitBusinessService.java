/**
 * 
 */
package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;

/**
 * @author ECO
 * 
 */
public interface DroitBusinessService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la création d'une entité droit
     * 
     * @param droit
     *            La droit à créer
     * @return La droit créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public Droit create(Droit droit) throws JadePersistenceException, DroitException, DemandePrestationException,
            DossierException;

    /**
     * Permet la suppression d'une entité droit PC
     * 
     * @param droit
     *            La droit PC à supprimer
     * @return La droit PC supprimé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Droit delete(Droit droit) throws DroitException, JadePersistenceException;

}
