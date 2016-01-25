/*
 * Créé le 6 août 07
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.annonces.REAnnoncePonctuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
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
 * @author SCR
 */
public class REAnnoncePonctuelleAction extends PRDefaultAction {

    public REAnnoncePonctuelleAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String destination = null;
        try {
            String idRenteAccorde = request.getParameter("idRenteAccordee");

            REAnnoncePonctuelleViewBean viewBean = new REAnnoncePonctuelleViewBean();
            viewBean.setIdRenteAccordee(idRenteAccorde);

            viewBean = (REAnnoncePonctuelleViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (REAnnoncePonctuelleViewBean) mainDispatcher.dispatch(viewBean, action);

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";

        try {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            // Récupération du bean depuis la session
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            // set automatique des properties
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // beforeAdd() call du dispatcher, puis mis en session
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // choix de la destination
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        // redirection vers la destination
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            // Récupération du bean depuis la session
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            // set des properties
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            REAnnoncePonctuelleViewBean vbAnnonce = (REAnnoncePonctuelleViewBean) viewBean;

            if (!JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire1())
                    || !JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire2())) {

                if (!JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire1())) {
                    String nss1 = "756." + vbAnnonce.getNssComplementaire1();
                    PRTiersWrapper tw = PRTiersHelper.getTiers(viewBean.getISession(), nss1);
                    if (null != tw) {
                        vbAnnonce.setIdTiersComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    } else {
                        vbAnnonce.setMsgType(FWViewBeanInterface.ERROR);
                        vbAnnonce.setMessage(vbAnnonce.getSession().getLabel("ERREUR_ANN_PONCT_NSS_COMP_1") + " "
                                + nss1);
                    }
                }

                if (!JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire2())) {
                    String nss2 = "756." + vbAnnonce.getNssComplementaire2();
                    PRTiersWrapper tw = PRTiersHelper.getTiers(viewBean.getISession(), nss2);
                    if (null != tw) {
                        vbAnnonce.setIdTiersComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    } else {
                        vbAnnonce.setMsgType(FWViewBeanInterface.ERROR);
                        vbAnnonce.setMessage(vbAnnonce.getMessage() + "<BR>"
                                + vbAnnonce.getSession().getLabel("ERREUR_ANN_PONCT_NSS_COMP_2") + " " + nss2);
                    }
                }
            }
            if (JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire1())) {
                vbAnnonce.setIdTiersComplementaire1("");
            }
            if (JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire2())) {
                vbAnnonce.setIdTiersComplementaire2("");
            }

            // beforeUpdate, call du dispatcher puis mis en session
            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                vbAnnonce = (REAnnoncePonctuelleViewBean) beforeModifier(session, request, response, viewBean);
                vbAnnonce = (REAnnoncePonctuelleViewBean) mainDispatcher.dispatch(viewBean, _action);
            }

            session.setAttribute("viewBean", vbAnnonce);

            // choix de la destination _valid=fail : revient en mode edition
            if (!vbAnnonce.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = "/corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                        + ".chercher";
            } else {
                destination = _getDestModifierEchec(session, request, response, vbAnnonce);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        // redirection vers la destination
        goSendRedirect(destination, request, response);
    }

    public void ajouterAnnoncePonctuelle(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String _destination = "";

        try {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            REAnnoncePonctuelleViewBean vbAnnonce = (REAnnoncePonctuelleViewBean) viewBean;

            if (!JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire1())
                    || !JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire2())) {

                if (!JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire1())) {
                    String nss1 = "756." + vbAnnonce.getNssComplementaire1();
                    PRTiersWrapper tw = PRTiersHelper.getTiers(viewBean.getISession(), nss1);
                    if (null != tw) {
                        vbAnnonce.setIdTiersComplementaire1(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    } else {
                        vbAnnonce.setMsgType(FWViewBeanInterface.ERROR);
                        vbAnnonce.setMessage(vbAnnonce.getSession().getLabel("ERREUR_ANN_PONCT_NSS_COMP_1") + " "
                                + nss1);
                    }
                }

                if (!JadeStringUtil.isEmpty(vbAnnonce.getNssComplementaire2())) {
                    String nss2 = "756." + vbAnnonce.getNssComplementaire2();
                    PRTiersWrapper tw = PRTiersHelper.getTiers(viewBean.getISession(), nss2);
                    if (null != tw) {
                        vbAnnonce.setIdTiersComplementaire2(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                    } else {
                        vbAnnonce.setMsgType(FWViewBeanInterface.ERROR);
                        vbAnnonce.setMessage(vbAnnonce.getMessage() + "<br />"
                                + vbAnnonce.getSession().getLabel("ERREUR_ANN_PONCT_NSS_COMP_2") + " " + nss2);
                    }
                }
            }

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                vbAnnonce = (REAnnoncePonctuelleViewBean) beforeModifier(session, request, response, viewBean);
                vbAnnonce = (REAnnoncePonctuelleViewBean) mainDispatcher.dispatch(viewBean, _action);
            }

            session.setAttribute("viewBean", vbAnnonce);

            if (!vbAnnonce.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = "/corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                        + ".chercher";
            } else {
                _destination = _getDestModifierEchec(session, request, response, vbAnnonce);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        goSendRedirect(_destination, request, response);
    }

    public void ajouterAnnoncePonctuelleAncienNss(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        String _destination = "";

        try {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = "/corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                        + ".chercher";
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        goSendRedirect(_destination, request, response);
    }
}
