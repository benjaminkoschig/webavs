package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJAnnulerCorrigerDepuisPrononceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author rco Crée le 09.07.2013 Modifié le 04.10.2013
 * 
 */
public class IJAnnulerCorrigerDepuisPrononceAction extends PRDefaultAction {
    private String destination = "";
    private FWAction privateAction = null;

    /**
     * 
     * @param servlet
     */
    public IJAnnulerCorrigerDepuisPrononceAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String action = request.getParameter("userAction");
        privateAction = FWAction.newInstance(action);
        // TODO: retourner du genre: userAction=ij.prononces.prononceJointDemande.chercher
        if ((privateAction != null) && (privateAction.isWellFormed())) {
            return "/" + privateAction.getApplicationPart() + "?userAction=" + privateAction.getApplicationPart() + "."
                    + privateAction.getPackagePart() + ".prononceJointDemande.chercher";
        }
        return "";
    }

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

    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        FWViewBeanInterface viewBean = (IJAnnulerCorrigerDepuisPrononceViewBean) session.getAttribute("viewBean");
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // appelle du dispatcher
        viewBean = beforeSupprimer(session, request, response, viewBean);
        viewBean = mainDispatcher.dispatch(viewBean, newAction);

        /*
         * choix de la destination
         */
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            destination = _getDestSupprimerSucces(session, request, response, viewBean);
        } else {
            destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }
        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }
}
