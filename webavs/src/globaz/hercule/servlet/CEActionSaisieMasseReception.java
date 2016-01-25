package globaz.hercule.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.hercule.db.declarationStructuree.CESaisieMasseReceptionViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CEActionSaisieMasseReception extends FWDefaultServletAction {

    public CEActionSaisieMasseReception(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = "";

        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);

        CESaisieMasseReceptionViewBean vb = (CESaisieMasseReceptionViewBean) session.getAttribute(FWServlet.VIEWBEAN);
        String annee = vb.getForAnnee();
        String dateReception = vb.getForDateReception();
        String msg = ((BSession) ((FWController) session.getAttribute("objController")).getSession())
                .getLabel("DS_SAISIE_MASSE_RECEPTION_DERNIER_AFFILIE_TRAITE") + " : " + vb.getForNumeroAffilie();

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getActionFullURL() + ".afficher&_method=upd&" + "forDateReception=" + dateReception
                    + "&forAnnee" + annee + "&msg=" + msg;
        }

        return _destination;
    }

    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) throws UnsupportedEncodingException {
        if (url == null) {
            return url;
        }
        if (url.indexOf("userAction=hercule.declarationStructuree.saisieMasseReception.afficher") > -1) {
            return url;
        }
        return super.addParametersFrom(request, url);
    }

}
