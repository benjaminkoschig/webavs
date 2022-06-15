package globaz.apg.acorweb.ws;

import ch.globaz.common.ws.token.TokenFilterAbstract;
import ch.globaz.common.ws.token.TokenService;
import globaz.apg.acorweb.ws.token.APAcorTokenServiceImpl;
import globaz.apg.application.APApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class APAcorTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(APAcorApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return APApplication.DEFAULT_APPLICATION_APG;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return APAcorTokenServiceImpl.getInstance();
    }
}
