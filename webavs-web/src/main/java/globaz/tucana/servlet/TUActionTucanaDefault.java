/*
 * Créé le 25 avr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.tucana.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jsp.util.GlobazJSPBeanUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author fgo
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class TUActionTucanaDefault extends FWDefaultServletAction {

    /**
     * @param servlet
     */
    public TUActionTucanaDefault(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            // Défini les droits pour notre action custom
            getAction().setRight(FWSecureConstants.READ);
            // this.getAction().setRight(FWSecureConstants.ADD);
            // this.getAction().setRight(FWSecureConstants.UPDATE);
            // getAction().setRight(FWSecureConstants.REMOVE);

            // Récupère le viewbean de la session
            FWViewBeanInterface viewBean = getActionCustomViewBean(getAction(), request, session, dispatcher);
            // Défini les properties contenues dedans
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // GlobazJSPBeanUtil.populate(request, viewBean);
            // Effectue le traitement de l'action custom
            viewBean = dispatcher.dispatch(viewBean, getAction());
            // Mets dans la session le viewbean
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination, les méthodes appelées ci-dessous peuvent être surchargées selon le besoin...
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _getDestExecuterEchec(session, request, response, viewBean);
            } else {
                _destination = _getDestExecuterSucces(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        // Redirection de la page
        goSendRedirect(_destination, request, response);
    }

    /**
     * Action standard d'importation
     * 
     * @param session
     *            La session courante
     * @param request
     *            La request courante
     * @param response
     *            La response courante
     * @param dispatcher
     *            Le dispatcher
     * @throws ServletException
     * @throws IOException
     */
    protected void actionCustomImporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            getAction().setRight(FWSecureConstants.READ);
            getAction().setRight(FWSecureConstants.ADD);
            getAction().setRight(FWSecureConstants.UPDATE);
            getAction().setRight(FWSecureConstants.REMOVE);

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            GlobazJSPBeanUtil.populate(request, viewBean);
            viewBean = dispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _getDestExecuterEchec(session, request, response, viewBean);
            } else {
                _destination = _getDestExecuterSucces(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * Factorise cette méthode afin d'y accéder par héritage. Elle permet l'ajout d'un viewBean de manière générique.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @return Le viewBean ajouté
     * @throws Exception
     */
    protected FWViewBeanInterface ajouterViewBean(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws Exception {
        /*
         * recuperation du bean depuis la session
         */
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        /*
         * set automatique des proprietes
         */
        GlobazJSPBeanUtil.populate(request, viewBean);

        /*
         * beforeAdd() call du dispatcher, puis mis en session
         */
        viewBean = beforeAjouter(session, request, response, viewBean);
        viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));
        session.setAttribute("viewBean", viewBean);
        return viewBean;
    }

    /**
     * @param action
     * @param request
     * @param session
     * @return
     */
    protected FWViewBeanInterface getActionCustomViewBean(FWAction action, HttpServletRequest request,
            HttpSession session, FWDispatcher dispatcher) {
        return FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
    }

    /**
     * Permet de récupérer l'objet BSession contenu dans l'HttpSession
     * 
     * @param session
     *            La session HTTP
     * @return L'instance de l'interface Session du Framework
     */
    protected BSession getSession(HttpSession session) {
        return (BSession) ((globaz.framework.controller.FWController) session.getAttribute("objController"))
                .getSession();
    }

}
