package globaz.corvus.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020TokenService;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.corvus.acor2020.ws.REApiRestAcor2020;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import globaz.jade.i18n.JadeI18n;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Path;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.*;

@Slf4j
public class REAcor2020TokenService implements Acor2020TokenService<REAcor2020Token> {

    private static REAcor2020TokenService instance;

    private static final String LANGUE_PAR_DEFAUT = "FR";
    private static final String API_REST_PATH = REApiRestAcor2020.class.getAnnotation(Path.class).value();
    private static String importUrl = "";
    private static String exportUrl = "";
    private static String export9Url = "";
    private static String acorBaseUrl = "";
    private static String nomHote = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "back.ws.root");

    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // static method to create instance of Singleton class
    public static REAcor2020TokenService getInstance()
    {
        if (instance == null)
            instance = new REAcor2020TokenService();

        return instance;
    }

    private REAcor2020TokenService() {
    }

    public static String createToken(RECalculACORDemandeRenteViewBean bean, String dateDemande, String timeDemande, String timeStampGedo, BSession bSession) {
        try {
            generateWebServiceAddress();
        } catch (UnknownHostException | PropertiesException e) {
            LOG.error("AcorV4TokenService#createToken - Erreur à la création des adresses du WebService.", e);
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

        Map<String, Object> header = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();

        // Header
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        header.put("Access-Control-Allow-Origin", "*");

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

        String jws = Jwts.builder()
                         .setHeader(header)
                         .setClaims(claims)
                         .setIssuer("auth0")
                         .setIssuedAt(new Date()).setExpiration(cal.getTime())
                .signWith(key).compact();
        return jws;
    }

    private static void generateWebServiceAddress() throws UnknownHostException, PropertiesException {
//        InetAddress inetadr = InetAddress.getLocalHost();
//        //nom de machine
//        String nomHote = (String) inetadr.getHostName();

        nomHote = (nomHote.substring(nomHote.length() - 1, nomHote.length()).equals("/") ? nomHote.substring(0, nomHote.length() - 1) : nomHote);

        acorBaseUrl = CommonProperties.ACOR_ADRESSE_WEB.getValue() + "backend/acor-ws-core-web";
        export9Url = nomHote + "/rest/" + API_REST_PATH + "/export9";
        exportUrl = nomHote + "/rest/" + API_REST_PATH + "/export";
        importUrl = nomHote + "/rest/" + API_REST_PATH + "/import";
    }

    @Override
    public REAcor2020Token getToken(String token) {
        if (Objects.isNull(token)) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

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

    @Override
    public String createToken() {
        // TODO : implémenté createToken commun.
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
