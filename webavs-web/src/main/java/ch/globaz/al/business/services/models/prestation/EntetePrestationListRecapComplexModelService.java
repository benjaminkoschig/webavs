package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexSearchModel;

/**
 * Service de gestion de la persistance des ent�te des prestations li�es � une r�cap
 * 
 * @author GMO
 * 
 */
public interface EntetePrestationListRecapComplexModelService extends JadeApplicationService {

    /**
     * Recherche les prestations correspondant au mod�le de recherche <code>prestationSearchModel</code>
     * 
     * @param prestationSearchModel
     *            Mod�le de recherche d'ent�te prestation contenant les crit�res de recherche souhait�s
     * @return r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexModel
     */
    public EntetePrestationListRecapComplexSearchModel search(
            EntetePrestationListRecapComplexSearchModel prestationSearchModel) throws JadeApplicationException,
            JadePersistenceException;

}
