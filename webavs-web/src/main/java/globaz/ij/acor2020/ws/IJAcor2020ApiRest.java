package globaz.ij.acor2020.ws;

import acor.ij.xsd.ij.out.FCalcul;
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
     * Web Service expos� pour r�cup�rer les donn�es d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/import/{idPrononce}")
    public Response importDossierCalcul(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce) {
        return Response.ok(this.ijAcor2020Service.createInHostCalcul(idPrononce)).build();
    }

    /**
     * Web Service expos� pour r�cup�rer les donn�es d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/decompte/import/{idPrononce}/{idBaseIndemnisation}")
    public Response importDossierDecompte(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce, @PathParam("idBaseIndemnisation") String idBaseIndemnisation) {
        return Response.ok(this.ijAcor2020Service.createInHostDecompte(idPrononce, idBaseIndemnisation)).build();
    }

    /**
     * Web Service expos� pour transmettre � WebAVS les donn�es IJout suite � un calcul dans ACOR.
     *
     * @param token le token � contr�ler.
     * @param idPrononce  L'id du prononc� en cours de traitement correspondant � la requ�te de Calcul ACOR demand�e par WebAVS.
     * @param fCalcul Donn�es du calcul ACOR � importer et mapper dans WebAVS.
     *
     * @return OK si le json a �t� correctement trait�. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export/{idPrononce}")
    public Response exportDossierCalcul(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce, FCalcul fCalcul) {
        LOG.info("Exportation du dossier.");

        IJAcor2020Token acor2020Token = IJAcor2020TokenService.getInstance().convertToken(token);
        ijAcor2020Service.importCalculAcor(idPrononce, fCalcul);
        return Response.ok("{}").build();
    }

    @GET
    @Path(value = "/decompte/export/{idPrononce}/{idBaseIndemnisation}")
    public Response exportDossierDecompte(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce, @PathParam("idBaseIndemnisation") String idBaseIndemnisation, FCalcul fCalcul){
        LOG.info("Exportation du dossier.");

        IJAcor2020Token acor2020Token = IJAcor2020TokenService.getInstance().convertToken(token);
        ijAcor2020Service.importDecompteAcor(idPrononce, idBaseIndemnisation, fCalcul);
        return Response.ok().build();
    }
}
