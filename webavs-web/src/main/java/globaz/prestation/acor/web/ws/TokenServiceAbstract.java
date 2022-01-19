package globaz.prestation.acor.web.ws;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.util.Slashs;
import ch.globaz.common.ws.configuration.WSConfiguration;
import globaz.globall.db.BSession;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypter;
import globaz.jade.crypto.JadeEncrypterLocator;
import globaz.jade.i18n.JadeI18n;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class TokenServiceAbstract<T extends Token> implements TokenService<T> {

    private static final String NOM_HOTE = resolveNomHote();
    private static final String BASE_REST_URI = NOM_HOTE + WSConfiguration.class.getAnnotation(ApplicationPath.class).value();
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String LANGUE = "langue";
    private static final String EMAIL = "email";
    protected static final String USER_ID = "userId";

    public static String createApiPath(Class<?> apiRestClass) {
        String apiPath = apiRestClass.getAnnotation(Path.class).value();
        return BASE_REST_URI + apiPath;
    }

    public static String loadAcorBaseUrl() {
        return Exceptions.checkedToUnChecked(() -> {
            String adresseWeb = CommonProperties.ACOR_ADRESSE_WEB.getValue();
            String adressePath = CommonProperties.ACOR_BACKEND_PATH.getValueWithDefault("/acor-ws-core-web");
            return Slashs.deleteLastSlash(adresseWeb) + Slashs.addFirstSlash(adressePath);
        }, "Impossible to have properties for acor");
    }

    private static String resolveNomHote() {
        String hote = JadeI18n.getInstance().getMessage("FR", "back.ws.root");
        return Slashs.deleteLastSlash(hote);
    }

    public static String createToken(Integer tokenDuration, final Map<String, Object> claims, final BSession bSession) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, tokenDuration);

        claims.put(LANGUE, bSession.getIdLangueISO());
        claims.put(EMAIL, bSession.getUserEMail());

        Map<String, Object> header = new HashMap<>();
        // Header
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        header.put("Access-Control-Allow-Origin", "*");

        return Jwts.builder()
                   .setHeader(header)
                   .setClaims(claims)
                   .setIssuer("auth0")
                   .setIssuedAt(new Date()).setExpiration(cal.getTime())
                   .signWith(KEY).compact();
    }

    @Override
    public T convertToken(String tokenJson) {
        if (Objects.isNull(tokenJson)) {
            return null;
        }

        Jws<Claims> jws = this.convertTokenToJws(tokenJson);

        T token = this.convertTokenSpecific(jws);

        token.setLangue(jws.getBody().get(LANGUE).toString());
        token.setEmail(jws.getBody().get(EMAIL).toString());
        token.setUserId(jws.getBody().get(USER_ID).toString());

        return token;
    }

    protected abstract T convertTokenSpecific(Jws<Claims> jws);

    public Jws<Claims> convertTokenToJws(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parserBuilder()
                   .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
    }

    protected static String encrypt(String value) {
        JadeEncrypter encrypter = JadeEncrypterLocator.getInstance().getAdapter(JadeDefaultEncrypters.JADE_DEFAULT_ENCRYPTER);
        try {
            return encrypter.encrypt(value);
        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de l'encryptage d'une valeur dans le token!", e);
        }
        return null;
    }
}
