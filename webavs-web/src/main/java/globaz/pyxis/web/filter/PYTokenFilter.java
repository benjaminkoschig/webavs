package globaz.pyxis.web.filter;

import ch.globaz.common.ws.token.TokenFilterAbstract;
import ch.globaz.common.ws.token.TokenService;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.web.token.PYTokenServiceImpl;
import globaz.pyxis.web.ws.PYApiRestExecute;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused"/*Cette classe est appelée par réflexion par la classe WSConfiguration*/)
public class PYTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(HttpServletRequest request) {
        return request.getPathInfo().startsWith(PYApiRestExecute.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return TIApplication.DEFAULT_APPLICATION_PYXIS;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return PYTokenServiceImpl.getInstance();
    }
}
