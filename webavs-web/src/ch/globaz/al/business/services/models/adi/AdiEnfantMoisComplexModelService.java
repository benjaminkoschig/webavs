package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;

/**
 * Service de la persistance AdiEnfantMoisComplexModel
 * 
 * @author GMO
 * 
 */
public interface AdiEnfantMoisComplexModelService extends JadeApplicationService {

    /**
     * Recherche sur le mod�le
     * 
     * @param adiEnfantMoisComplexSearchModel
     *            mod�le de recherche contenant les crit�res
     * @return mod�le de recherche avec les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisComplexSearchModel search(AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;
}
