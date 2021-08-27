package globaz.ij.acor2020.ws;

import globaz.ij.acor2020.service.IJAcor2020Service;
import globaz.ij.acor2020.ws.token.IJAcor2020Token;
import globaz.ij.acor2020.ws.token.IJAcor2020TokenService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/acor2020/ij")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IJAcor2020ApiRest {

    private final IJAcor2020Service ijAcor2020Service;

    public IJAcor2020ApiRest() {
        this.ijAcor2020Service = new IJAcor2020Service();
    }

    /**
     * Web Service exposé pour récupérer les données d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/import/{idPrononce}")
    public Response importDossier(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce) {
        return Response.ok(this.ijAcor2020Service.createInHost(idPrononce)).build();
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
    @Path("/export")
    public Response exportDossier(@HeaderParam("authorization") String token, String json) {
        LOG.info("Exportation du dossier.");

        IJAcor2020Token acor2020Token = IJAcor2020TokenService.getInstance().convertToken(token);

        return Response.ok().build();
    }
}
