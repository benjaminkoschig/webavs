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
     * Web Service expos� pour r�cup�rer les donn�es d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/calcul/{idPrononce}/import")
    public Response importDossierCalcul(@HeaderParam("authorization") String token, @PathParam("idPrononce") String idPrononce) {
        return Response.ok(this.ijAcorService.createInHostCalcul(idPrononce)).build();
    }

    /**
     * Web Service expos� pour r�cup�rer les donn�es d'un dossier de IJAI dans le cadre d'un calcul ACOR.
     *
     * @return le json ij-in.
     */
    @GET
    @Path(value = "/decompte/{idIJCalculee}/{idBaseIndemnisation}/import")
    public Response importDossierDecompte(@HeaderParam("authorization") String token, @PathParam("idIJCalculee") String idIJCalculee,  @PathParam("idBaseIndemnisation") String idBaseIndemnisation) {
        return Response.ok(this.ijAcorService.createInHostDecompte(idIJCalculee,idBaseIndemnisation)).build();
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
