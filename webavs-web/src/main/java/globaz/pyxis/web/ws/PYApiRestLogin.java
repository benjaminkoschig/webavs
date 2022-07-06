package globaz.pyxis.web.ws;

import globaz.pyxis.web.service.PYLoginService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Function;

@Slf4j
@Path("/py/login")
@Produces(MediaType.APPLICATION_JSON)
public class PYApiRestLogin {
    private final PYLoginService pyLoginService;

    public PYApiRestLogin() {
        pyLoginService = new PYLoginService();
    }

    /**
     * Endpoint exposé pour récupérer le token d'authentification pour l'application Pyxis.
     *
     * @param authorization HTTP Basic Authentification qui contient le username:password pour lequel on veut générer un token
     * @return token d'identification
     */
    @GET
    @Path(value="/get_token")
    public Response getToken(@HeaderParam("authorization") String authorization) {
        LOG.info("get_token");
        return execute(authorization, pyLoginService::getToken);
    }

    /**
     * Execution de l'action du Webservice et création de la réponse.
     *
     * @param authorization header d'identification
     * @param function méthode d'exécution à appeler
     * @return Response
     */
    private <T, R> Response execute(T authorization, Function<T, R> function) {
        return Response.ok(function.apply(authorization)).build();
    }
}
