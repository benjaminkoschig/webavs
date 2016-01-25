/*
 * Created on 17-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.naos.db.suiviAssurance.AFSuiviAssuranceViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité SuiviAssurance.
 * 
 * @author sau
 */
public class AFActionSuiviAssurance extends FWDefaultServletAction {

    /**
     * Constructeur d'AFActionSuiviAssurance.
     * 
     * @param servlet
     */
    public AFActionSuiviAssurance(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        AFSuiviAssuranceViewBean viewBean = new AFSuiviAssuranceViewBean();

        try {
            viewBean = (AFSuiviAssuranceViewBean) mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        String assuranceId = request.getParameter("assuranceId");
        viewBean.setAssuranceId(assuranceId);

        // String selectedId = "";
        /*
         * if (! JAUtil.isStringEmpty(request.getParameter("selectedId"))) { selectedId =
         * request.getParameter("selectedId"); session.removeAttribute("tiersPrincipale");
         * session.setAttribute("tiersPrincipale", selectedId);
         * 
         * } else if (! JAUtil.isStringEmpty((String) session.getAttribute("tiersPrincipale"))) { selectedId =
         * (String)session.getAttribute("tiersPrincipale");
         * 
         * } else { viewBean.setMsgType(FWViewBeanInterface.ERROR); viewBean.setMessage("Aucun id trouvé."); }
         */
        // viewBean.setIdTiers(selectedId);

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFSuiviAssuranceViewBean vBean = (AFSuiviAssuranceViewBean) viewBean;
        vBean.setAssuranceId(request.getParameter("assuranceId"));

        return vBean;
    }
}
