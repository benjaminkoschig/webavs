package globaz.pyxis.web.service;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.web.DTO.PYTokenDTO;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYUnauthorizedException;
import globaz.pyxis.web.token.PYTokenServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PYLoginService {
    public PYLoginService(){

    }

    private BSession tryConnect(String username, String password) {
        BSession session = null;
        try {
            session = new BSession(TIApplication.DEFAULT_APPLICATION_PYXIS);
            session.connect(username, password);
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération du token : ", e);
            throw new PYUnauthorizedException(e);
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

    public PYTokenDTO getToken(String authorization) {
        BSession session = null;
        try {
            if (authorization.startsWith("Basic ")) {
                authorization = authorization.substring(6);
            }

            String[] authorizationSplitted = authorization.split(":");

            if (!isFormatValid(authorizationSplitted)) {
                throw new PYBadRequestException("Chaine d'authorisation incomplète");
            }

            session = tryConnect(authorizationSplitted[0], authorizationSplitted[1]);
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la récupération du token :", e);
            throw new PYUnauthorizedException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return new PYTokenDTO(PYTokenServiceImpl.createTokenPY(session));
    }
}
