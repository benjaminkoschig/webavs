/*
 * Créé le 26 juin 07
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.ci.REInscriptionCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bsc
 * 
 */
public class REInscriptionCIAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REInscriptionCIAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            REInscriptionCIViewBean vb = (REInscriptionCIViewBean) session.getAttribute("viewBean");
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // mise a jour de la caisse tanant le CI si no complet (7 positions)
            String caisseTenantCI = request.getParameter("caisseTenantCI");
            if (caisseTenantCI.length() == 7) {
                String numCaisse = JadeStringUtil.substring(caisseTenantCI, 0, 3);
                String numAgence = JadeStringUtil.substring(caisseTenantCI, 4, 7);

                vb.getAnnonceInscriptionCI().setNoAgenceTenantCI(numAgence);
                vb.getAnnonceInscriptionCI().setNoCaisseTenantCI(numCaisse);
            }

            PRTiersWrapper tiersAyantDroit = PRTiersHelper.getTiers(vb.getSession(),
                    request.getParameter("noAyantDroit"));
            if (null == tiersAyantDroit) {
                vb.getAnnonceInscriptionCI().setIdTiersAyantDroit("");
            } else {
                vb.getAnnonceInscriptionCI().setIdTiersAyantDroit(
                        tiersAyantDroit.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            }

            PRTiersWrapper tiers = PRTiersHelper.getTiers(vb.getSession(), request.getParameter("noAssure"));
            if (null == tiers) {
                vb.getAnnonceInscriptionCI().setIdTiers("");
            } else {
                vb.getAnnonceInscriptionCI().setIdTiers(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            }

            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", vb);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);
        return viewBean;
    }

}