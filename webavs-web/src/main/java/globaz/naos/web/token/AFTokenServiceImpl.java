package globaz.naos.web.token;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.ws.token.TokenServiceAbstract;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.naos.web.ws.AFApiRestLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AFTokenServiceImpl extends TokenServiceAbstract<AFTokenImpl> {
    private static final AFTokenServiceImpl INSTANCE = new AFTokenServiceImpl();
    private static final String API_PATH = createApiPath(AFApiRestLogin.class);
    private static Integer TOKEN_DURATION = CommonProperties.PY_TOKEN_DURATION.getIntegerWithDefaultValue(1);
    private static final String LOGIN_URL = API_PATH + "/get_token";

    public static AFTokenServiceImpl getInstance() {
        return INSTANCE;
    }

    public static String createTokenAF(BSession bSession) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("loginUrl", LOGIN_URL);

        Date actualDate = new Date();
        String day = JadeDateUtil.getDMYDate(actualDate);
        claims.put("dateDemande", day);
        claims.put("timeDemande", JadeDateUtil.getHMTime(actualDate));
        claims.put(USER_ID, encrypt(bSession.getUserId()));

        return createToken(TOKEN_DURATION, claims, bSession);
    }

    @Override
    protected AFTokenImpl convertTokenSpecific(final Jws<Claims> jws) {
        AFTokenImpl token = new AFTokenImpl();

        if (jws != null && jws.getBody() != null) {
            token.setUserId(decrypt(jws.getBody().get(USER_ID).toString()));
            return token;
        }
        return null;
    }
}
