package globaz.osiris.eservices.filter;

import globaz.osiris.application.CAApplication;
import globaz.osiris.eservices.token.ESTokenServiceImpl;
import globaz.osiris.eservices.ws.ESApiRestRetrieve;
import ch.globaz.common.ws.token.TokenService;
import ch.globaz.common.ws.token.TokenFilterAbstract;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

@SuppressWarnings("unused" /*Cette class est appel? par r?flexion par la class WSConfiguration */)
public class ESTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(ESApiRestRetrieve.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return CAApplication.DEFAULT_APPLICATION_OSIRIS;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return ESTokenServiceImpl.getInstance();
    }
}
