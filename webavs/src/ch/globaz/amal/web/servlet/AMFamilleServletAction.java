/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.vb.famille.AMFamilleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author CBU
 * 
 */
public class AMFamilleServletAction extends AMAbstractServletAction {

    private static final String FAMILLE_DETAIL_URL = "/amal?userAction=amal.famille.famille.afficher";

    public AMFamilleServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = super._getDestAjouterEchec(session, request, response, viewBean);
        destination += "&_method=add";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (HttpSession
     * session,HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "";
        AMFamilleViewBean familleViewBean = (AMFamilleViewBean) viewBean;
        destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
        destination += familleViewBean.getContribuable().getContribuable().getIdContribuable();
        destination += "&selectedTabId=0";
        return destination;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String id = ((AMFamilleViewBean) viewBean).getFamilleContribuable().getSimpleFamille().getId();

        return AMFamilleServletAction.FAMILLE_DETAIL_URL + "&selectedId=" + id;
    }

    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        session.setAttribute("msgType", viewBean.getMsgType());
        session.setAttribute("message", viewBean.getMessage());
        String destination = "/amal?userAction=amal.contribuable.contribuable.reAfficher";
        return destination;
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = "";
        AMFamilleViewBean familleViewBean = (AMFamilleViewBean) viewBean;
        destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
        destination += familleViewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable();
        destination += "&selectedTabId=0";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionReAfficher(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        if (!(session.getAttribute("viewBean") == null) && !(session.getAttribute("message") == null)
                && !(session.getAttribute("msgType") == null)) {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            viewBean.setMessage((String) session.getAttribute("message"));
            viewBean.setMsgType((String) session.getAttribute("msgType"));
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
        }
        super.actionReAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof AMFamilleViewBean) {
            AMFamilleViewBean viewBeanFamille = (AMFamilleViewBean) viewBean;

            viewBeanFamille.getFamilleContribuable().getSimpleFamille()
                    .setIdContribuable(viewBeanFamille.getContribuable().getContribuable().getIdContribuable());

            return super.beforeAjouter(session, request, response, viewBeanFamille);
        } else {
            return super.beforeAjouter(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(HttpSession session, HttpServletRequest
     * request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // Get parameters >> target the correct revenu
        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        String selectedId = request.getParameter("selectedId");
        if (JadeStringUtil.isEmpty(selectedId)) {
            selectedId = request.getParameter("id");
        }

        /*
         * Creation dynamique de notre viewBean
         */
        AMFamilleViewBean familleViewBean = (AMFamilleViewBean) FWViewBeanActionFactory.newInstance(_action, "AM");

        // Set Id
        familleViewBean.setId(selectedId);

        // Retrieve (fill) View Bean
        try {
            familleViewBean.retrieve();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return familleViewBean;
    }
}
