package globaz.osiris.eservices.filter;

import globaz.corvus.application.REApplication;
import globaz.osiris.eservices.token.ESTokenServiceImpl;
import globaz.osiris.eservices.ws.ESApiRestRetrieve;
import globaz.prestation.acor.web.ws.TokenService;
import globaz.prestation.acor.web.ws.TokenFilterAbstract;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class ESTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(ESApiRestRetrieve.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return ESTokenServiceImpl.getInstance();
    }
}
