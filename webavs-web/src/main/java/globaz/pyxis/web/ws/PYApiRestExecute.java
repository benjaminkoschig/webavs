package globaz.pyxis.web.ws;

import globaz.pyxis.web.DTO.PYContactCreateDTO;
import globaz.pyxis.web.DTO.PYContactDTO;
import globaz.pyxis.web.DTO.PYMeanOfCommunicationCreationDTO;
import globaz.pyxis.web.DTO.PYTiersDTO;
import globaz.pyxis.web.DTO.PYTiersUpdateDTO;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.service.PYExecuteService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response updateTiers(@HeaderParam("authorization") String token, PYTiersUpdateDTO dto) {
        LOG.info("update_tiers");
        return execute(token, dto, service::updateTiers, dto::isValid);
    }

    @PUT
    @Path(value = "contact")
    public Response updateContact(@HeaderParam("authorization") String token, PYContactDTO dto) {
        LOG.info("update_contact");
        return execute(token, dto, service::updateContact, dto::isValid);
    }

    @POST
    @Path(value = "contact")
    public Response createContact(@HeaderParam("authorization") String token, PYContactCreateDTO dto) {
        LOG.info("create_contact");
        return execute(token, dto, service::createContact, dto::isValidForCreation);
    }

    @DELETE
    @Path(value = "contact")
    public Response deleteContact(@HeaderParam("authorization") String token, PYContactCreateDTO dto) {
        LOG.info("delete_contact");
        return execute(token, dto, service::deleteContact, dto::isValidForDeletion);
    }

    @PUT
    @Path(value = "mean_of_communication")
    public Response updateMeanOfCommunication(@HeaderParam("authorization") String token, PYMeanOfCommunicationCreationDTO dto) {
        LOG.info("create_mean_of_communication");
        return execute(token, dto, service::updateMeanOfCommunication, dto::isValid);
    }

    @POST
    @Path(value = "mean_of_communication")
    public Response createMeanOfCommunication(@HeaderParam("authorization") String token, PYMeanOfCommunicationCreationDTO dto) {
        LOG.info("create_mean_of_communication");
        return execute(token, dto, service::createMeanOfCommunication, dto::isValid);
    }

    @DELETE
    @Path(value = "mean_of_communication")
    public Response deleteMeanOfCommunication(@HeaderParam("authorization") String token, PYMeanOfCommunicationCreationDTO dto) {
        LOG.info("delete_mean_of_communication");
        return execute(token, dto, service::deleteMeanOfCommunication, dto::isValid);
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
            LOG.error("Une erreur de paramètres s'est produite lors de la validation de la requête. Un paramètre obligatoire est manquant ?");
            throw new PYBadRequestException("Une erreur de paramètre s'est produite lors de la validation de la requête. Un paramètre obligatoire est manquant ?");
        }

        return Response.ok(function.apply(dto, token)).header("authorization", token).build();
    }
}
