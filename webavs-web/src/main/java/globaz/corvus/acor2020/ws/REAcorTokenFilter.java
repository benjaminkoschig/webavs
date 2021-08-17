package globaz.corvus.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import ch.globaz.common.acor.Acor2020StandardErrorUtil;
import globaz.corvus.acor2020.ws.token.REAcor2020Token;
import globaz.corvus.acor2020.ws.token.REAcor2020TokenService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Objects;

@Provider
@PreMatching
@Slf4j
public class REAcorTokenFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!Objects.equals(HttpMethod.OPTIONS, requestContext.getMethod())) {
            String token = requestContext.getHeaderString("authorization");
            REAcor2020Token acor2020Token = REAcor2020TokenService.getInstance().getToken(token);
            if (Objects.isNull(acor2020Token)) {
                LOG.error(Acor2020StandardErrorUtil.TOKEN_INVALIDE);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                responseBuilder.type(MediaType.APPLICATION_JSON_TYPE);
                Response response = responseBuilder.entity(Acor2020StandardErrorUtil.getStandardError(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
                requestContext.abortWith(response);
            }
        }
    }
}
