package ch.globaz.musca.business.services.models.passage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.business.models.PassageSearchModel;

/**
 * service de gestion de la persistance des données liées aux passage facturation
 * 
 * @author GMO
 */
public interface PassageModelService extends JadeApplicationService {
    /**
     * Charge un passage selon l'id passé en paramètre
     * 
     * @param idPassage
     *            l'id du passage qu'on veut chargé
     * @return PassageModel le modèle chargé depuis la DB
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public PassageModel read(String idPassage) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche sur les passages facturation
     * 
     * @param searchModel
     *            modèle de recherche PassageSearchModel
     * @return les résultats stockés dans le modèle de recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PassageSearchModel search(PassageSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;
}
