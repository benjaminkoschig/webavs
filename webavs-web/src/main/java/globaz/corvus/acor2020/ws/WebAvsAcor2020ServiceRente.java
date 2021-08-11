package globaz.corvus.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.globaz.common.acor.WebAvsAcor2020Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globaz.corvus.acor2020.REExportationCalculAcor2020;
import globaz.corvus.acor2020.REImportationCalculAcor2020;
import globaz.corvus.acor2020.ws.token.Acor2020TokenRentes;
import globaz.corvus.acor2020.ws.token.Acor2020TokenRentesServiceImpl;
import globaz.corvus.application.REApplication;
import globaz.corvus.exceptions.REBusinessException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.prestation.acor.PRACORException;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Slf4j
@Path("rentesAcor2020")
public class WebAvsAcor2020ServiceRente implements WebAvsAcor2020Service {


    /**
     * Web Service exposé pour récupérer les données d'un dossier de rente dans le cadre d'un calcul ACOR.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token le token à contrôler.
     * @return le json inhost.
     */
    @GET
    @Path(value = "/import/")
    @Produces("application/json")
    public Response importDossierRente(@HeaderParam("authorization") String token) {
        LOG.info("Importation des données.");
        Acor2020TokenRentes acor2020TokenRentes = (Acor2020TokenRentes) Acor2020TokenRentesServiceImpl.getInstance().getToken(token);
        if (Objects.nonNull(token) && Objects.nonNull(acor2020TokenRentes)) {
            String idDemande = acor2020TokenRentes.getIdDemande();
            String userId = acor2020TokenRentes.getUserId();
            ObjectMapper objMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            objMapper.setDateFormat(df);

            try {
                BSession session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);
                REExportationCalculAcor2020 inHostService = new REExportationCalculAcor2020(session, idDemande);
                InHostType inHost = inHostService.createInHost();
                inHost.setVersionSchema("5.0");
                return Response.ok(objMapper.writeValueAsString(inHost)).build();
            } catch (JsonProcessingException e) {
                LOG.error("Les données n'ont pas pu être chargées correctement : " , e);
                    Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_IMPORT_IN_HOST, e, 2, OriginType.TECHNICAL_IMPORT)).build();
            } catch (Exception e) {
                LOG.error("Une erreur inconnue est intervenue lors de l'importation.", e);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_IMPORT_UNKOWN, e, 1, OriginType.TECHNICAL_IMPORT)).build();
            }
        }
        LOG.error(TOKEN_INVALIDE);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données FCalcul suite à un calcul dans ACOR pour une 10e révision.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token le token à contrôler.
     * @param json le json fCalcul.
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export/")
    @Produces("application/json")
    public Response exportDossierRente10(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier.");
        Acor2020TokenRentes acor2020TokenRentes = (Acor2020TokenRentes) Acor2020TokenRentesServiceImpl.getInstance().getToken(token);
        if (Objects.nonNull(token) && Objects.nonNull(acor2020TokenRentes)) {
            String idDemande = acor2020TokenRentes.getIdDemande();
            String idTiers = acor2020TokenRentes.getIdTiers();
            String userId = acor2020TokenRentes.getUserId();

            BSession session;
            try {
                session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);

                REImportationCalculAcor2020 exportProcess = new REImportationCalculAcor2020();
                exportProcess.actionImporterScriptACOR(idDemande, idTiers, json, session);
                return Response.ok().build();
            } catch (REBusinessException | PRACORException businessException) {
                LOG.error("Une erreur métier a été remontée lors de l'exportation des données. ", businessException);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                // TODO : voir comment on gère ces messages car ils ne sont pas tous dans un fichier properties : problème de gestion des langues.
                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(businessException.getMessage(), businessException, 2, OriginType.TECHNICAL_EXPORT)).build();
            } catch (Exception e) {
                LOG.error("Une erreur inconnue est intervenue lors de l'exportation.", e);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 1, OriginType.TECHNICAL_EXPORT)).build();
            }
        }
        LOG.error(TOKEN_INVALIDE);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données Resultat9 suite à un calcul dans ACOR pour une 9e révision.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token le token à contrôler.
     * @param json le json resultat9.
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export9/")
    @Produces("application/json")
    public Response exportDossierRente9(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier de type 9.");
        Acor2020TokenRentes acor2020TokenRentes = (Acor2020TokenRentes) Acor2020TokenRentesServiceImpl.getInstance().getToken(token);
        if (Objects.nonNull(token) && Objects.nonNull(acor2020TokenRentes)) {
            String idDemande = acor2020TokenRentes.getIdDemande();
            String idTiers = acor2020TokenRentes.getIdTiers();
            String userId = acor2020TokenRentes.getUserId();

            BSession session = null;
            try {
                session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);
                REImportationCalculAcor2020 exportProcess = new REImportationCalculAcor2020();
                exportProcess.actionImporterScriptACOR9(idDemande, idTiers, json, session);
                return Response.ok().build();

            } catch (Exception e) {
                if (e instanceof REBusinessException || e instanceof PRACORException) {
                    LOG.error("Les données n'ont pas pu être exportée correctement.", e);
                    Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    // TODO ajuster ce message.
                    return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 2, OriginType.TECHNICAL_EXPORT)).build();
                }
                LOG.error("Une erreur inconnue est intervenue lors de l'exportation.", e);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 1, OriginType.TECHNICAL_EXPORT)).build();
            }
        }
        LOG.error(TOKEN_INVALIDE);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

}
