package globaz.ij.acor2020.ws;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("acor2020/ij")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IJApiRestAcor2020 {

    /**
     * Web Service exposé pour récupérer les données d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @param token le token à contrôler.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/import/")
    public Response importDossierIJ(@HeaderParam("authorization") String token) {
        LOG.info("Importation des données.");
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        return responseBuilder.build();
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données IJout suite à un calcul dans ACOR.
     *
     * @param token le token à contrôler.
     * @param json  le json ij-out.
     *
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export/")
    public Response exportDossierIJ(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier.");
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        return responseBuilder.build();
    }
}
