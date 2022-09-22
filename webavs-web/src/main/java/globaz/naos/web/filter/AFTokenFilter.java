package globaz.naos.web.filter;

import ch.globaz.common.ws.token.TokenFilterAbstract;
import ch.globaz.common.ws.token.TokenService;
import globaz.naos.application.AFApplication;
import globaz.naos.web.token.AFTokenServiceImpl;
import globaz.naos.web.ws.AFApiRestExecute;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused"/*Cette classe est appelée par réflexion par la classe WSConfiguration*/)
public class AFTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(HttpServletRequest request) {
        return request.getPathInfo().startsWith(AFApiRestExecute.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return AFApplication.DEFAULT_APPLICATION_NAOS;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return AFTokenServiceImpl.getInstance();
    }
}
