package globaz.cygnus.servlet;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFSaisieQdViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jje
 */
public class RFSaisieQdPiedDePageAction extends RFDefaultAction {

    public RFSaisieQdPiedDePageAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((RFSaisieQdViewBean) viewBean).setAfficherDetail(false);
        return super._getDestAjouterEchec(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE
                + ".chercher";
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        ((RFSaisieQdViewBean) viewBean).setAfficherDetail(true);
        return super._getDestModifierEchec(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE
                + ".chercher";
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE
                + ".chercher";
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            String selectedId = request.getParameter("selectedId");
            if (!JadeStringUtil.isEmpty(selectedId)) {
                ((RFSaisieQdViewBean) viewBean).setIdQd(selectedId);
            }

            return viewBean;
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    // @Override INUTILE voir RFDefaultAction
    /*
     * protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request, HttpServletResponse
     * response, FWViewBeanInterface viewBean) { return RFApplication.CYGNUS_RELATIVE_URL +
     * IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE + ".afficher"; }
     */

    @Override
    protected String getRelativeURL(HttpServletRequest request, HttpSession session) {

        String _destination = "";
        String cygnusRelativeUrl = "/cygnusRoot/qds/saisieQd";

        FWViewBeanInterface sessionVb = this.loadViewBean(session);

        String csGenreQd = ((RFSaisieQdViewBean) sessionVb).getCsGenreQd();

        if (IRFQd.CS_GRANDE_QD.equals(csGenreQd)) {
            _destination = cygnusRelativeUrl + "DroitPc";
        } else if (IRFQd.CS_PETITE_QD.equals(csGenreQd)) {
            _destination = cygnusRelativeUrl + "Assure";
        }

        return _destination;
    }

    public void majLimiteAnnuelleQdAssure(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */

            _destination = getRelativeURL(request, session) + "_de.jsp";

            /*
             * redirection vers la destination
             */
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }

    }

    public void majLimiteAnnuelleQdDroitPC(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */

            _destination = getRelativeURL(request, session) + "_de.jsp";

            /*
             * redirection vers la destination
             */
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }

    }
}
