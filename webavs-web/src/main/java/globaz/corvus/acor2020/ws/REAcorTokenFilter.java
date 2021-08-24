package globaz.corvus.acor2020.ws;

import ch.globaz.common.acor.Acor2020TokenService;
import ch.globaz.common.acor.AcorTokenFilterAbstract;
import globaz.corvus.acor2020.ws.token.REAcor2020TokenService;
import globaz.corvus.application.REApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

public class REAcorTokenFilter extends AcorTokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(REAcor2020ApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    @Override
    public Acor2020TokenService getInstanceTokenConverter() {
        return REAcor2020TokenService.getInstance();
    }
}
