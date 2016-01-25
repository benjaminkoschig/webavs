package ch.globaz.ci.business.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.ci.business.models.CompteIndividuelSearchModel;

/**
 * Service de gestion de persistance des donn�es des ci
 * 
 * @author GMO
 * 
 */
public interface CompteIndividuelService extends JadeApplicationService {

    /**
     * Recherche des donn�es CI de base
     * 
     * @param ciSearchModel
     *            Le mod�le de recherche
     * @return Le mod�le de recherche avec les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CompteIndividuelSearchModel search(CompteIndividuelSearchModel ciSearchModel)
            throws JadeApplicationException, JadePersistenceException;
}
