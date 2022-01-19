package globaz.corvus.acorweb.ws;

import globaz.prestation.acor.web.ws.TokenService;
import globaz.prestation.acor.web.ws.TokenFilterAbstract;
import globaz.corvus.acorweb.ws.token.REAcorTokenServiceImpl;
import globaz.corvus.application.REApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused" /*Cette class est appel� par r�flexion par la class WSConfiguration */)
public class REAcorTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(REAcorApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return REAcorTokenServiceImpl.getInstance();
    }
}
