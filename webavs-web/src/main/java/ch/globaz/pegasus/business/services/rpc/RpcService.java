package ch.globaz.pegasus.business.services.rpc;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.common.exceptions.ValidationException;

public interface RpcService extends JadeApplicationService {
    String loadXmlByIdAnnonce(String idAnnonce) throws ValidationException;

    void testPlausiForDecision(String idDecision);
}
