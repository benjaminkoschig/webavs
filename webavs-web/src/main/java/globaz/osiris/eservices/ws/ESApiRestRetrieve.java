package globaz.osiris.eservices.ws;

import globaz.osiris.eservices.dto.ESInfoFacturationDTO;
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
     * Web Service expos� pour r�cup�rer les extraits de comptes dans le cadre des eServices Ferciam.
     * On contr�le le token pass� en param�tre pour authoriser l'appel.
     *
     * @param token header d'identification
     * @param dto json mapp� en objet qui contient des informations sur les informations voulue par la requ�te
     * @return json avec un extrait de compte binaire
     */
    @POST
    @Path(value = "get_extrait_compte")
    public Response getExtraitCompte(@HeaderParam("authorization") String token, ESInfoFacturationDTO dto) {
        LOG.info("get_extrait_compte");
        return execute(token, dto, service::getExtraitCompte);
    }

    /**
     * Web Service expos� pour r�cup�rer les informations de facturations dans le cadre des eServices Ferciam.
     * On contr�le le token pass� en param�tre pour authoriser l'appel.
     *
     * @param token header d'identification
     * @param dto json mapp� en objet qui contient des informations sur les informations voulue par la requ�te
     * @return json avec les informations de facturation
     */
    @POST
    @Path(value = "get_info_facturation")
    public Response getInfoFacturation(@HeaderParam("authorization") String token, ESInfoFacturationDTO dto) {
        LOG.info("get_info_facturation");
        return execute(token, dto, service::getInfoFacturation);
    }

    private <T> Response execute(String token, T object, BiConsumer<T, ESTokenImpl> consumer) {
        ESTokenImpl esToken = ESTokenServiceImpl.getInstance().convertToken(token);
        consumer.accept(object, esToken);
        return Response.ok("{}").build();
    }

}
