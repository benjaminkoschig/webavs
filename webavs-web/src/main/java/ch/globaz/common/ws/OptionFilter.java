package ch.globaz.common.ws;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class OptionFilter implements ContainerRequestFilter {
    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if (HttpMethod.OPTIONS.equals(requestContext.getMethod())) {
            requestContext.abortWith(Response.status(Response.Status.NO_CONTENT).build());
        }
    }
}
