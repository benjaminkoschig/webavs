package globaz.naos.web.service;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.naos.application.AFApplication;
import globaz.naos.web.DTO.AFTokenDTO;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.naos.web.exceptions.AFUnauthorizedException;
import globaz.naos.web.token.AFTokenServiceImpl;
import globaz.webavs.common.ws.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AFLoginService {
    public AFLoginService() {

    }

    private BSession tryConnect(String username, String password) {
        BSession session = null;
        try {
            session = new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);
            session.connect(username, password);
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération du token : ", e);
            throw new AFUnauthorizedException(e);
        }
        return session;
    }

    private Boolean isFormatValid(String[] authorizationSplitted) {
        if (authorizationSplitted.length != 2) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public AFTokenDTO getToken(String authorization) {
        BSession session = null;
        try {
            if (authorization.startsWith("Basic ")) {
                authorization = authorization.substring(6);
            }

            String[] authorizationSplitted = authorization.split(":");

            if (!isFormatValid(authorizationSplitted)) {
                throw new AFBadRequestException("Chaine d'authorisation incomplète");
            }

            session = tryConnect(authorizationSplitted[0], authorizationSplitted[1]);

            if (!SecurityUtils.hasAccessToWS(session)) {
                throw new AFUnauthorizedException("Accès au WebService refusé !");
            }
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération du token :", e);
            throw new AFUnauthorizedException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return new AFTokenDTO(AFTokenServiceImpl.createTokenAF(session));
    }
}
