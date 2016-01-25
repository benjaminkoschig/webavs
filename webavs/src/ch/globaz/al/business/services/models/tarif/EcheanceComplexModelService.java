package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.EcheanceComplexSearchModel;

/**
 * Service li� aux �ch�ance
 * 
 * @author PTA
 * 
 */
public interface EcheanceComplexModelService extends JadeApplicationService {
    /**
     * Recherche d'une �ch�ance
     * 
     * @param echeanceComplexSearchModel
     *            selon mod�le complexe de recherche d'une �ch�ance
     * @return EcheanceComplexModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EcheanceComplexSearchModel search(EcheanceComplexSearchModel echeanceComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

}
