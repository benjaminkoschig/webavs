package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexSearchModel;

/**
 * Service de gestion de la persistance des d�tails de prestation
 * 
 * @author gmo
 * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
 */
public interface EnteteAndDetailPrestationComplexModelService extends JadeApplicationService {
    /**
     * Recherche les ent�te ainsi que leur d�tail <code>enteteAndDetailPrestationComplexSearchModel</code>
     * 
     * @param enteteAndDetailPrestationComplexSearchModel
     *            Mod�le de recherche de d�tail prestation contenant les crit�res de recherche souhait�s
     * @return r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
     */
    public EnteteAndDetailPrestationComplexSearchModel search(
            EnteteAndDetailPrestationComplexSearchModel enteteAndDetailPrestationComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

}
