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
        LOG.info("getRequestURI : {}",request.getRequestURI());
        LOG.info("getContextPath : {}",request.getContextPath());
        LOG.info("getPathInfo : {}",request.getPathInfo());
        LOG.info("getQueryString : {}",request.getQueryString());
        LOG.info("getAuthType : {}",request.getAuthType());
        LOG.info("getRemoteUser : {}",request.getRemoteUser());
        LOG.info("getMethod : {}",request.getMethod());
        LOG.info("getLocalAddr : {}",request.getLocalAddr());
        LOG.info("getRequestedSessionId : {}",request.getRequestedSessionId());
        LOG.info("getServerName : {}",request.getServerName());

        LOG.info("getPath : {}",uriInfo.getPath());
        LOG.info("getPath : {}",uriInfo.getPath());
        LOG.info("getRequestUri : {}",uriInfo.getRequestUri().toString());
        LOG.info("getBaseUri : {}",uriInfo.getBaseUri().toString());
        LOG.info("getAbsolutePath : {}",uriInfo.getAbsolutePath().toString());
        LOG.info("getPathSegments : {}",uriInfo.getPathSegments().toString());
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        responseBuilder.type(MediaType.APPLICATION_JSON_TYPE);
        if (e instanceof PRACORException) {
            return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(e.getMessage(), e, 2, OriginType.TECHNICAL_EXPORT)).build();
        } else {
            return responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_IMPORT_UNKOWN, e, 1, OriginType.TECHNICAL_IMPORT)).build();
        }
    }
}
