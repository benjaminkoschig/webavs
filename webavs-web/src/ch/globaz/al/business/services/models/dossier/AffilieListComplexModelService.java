package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;

/**
 * Services liés à la persistance des affilié des dossier
 * 
 * @author jts
 */
public interface AffilieListComplexModelService extends JadeApplicationService {

    /**
     * Recherche les numéros d'affiliés correspondant au critères du modèle de recherche
     * 
     * @param searchModel
     *            modèle de recherche
     * @return Résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AffilieListComplexSearchModel search(AffilieListComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

}
