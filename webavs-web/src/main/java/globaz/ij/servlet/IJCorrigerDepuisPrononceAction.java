package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJCorrigerDepuisPrononceViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author rco Crée le 26.06.2013 Modifié le 30.09.2013
 * 
 */
public class IJCorrigerDepuisPrononceAction extends PRDefaultAction {

    private final static String NO_AVS = "noAVS";
    private String destination = "";
    private FWAction privateAction = null;

    /**
     * Crée une nouvelle instance de la classe IJCorrigerDepuisPrononceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJCorrigerDepuisPrononceAction(FWServlet servlet) {
        super(servlet);
    }

    /**
	 * 
	 */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String action = request.getParameter("userAction");
        privateAction = FWAction.newInstance(action);
        // TODO: retourner du genre: userAction=ij.prononces.prononceJointDemande.chercher
        if ((privateAction != null) && (privateAction.isWellFormed())) {
            return "/" + privateAction.getApplicationPart() + "?userAction=" + privateAction.getApplicationPart() + "."
                    + privateAction.getPackagePart() + ".prononceJointDemande.chercher";
        }

        return "";// this.getRelativeURLwithoutClassPart
    }

    /**
	 * 
	 */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        /*
         * Recréation dynamique de notre viewBean
         */
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
        viewBean.setISession(mainDispatcher.getSession());

        try {
            // on rempli le viewBean avec les données de la requêtes
            JSPUtils.setBeanProperties(request, viewBean);

            // on l'envoie au Helper
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            this.saveViewBean(viewBean, request);
            this.saveViewBean(viewBean, session);

            destination = getRelativeURL(request, session) + "_de.jsp";

            if (JadeStringUtil.isBlank(destination)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination += "?_method=add"; // Active le bouton valider directement
            }

            // à l'inverse du goSendRedirect, le forward sera enregistré pour la flèche "back" (en vert en haut à
            // droite)
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

    }

    /**
	 * 
	 */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        IJCorrigerDepuisPrononceViewBean viewBean = (IJCorrigerDepuisPrononceViewBean) session.getAttribute("viewBean");
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            // FWViewBeanInterface viewBean = prononce;

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            // viewBean = this.beforeModifier(session, request, response, prononce);
            mainDispatcher.dispatch(viewBean, newAction);
            this.saveViewBean(viewBean, request);
            this.saveViewBean(viewBean, session);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

}
