package globaz.osiris.eservices.ws;

import globaz.osiris.eservices.service.ESService;
import globaz.osiris.eservices.dto.req.ESLoginREQ;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/es/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ESApiRestLogin {

    private final ESService service;

    public ESApiRestLogin() {
        service = new ESService();
    }

    /**
     * Web Service exposé pour récupérer un token utilisé dans le cadre des eServices Ferciam.
     *
     * @param dto json mappé en objet qui contient le username et password pour lequel on veut générer un token
     * @return token String d'identification
     */
    @POST
    @Path(value = "/get_token")
    public Response getToken(ESLoginREQ dto) {
        LOG.info("get_token");
        return execute(service.getToken(dto));
    }

    private Response execute(String token) {
        return Response.ok(token).build();
    }

}
