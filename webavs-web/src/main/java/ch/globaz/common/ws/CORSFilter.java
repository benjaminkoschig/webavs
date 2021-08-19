package ch.globaz.common.ws;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
@Slf4j
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.info("CORSFilter");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpResponse.addHeader("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.addHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        httpResponse.addHeader("Access", "HEAD, GET, OPTIONS");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getMethod().contentEquals("OPTIONS")) {
            httpResponse.setStatus(Response.Status.NO_CONTENT.getStatusCode());
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
