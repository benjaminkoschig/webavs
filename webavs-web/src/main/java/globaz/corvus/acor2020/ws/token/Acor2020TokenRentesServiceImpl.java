package globaz.corvus.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020Token;
import ch.globaz.common.acor.Acor2020TokenService;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.globall.db.BSession;
import globaz.jade.i18n.JadeI18n;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.security.Key;
import java.util.*;

@Service
@Slf4j
public class Acor2020TokenRentesServiceImpl implements Acor2020TokenService {

    private static Acor2020TokenRentesServiceImpl instance;

    private static final String LANGUE_PAR_DEFAUT = "FR";
    private static String importUrl = "";
    private static String exportUrl = "";
    private static String export9Url = "";
    private static String acorBaseUrl = "";
    private static String nomHote = JadeI18n.getInstance().getMessage(LANGUE_PAR_DEFAUT, "back.ws.root");

    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // static method to create instance of Singleton class
    public static Acor2020TokenRentesServiceImpl getInstance()
    {
        if (instance == null)
            instance = new Acor2020TokenRentesServiceImpl();

        return instance;
    }

    private Acor2020TokenRentesServiceImpl() {
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

        String jws = Jwts.builder().setHeader(header).setClaims(claims).setIssuer("auth0").setIssuedAt(new Date()).setExpiration(cal.getTime())
                .signWith(key).compact();
        return jws;
    }

    private static void generateWebServiceAddress() throws UnknownHostException, PropertiesException {
//        InetAddress inetadr = InetAddress.getLocalHost();
//        //nom de machine
//        String nomHote = (String) inetadr.getHostName();

        nomHote = (nomHote.substring(nomHote.length() - 1, nomHote.length()).equals("/") ? nomHote.substring(0, nomHote.length() - 1) : nomHote);

        acorBaseUrl = CommonProperties.ACOR_ADRESSE_WEB.getValue() + "backend/acor-ws-core-web";
        export9Url = nomHote + "/export9";
        exportUrl = nomHote + "/export";
        importUrl = nomHote + "/import";
    }

    @Override
    public Acor2020Token getToken(String token) {
        if (Objects.nonNull(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Acor2020TokenRentes acorV4Token = new Acor2020TokenRentes();

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
        Acor2020TokenRentesServiceImpl.importUrl = importUrl;
    }

    public static String getExportUrl() {
        return exportUrl;
    }

    public static void setExportUrl(String exportUrl) {
        Acor2020TokenRentesServiceImpl.exportUrl = exportUrl;
    }

    public static String getExport9Url() {
        return export9Url;
    }

    public static void setExport9Url(String export9Url) {
        Acor2020TokenRentesServiceImpl.export9Url = export9Url;
    }

    public static String getAcorBaseUrl() {
        return acorBaseUrl;
    }

    public static void setAcorBaseUrl(String acorBaseUrl) {
        Acor2020TokenRentesServiceImpl.acorBaseUrl = acorBaseUrl;
    }
}
