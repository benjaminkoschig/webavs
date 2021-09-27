package globaz.corvus.acorweb.ws;

import acor.rentes.xsd.fcalcul.FCalcul;
import acor.ch.admin.zas.xmlns.acor_rentes9_out_resultat._0.Resultat9;
import globaz.corvus.acorweb.service.REAcorService;
import globaz.corvus.acorweb.ws.token.REAcorToken;
import globaz.corvus.acorweb.ws.token.REAcorTokenService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
     * @param token le token à contrôler.
     * @return le json inhost.
     */
    @GET
    @Path(value = "/import")
    public Response importDossierRente(@HeaderParam("authorization") String token) {
        LOG.info("Importation des données.");
        return execute(token, service::createInHostJson);
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données FCalcul suite à un calcul dans ACOR pour une 10e révision.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token   le token à contrôler.
     * @param fCalcul le json fCalcul.
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export")
    public Response exportDossierRente10(@HeaderParam("authorization") String token, FCalcul fCalcul) {
        LOG.info("Exportation du dossier.");
        return execute(token, fCalcul, service::updateRente10afterAcorCalcul);
    }

    /**
     * Web Service exposé pour transmettre à WebAVS les données Resultat9 suite à un calcul dans ACOR pour une 9e révision.
     * On contrôle le token passé en paramètre pour authoriser l'appel.
     *
     * @param token     le token à contrôler.
     * @param resultat9 le resultat9.
     * @return OK si le json a été correctement traité. Sinon des messages d'erreurs.
     */
    @POST
    @Path("/export9")
    public Response exportDossierRente9(@HeaderParam("authorization") String token, Resultat9 resultat9) {
        LOG.info("Exportation du dossier de type 9.");
        return execute(token, resultat9, service::updateRente9afterAcorCalcul);
    }

    private <T> Response execute(String token, Function<REAcorToken, T> function) {
        REAcorToken acor2020Token = REAcorTokenService.getInstance().convertToken(token);
        return Response.ok(function.apply(acor2020Token)).build();
    }

    private <T> Response execute(String token, T object, BiConsumer<T, REAcorToken> consumer) {
        REAcorToken acor2020Token = REAcorTokenService.getInstance().convertToken(token);
        consumer.accept(object, acor2020Token);
        return Response.ok("{}").build();
    }

}
