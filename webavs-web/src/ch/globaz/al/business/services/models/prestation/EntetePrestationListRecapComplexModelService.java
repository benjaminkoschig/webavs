package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexSearchModel;

/**
 * Service de gestion de la persistance des entête des prestations liées à une récap
 * 
 * @author GMO
 * 
 */
public interface EntetePrestationListRecapComplexModelService extends JadeApplicationService {

    /**
     * Recherche les prestations correspondant au modèle de recherche <code>prestationSearchModel</code>
     * 
     * @param prestationSearchModel
     *            Modèle de recherche d'entête prestation contenant les critères de recherche souhaités
     * @return résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexModel
     */
    public EntetePrestationListRecapComplexSearchModel search(
            EntetePrestationListRecapComplexSearchModel prestationSearchModel) throws JadeApplicationException,
            JadePersistenceException;

}
