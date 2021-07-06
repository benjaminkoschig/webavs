package globaz.corvus.ws;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.admin.zas.xmlns.acor_standard_erreur._0.OriginType;
import ch.admin.zas.xmlns.acor_standard_erreur._0.StandardError;
import ch.globaz.common.exceptions.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import globaz.corvus.acor2020.utils.Acor2020Token;
import globaz.corvus.acor2020.utils.Acor2020TokenService;
import globaz.corvus.acor2020.REExportationCalculAcor2020;
import globaz.corvus.acor2020.REImportationCalculAcor2020;
import globaz.corvus.application.REApplication;
import globaz.corvus.exceptions.REBusinessException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.prestation.acor.PRACORException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Path("acor2020")
@Slf4j
public class WebAvsAcor2020Service {

    public static final String ERROR_ACOR_EXTERN_TOKEN_INVALID = "ERROR.ACOR_EXTERN.TOKEN.INVALID";
    public static final String ERROR_ACOR_EXTERN_IMPORT_UNKOWN = "ERROR.ACOR_EXTERN.IMPORT.UNKOWN";
    public static final String ERROR_ACOR_EXTERN_IMPORT_IN_HOST = "ERROR.ACOR_EXTERN.IMPORT.IN_HOST";
    public static final String ERROR_ACOR_EXTERN_IMPORT_CONVERT = "ERROR.ACOR_EXTERN.IMPORT.CONVERT";
    public static final String ERROR_ACOR_EXTERN_EXPORT_UNKNOWN = "ERROR.ACOR_EXTERN.EXPORT.UNKNOWN";
    public static final String ERROR_ACOR_EXTERN_EXPORT_SPE = "&ERROR.ACOR_EXTERN.EXPORT.SPE";

    /**
     * @return
     */
    @GET
    @Path(value = "/import/")
    @Produces("application/json")
    public Response importDossier(@HeaderParam("authorization") String token) {
        LOG.info("Importation des données.");
        Acor2020Token acor2020Token = Acor2020TokenService.getToken(token);
        if (Objects.nonNull(token) && Objects.nonNull(acor2020Token)) {
            String idDemande = acor2020Token.getIdDemande();
            String userId = acor2020Token.getUserId();
            ObjectMapper objMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            objMapper.setDateFormat(df);

            try {
                BSession session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);
                REExportationCalculAcor2020 inHostService = new REExportationCalculAcor2020(session, idDemande);
                InHostType inHost = inHostService.createInHost();
                inHost.setVersionSchema("5.0");
                inHostService.validateUnitMessage(inHost);
                return Response.ok(objMapper.writeValueAsString(inHost)).build();
            } catch (Exception e) {
                if (e instanceof ValidationException) {
                    LOG.error("Les données n'ont pas pu être chargée correctement : " + ((ValidationException)e).getFormattedMessage(), e);
                    Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_IMPORT_CONVERT, e, 2, OriginType.TECHNICAL_IMPORT)).build();
                } else if (e instanceof JAXBException) {
                    LOG.error("Les données n'ont pas pu être chargée correctement : " + ((JAXBException)e).getErrorCode(), e);
                    Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_IMPORT_IN_HOST, e, 2, OriginType.TECHNICAL_IMPORT)).build();
                }
                LOG.error("Une erreur inconnue est intervenue lors de l'importation.", e);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_IMPORT_UNKOWN, e, 1, OriginType.TECHNICAL_IMPORT)).build();
            }
        }
        LOG.error("Token invalide.");
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

    /**
     * @param json
     * @return
     */
    @POST
    @Path("/export/")
    @Produces("application/json")
    public Response exportDossier(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier.");
        Acor2020Token acor2020Token = Acor2020TokenService.getToken(token);
        if (Objects.nonNull(token) && Objects.nonNull(acor2020Token)) {
            String idDemande = acor2020Token.getIdDemande();
            String idTiers = acor2020Token.getIdTiers();
            String userId = acor2020Token.getUserId();

            BSession session;
            try {
                session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);

                REImportationCalculAcor2020 exportProcess = new REImportationCalculAcor2020();
                exportProcess.actionImporterScriptACOR(idDemande, idTiers, json, session);
                return Response.ok().build();
            } catch (Exception e) {
                if (e instanceof REBusinessException || e instanceof PRACORException) {
                    LOG.error("Les données n'ont pas pu être exportée correctement.", e);
                    Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                    // TODO ajuster ce message.
                    return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 2, OriginType.TECHNICAL_EXPORT)).build();
                }
                LOG.error("Une erreur inconnue est intervenue lors de l'exportation.", e);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 1, OriginType.TECHNICAL_EXPORT)).build();
            }
        }
        LOG.error("Token invalide.");
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }


    @POST
    @Path("/export9/")
    @Produces("application/json")
    public Response exportDossier9(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier de type 9.");
        Acor2020Token acor2020Token = Acor2020TokenService.getToken(token);
        if (Objects.nonNull(token) && Objects.nonNull(acor2020Token)) {
            String idDemande = acor2020Token.getIdDemande();
            String idTiers = acor2020Token.getIdTiers();
            String userId = acor2020Token.getUserId();

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
                    return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 2, OriginType.TECHNICAL_EXPORT)).build();
                }
                LOG.error("Une erreur inconnue est intervenue lors de l'exportation.", e);
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 1, OriginType.TECHNICAL_EXPORT)).build();
            }
        }
        LOG.error("Token invalide.");
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

    private String getStandardError(String label, Exception e, int level, OriginType type) {
        try {
        StandardError error = new StandardError();
        ObjectMapper objMapper = new ObjectMapper();
        error.setLabelId(label);
        error.setOrigin(type);
        error.setLevel(level);
        error.setType(0);
        if (e != null) {
            error.setDebug(e.getStackTrace().toString());
        }
            return objMapper.writeValueAsString(error);
        } catch (JsonProcessingException jsonProcessingException) {
            LOG.error("Impossible de convertir l'erreur au format JSON. ", jsonProcessingException);
            return StringUtils.EMPTY;
        }
    }
}
