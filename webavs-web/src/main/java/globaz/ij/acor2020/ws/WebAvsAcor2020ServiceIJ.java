package globaz.ij.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import ch.globaz.common.acor.WebAvsAcor2020Service;
import globaz.ij.acor2020.ws.token.Acor2020TokenIJ;
import globaz.ij.acor2020.ws.token.Acor2020TokenIJServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("ijAcor2020")
@Slf4j
public class WebAvsAcor2020ServiceIJ implements WebAvsAcor2020Service {

    /**
     * Web Service expos� pour r�cup�rer les donn�es d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     * On contr�le le token pass� en param�tre pour authoriser l'appel.
     *
     * @param token le token � contr�ler.
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/import/")
    @Produces("application/json")
    public Response importDossierIJ(@HeaderParam("authorization") String token) {
        LOG.info("Importation des donn�es.");
        Acor2020TokenIJ acor2020TokenIJ = (Acor2020TokenIJ) Acor2020TokenIJServiceImpl.getInstance().getToken(token);
//        if (Objects.nonNull(token) && Objects.nonNull(acor2020TokenIJ)) {
//            String idDemande = acor2020TokenIJ.getIdDemande();
//            String userId = acor2020TokenIJ.getUserId();
//            ObjectMapper objMapper = new ObjectMapper();
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            objMapper.setDateFormat(df);
//
//            try {
//                BSession session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);
//                REExportationCalculAcor2020 inHostService = new REExportationCalculAcor2020(session, idDemande);
//                InHostType inHost = inHostService.createInHost();
//                inHost.setVersionSchema("5.0");
//                return Response.ok(objMapper.writeValueAsString(inHost)).build();
//            } catch (JsonProcessingException e) {
//                LOG.error("Les donn�es n'ont pas pu �tre charg�es correctement : " , e);
//                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_IMPORT_IN_HOST, e, 2, OriginType.TECHNICAL_IMPORT)).build();
//            } catch (Exception e) {
//                LOG.error("Une erreur inconnue est intervenue lors de l'importation.", e);
//                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_IMPORT_UNKOWN, e, 1, OriginType.TECHNICAL_IMPORT)).build();
//            }
//        }
        LOG.error(TOKEN_INVALIDE);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

    /**
     * Web Service expos� pour transmettre � WebAVS les donn�es IJout suite � un calcul dans ACOR.
     * On contr�le le token pass� en param�tre pour authoriser l'appel.
     *
     * @param token le token � contr�ler.
     * @param json le json ij-out.
     * @return OK si le json a �t� correctement trait�. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export/")
    @Produces("application/json")
    public Response exportDossierIJ(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier.");
        Acor2020TokenIJ acor2020TokenIJ = (Acor2020TokenIJ) Acor2020TokenIJServiceImpl.getInstance().getToken(token);
//        if (Objects.nonNull(token) && Objects.nonNull(acor2020TokenIJ)) {
//            String idDemande = acor2020TokenRentes.getIdDemande();
//            String idTiers = acor2020TokenRentes.getIdTiers();
//            String userId = acor2020TokenRentes.getUserId();
//
//            BSession session;
//            try {
//                session = BSessionUtil.createSession(REApplication.DEFAULT_APPLICATION_CORVUS, userId);
//
//                REImportationCalculAcor2020 exportProcess = new REImportationCalculAcor2020();
//                exportProcess.actionImporterScriptACOR(idDemande, idTiers, json, session);
//                return Response.ok().build();
//            } catch (REBusinessException | PRACORException businessException) {
//                LOG.error("Une erreur m�tier a �t� remont�e lors de l'exportation des donn�es. ", businessException);
//                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//                // TODO : voir comment on g�re ces messages car ils ne sont pas tous dans un fichier properties : probl�me de gestion des langues.
//                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(businessException.getMessage(), businessException, 2, OriginType.TECHNICAL_EXPORT)).build();
//            } catch (Exception e) {
//                LOG.error("Une erreur inconnue est intervenue lors de l'exportation.", e);
//                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//                return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_EXPORT_UNKNOWN, e, 1, OriginType.TECHNICAL_EXPORT)).build();
//            }
//        }
        LOG.error(TOKEN_INVALIDE);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        return responseBuilder.entity(WebAvsAcor2020Service.getStandardError(ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN)).build();
    }

}
