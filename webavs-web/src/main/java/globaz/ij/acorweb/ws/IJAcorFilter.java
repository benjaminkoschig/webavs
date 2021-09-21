package globaz.ij.acorweb.ws;

import globaz.ij.acorweb.ws.token.IJAcorTokenService;
import globaz.prestation.acor.web.ws.AcorTokenService;
import globaz.prestation.acor.web.ws.AcorFilterAbstract;
import globaz.ij.application.IJApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
@SuppressWarnings("unused" /*Cette class est appelé par réflexion par la class WSConfiguration */)
public class IJAcorFilter extends AcorFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(IJAcorApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
    }

    @Override
    public AcorTokenService getInstanceTokenService() {
        return IJAcorTokenService.getInstance();
    }
}
