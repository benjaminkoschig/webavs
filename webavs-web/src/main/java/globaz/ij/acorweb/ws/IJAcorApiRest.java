package globaz.ij.acorweb.ws;

import acor.ij.xsd.ij.out.FCalcul;
import globaz.ij.acorweb.service.IJAcorService;
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

@Slf4j
@Path("/acor/ij")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IJAcorApiRest {

    private final IJAcorService ijAcorService;

    public IJAcorApiRest() {
        this.ijAcorService = new IJAcorService();
    }

    /**
     * Web Service exposé pour récupérer les données d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/calcul/{idPrononce}/import")
    public Response importDossierCalcul(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce) {
        return Response.ok(this.ijAcorService.createInHostCalcul(idPrononce)).build();
    }

    /**
     * Web Service exposé pour récupérer les données d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/decompte/{idIJCalculee}/{idBaseIndemnisation}/import")
    public Response importDossierDecompte(@HeaderParam("authorization") String token, @PathParam("idIJCalculee") String idIJCalculee,  @PathParam("idBaseIndemnisation") String idBaseIndemnisation) {
        return Response.ok(this.ijAcorService.createInHostDecompte(idIJCalculee,idBaseIndemnisation)).build();
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données IJout suite à un calcul dans ACOR.
     *
     * @param token le token à contrôler.
     * @param idPrononce  L'id du prononcé en cours de traitement correspondant à la requête de Calcul ACOR demandée par WebAVS.
     * @param fCalcul Données du calcul ACOR à importer et mapper dans WebAVS.
     *
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/calcul/{idPrononce}/export")
    public Response exportDossierCalcul(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce, FCalcul fCalcul) {
        LOG.info("Exportation du dossier.");
        ijAcorService.importCalculAcor(idPrononce, fCalcul);
        return Response.ok("{}").build();
    }

    @POST
    @Path(value = "/decompte/{idIJCalculee}/{idBaseIndemnisation}/export")
    public Response exportDossierDecompte(@HeaderParam("authorization") String token, @PathParam("idIJCalculee") String idIJCalculee, @PathParam("idBaseIndemnisation") String idBaseIndemnisation, FCalcul fCalcul){
        LOG.info("Exportation du dossier.");
        ijAcorService.importDecompteAcor(idIJCalculee, idBaseIndemnisation, fCalcul);
        return Response.ok("{}").build();
    }
}
