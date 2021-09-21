package globaz.corvus.acorweb.ws;

import globaz.prestation.acor.web.ws.AcorTokenService;
import globaz.prestation.acor.web.ws.AcorFilterAbstract;
import globaz.corvus.acorweb.ws.token.REAcorTokenService;
import globaz.corvus.application.REApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class REAcorFilter extends AcorFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(REAcorApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    @Override
    public AcorTokenService getInstanceTokenService() {
        return REAcorTokenService.getInstance();
    }
}
