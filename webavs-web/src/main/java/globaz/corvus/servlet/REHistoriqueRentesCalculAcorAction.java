/*
 * Créé le 29 juin 07
 */

package globaz.corvus.servlet;

import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteListViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * 
 * @deprecated
 * @author HPE
 * 
 */

@Deprecated
public class REHistoriqueRentesCalculAcorAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REHistoriqueRentesCalculAcorAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            RERenteAccordeeJointDemandeRenteViewBean viewBean = new RERenteAccordeeJointDemandeRenteViewBean();

            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (RERenteAccordeeJointDemandeRenteViewBean) beforeNouveau(session, request, response,
                        viewBean);
            }

            viewBean = (RERenteAccordeeJointDemandeRenteViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (RERenteAccordeeJointDemandeRenteViewBean) mainDispatcher.dispatch(viewBean, getAction());
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

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            RERenteAccordeeJointDemandeRenteListViewBean viewBean = new RERenteAccordeeJointDemandeRenteListViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            if (!JadeStringUtil.isIntegerEmpty(request.getParameter("idTierRequerant"))) {

                PRTiersWrapper t = PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                        request.getParameter("idTierRequerant"));
                String navs = t.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                if (navs.length() > 14) {
                    viewBean.setLikeNumeroAVSNNSS("true");
                } else {
                    viewBean.setLikeNumeroAVSNNSS("False");
                }

                viewBean.setLikeNumeroAVS(navs);
                viewBean.wantCallMethodBeforeFind(true);
                viewBean.setRechercheFamille(true);
            }

            viewBean = (RERenteAccordeeJointDemandeRenteListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (RERenteAccordeeJointDemandeRenteListViewBean) mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";

        } catch (Exception e) {

            _destination = ERROR_PAGE;

        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

}
