package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Service de gestion de la persistance des détails de prestation
 * 
 * @author gmo
 * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
 */
public interface DetailPrestationComplexModelService extends JadeApplicationService {

    /**
     * Compte les détails de prestation correspondant au modèle de recherche
     * <code>detailPrestationComplexSearchModel</code>
     * 
     * 
     * @param searchModel
     *            Modèle de recherche de détail prestation contenant les critères de recherche souhaités
     * @return le nombre d'enregistrement trouvés
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(DetailPrestationComplexSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Recherche les détails de prestation correspondant au modèle de recherche
     * <code>detailPrestationComplexSearchModel</code>
     * 
     * @param detailPrestationComplexSearchModel
     *            Modèle de recherche de détail prestation contenant les critères de recherche souhaités
     * @return résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
     */
    public DetailPrestationComplexSearchModel search(
            DetailPrestationComplexSearchModel detailPrestationComplexSearchModel) throws JadeApplicationException,
            JadePersistenceException;

}
