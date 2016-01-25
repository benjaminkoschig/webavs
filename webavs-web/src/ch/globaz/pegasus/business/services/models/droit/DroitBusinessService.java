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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DroitSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� droit
     * 
     * @param droit
     *            La droit � cr�er
     * @return La droit cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public Droit create(Droit droit) throws JadePersistenceException, DroitException, DemandePrestationException,
            DossierException;

    /**
     * Permet la suppression d'une entit� droit PC
     * 
     * @param droit
     *            La droit PC � supprimer
     * @return La droit PC supprim�
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Droit delete(Droit droit) throws DroitException, JadePersistenceException;

}
