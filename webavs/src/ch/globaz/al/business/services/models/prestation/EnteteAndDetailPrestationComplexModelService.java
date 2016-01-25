package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexSearchModel;

/**
 * Service de gestion de la persistance des détails de prestation
 * 
 * @author gmo
 * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
 */
public interface EnteteAndDetailPrestationComplexModelService extends JadeApplicationService {
    /**
     * Recherche les entête ainsi que leur détail <code>enteteAndDetailPrestationComplexSearchModel</code>
     * 
     * @param enteteAndDetailPrestationComplexSearchModel
     *            Modèle de recherche de détail prestation contenant les critères de recherche souhaités
     * @return résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
     */
    public EnteteAndDetailPrestationComplexSearchModel search(
            EnteteAndDetailPrestationComplexSearchModel enteteAndDetailPrestationComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

}
