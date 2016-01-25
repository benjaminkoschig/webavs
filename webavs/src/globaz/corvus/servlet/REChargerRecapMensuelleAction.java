/*
 * Créé le 8 févr. 07
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.recap.REChargerRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jsp.util.GlobazJSPBeanUtil;
import globaz.prestation.servlet.PRDefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 */
public class REChargerRecapMensuelleAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String ISCHARGEMENTRECAP = "isChargementRecap";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REChargerRecapMensuelleAction(FWServlet servlet) {
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
    // public void afficherChargement(HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWDispatcher mainDispatcher,
    // FWViewBeanInterface viewBean) throws Exception {
    // REChargerRecapMensuelleViewBean vb = new
    // REChargerRecapMensuelleViewBean();
    //
    // String destination = getRelativeURL(request, session) + "_de.jsp";
    // getAction().setRight(FWSecureConstants.ADD);
    // /*
    // * initialisation du viewBean
    // */
    // if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
    // viewBean = beforeNouveau(session,request,response,viewBean);
    // }
    //
    // /*
    // * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
    // */
    // viewBean = beforeAfficher(session,request,response,viewBean);
    // mainDispatcher.dispatch(vb, getAction());
    // saveViewBean(vb, session);
    // servlet.getServletContext().getRequestDispatcher(destination).forward(request,
    // response);
    //
    // }
    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.servlet.PRDefaultAction#actionCustom(javax.servlet. http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    public void afficherChargement(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        String _destination = "";
        try {

            // Récupère le viewbean de la session
            if (viewBean != null && viewBean instanceof REChargerRecapMensuelleViewBean) {
                viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            } else {
                viewBean = new REChargerRecapMensuelleViewBean();
            }
            // Défini les properties contenues dedans
            GlobazJSPBeanUtil.populate(request, viewBean);
            // Effectue le traitement de l'action custom
            boolean viderMsg = false;
            if (JadeStringUtil.isEmpty(viewBean.getMessage())) {
                viderMsg = true;
            }
            // viewBean = mainDispatcher.dispatch(viewBean, getAction());
            if (viderMsg) {
                viewBean.setMessage("");
                viewBean.setMsgType("");
            }
            // Mets dans la session le viewbean
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination, les méthodes appelées ci-dessous peuvent être surchargées selon le besoin...
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _getDestEchec(session, request, response, viewBean);
            } else {
                _destination = FWScenarios.getInstance().getDestination(
                        (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                        new FWRequestActionAdapter().adapt(request), viewBean);
                if (JadeStringUtil.isBlank(_destination)) {
                    _destination = getRelativeURL(request, session) + "_de.jsp";
                }
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    public void charger(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        boolean forward = false;
        String _destination = "";
        try {

            // Récupère le viewbean de la session
            viewBean = new REChargerRecapMensuelleViewBean();
            // Défini les properties contenues dedans
            GlobazJSPBeanUtil.populate(request, viewBean);
            // Effectue le traitement de l'action custom
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            // Mets dans la session le viewbean
            session.setAttribute("viewBean", viewBean);
            // On indique que l'on a charger la recap
            session.setAttribute(ISCHARGEMENTRECAP, "True");

            /*
             * choix de la destination, les méthodes appelées ci-dessous peuvent être surchargées selon le besoin...
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                forward = true;
                _destination = getActionFullURL() + ".afficherChargement";
            } else {
                _destination = "/" + getAction().getApplicationPart() + "?userAction=" + IREActions.ACTION_RECAP_DETAIL
                        + ".chargerAvs" + "&" + METHOD_UPD;
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        if (forward) {
            goSendRedirect(_destination, request, response);
        } else {

            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }

}