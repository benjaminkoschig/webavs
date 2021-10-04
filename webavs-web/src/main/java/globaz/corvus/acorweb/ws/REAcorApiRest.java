package globaz.corvus.acorweb.ws;

import acor.ch.admin.zas.xmlns.acor_rentes9_out_resultat._0.Resultat9;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import acor.rentes.xsd.fcalcul.FCalcul;
import globaz.corvus.acorweb.service.REAcorService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/acor/rente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class REAcorApiRest {

    private final REAcorService service;

    public REAcorApiRest() {
        service = new REAcorService();
    }

    /**
     * Web Service exposé pour récupérer les données d'un dossier de rente dans le cadre d'un calcul ACOR.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param idDemande L'id de la demande
     *
     * @return le json inhost.
     */
    @GET
    @Path(value = "/{idDemande}/import")
    public InHostType importDossierRente(@PathParam("idDemande") String idDemande) {
        LOG.info("Importation des données.");
        return service.createInHostJson(idDemande);
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données FCalcul suite à un calcul dans ACOR pour une 10e révision.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param idDemande L'id de la demande
     * @param fCalcul   le json fCalcul.
     *
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/{idDemande}/{idTiers}/export")
    public Response exportDossierRente10(@PathParam("idDemande") String idDemande,
                                         @PathParam("idTiers") String idTiers,
                                         FCalcul fCalcul) {
        LOG.info("Exportation du dossier.");
        service.updateRente10afterAcorCalcul(fCalcul, idDemande, idTiers);
        return Response.ok("{}")
                .build();
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données Resultat9 suite à un calcul dans ACOR pour une 9e révision.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param idDemande L'id de la demande
     * @param resultat9 le resultat9.
     *
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/{idDemande}/{idTiers}/export9")
    public Response exportDossierRente9(@PathParam("idDemande") String idDemande,
                                        @PathParam("idTiers") String idTiers,
                                        Resultat9 resultat9) {
        LOG.info("Exportation du dossier de type 9.");
        service.updateRente9afterAcorCalcul(resultat9, idDemande, idTiers);
        return Response.ok("{}")
                .build();

    }

}
