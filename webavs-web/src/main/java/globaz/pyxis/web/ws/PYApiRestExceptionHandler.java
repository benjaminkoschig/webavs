package globaz.pyxis.web.ws;

import acor.xsd.standard.error.OriginType;
import ch.globaz.common.ws.ExceptionHandler;
import ch.globaz.common.ws.ExceptionMapper;
import ch.globaz.common.ws.configuration.ExceptionRequestInfo;
import ch.globaz.common.ws.configuration.RequestInfo;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.i18n.JadeI18n;
import ch.globaz.common.ws.token.TokenStandardErrorUtil;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYInternalException;
import globaz.pyxis.web.exceptions.PYUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@Slf4j
@ExceptionMapper("/py")
@SuppressWarnings("unused" /*Cette classe est appelée par réflexion par la class WSConfiguration */)
public class PYApiRestExceptionHandler implements ExceptionHandler {

    @Override
    public Response generateResponse(final Exception e, Response.ResponseBuilder responseBuilder, final HttpServletRequest request) {
        RequestInfo requestInfo = new RequestInfo(request);
        ExceptionRequestInfo exceptionRequestInfo = new ExceptionRequestInfo(requestInfo, e);
        responseBuilder = mapPYExceptionWithErrorStatus(e, responseBuilder);
        BSession session = BSessionUtil.getSessionFromThreadContext();
        LOG.error("Une erreur imprévue s'est produite.", e);
        String label = "Global error";
        if (session != null) {
            if (StringUtils.contains(requestInfo.getPathInfo(), "login")) {
                LOG.error("Une erreur est intervenue lors de l'obtention du token pour le web service Pyxis : ", e);
                label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), TokenStandardErrorUtil.ERROR_PY_TOKEN);
            } else if (StringUtils.contains(requestInfo.getPathInfo(), "retrieve")) {
                LOG.error("Une erreur est intervenue lors de la récupération des données pour le web service Pyxis : ", e);
                label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), TokenStandardErrorUtil.ERROR_PY_EXECUTE);
            }
        }
        return responseBuilder.entity(TokenStandardErrorUtil.getStandardError(label, e, 1, OriginType.TECHNICAL_EXPORT)).build();
    }

    private Response.ResponseBuilder mapPYExceptionWithErrorStatus(Exception e, Response.ResponseBuilder responseBuilder) {
        if (e instanceof PYUnauthorizedException) {
            return responseBuilder.status(Response.Status.UNAUTHORIZED);
        } else if (e instanceof PYBadRequestException) {
            return responseBuilder.status(Response.Status.BAD_REQUEST);
        } else if (e instanceof PYInternalException) {
            return responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
        } else {
            return responseBuilder;
        }
    }
}
