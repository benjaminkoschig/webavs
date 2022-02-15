package globaz.osiris.eservices.ws;

import globaz.osiris.eservices.service.ESLoginService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Function;

@Slf4j
@Path("/es/login")
@Produces(MediaType.APPLICATION_JSON)
public class ESApiRestLogin {

    private final ESLoginService esLoginService;

    public ESApiRestLogin() {
        esLoginService = new ESLoginService();
    }

    /**
     * Web Service expos� pour r�cup�rer un token utilis� dans le cadre des eServices Ferciam.
     *
     * @param authorization HTTP Basic Authentification qui contient le username:password pour lequel on veut g�n�rer un token
     * @return token d'identification
     */
    @GET
    @Path(value = "/get_token")
    public Response getToken(@HeaderParam("authorization") String authorization) {
        LOG.info("get_token");
        return execute(authorization, esLoginService::getToken);
    }

    /**
     * Execution de l'action du Webservice et cr�ation de la r�ponse.
     *
     * @param authorization header d'identification
     * @param function m�thode d'ex�cution � appeler
     * @return Response
     */
    private <T, R> Response execute(T authorization, Function<T, R> function) {
        return Response.ok(function.apply(authorization)).build();
    }

}
