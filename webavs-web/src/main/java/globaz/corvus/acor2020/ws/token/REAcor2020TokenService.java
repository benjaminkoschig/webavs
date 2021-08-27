package globaz.corvus.acor2020.ws.token;

import globaz.prestation.acor.acor2020.ws.Acor2020TokenServiceAbstract;
import globaz.corvus.acor2020.ws.REAcor2020ApiRest;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class REAcor2020TokenService extends Acor2020TokenServiceAbstract<REAcor2020Token> {

    private static final REAcor2020TokenService INSTANCE = new REAcor2020TokenService();
    private static final String API_PATH = createApiPath(REAcor2020ApiRest.class);

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

        return creatToken(claims,bSession);
    }

    public static REAcor2020TokenService getInstance() {
        return INSTANCE;
    }

    @Override
    protected REAcor2020Token convertToken(final Jws<Claims> jws) {
        REAcor2020Token token = new REAcor2020Token();
        // TODO Null pointer à traiter
        if (jws.getBody() != null) {
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
