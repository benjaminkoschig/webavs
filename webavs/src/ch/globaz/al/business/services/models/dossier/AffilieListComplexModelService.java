package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;

/**
 * Services li�s � la persistance des affili� des dossier
 * 
 * @author jts
 */
public interface AffilieListComplexModelService extends JadeApplicationService {

    /**
     * Recherche les num�ros d'affili�s correspondant au crit�res du mod�le de recherche
     * 
     * @param searchModel
     *            mod�le de recherche
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AffilieListComplexSearchModel search(AffilieListComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

}
