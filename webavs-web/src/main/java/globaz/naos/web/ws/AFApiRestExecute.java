package globaz.naos.web.ws;

import globaz.naos.web.DTO.AFAffiliationDTO;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.naos.web.service.AFExecuteService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Slf4j
@Path("/af/execute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AFApiRestExecute {

    private final AFExecuteService service;

    public AFApiRestExecute() {
        service = new AFExecuteService();
    }

    /**
     * Web Service exposé pour la création d'affiliation.
     *
     * @param token header d'authentification
     * @param dto   JSON mappé en objet qui contient des informations sur l'affiliation
     * @return JSON avec l'affiliation crée
     */
    @POST
    @Path(value = "affiliation")
    public Response createAffiliation(@HeaderParam("authorization") String token, AFAffiliationDTO dto) {
        LOG.info("create_affiliation");
        return execute(token, dto, service::createAffiliation, dto::isValidForCreation);
    }

    @PUT
    @Path(value = "affiliation")
    public Response updateAffiliation(@HeaderParam("authorization") String token, AFAffiliationDTO dto) {
        LOG.info("update_contact");
        return execute(token, dto, service::updateAffiliation, dto::isValidForUpdate);
    }

    @DELETE
    @Path(value = "affiliation")
    public Response deleteAffiliation(@HeaderParam("authorization") String token, AFAffiliationDTO dto) {
        LOG.info("delete_contact");
        return execute(token, dto, service::deleteAffiliation, dto::isValidForDeletion);
    }


    /**
     * Execution de l'action du web service, exécution de la validation et création de la réponse.
     *
     * @param token    header d'identification
     * @param dto      json mappé en objet qui contient des informations sur les informations voulue par la requête
     * @param function méthode d'exécution à appeler
     * @param isValid  méthode de validation à appeler
     * @return Response
     */
    private <T, U, R> Response execute(U token, T dto, BiFunction<T, U, R> function, Supplier<Boolean> isValid) {

        if (!isValid.get()) {
            LOG.error("Une erreur de paramètres s'est produite lors de la validation de la requête. Un paramètre obligatoire est manquant ?");
            throw new AFBadRequestException("Une erreur de paramètre s'est produite lors de la validation de la requête. Un paramètre obligatoire est manquant ?");
        }

        return Response.ok(function.apply(dto, token)).header("authorization", token).build();
    }
}
