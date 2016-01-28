package globaz.corvus.servlet;

import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.rentesaccordees.RESaisirDateEcheanceViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RESaisirEcheanceAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RESaisirEcheanceAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("idRenteAccordee");

            FWViewBeanInterface viewBean = new RESaisirDateEcheanceViewBean();

            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            PRTiersWrapper tier = PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                    ((RESaisirDateEcheanceViewBean) viewBean).getIdTiersBeneficiaire());

            String nss = "";

            if (tier != null) {
                nss = tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            }

            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(nss);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = "/corvus?userAction=corvus.rentesaccordees.renteAccordeeJointDemandeRente.chercher";
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

}
