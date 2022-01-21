package globaz.osiris.eservices.ws;

import acor.xsd.standard.error.OriginType;
import ch.globaz.common.ws.ExceptionHandler;
import ch.globaz.common.ws.ExceptionMapper;
import ch.globaz.common.ws.configuration.ExceptionRequestInfo;
import ch.globaz.common.ws.configuration.RequestInfo;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.i18n.JadeI18n;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.exceptions.ESUnauthorizedException;
import globaz.prestation.acor.web.ws.TokenStandardErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@Slf4j
@ExceptionMapper("/es")
@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class ESApiRestExceptionHandler implements ExceptionHandler {

    @Override
    public Response generateResponse(final Exception e, final Response.ResponseBuilder responseBuilder, final HttpServletRequest request) {
        RequestInfo requestInfo = new RequestInfo(request);
        ExceptionRequestInfo exceptionRequestInfo = new ExceptionRequestInfo(requestInfo, e);
        mapESExceptionWithErrorStatus(e, responseBuilder);
        BSession session = BSessionUtil.getSessionFromThreadContext();
        LOG.error("Une erreur imprévue s'est produite.", e);
        String label = "Global error";
        if (session != null) {
            if (StringUtils.contains(requestInfo.getPathInfo(), "login")) {
                LOG.error("Une erreur est intervenue lors de l'obtention du token pour eServices Ferciam : ", e);
                label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), TokenStandardErrorUtil.ERROR_ES_TOKEN);
            } else if (StringUtils.contains(requestInfo.getPathInfo(), "retrieve")) {
                LOG.error("Une erreur est intervenue lors de la récupération des données pour eServices Ferciam : ", e);
                label = JadeI18n.getInstance().getMessage(session.getIdLangueISO(), TokenStandardErrorUtil.ERROR_ES_RETRIEVE);
            }
        }
        return responseBuilder.entity(TokenStandardErrorUtil.getStandardError(label, e, 1, OriginType.TECHNICAL_EXPORT)).build();
    }

    private void mapESExceptionWithErrorStatus(Exception e, Response.ResponseBuilder responseBuilder) {
        if (e instanceof ESUnauthorizedException) {
            responseBuilder.status(Response.Status.UNAUTHORIZED);
        } else if (e instanceof ESBadRequestException) {
            responseBuilder.status(Response.Status.BAD_REQUEST);
        }
    }
}
