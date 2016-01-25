package globaz.hercule.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CEActionSaisieMasseReviseur extends FWDefaultServletAction {

    public CEActionSaisieMasseReviseur(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = "";

        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getActionFullURL() + ".afficher&_method=upd";
        }

        return _destination;
    }

    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) throws UnsupportedEncodingException {
        /*
         * if (url == null) { return url; } if(url.indexOf(
         * "userAction=hercule.controleEmployeur.saisieMasseReviseur.afficher") > -1) { return url; }
         */
        return super.addParametersFrom(request, url);
    }

}
