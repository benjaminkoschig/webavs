package globaz.apg.acorweb.ws.token;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.ws.token.TokenServiceAbstract;
import globaz.apg.acorweb.ws.APAcorApiRest;
import globaz.apg.vb.prestation.APCalculACORViewBean;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.acor.web.ws.AcorTokenImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class APAcorTokenServiceImpl extends TokenServiceAbstract<AcorTokenImpl> {

    private static final APAcorTokenServiceImpl INSTANCE = new APAcorTokenServiceImpl();
    private static final String API_PATH = createApiPath(APAcorApiRest.class);
    private static Integer TOKEN_DURATION = CommonProperties.ACOR_TOKEN_DURATION.getIntegerWithDefaultValue(1);
    private static final String APG_URL = API_PATH;
    private static final String ACTION_IMPORT = "import";
    private static final String ACTION_EXPORT = "export";

    public static APAcorTokenServiceImpl getInstance() {
        return INSTANCE;
    }

    public static String createTokenAPG(APCalculACORViewBean viewBean, BSession bSession) {
        return createTokenAP(bSession, APG_URL + "/" + viewBean.getIdDroit() + "/" + viewBean.getGenreService() + "/", viewBean.getIdDroit());
    }

    public static String createTokenAP(BSession bSession, String urlBase, String idDroit) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("exportAPGUrl", urlBase + ACTION_EXPORT);
        // claims.put("exportIJUrl", urlBase + ACTION_EXPORT); // TODO WS ACOR APG /!\ UTILE POUR TESTER LE INHOSTTYPE DE TEST SUR CICICAM /!\
        claims.put("importUrl", urlBase + ACTION_IMPORT);
        claims.put("recordId", idDroit);

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
