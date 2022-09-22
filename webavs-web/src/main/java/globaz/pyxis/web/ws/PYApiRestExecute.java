package globaz.pyxis.web.ws;

import globaz.pyxis.web.DTO.*;
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
     * Web Service exposé pour la création de tiers (y.c. adresses, adresses de paiement, contacts et moyens de communication).
     *
     * @param token header d'authentification
     * @param dto JSON mappé en objet qui contient des informations sur les tiers
     * @return JSON avec l'ID du tiers créé
     */
    @POST
    @Path(value = "create_tiers")
    public Response createTiers(@HeaderParam("authorization") String token, PYTiersDTO dto) {
        LOG.info("create_tiers");
        return execute(token, dto, service::createTiers, dto::isValidForCreation);
    }

    @PUT
    @Path(value = "tiers")
    public Response updateTiersPage1(@HeaderParam("authorization") String token, PYTiersPage1DTO dto) {
        LOG.info("update_tiers_page_1");
        return execute(token, dto, service::updateTiersPage1, dto::isValidForUpdate);
    }

    @POST
    @Path(value = "tiers")
    public Response createTiersPage1(@HeaderParam("authorization") String token, PYTiersPage1DTO dto) {
        LOG.info("update_tiers_page_1");
        return execute(token, dto, service::createTiersPage1, dto::isValidForCreation);
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
    public Response updateMeanOfCommunication(@HeaderParam("authorization") String token, PYMeanOfCommunicationDTO dto) {
        LOG.info("update_mean_of_communication");
        return execute(token, dto, service::updateMeanOfCommunication, dto::isValid);
    }

    @POST
    @Path(value = "mean_of_communication")
    public Response createMeanOfCommunication(@HeaderParam("authorization") String token, PYMeanOfCommunicationDTO dto) {
        LOG.info("create_mean_of_communication");
        return execute(token, dto, service::createMeanOfCommunication, dto::isValid);
    }

    @DELETE
    @Path(value = "mean_of_communication")
    public Response deleteMeanOfCommunication(@HeaderParam("authorization") String token, PYMeanOfCommunicationDTO dto) {
        LOG.info("delete_mean_of_communication");
        return execute(token, dto, service::deleteMeanOfCommunication, dto::isValid);
    }

    @PUT
    @Path(value = "address")
    public Response updateAddress(@HeaderParam("authorization") String token, PYAddressDTO dto) {
        LOG.info("update_address");
        return execute(token, dto, service::updateAddress, dto::isValidForUpdate);
    }

    @POST
    @Path(value = "address")
    public Response createAddress(@HeaderParam("authorization") String token, PYAddressDTO dto) {
        LOG.info("create_address");
        return execute(token, dto, service::createAddress, dto::isValidForCreation);
    }

    @DELETE
    @Path(value = "address")
    public Response deleteAddress(@HeaderParam("authorization") String token, PYAddressDTO dto) {
        LOG.info("delete_address");
        return execute(token, dto, service::deleteAddress, dto::isValidForDelete);
    }

    @PUT
    @Path(value = "payment_address")
    public Response updatePaymentAddress(@HeaderParam("authorization") String token, PYPaymentAddressDTO dto) {
        LOG.info("update_payment_address");
        return execute(token, dto, service::updatePaymentAddress, dto::isValidForUpdate);
    }

    @POST
    @Path(value = "payment_address")
    public Response createPaymentAddress(@HeaderParam("authorization") String token, PYPaymentAddressDTO dto) {
        LOG.info("create_payment_address");
        return execute(token, dto, service::createPaymentAddress, dto::isValidForCreation);
    }

    @DELETE
    @Path(value = "payment_address")
    public Response deletePaymentAddress(@HeaderParam("authorization") String token, PYPaymentAddressDTO dto) {
        LOG.info("delete_payment_address");
        return execute(token, dto, service::deletePaymentAddress, dto::isValidForDeletion);
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
            throw new PYBadRequestException("Une erreur de paramètre s'est produite lors de la validation de la requête. Un paramètre obligatoire est manquant ?");
        }

        return Response.ok(function.apply(dto, token)).header("authorization", token).build();
    }
}
