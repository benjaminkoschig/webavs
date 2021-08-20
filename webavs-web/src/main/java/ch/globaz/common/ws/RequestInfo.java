package ch.globaz.common.ws;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

/**
 * Permet d'avoir des informations sur une request qui sont facilement sérializable(DTO)
 */
@Data
public class RequestInfo {
    private final String requestURI;
    private final String contextPath;
    private final String pathInfo;
    private final String queryString;
    private final String authType;
    private final String remoteUser;
    private final String method;
    private final String localAddr;
    private final String requestedSessionId;
    private final String serverName;
    private final String remoteAddr;

    private final String scheme;
    private final String path;
    private final String baseUri;
    private final String absolutePath;
    private final String pathSegments;

    public RequestInfo(HttpServletRequest request, UriInfo uriInfo) {
        this.requestURI = request.getRequestURI();
        this.contextPath = request.getContextPath();
        this.pathInfo = request.getPathInfo();
        this.queryString = request.getQueryString();
        this.authType = request.getAuthType();
        this.remoteUser = request.getRemoteUser();
        this.method = request.getMethod();
        this.localAddr = request.getLocalAddr();
        this.requestedSessionId = request.getRequestedSessionId();
        this.serverName = request.getServerName();
        this.remoteAddr = request.getRemoteAddr();
        this.scheme = request.getScheme();

        this.path = uriInfo.getPath();
        this.baseUri = uriInfo.getBaseUri().toString();
        this.absolutePath = uriInfo.getAbsolutePath().toString();
        this.pathSegments = uriInfo.getPathSegments().toString();
    }
}
