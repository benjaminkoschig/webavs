package ch.globaz.common.acor;

import globaz.jade.i18n.JadeI18n;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Acor2020TokenServiceAbstract<T extends Acor2020Token> implements Acor2020TokenService<T> {

    protected static final String NOM_HOTE = resolveNomHote();
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static String resolveNomHote() {
        String hote = JadeI18n.getInstance().getMessage("FR", "back.ws.root");
        return (hote.endsWith("/") ? hote.substring(0, hote.length() - 1) : hote);
    }

    public static String creatToken(final Map<String, Object> claims) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

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

    protected abstract T convertToken(Jws<Claims> jws);

    public Jws<Claims> convertTokenToJws(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parserBuilder()
                   .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
    }

    @Override
    public T convertToken(String token) {
        if (Objects.isNull(token)) {
            return null;
        }
        Jws<Claims> jws = this.convertTokenToJws(token);
        return this.convertToken(jws);
    }
}
