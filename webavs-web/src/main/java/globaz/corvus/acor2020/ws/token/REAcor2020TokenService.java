package globaz.corvus.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020TokenServiceAbstract;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.corvus.acor2020.ws.REAcor2020ApiRest;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Path;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class REAcor2020TokenService extends Acor2020TokenServiceAbstract<REAcor2020Token> {

    @Getter
    private static final REAcor2020TokenService INSTANCE = new REAcor2020TokenService();
    private static final String API_REST_PATH = REAcor2020ApiRest.class.getAnnotation(Path.class).value();

    private static String importUrl = "";
    private static String exportUrl = "";
    private static String export9Url = "";
    private static String acorBaseUrl = "";

    private REAcor2020TokenService() {}

    public static String createToken(RECalculACORDemandeRenteViewBean bean, String dateDemande, String timeDemande, String timeStampGedo, BSession bSession) {
        try {
            generateWebServiceAddress();
        } catch (UnknownHostException | PropertiesException e) {
            LOG.error("AcorV4TokenService#createToken - Erreur à la création des adresses du WebService.", e);
        }

        Map<String, Object> claims = new HashMap<>();

        // Claim
        claims.put("acorBaseUrl", acorBaseUrl);
        claims.put("exportUrl", exportUrl);
        claims.put("export9Url", export9Url);
        claims.put("importUrl", importUrl);
        claims.put("idDemande", bean.getIdDemandeRente());
        claims.put("navs", bean.getNoAVSAssure());
        claims.put("dateDemande", dateDemande);
        claims.put("timeDemande", timeDemande);
        claims.put("timeStampGedo", timeStampGedo);
        claims.put("langue", bSession.getIdLangueISO());
        claims.put("email", bSession.getUserEMail());
        claims.put("userId", bSession.getUserId());
        claims.put("idTiers", bean.getIdTiers());
        claims.put("recordId", bean.getNoAVSAssure());

        return creatToken(claims);
    }

    private static void generateWebServiceAddress() throws UnknownHostException, PropertiesException {
        acorBaseUrl = CommonProperties.ACOR_ADRESSE_WEB.getValue() + "backend/acor-ws-core-web";
        export9Url = NOM_HOTE + "/rest/" + API_REST_PATH + "/export9";
        exportUrl = NOM_HOTE + "/rest/" + API_REST_PATH + "/export";
        importUrl = NOM_HOTE + "/rest/" + API_REST_PATH + "/import";
    }

    public static REAcor2020TokenService getInstance() {
        return INSTANCE;
    }

    @Override
    protected REAcor2020Token convertToken(final Jws<Claims> jws) {
        REAcor2020Token acorV4Token = new REAcor2020Token();
        // TODO Null pointer à traiter
        if (jws.getBody() != null) {
            acorV4Token.setIdDemande(jws.getBody().get("idDemande").toString());
            acorV4Token.setNoAVSDemande(jws.getBody().get("navs").toString());
            acorV4Token.setIdTiers(jws.getBody().get("idTiers").toString());
            acorV4Token.setDateDemande(jws.getBody().get("dateDemande").toString());
            acorV4Token.setTimeDemande(jws.getBody().get("timeDemande").toString());
            acorV4Token.setTimeStampGedo(jws.getBody().get("timeStampGedo").toString());
            acorV4Token.setLangue(jws.getBody().get("langue").toString());
            acorV4Token.setEmail(jws.getBody().get("email").toString());
            acorV4Token.setUserId(jws.getBody().get("userId").toString());
            return acorV4Token;
        }
        return null;
    }

    public static String getImportUrl() {
        return importUrl;
    }

    public static void setImportUrl(String importUrl) {
        REAcor2020TokenService.importUrl = importUrl;
    }

    public static String getExportUrl() {
        return exportUrl;
    }

    public static void setExportUrl(String exportUrl) {
        REAcor2020TokenService.exportUrl = exportUrl;
    }

    public static String getExport9Url() {
        return export9Url;
    }

    public static void setExport9Url(String export9Url) {
        REAcor2020TokenService.export9Url = export9Url;
    }

    public static String getAcorBaseUrl() {
        return acorBaseUrl;
    }

    public static void setAcorBaseUrl(String acorBaseUrl) {
        REAcor2020TokenService.acorBaseUrl = acorBaseUrl;
    }


}
