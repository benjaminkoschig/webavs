package globaz.ij.acor2020.ws.token;

import ch.globaz.common.acor.Acor2020TokenServiceAbstract;
import globaz.ij.acor2020.ws.IJAcor2020ApiRest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.ws.rs.Path;

public class IJAcor2020TokenService extends Acor2020TokenServiceAbstract<IJAcor2020Token> {

    private static final IJAcor2020TokenService INSTANCE = new IJAcor2020TokenService();
    private static final String API_REST_PATH = IJAcor2020ApiRest.class.getAnnotation(Path.class).value();

    private IJAcor2020TokenService() {}

    public static IJAcor2020TokenService getInstance() {
        return INSTANCE;
    }

    @Override
    protected IJAcor2020Token convertToken(final Jws<Claims> jws) {
        return new IJAcor2020Token();
    }
}
