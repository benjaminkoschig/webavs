package globaz.pyxis.web.ws;

import globaz.pyxis.web.DTO.PYTiersDTO;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.service.PYExecuteService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Slf4j
@Path("/py/execute")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PYApiRestExecute {

    private final PYExecuteService service;

    public PYApiRestExecute() { service = new PYExecuteService(); }

    /**
     * Web Service exposé pour la création de tiers.
     *
     * @param token header d'authentification
     * @param dto JSON mappé en objet qui contient des informations sur les tiers
     * @return JSON avec l'ID du tiers créé
     */
    @POST
    @Path(value = "create_tiers")
    public Response createTiers(@HeaderParam("authorization") String token, PYTiersDTO dto) {
        LOG.info("create_tiers");
        return execute(token, dto, service::createTiers, dto::isValid);
    }

    @PUT
    @Path(value = "update_tiers")
    public Response updateTiers(@HeaderParam("authorization") String token, PYTiersDTO dto) {
        LOG.info("update_tiers");
        return execute(token, dto, service::updateTiers, dto::isValidUpdate);
    }

    /**
     * Execution de l'action du web service, exécution de la validation et création de la réponse.
     *
     * @param token header d'identification
     * @param dto json mappé en objet qui contient des informations sur les informations voulue par la requête
     * @param function méthode d'exécution à appeler
     * @param isValid méthode de validation à appeler
     * @return Response
     */
    private <T, U, R> Response execute(U token, T dto, BiFunction<T, U, R> function, Supplier<Boolean> isValid) {

        if (!isValid.get()) {
            LOG.error("Une erreur de paramètres s'est produite lors de la validation de la requête.");
            throw new PYBadRequestException("Une erreur de paramètres s'est produite lors de la validation de la requête.");
        }

        return Response.ok(function.apply(dto, token)).header("authorization", token).build();
    }
}
