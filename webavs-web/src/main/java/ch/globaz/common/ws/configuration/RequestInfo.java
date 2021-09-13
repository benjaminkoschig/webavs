package ch.globaz.common.ws.configuration;

import lombok.Data;
import lombok.Value;

import javax.servlet.http.HttpServletRequest;

/**
 * Permet d'avoir des informations sur une request qui sont facilement sérializable(DTO).
 */
@Value
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

    public RequestInfo(HttpServletRequest request) {
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
    }
}
