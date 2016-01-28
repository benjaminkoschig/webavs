package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;

public interface CleanDecisionsService extends JadeApplicationService {

    /**
     * Service de suppression des décisionsAprèsCalcul pour une cversion de droit données
     * 
     * @param idVersionDroit
     *            , l'id de la version du droit concerné
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void deleteDecisionsApresCalculForVersion(String idVersionDroit) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException;

    public abstract void deleteDecisionsSuppressionForVersion(String idVersionDroit) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException;

    public abstract void deleteDecisionsForVersion(String idVersionDroit) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
