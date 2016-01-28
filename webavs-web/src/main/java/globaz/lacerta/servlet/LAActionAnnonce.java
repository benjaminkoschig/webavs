package globaz.lacerta.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.application.AFApplication;
import globaz.naos.process.AFImpressionAnnonceBatch;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import process.LAProcessAnnonceEnvoiViewBean;

/**
 * @author jpa
 */
public class LAActionAnnonce extends FWDefaultServletAction {

    public LAActionAnnonce(FWServlet servlet) {
        super(servlet);
    }

    private void _actionAfficherEnvoiAnnonces(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            LAProcessAnnonceEnvoiViewBean viewBean = new LAProcessAnnonceEnvoiViewBean();
            viewBean.setSession(bSession);
            request.setAttribute("viewBean", viewBean);
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURLwithoutClassPart(request, session) + "annonceEnvoi_de.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }

    private void _actionAfficherReceptionAnnonces(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            LAProcessAnnonceEnvoiViewBean viewBean = new LAProcessAnnonceEnvoiViewBean();
            viewBean.setSession(bSession);
            request.setAttribute("viewBean", viewBean);
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURLwithoutClassPart(request, session) + "reception_de.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }

    private void _actionCreationEnvoi(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(bSession));
            LAProcessAnnonceEnvoiViewBean viewBean = new LAProcessAnnonceEnvoiViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            AFImpressionAnnonceBatch annonce = new AFImpressionAnnonceBatch();
            annonce.setISession(sessionNaos);
            annonce.setEMailAddress(viewBean.getEMailAddress());
            annonce.setDateAnnonce(viewBean.getDateAnnonce());
            // annonce.setImpression(viewBean.getImpression());
            // annonce.setCreationFichier(viewBean.getCreationFichier());
            BProcessLauncher.start(annonce);
            request.setAttribute("viewBean", viewBean);
            _destination = "/lacerta?userAction=lacerta.annonce.afficherEnvoiAnnonces";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == false) {
                _destination = _destination + "&process=launched";
                response.sendRedirect(request.getContextPath() + _destination);
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "annonceEnvoi_de.jsp";
                servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if ("afficherEnvoiAnnonces".equals(getAction().getActionPart())) {
            _actionAfficherEnvoiAnnonces(session, request, response, (BSession) dispatcher.getSession());
        } else if ("envoyer".equals(getAction().getActionPart())) {
            _actionCreationEnvoi(session, request, response, (BSession) dispatcher.getSession());
        } else if ("afficherReceptionAnnonces".equals(getAction().getActionPart())) {
            _actionAfficherReceptionAnnonces(session, request, response, (BSession) dispatcher.getSession());
        }
    }
}
