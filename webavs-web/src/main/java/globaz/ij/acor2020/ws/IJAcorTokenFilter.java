package globaz.ij.acor2020.ws;

import globaz.ij.acor2020.ws.token.IJAcor2020TokenService;
import globaz.prestation.acor.acor2020.ws.Acor2020TokenService;
import globaz.prestation.acor.acor2020.ws.AcorTokenFilterAbstract;
import globaz.corvus.acor2020.ws.token.REAcor2020TokenService;
import globaz.ij.application.IJApplication;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

public class IJAcorTokenFilter extends AcorTokenFilterAbstract {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().startsWith(IJAcor2020ApiRest.class.getAnnotation(Path.class).value());
    }

    @Override
    public String getApplicationId() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
    }

    @Override
    public Acor2020TokenService getInstanceTokenConverter() {
        return IJAcor2020TokenService.getInstance();
    }
}
