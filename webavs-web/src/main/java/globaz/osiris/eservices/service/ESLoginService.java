package globaz.osiris.eservices.service;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.osiris.application.CAApplication;
import globaz.osiris.eservices.dto.ESTokenDTO;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.exceptions.ESUnauthorizedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ESLoginService {

    public ESLoginService() {
    }

    private BSession tryConnect(String username, String password) {
        try {
            BSession session = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            session.connect(username, password);

            return session;
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération du token : ", e);
            throw new ESUnauthorizedException(e);
        }
    }

    private Boolean isFormatValid(String[] authorizationSplitted) {
        if (authorizationSplitted.length != 2) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public ESTokenDTO getToken(String authorization) {
        try {
            if (authorization.startsWith("Basic ")) {
                authorization = authorization.substring(6);
            }

            String[] authorizationSplitted = authorization.split(":");

            if (!isFormatValid(authorizationSplitted)) {
                throw new ESBadRequestException("Chaine d'authorisation incomplète");
            }

            BSession session = tryConnect(authorizationSplitted[0], authorizationSplitted[1]);

            return new ESTokenDTO(ESTokenServiceImpl.createTokenES(session));

        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération du token : ", e);
            throw new ESUnauthorizedException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
}