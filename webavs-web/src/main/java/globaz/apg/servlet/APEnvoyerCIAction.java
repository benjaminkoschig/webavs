/*
 * Créé le 7 oct. 05
 */
package globaz.apg.servlet;

import globaz.apg.vb.process.APEnvoyerCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnvoyerCIAction extends APDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJEnvoyerAnnoncesAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APEnvoyerCIAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        // Définition du viewBean et settage des paramètres nécessaires dans le
        // viewBean

        String isRegeneration = request.getParameter("isRegeneration");
        String noPassage = "";

        APEnvoyerCIViewBean viewBean = new APEnvoyerCIViewBean();

        viewBean.setEMailAddress(request.getParameter("eMailAddress"));

        if (null != isRegeneration) {
            noPassage = request.getParameter("noPassage");
            viewBean.setRegeneration(true);
        } else {
            noPassage = request.getParameter("noPassageNew");
            viewBean.setRegeneration(false);
        }

        viewBean.setNoPassageFinal(noPassage);

        // Définition de la destination et redirection suivant l'exécution...
        String destination = "";
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            destination = _getDestExecuterEchec(session, request, response, viewBean);
        } else {
            destination = _getDestExecuterSucces(session, request, response, viewBean);
        }

        goSendRedirect(destination, request, response);

        // Appelle du dispatch pour atteindre le helper
        mainDispatcher.dispatch(viewBean, getAction());

    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String processLaunchedStr = request.getParameter("process");
        boolean processSeemsOk = "launched".equals(processLaunchedStr);
        String validFail = processSeemsOk ? "" : "?_valid=fail";
        servlet.getServletContext()
                .getRequestDispatcher(getRelativeURLwithoutClassPart(request, session) + "envoyerCI_de.jsp" + validFail)
                .forward(request, response);
    }

}
