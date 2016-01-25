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
     * Recherche sur le modèle
     * 
     * @param adiEnfantMoisComplexSearchModel
     *            modèle de recherche contenant les critères
     * @return modèle de recherche avec les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiEnfantMoisComplexSearchModel search(AdiEnfantMoisComplexSearchModel adiEnfantMoisComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;
}
