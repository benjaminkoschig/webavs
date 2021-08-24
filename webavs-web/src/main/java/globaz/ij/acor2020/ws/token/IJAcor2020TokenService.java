package globaz.ij.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020TokenServiceAbstract;
import globaz.globall.db.BSession;
import globaz.ij.acor2020.ws.IJAcor2020ApiRest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.HashMap;
import java.util.Map;

public class IJAcor2020TokenService extends Acor2020TokenServiceAbstract<IJAcor2020Token> {

    private static final IJAcor2020TokenService INSTANCE = new IJAcor2020TokenService();
    private static final String API_PATH = createApiPath(IJAcor2020ApiRest.class);

    private static final String EXPORT_URL = API_PATH + "/export";
    private static final String IMPORT_URL = API_PATH + "/import";

    private IJAcor2020TokenService() {}

    public static IJAcor2020TokenService getInstance() {
        return INSTANCE;
    }

    public static String createToken(BSession bSession) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("exportUrl", EXPORT_URL);
        claims.put("importUrl", IMPORT_URL);

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
