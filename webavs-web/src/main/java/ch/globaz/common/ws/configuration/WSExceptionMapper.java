package ch.globaz.common.ws.configuration;

import ch.globaz.common.ws.ExceptionHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.Optional;

/**
 * Ce provider permet de traiter les excpetions issues des WebService rest.
 */
@Provider
@Slf4j
public class WSExceptionMapper implements ExceptionMapper<Exception> {

    @Context
    @Setter
    private HttpServletRequest request;
    @Context
    @Setter
    private HttpServletResponse response;

    @Override
    public Response toResponse(Exception e) {
        LOG.error("WebService Error : ", e);
        LOG.info("RequestURI : {}", request.getRequestURI());
        LOG.info("ContextPath : {}", request.getContextPath());
        LOG.info("PathInfo : {}", request.getPathInfo());
        LOG.info("QueryString : {}", request.getQueryString());
        LOG.info("AuthType : {}", request.getAuthType());
        LOG.info("RemoteUser : {}", request.getRemoteUser());
        LOG.info("Method : {}", request.getMethod());
        LOG.info("LocalAddr : {}", request.getLocalAddr());
        LOG.info("ServerName : {}", request.getServerName());
        LOG.info("LocalAddr : {}", request.getLocalAddr());
        LOG.info("RemoteAddr : {}", request.getRemoteAddr());
        LOG.info("Scheme : {}", request.getScheme());

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        responseBuilder.type(MediaType.APPLICATION_JSON_TYPE);

        Optional<ExceptionHandler> exceptionHandler = resolveExceptionHandler(WSConfiguration.getInstance());

        if (exceptionHandler.isPresent()) {
            return exceptionHandler.get().generateResponse(e, responseBuilder, request);
        } else {
            RequestInfo requestInfo = new RequestInfo(request);
            ExceptionRequestInfo exceptionRequestInfo = new ExceptionRequestInfo(requestInfo, e);
            return responseBuilder.entity(exceptionRequestInfo).build();
        }
    }

    private Optional<ExceptionHandler> resolveExceptionHandler(final WSConfiguration wsConfiguration) {
        return wsConfiguration.getExceptionMapperClasses().entrySet()
                              .stream()
                              .filter(o -> (request.getPathInfo().startsWith(o.getKey()) || request.getPathInfo().startsWith("/" + o.getKey())))
                              .map(Map.Entry::getValue)
                              .findFirst();
    }
}
