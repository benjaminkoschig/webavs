package globaz.corvus.acorweb.ws.token;

import globaz.corvus.acorweb.ws.REAcorApiRest;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.acor.web.ws.AcorTokenServiceAbstract;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class REAcorTokenService extends AcorTokenServiceAbstract {

    private static final REAcorTokenService INSTANCE = new REAcorTokenService();
    private static final String API_PATH = createApiPath(REAcorApiRest.class);

    public static String createToken(RECalculACORDemandeRenteViewBean bean, BSession bSession) {
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

        return createToken(claims, bSession);
    }

    public static REAcorTokenService getInstance() {
        return INSTANCE;
    }

}
