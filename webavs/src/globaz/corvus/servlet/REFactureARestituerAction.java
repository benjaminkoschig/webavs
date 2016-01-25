/*
 * Créé le 6 juillet 08
 */
package globaz.corvus.servlet;

import globaz.corvus.application.REApplication;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bsc
 * 
 */
public class REFactureARestituerAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_rc.jsp?";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REFactureARestituerAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    /**
     * Mise en attente ou mise a facturer de la facture a restituer
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    public String actionChangeEtat(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        String destination = null;

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = "/corvusRoot/rentesaccordees/factureARestituer" + VERS_ECRAN_DE;
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        return destination;
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String idTiersBenefPrincipal = request.getParameter("idTiersBenefPrincipal");

        if (!JadeStringUtil.isIntegerEmpty(idTiersBenefPrincipal)) {
            try {
                PRTiersWrapper benefPrincipal = PRTiersHelper.getTiersParId(new BSession(
                        REApplication.DEFAULT_APPLICATION_CORVUS), idTiersBenefPrincipal);

                // On mémorise ls NSS dans la session
                RENSSDTO dto = new RENSSDTO();
                dto.setNSS(benefPrincipal.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

                PRSessionDataContainerHelper.setData(session,
                        PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return viewBean;
    }

}