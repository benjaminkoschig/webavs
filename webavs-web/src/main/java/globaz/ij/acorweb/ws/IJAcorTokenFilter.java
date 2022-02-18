package globaz.ij.acorweb.ws;

import globaz.ij.acorweb.ws.token.IJAcorTokenServiceImpl;
import ch.globaz.common.ws.token.TokenService;
import ch.globaz.common.ws.token.TokenFilterAbstract;
import globaz.ij.application.IJApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class IJAcorTokenFilter extends TokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(IJAcorApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
    }

    @Override
    public TokenService getInstanceTokenService() {
        return IJAcorTokenServiceImpl.getInstance();
    }
}
