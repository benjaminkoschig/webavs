/*
 * Créé le 21 févr. 06
 */
package globaz.aquila.servlet;

import globaz.aquila.db.administrateurs.COAdministrateurViewBean;
import globaz.aquila.db.administrateurs.CORechercheCompteAnnexeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author dvh
 */
public class COActionAdministrateur extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public COActionAdministrateur(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface viewBean = (FWViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN);
        if (viewBean instanceof COAdministrateurViewBean) {
            request.getSession().setAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((COAdministrateurViewBean) viewBean).getIdTiers());
        }

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // construit un faux viewbean qui contient le nom et le numero d'affilié
        // pour l'affichage dans le RC, puis
        // effectue l'action standard
        CORechercheCompteAnnexeViewBean rechercheCompteAnnexeViewBean = new CORechercheCompteAnnexeViewBean();
        rechercheCompteAnnexeViewBean.setNomAffilie(request.getParameter("nomAffilie"));
        rechercheCompteAnnexeViewBean.setLikeIdExterneRole(request.getParameter("likeIdExterneRole"));
        rechercheCompteAnnexeViewBean.setIdExterneRoleChoisi(request.getParameter("idExterneRoleChoisi"));
        session.setAttribute("viewBean", rechercheCompteAnnexeViewBean);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChoisir(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChoisir(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        request.setAttribute("bFind", "true");
        super.actionChoisir(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /**
             * check des critères
             */

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            // boolean criteresOk = _criteresOk(request);

            /*
             * if (criteresOk) { viewBean = beforeLister(session, request, response, viewBean); viewBean =
             * mainDispatcher.dispatch(viewBean, _action); } else { BSession ses = (BSession)
             * mainDispatcher.getSession(); viewBean.setMessage(ses.getLabel("AQUILA_ERR_CRITERES_INSUFFISANTS" ));
             * viewBean.setMsgType(FWViewBeanInterface.ERROR); }
             */

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        COAdministrateurViewBean administrateurViewBean = (COAdministrateurViewBean) viewBean;
        String forIdExterneLike = request.getParameter("forIdExterneLike");

        if (JadeStringUtil.isEmpty(administrateurViewBean.getForIdExterneLike())) {
            administrateurViewBean.setForIdExterneLike(forIdExterneLike);
        }

        return administrateurViewBean;
    }

}
