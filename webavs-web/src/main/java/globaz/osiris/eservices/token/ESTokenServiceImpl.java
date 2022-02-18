package globaz.osiris.eservices.token;

import ch.globaz.common.properties.CommonProperties;
import globaz.osiris.eservices.ws.ESApiRestLogin;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import ch.globaz.common.ws.token.TokenServiceAbstract;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ESTokenServiceImpl extends TokenServiceAbstract<ESTokenImpl> {

    private static final ESTokenServiceImpl INSTANCE = new ESTokenServiceImpl();
    private static final String API_PATH = createApiPath(ESApiRestLogin.class);
    private static Integer TOKEN_DURATION = CommonProperties.ES_TOKEN_DURATION.getIntegerWithDefaultValue(1);
    private static final String LOGIN_URL = API_PATH + "/get_token";

    public static ESTokenServiceImpl getInstance() {
        return INSTANCE;
    }

    public static String createTokenES(BSession bSession) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("loginUrl", LOGIN_URL);

        Date actualDate = new Date();
        String day = JadeDateUtil.getDMYDate(actualDate);
        claims.put("dateDemande", day);
        claims.put("timeDemande", JadeDateUtil.getHMTime(actualDate));
        claims.put("timeStampGedo", day);
        claims.put(USER_ID, encrypt(bSession.getUserId()));

        return createToken(TOKEN_DURATION, claims, bSession);
    }

    @Override
    protected ESTokenImpl convertTokenSpecific(final Jws<Claims> jws) {
        ESTokenImpl token = new ESTokenImpl();

        if (jws != null && jws.getBody() != null) {
            token.setDateDemande(jws.getBody().get("dateDemande").toString());
            token.setTimeDemande(jws.getBody().get("timeDemande").toString());
            token.setTimeStampGedo(jws.getBody().get("timeStampGedo").toString());
            token.setUserId(decrypt(jws.getBody().get(USER_ID).toString()));
            return token;
        }
        return null;
    }

}

