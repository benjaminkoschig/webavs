package globaz.naos.web.ws;

import globaz.naos.web.exceptions.AFUnauthorizedException;
import globaz.naos.web.service.AFLoginService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Function;

@Slf4j
@Path("/af/login")
@Produces(MediaType.APPLICATION_JSON)
public class AFApiRestLogin {
    private final AFLoginService afLoginService;

    public AFApiRestLogin() {
        afLoginService = new AFLoginService();
    }

    /**
     * Endpoint exposé pour récupérer le token d'authentification pour l'application Naos.
     *
     * @param authorization HTTP Basic Authentification qui contient le username:password pour lequel on veut générer un token
     * @return token d'identification
     */
    @POST
    @Path(value = "/get_token")
    public Response getToken(@HeaderParam("authorization") String authorization) {
        LOG.info("get_token");
        return execute(authorization, afLoginService::getToken);
    }

    /**
     * Execution de l'action du Webservice et création de la réponse.
     *
     * @param authorization header d'identification
     * @param function      méthode d'exécution à appeler
     * @return Response
     */
    private <T, R> Response execute(T authorization, Function<T, R> function) {
        try {
            return Response.ok(function.apply(authorization)).build();
        } catch (AFUnauthorizedException pue) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
