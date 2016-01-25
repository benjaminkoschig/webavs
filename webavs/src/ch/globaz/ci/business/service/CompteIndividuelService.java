package ch.globaz.ci.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.ci.business.models.CompteIndividuelSearchModel;

/**
 * Service de gestion de persistance des données des ci
 * 
 * @author GMO
 * 
 */
public interface CompteIndividuelService extends JadeApplicationService {

    /**
     * Recherche des données CI de base
     * 
     * @param ciSearchModel
     *            Le modèle de recherche
     * @return Le modèle de recherche avec les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CompteIndividuelSearchModel search(CompteIndividuelSearchModel ciSearchModel)
            throws JadeApplicationException, JadePersistenceException;
}
