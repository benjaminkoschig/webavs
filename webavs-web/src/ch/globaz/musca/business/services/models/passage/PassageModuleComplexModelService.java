package ch.globaz.musca.business.services.models.passage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.business.models.PassageModuleComplexSearchModel;

/**
 * 
 * Services persistence d'un modèle <code>PasasgeModuleComplexModel</code>
 * 
 */
public interface PassageModuleComplexModelService extends JadeApplicationService {
    /**
     * Récupère les données du passage correspondant à <code>idPassage</code>
     * 
     * @param idPassage
     *            Id du passage à charger
     * @return Le modèle du passage chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             fair
     */
    public PassageModuleComplexModel read(String idPassage) throws JadeApplicationException, JadePersistenceException;

    /**
     * Effectue une recherche selon le modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            modèle de recherche de passage
     * @return Résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PassageModuleComplexSearchModel search(PassageModuleComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}
