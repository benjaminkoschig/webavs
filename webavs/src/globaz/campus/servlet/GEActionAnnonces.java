package globaz.campus.servlet;

import globaz.campus.vb.annonces.GEAnnoncesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GEActionAnnonces extends FWDefaultServletAction {

    public GEActionAnnonces(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        GEAnnoncesViewBean annonce = (GEAnnoncesViewBean) viewBean;
        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getActionFullURL() + ".afficher&selectedId=" + annonce.getIdAnnonce() + "&_method=upd";
        }
        return _destination;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        GEAnnoncesViewBean annonce = (GEAnnoncesViewBean) viewBean;
        return getActionFullURL() + ".afficher&selectedId=" + annonce.getIdAnnonce();
    }

}
