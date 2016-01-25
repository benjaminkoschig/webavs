package globaz.cygnus.servlet;

import globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFSaisieMotifsDeRefusAction extends RFDefaultAction {

    public RFSaisieMotifsDeRefusAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        session.removeAttribute("viewBean");
        return "/cygnus?userAction=" + IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS + ".chercher";
    }

    /**
     * sur annuler on renvoi sur la recherche d'une convention
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/cygnus?userAction=" + IRFActions.ACTION_RECHERCHE_MOTIFS_DE_REFUS + ".chercher";
    }

    /**
     * Sur annuler on vide le contenu du tableau SoinMotif
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((RFRechercheMotifsDeRefusViewBean) viewBean).getSoinMotifArray().clear();

        return viewBean;
    }

    public void supprimerCoupleMotifRefusSTS(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        /*
         * recuperation du bean depuis la session
         */
        viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        // retrouver la ligne à supprimer
        ((RFRechercheMotifsDeRefusViewBean) viewBean).setIdMotifRefus(request.getParameter("idMotifRefus"));
        ((RFRechercheMotifsDeRefusViewBean) viewBean).setIdSousTypeDeSoin(request.getParameter("idSousType"));
        /*
         * appelle du dispatcher
         */
        viewBean = mainDispatcher.dispatch(viewBean, _action);

        /*
         * choix de la destination
         */
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            _destination = getRelativeURL(request, session) + "_de.jsp";
        } else {
            _destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
