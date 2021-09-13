package globaz.ij.acor2020.ws.token;

import globaz.globall.db.BSession;
import globaz.ij.acor2020.ws.IJAcor2020ApiRest;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.prestation.acor.acor2020.ws.Acor2020TokenServiceAbstract;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.HashMap;
import java.util.Map;

public class IJAcor2020TokenService extends Acor2020TokenServiceAbstract<IJAcor2020Token> {

    private static final IJAcor2020TokenService INSTANCE = new IJAcor2020TokenService();
    private static final String API_PATH = createApiPath(IJAcor2020ApiRest.class);
    private static final String CALCUL_URL = API_PATH + "/calcul";
    private static final String DECOMPTE_URL =  API_PATH + "/decompte";
    private static final String ACTION_IMPORT = "import";
    private static final String ACTION_EXPORT = "export";

    private IJAcor2020TokenService() {}

    public static IJAcor2020TokenService getInstance() {
        return INSTANCE;
    }

    public static String createTokenCalcul(BSession bSession, final String idPrononce, final String noAVSAssure) {
        Map<String, Object> claims = new HashMap<>();
        String urlBase = CALCUL_URL + "/" + idPrononce + "/";
        claims.put("exportIJUrl",  urlBase + ACTION_EXPORT);
        claims.put("importUrl", urlBase + ACTION_IMPORT);
        claims.put("recordId", noAVSAssure);

        return creatToken(claims, bSession);
    }

    public static String createTokenDecompte(BSession bSession, final String idijCalculee, final String idBaseIdemnisation, final String noAVSAssure) {
        Map<String, Object> claims = new HashMap<>();
        String urlBase = DECOMPTE_URL + "/" + idijCalculee + "/" + idBaseIdemnisation + "/";
        claims.put("exportIJUrl", urlBase + ACTION_EXPORT );
        claims.put("importUrl", urlBase + ACTION_IMPORT);
        claims.put("recordId", noAVSAssure);

        return creatToken(claims, bSession);
    }

    @Override
    protected IJAcor2020Token convertToken(final Jws<Claims> jws) {
        IJAcor2020Token token = new IJAcor2020Token();

        if (jws.getBody() != null) {
            return token;
        }

        return null;
    }
}
