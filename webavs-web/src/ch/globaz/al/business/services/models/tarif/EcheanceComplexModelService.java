package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.EcheanceComplexSearchModel;

/**
 * Service lié aux échéance
 * 
 * @author PTA
 * 
 */
public interface EcheanceComplexModelService extends JadeApplicationService {
    /**
     * Recherche d'une échéance
     * 
     * @param echeanceComplexSearchModel
     *            selon modèle complexe de recherche d'une échéance
     * @return EcheanceComplexModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EcheanceComplexSearchModel search(EcheanceComplexSearchModel echeanceComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;

}
