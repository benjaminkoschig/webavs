package globaz.ij.acorweb.ws.token;

import globaz.globall.db.BSession;
import globaz.ij.acorweb.ws.IJAcorApiRest;
import globaz.prestation.acor.web.ws.AcorTokenServiceAbstract;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.HashMap;
import java.util.Map;

public class IJAcorTokenService extends AcorTokenServiceAbstract<IJAcorToken> {

    private static final IJAcorTokenService INSTANCE = new IJAcorTokenService();
    private static final String API_PATH = createApiPath(IJAcorApiRest.class);
    private static final String CALCUL_URL = API_PATH + "/calcul";
    private static final String DECOMPTE_URL =  API_PATH + "/decompte";
    private static final String ACTION_IMPORT = "import";
    private static final String ACTION_EXPORT = "export";

    private IJAcorTokenService() {}

    public static IJAcorTokenService getInstance() {
        return INSTANCE;
    }

    public static String createTokenCalcul(BSession bSession, final String idPrononce, final String noAVSAssure) {
        return createTokenIJ(bSession, CALCUL_URL + "/" + idPrononce + "/", noAVSAssure);
    }

    public static String createTokenDecompte(BSession bSession, final String idijCalculee, final String idBaseIdemnisation, final String noAVSAssure) {
        return createTokenIJ(bSession, DECOMPTE_URL + "/" + idijCalculee + "/" + idBaseIdemnisation + "/", noAVSAssure);
    }

    private static String createTokenIJ(BSession bSession, String urlBase, String noAVSAssure){
        Map<String, Object> claims = new HashMap<>();
        claims.put("exportIJUrl", urlBase + ACTION_EXPORT );
        claims.put("importUrl", urlBase + ACTION_IMPORT);
        claims.put("recordId", noAVSAssure);

        return createToken(claims, bSession);
    }

    @Override
    protected IJAcorToken convertToken(final Jws<Claims> jws) {
        IJAcorToken token = new IJAcorToken();

        if (jws.getBody() != null) {
            return token;
        }

        return null;
    }
}
