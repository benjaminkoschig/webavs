package globaz.ij.acorweb.ws.token;

import ch.globaz.common.properties.CommonProperties;
import globaz.globall.db.BSession;
import globaz.ij.acorweb.ws.IJAcorApiRest;
import globaz.prestation.acor.web.ws.TokenServiceAbstract;
import globaz.prestation.acor.web.ws.AcorTokenImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.HashMap;
import java.util.Map;

public class IJAcorTokenServiceImpl extends TokenServiceAbstract<AcorTokenImpl> {

    private static final IJAcorTokenServiceImpl INSTANCE = new IJAcorTokenServiceImpl();
    private static final String API_PATH = createApiPath(IJAcorApiRest.class);
    private static Integer TOKEN_DURATION = CommonProperties.ACOR_TOKEN_DURATION.getIntegerWithDefaultValue(1);
    private static final String CALCUL_URL = API_PATH + "/calcul";
    private static final String DECOMPTE_URL = API_PATH + "/decompte";
    private static final String ACTION_IMPORT = "import";
    private static final String ACTION_EXPORT = "export";

    public static IJAcorTokenServiceImpl getInstance() {
        return INSTANCE;
    }

    public static String createTokenIJCalcul(BSession bSession, final String idPrononce, final String noAVSAssure) {
        return createTokenIJ(bSession, CALCUL_URL + "/" + idPrononce + "/", noAVSAssure);
    }

    public static String createTokenIJDecompte(BSession bSession, final String idijCalculee, final String idBaseIdemnisation, final String noAVSAssure) {
        return createTokenIJ(bSession, DECOMPTE_URL + "/" + idijCalculee + "/" + idBaseIdemnisation + "/", noAVSAssure);
    }

    private static String createTokenIJ(BSession bSession, String urlBase, String noAVSAssure) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("exportIJUrl", urlBase + ACTION_EXPORT);
        claims.put("importUrl", urlBase + ACTION_IMPORT);
        claims.put("recordId", noAVSAssure);

        claims.put("acorBaseUrl", loadAcorBaseUrl());
        claims.put(USER_ID, bSession.getUserId());

        return createToken(TOKEN_DURATION, claims, bSession);
    }

    @Override
    protected AcorTokenImpl convertTokenSpecific(final Jws<Claims> jws) {
        return new AcorTokenImpl();
    }

}
