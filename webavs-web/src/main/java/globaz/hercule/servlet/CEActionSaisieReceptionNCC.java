/*
 * Globaz SA.
 */
package globaz.hercule.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.hercule.db.noncertifiesconformes.CESaisieReceptionNCCViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CEActionSaisieReceptionNCC extends FWDefaultServletAction {

    public CEActionSaisieReceptionNCC(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);

        CESaisieReceptionNCCViewBean vb = (CESaisieReceptionNCCViewBean) session.getAttribute(FWServlet.VIEWBEAN);
        String annee = vb.getForAnnee();
        String dateReception = vb.getForDateReception();
        String msg = ((BSession) ((FWController) session.getAttribute("objController")).getSession())
                .getLabel("DS_SAISIE_MASSE_RECEPTION_DERNIER_AFFILIE_TRAITE") + " : " + vb.getForNumeroAffilie();

        if (JadeStringUtil.isBlank(destination)) {
            destination = getActionFullURL() + ".afficher&_method=upd&" + "forDateReception=" + dateReception
                    + "&forAnnee=" + annee + "&msg=" + msg;
        }

        return destination;
    }

    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) throws UnsupportedEncodingException {
        if (url == null) {
            return url;
        }
        if (url.indexOf("userAction=hercule.noncertifiesconformes.saisieReceptionNCC.afficher") > -1) {
            return url;
        }
        return super.addParametersFrom(request, url);
    }
}
