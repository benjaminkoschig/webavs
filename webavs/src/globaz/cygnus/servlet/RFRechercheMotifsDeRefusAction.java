/*
 * créé le 01 octobre 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusListViewBean;
import globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFRechercheMotifsDeRefusAction extends RFDefaultAction {

    public RFRechercheMotifsDeRefusAction(FWServlet servlet) {
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

    // ajout d'un couple motif de refus-type de soin
    public void ajouterSoinMotif(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            // si on a un idMotifRefus c un update sinon c un ajout
            String methode = "";
            if (JadeStringUtil.isEmpty(((RFRechercheMotifsDeRefusViewBean) viewBean).getIdMotifRefus())) {
                methode = "add";
            } else {
                methode = "upd";
                ((RFRechercheMotifsDeRefusViewBean) viewBean).setIsUpdate(Boolean.TRUE);
            }

            if (goesToSuccessDest) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=" + methode;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        RFRechercheMotifsDeRefusListViewBean listViewBean = (RFRechercheMotifsDeRefusListViewBean) viewBean;
        return listViewBean;
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
        ((RFRechercheMotifsDeRefusViewBean) viewBean).setCodeTypeDeSoinList(request.getParameter("type"));
        ((RFRechercheMotifsDeRefusViewBean) viewBean).setCodeSousTypeDeSoinList(request.getParameter("sousType"));
        /*
         * appelle du dispatcher
         */
        viewBean = mainDispatcher.dispatch(viewBean, _action);

        /*
         * choix de la destination
         */
        // si on a un idMotifRefus c un update sinon c un ajout
        String methode = "";
        if (JadeStringUtil.isEmpty(((RFRechercheMotifsDeRefusViewBean) viewBean).getIdMotifRefus())) {
            methode = "add";
        } else {
            methode = "upd";
            ((RFRechercheMotifsDeRefusViewBean) viewBean).setIsUpdate(Boolean.TRUE);
        }
        /*
         * On vide les code type et sous type de soin
         */
        ((RFRechercheMotifsDeRefusViewBean) viewBean).setCodeSousTypeDeSoin("");
        ((RFRechercheMotifsDeRefusViewBean) viewBean).setCodeTypeDeSoin("");

        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            _destination = getRelativeURL(request, session) + "_de.jsp?_methode=" + methode;
        } else {
            _destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }
}
