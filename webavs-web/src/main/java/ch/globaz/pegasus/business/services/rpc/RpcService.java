package ch.globaz.pegasus.business.services.rpc;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;

public interface RpcService extends JadeApplicationService {
    String loadXmlByIdAnnonce(String idAnnonce) throws ValidationException;

    void testPlausiForDecision(String... idDecision) throws VariableMetierException, JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
