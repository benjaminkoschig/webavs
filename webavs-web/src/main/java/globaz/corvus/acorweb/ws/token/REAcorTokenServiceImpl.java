package globaz.corvus.acorweb.ws.token;

import ch.globaz.common.properties.CommonProperties;
import globaz.corvus.acorweb.ws.REAcorApiRest;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import ch.globaz.common.ws.token.TokenServiceAbstract;
import globaz.prestation.acor.web.ws.AcorTokenImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class REAcorTokenServiceImpl extends TokenServiceAbstract<AcorTokenImpl> {

    private static final REAcorTokenServiceImpl INSTANCE = new REAcorTokenServiceImpl();
    private static final String API_PATH = createApiPath(REAcorApiRest.class);
    private static Integer TOKEN_DURATION = CommonProperties.ACOR_TOKEN_DURATION.getIntegerWithDefaultValue(1);

    public static REAcorTokenServiceImpl getInstance() {
        return INSTANCE;
    }

    public static String createTokenRE(RECalculACORDemandeRenteViewBean bean, BSession bSession) {
        Map<String, Object> claims = new HashMap<>();

        String apiPathWithIdDemande = API_PATH + "/" + bean.getIdDemandeRente();

        claims.put("exportUrl", apiPathWithIdDemande + "/" + bean.getIdTiers() + "/export");
        claims.put("export9Url", apiPathWithIdDemande + "/" + bean.getIdTiers() + "/export9");
        claims.put("importUrl", apiPathWithIdDemande + "/import");

        claims.put("recordId", bean.getNoAVSAssure());

        Date actualDate = new Date();
        String day = JadeDateUtil.getDMYDate(actualDate);
        claims.put("dateDemande", day);
        claims.put("timeDemande", JadeDateUtil.getHMTime(actualDate));
        claims.put("timeStampGedo", day);

        claims.put("acorBaseUrl", loadAcorBaseUrl());
        claims.put(USER_ID, bSession.getUserId());

        return createToken(TOKEN_DURATION, claims, bSession);
    }

    @Override
    protected AcorTokenImpl convertTokenSpecific(final Jws<Claims> jws) {
        return new AcorTokenImpl();
    }

}
