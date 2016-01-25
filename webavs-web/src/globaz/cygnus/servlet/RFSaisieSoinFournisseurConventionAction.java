package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.conventions.RFConventionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFSaisieSoinFournisseurConventionAction extends RFDefaultAction {

    public RFSaisieSoinFournisseurConventionAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        session.removeAttribute("viewBean");
        return "/cygnus?userAction=" + IRFActions.ACTION_CONVENTION + ".chercher";
    }

    /**
     * sur annuler on renvoi sur la recherche d'une convention
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/cygnus?userAction=" + IRFActions.ACTION_CONVENTION + ".chercher";
    }

    @Override
    public void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            FWViewBeanInterface viewBean = this.loadViewBean(session);

            _action.changeActionPart(FWAction.ACTION_NOUVEAU);

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // recuperer les id et genre de service du droit pour le retour depuis
        // pyxis
        RFConventionViewBean conventionViewBean = (RFConventionViewBean) this.loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append("userAction");
        queryString.append("=");
        queryString.append(IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION);

        queryString.append(".");
        queryString.append(FWAction.ACTION_AFFICHER);
        queryString.append("&idConvention");
        queryString.append(conventionViewBean.getIdConvention());
        queryString.append("&libelle");
        queryString.append(conventionViewBean.getLibelle());

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /*
     * ajout d'un assuré
     */
    public void ajouterAssure(HttpSession session, HttpServletRequest request, HttpServletResponse response,
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
            if (goesToSuccessDest) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_ajout=assure";
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    public void ajouterBDD(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            Boolean isAjout = ((RFConventionViewBean) viewBean).getIsAjout();

            viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // save isAjout
            ((RFConventionViewBean) viewBean).setIsAjout(isAjout);

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
            if (goesToSuccessDest) {
                _destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

    /*
     * ajout d'un couple Fournisseur-type de soin
     */
    public void ajouterFournisseurType(HttpSession session, HttpServletRequest request, HttpServletResponse response,
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
            if (goesToSuccessDest) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_ajout=fournisseur";
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {

            viewBean = this.loadViewBean(session);

            // sauvegarder isAjout
            Boolean isAjout = ((RFConventionViewBean) viewBean).getIsAjout();

            JSPUtils.setBeanProperties(request, viewBean);

            // charger isAjout
            ((RFConventionViewBean) viewBean).setIsAjout(isAjout);

            return viewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    /**
     * Sur annuler on vide le contenu des 2 tableaux (assuré et soin/fournisseur)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((RFConventionViewBean) viewBean).getFournisseurTypeArray().clear();
        ((RFConventionViewBean) viewBean).getAssureArray().clear();

        return viewBean;
    }

    // suppression d'un assuré
    public void supprimerAssure(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        /*
         * recuperation du bean depuis la session
         */
        viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        ((RFConventionViewBean) viewBean).setNss(request.getParameter("nssKey"));
        ((RFConventionViewBean) viewBean).setDateDebut(request.getParameter("dDebutKey"));
        ((RFConventionViewBean) viewBean).setDateFin(request.getParameter("dFinKey"));
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

    public void supprimerCoupleFournisseurSTS(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        /*
         * recuperation du bean depuis la session
         */
        viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        /*
         * retrouver la ligne à supprimer
         */
        ((RFConventionViewBean) viewBean).setIdFournisseur(request.getParameter("idFournisseur"));
        ((RFConventionViewBean) viewBean).setIdSousTypeDeSoin(request.getParameter("idSousType"));
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
