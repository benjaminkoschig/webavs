package globaz.osiris.eservices.ws;

import globaz.osiris.eservices.dto.req.ESExtraitCompteREQ;
import globaz.osiris.eservices.dto.req.ESInfoFacturationREQ;
import globaz.osiris.eservices.dto.resp.ESExtraitCompteRESP;
import globaz.osiris.eservices.dto.resp.ESInfoFacturationRESP;
import globaz.osiris.eservices.service.ESService;
import globaz.osiris.eservices.token.ESTokenImpl;
import globaz.osiris.eservices.token.ESTokenServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.BiConsumer;

@Slf4j
@Path("/es/retrieve")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ESApiRestRetrieve {

    private final ESService service;

    public ESApiRestRetrieve() {
        service = new ESService();
    }

    /**
     * Web Service exposé pour récupérer les extraits de comptes dans le cadre des eServices Ferciam.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token header d'identification
     * @param dto json mappé en objet qui contient des informations sur les informations voulue par la requête
     * @return json avec un extrait de compte binaire
     */
    @POST
    @Path(value = "get_extrait_compte")
    public Response getExtraitCompte(@HeaderParam("authorization") String token, ESExtraitCompteREQ dto) {
        LOG.info("get_extrait_compte");
        ESTokenImpl esToken = ESTokenServiceImpl.getInstance().convertToken(token);
        ESExtraitCompteRESP response = service.getExtraitCompte(dto, esToken);
        return Response.ok(response).build();
    }

    /**
     * Web Service exposé pour récupérer les informations de facturations dans le cadre des eServices Ferciam.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token header d'identification
     * @param dto json mappé en objet qui contient des informations sur les informations voulue par la requête
     * @return json avec les informations de facturation
     */
    @POST
    @Path(value = "get_info_facturation")
    public Response getInfoFacturation(@HeaderParam("authorization") String token, ESInfoFacturationREQ dto) {
        LOG.info("get_info_facturation");
        ESTokenImpl esToken = ESTokenServiceImpl.getInstance().convertToken(token);
        ESInfoFacturationRESP response = service.getInfoFacturation(dto, esToken);
        return Response.ok(response).build();
    }

    private <T> Response execute(String token, T object, BiConsumer<T, ESTokenImpl> consumer) {
        ESTokenImpl esToken = ESTokenServiceImpl.getInstance().convertToken(token);
        consumer.accept(object, esToken);
        return Response.ok("{}").build();
    }

}
