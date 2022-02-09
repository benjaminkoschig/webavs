package globaz.osiris.eservices.ws;

import globaz.osiris.eservices.dto.ESExtraitCompteDTO;
import globaz.osiris.eservices.dto.ESInfoFacturationDTO;
import globaz.osiris.eservices.exceptions.ESBadRequestException;
import globaz.osiris.eservices.service.ESRetrieveService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Slf4j
@Path("/es/retrieve")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ESApiRestRetrieve {

    private final ESRetrieveService service;

    public ESApiRestRetrieve() {
        service = new ESRetrieveService();
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
    public Response getExtraitCompte(@HeaderParam("authorization") String token, ESExtraitCompteDTO dto) {
        LOG.info("get_extrait_compte");
        return execute(token, dto, service::getExtraitCompte, dto::isValid);
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
        return execute(token, dto, service::getInfoFacturation, dto::isValid);
    }

    /**
     * Execution de l'action du Webservice, ex�cution de la validation et cr�ation de la r�ponse.
     *
     * @param token header d'identification
     * @param dto json mapp� en objet qui contient des informations sur les informations voulue par la requ�te
     * @param function m�thode d'ex�cution � appeler
     * @param isValid m�thode de validation � appeler
     * @return Response
     */
    private <T, U, R> Response execute(U token, T dto, BiFunction<T, U, R> function, Supplier<Boolean> isValid) {

        if (!isValid.get()) {
             throw new ESBadRequestException("Requ�te invalide");
        }

        return Response.ok(function.apply(dto, token)).header("authorization", token).build();
    }

}
