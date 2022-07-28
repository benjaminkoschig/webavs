package globaz.apg.acorweb.ws;

import acor.apg.xsd.apg.out.FCalcul;
import globaz.apg.acorweb.service.APAcorService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/acor/apg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class APAcorApiRest {

    private final APAcorService apAcorService;

    public APAcorApiRest() {
        apAcorService = new APAcorService();
    }

    /**
     * Web Service expos� pour r�cup�rer les donn�es d'un droit APG dans le cadre d'un calcul ACOR.
     * On contr�le le token pass� en param�tre pour authoriser l'appel.
     *
     * @param idDroit L'id du droit
     * @param genreService le genre de service APG
     *
     * @return le json inhost.
     */
    @GET
    @Path(value = "/{idDroit}/{genreService}/import")
    public Response importDossierAPG(@PathParam("idDroit") String idDroit, @PathParam("genreService") String genreService) {
        LOG.info("Importation du dossier depuis WebAVS vers Acor.");
        return Response.ok(apAcorService.createInHostJson(idDroit, genreService)).build();
    }

    /**
     * Web Service expos� pour transmettre � WebAVS les donn�es FCalcul suite � un calcul dans ACOR.
     * On contr�le le token pass� en param�tre pour authoriser l'appel.
     *
     * @param idDroit L'id du droit
     * @param genreService le genre de service APG
     * @param fCalcul   le json fCalcul.
     *
     * @return OK si le json a �t� correctement trait�. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/{idDroit}/{genreService}/export")
    public Response exportDossierAPG(@PathParam("idDroit") String idDroit, @PathParam("genreService") String genreService,
                                     FCalcul fCalcul) {
        LOG.info("Exportation du dossier depuis Acor vers WebAVS.");
        apAcorService.importCalculAcor(fCalcul, idDroit, genreService);
        return Response.ok("{}").build();
    }
}
