/*
 * Créé le 7 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.http.JSPUtils;
import globaz.naos.process.AFCalculRetroactifProcess;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

;

/**
 * @author mmu
 */
public class AFActionCalculRetroactif extends FWDefaultServletAction {

    /**
     * @param servlet
     */
    public AFActionCalculRetroactif(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if ("generer".equals(getAction().getActionPart())) {
            actionGenerer(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            BISession bSession = mainDispatcher.getSession();

            AFCalculRetroactifProcess calcul = (AFCalculRetroactifProcess) session.getAttribute("viewBean");

            calcul.setISession(bSession);
            JSPUtils.setBeanProperties(request, calcul);
            calcul.setSendCompletionMail(true);
            calcul.setControleTransaction(true);
            mainDispatcher.dispatch(calcul, getAction());

            // Fait remonter les erreurs de validations
            if (calcul.isAborted() && bSession.hasErrors()) {
                calcul.setMsgType(FWViewBeanInterface.ERROR);
                calcul.setMessage(bSession.getErrors().toString());
            }

            if (calcul.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="
                        + calcul.getIdAffiliation();
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionGenerer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination = "";
        String idAffiliation = request.getParameter("affiliationId");

        AFCalculRetroactifProcess calcul = new AFCalculRetroactifProcess();
        calcul.setISession(dispatcher.getSession());
        calcul.setIdAffiliation(idAffiliation);
        session.setAttribute("viewBean", calcul);

        getAction().setRight(FWSecureConstants.READ);
        calcul = (AFCalculRetroactifProcess) dispatcher.dispatch(calcul, getAction());

        if (FWViewBeanInterface.ERROR.equals(calcul.getMsgType())) {
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_de.jsp";
        }

        // String _destination = getRelativeURL(request, session) + "_de.jsp";

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}
