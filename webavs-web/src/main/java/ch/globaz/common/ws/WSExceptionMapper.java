package ch.globaz.common.ws;

import acor.rentes.xsd.standard.error.OriginType;
import ch.globaz.common.acor.Acor2020StandardErrorUtil;
import globaz.prestation.acor.PRACORException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Ce provider permet de traiter les expections issues des WebService rest.
 */
@Provider
@Slf4j
public class WSExceptionMapper implements ExceptionMapper<Exception> {

    @Context private HttpServletRequest request;
    @Context private HttpServletResponse response;
    @Context private UriInfo uriInfo;

    @Override
    public Response toResponse(Exception e) {
        LOG.error("WebService Error : ", e);
        LOG.info("RequestURI : {}",request.getRequestURI());
        LOG.info("ContextPath : {}",request.getContextPath());
        LOG.info("PathInfo : {}",request.getPathInfo());
        LOG.info("QueryString : {}",request.getQueryString());
        LOG.info("AuthType : {}",request.getAuthType());
        LOG.info("RemoteUser : {}",request.getRemoteUser());
        LOG.info("Method : {}",request.getMethod());
        LOG.info("LocalAddr : {}",request.getLocalAddr());
        LOG.info("RequestedSessionId : {}",request.getRequestedSessionId());
        LOG.info("ServerName : {}",request.getServerName());
        LOG.info("LocalAddr : {}",request.getLocalAddr());
        LOG.info("RemoteAddr : {}",request.getRemoteAddr());
        LOG.info("Scheme : {}",request.getScheme());

        LOG.info("Path : {}",uriInfo.getPath());
        LOG.info("RequestUri : {}",uriInfo.getRequestUri().toString());
        LOG.info("BaseUri : {}",uriInfo.getBaseUri().toString());
        LOG.info("AbsolutePath : {}",uriInfo.getAbsolutePath().toString());
        LOG.info("PathSegments : {}",uriInfo.getPathSegments().toString());

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        responseBuilder.type(MediaType.APPLICATION_JSON_TYPE);
        if (e instanceof PRACORException) {
            return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(e.getMessage(), e, 2, OriginType.TECHNICAL_EXPORT)).build();
        } else {
            return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_IMPORT_UNKOWN, e, 1, OriginType.TECHNICAL_IMPORT)).build();
        }
    }
}
