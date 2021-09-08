package globaz.ij.acor2020.ws.token;

import globaz.globall.db.BSession;
import globaz.ij.acor2020.ws.IJAcor2020ApiRest;
import globaz.prestation.acor.acor2020.ws.Acor2020TokenServiceAbstract;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.HashMap;
import java.util.Map;

public class IJAcor2020TokenService extends Acor2020TokenServiceAbstract<IJAcor2020Token> {

    private static final IJAcor2020TokenService INSTANCE = new IJAcor2020TokenService();
    private static final String API_PATH = createApiPath(IJAcor2020ApiRest.class);

    private static final String EXPORT_CALCUL_URL = API_PATH + "/calcul/export";
    private static final String IMPORT_CALCUL_URL = API_PATH + "/calcul/import";

    private static final String EXPORT_DECOMPTE_URL =  API_PATH + "/decompte/export";
    private static final String IMPORT_DECOMPTE_URL =  API_PATH + "/decompte/import";


    private IJAcor2020TokenService() {}

    public static IJAcor2020TokenService getInstance() {
        return INSTANCE;
    }

    public static String createTokenCalcul(BSession bSession, final String idPrononce, final String noAVSAssure) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("exportIJUrl", EXPORT_CALCUL_URL + "/" + idPrononce);
        claims.put("importUrl", IMPORT_CALCUL_URL + "/" +  idPrononce);
        claims.put("recordId", noAVSAssure);

        return creatToken(claims, bSession);
    }

    public static String createTokenDecompte(BSession bSession, final String idPrononce, final String idBaseIdemnisation, final String noAVSAssure) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("exportIJUrl", EXPORT_DECOMPTE_URL + "/" +  idPrononce + "/" + idBaseIdemnisation);
        claims.put("importUrl", IMPORT_DECOMPTE_URL+ "/" +  idPrononce + "/" + idBaseIdemnisation);
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
