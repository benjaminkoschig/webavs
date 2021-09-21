package globaz.corvus.acorweb.ws.token;

import globaz.prestation.acor.web.ws.AcorTokenServiceAbstract;
import globaz.corvus.acorweb.ws.REAcorApiRest;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class REAcorTokenService extends AcorTokenServiceAbstract<REAcorToken> {

    private static final REAcorTokenService INSTANCE = new REAcorTokenService();
    private static final String API_PATH = createApiPath(REAcorApiRest.class);

    private static final String EXPORT_9URL = API_PATH + "/export9";
    private static final String EXPORT_URL = API_PATH + "/export";
    private static final String IMPORT_URL = API_PATH + "/import";

    public static String createToken(RECalculACORDemandeRenteViewBean bean, String dateDemande, String timeDemande, String timeStampGedo, BSession bSession) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("exportUrl", EXPORT_URL);
        claims.put("export9Url", EXPORT_9URL);
        claims.put("importUrl", IMPORT_URL);

        claims.put("idDemande", bean.getIdDemandeRente());
        claims.put("navs", bean.getNoAVSAssure());
        claims.put("idTiers", bean.getIdTiers());
        claims.put("recordId", bean.getNoAVSAssure());

        claims.put("dateDemande", dateDemande);
        claims.put("timeDemande", timeDemande);
        claims.put("timeStampGedo", timeStampGedo);

        return createToken(claims,bSession);
    }

    public static REAcorTokenService getInstance() {
        return INSTANCE;
    }

    @Override
    protected REAcorToken convertToken(final Jws<Claims> jws) {
        REAcorToken token = new REAcorToken();

        if (jws != null && jws.getBody() != null) {
            token.setIdDemande(jws.getBody().get("idDemande").toString());
            token.setNoAVSDemande(jws.getBody().get("navs").toString());
            token.setIdTiers(jws.getBody().get("idTiers").toString());
            token.setDateDemande(jws.getBody().get("dateDemande").toString());
            token.setTimeDemande(jws.getBody().get("timeDemande").toString());
            token.setTimeStampGedo(jws.getBody().get("timeStampGedo").toString());
            return token;
        }
        return null;
    }

    public static String getImportUrl() {
        return IMPORT_URL;
    }
}
