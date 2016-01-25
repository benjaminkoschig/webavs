package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactAQuittancerListViewBean;
import globaz.musca.db.facturation.FAAfactListViewBean;
import globaz.musca.db.facturation.FAAfactViewBean;
import globaz.musca.db.facturation.FAEnteteFactureViewBean;
import globaz.musca.db.facturation.FAPassageViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionAfact extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionAfact(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String quittancer = request.getParameter("quittancer");
        if (!JadeStringUtil.isBlank(quittancer) && !quittancer.equals("null")) {
            return getActionFullURL().substring(0, (getActionFullURL().length() - 5))
                    + "passageFacturation.aQuittancer&selectedId=" + ((FAAfactViewBean) viewBean).getIdPassage();
        } else {
            return getActionFullURL() + ".chercher";
        }
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String quittancer = request.getParameter("quittancer");
        if (!quittancer.equals("") && !quittancer.equals("null"))
        // if (!JadeStringUtil.isBlank(quittancer))
        {
            return getActionFullURL().substring(0, (getActionFullURL().length() - 5))
                    + "passageFacturation.aQuittancer&selectedId=" + ((FAAfactViewBean) viewBean).getIdPassage();
        } else {
            return getActionFullURL() + ".chercher";
        }
    }

    protected void actionAccepterAfact(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            BSession muscaSession = (BSession) mainDispatcher.getSession();

            FAPassageViewBean viewBean = new FAPassageViewBean();
            viewBean.setSession(muscaSession);
            String passageId = request.getParameter("idPassage");
            String fromIdExterneRole = request.getParameter("fromIdExtRole");
            // viewBean.setIdPassage(afact.getIdPassage());
            viewBean.setIdPassage(passageId);
            viewBean.retrieve();

            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = ERROR_PAGE;
            } else {
                FAAfact afact = new FAAfact();
                afact.setSession(muscaSession);
                afact.setIdAfact(request.getParameter("selectedId"));
                afact.retrieve();
                afact.setAQuittancer(new Boolean(false));
                afact.update();

                _destination = getActionFullURL() + ".succes&fromIdExterneRole=" + fromIdExterneRole;
            }

        } catch (Exception e1) {
            _destination = ERROR_PAGE;
            JadeLogger.error(this, e1);
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * Méthode redéfinie pour obtenir une redirection sur la même page Date de création : (26.03.2003 09:09:06)
     * 
     * @param session
     *            javax.servlet.http.HttpSession
     * @param request
     *            javax.servlet.http.HttpServletRequest
     * @param response
     *            javax.servlet.http.HttpServletResponse
     * @param mainDispatcher
     *            globaz.framework.controller.FWDispatcher
     * @exception javax.servlet.ServletException
     *                La description de l'exception.
     * @exception java.io.IOException
     *                La description de l'exception.
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        try {
            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            // GESTION DES DROITS
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * chois de la destination _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja
             * rempli par l'utilisateur
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getActionFullURL() + ".reAfficher";
            } else {
                if (globaz.jade.client.util.JadeStringUtil.isBlank(_destination)) {
                    _destination = getActionFullURL() + ".afficher&_method=add&_valid=_new" + "&idTypeAfact="
                            + request.getParameter("idTypeAfact");
                }
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
            JadeLogger.error(this, e);
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        FAEnteteFactureViewBean viewBean = new FAEnteteFactureViewBean();
        viewBean.setISession(mainController.getSession());
        viewBean.setIdEntete(request.getParameter("idEnteteFacture"));
        // Sauvegarde de l'id entête passé en paramètre pour le récupérer lors
        // de la création
        session.setAttribute("saveIdEntete", request.getParameter("idEnteteFacture"));
        // Extraction des données de l'entête de facture
        try {
            viewBean.retrieve();
            viewBean.setMessage("OK");
            viewBean.setMsgType(FWViewBeanInterface.OK);
        } catch (Exception e) {
            viewBean.setMessage("ERROR");
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        } else {
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        if (getAction().getActionPart().equals("listerAQuittancer")) {
            // chercher avec chargement des données nécessaire
            actionListerAfactAQuittancer(session, request, response, mainDispatcher);
        } else if (getAction().getActionPart().equals("accepterAfact")) {
            actionAccepterAfact(session, request, response, mainDispatcher);
        } else if (getAction().getActionPart().equals("refuserAfact")) {
            actionRefuserAfact(session, request, response, mainDispatcher);
        } else if (getAction().getActionPart().equals("succes")) {
            actionSucces(session, request, response, mainDispatcher);
        }
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     */
    protected void actionListerAfactAQuittancer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination;

        try {
            String rechercheEtatAfact = request.getParameter("forAQuittancer");
            FAAfactAQuittancerListViewBean afactAQuittancer = new FAAfactAQuittancerListViewBean();
            afactAQuittancer.setSession((BSession) mainDispatcher.getSession());

            // GESTION DES DROITS
            afactAQuittancer = (FAAfactAQuittancerListViewBean) mainDispatcher.dispatch(afactAQuittancer, getAction());

            // mettre en session le viewbean
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", afactAQuittancer);
            if (afactAQuittancer.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = ERROR_PAGE;
            } else {
                JSPUtils.setBeanProperties(request, afactAQuittancer);
                if (rechercheEtatAfact.equals("aValider")) {
                    afactAQuittancer.setForAQuittancer(new Boolean(true));
                } else if (rechercheEtatAfact.equals("aRefuser")) {
                    afactAQuittancer.setForAQuittancer(new Boolean(false));
                } else {
                    afactAQuittancer.setForAQuittancer(null);
                }
                afactAQuittancer.find();

                _destination = getRelativeURL(request, session) + "AQuittancer_rcListe.jsp";
            }
        } catch (Exception e1) {
            _destination = ERROR_PAGE;
            JadeLogger.error(this, e1);
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionRefuserAfact(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {
            BSession muscaSession = (BSession) mainDispatcher.getSession();
            FAPassageViewBean viewBean = new FAPassageViewBean();
            viewBean.setSession(muscaSession);
            String passageId = request.getParameter("idPassage");
            String fromIdExterneRole = request.getParameter("fromIdExtRole");
            viewBean.setIdPassage(passageId);
            viewBean.retrieve();

            // GESTION DES DROITS
            viewBean = (FAPassageViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = ERROR_PAGE;
            } else {
                FAAfact afact = new FAAfact();
                afact.setSession(muscaSession);
                afact.setIdAfact(request.getParameter("selectedId"));
                afact.retrieve();
                afact.setAQuittancer(new Boolean(true));
                afact.update();

                _destination = getActionFullURL() + ".succes&fromIdExterneRole=" + fromIdExterneRole;
            }

        } catch (Exception e1) {
            _destination = ERROR_PAGE;
            JadeLogger.error(this, e1);
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    protected void actionSucces(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        _destination = getRelativeURL(request, session) + "AQuittancer_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * Traitement avant l'action lister
     */
    @Override
    protected FWViewBeanInterface beforeLister(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAAfactListViewBean vBean = (FAAfactListViewBean) viewBean;
        // Positionnement selon les critères de recherche
        vBean.setForAQuittancer(null);
        vBean.setForIdModuleFacturation(request.getParameter("forIdModuleFacturation"));
        vBean.setForIdEnteteFacture(request.getParameter("idEnteteFacture"));
        // Tri par rubrique
        // vBean.setFromRubrique(request.getParameter("fromLibelle"));
        return vBean;
    }

    /*
     * Traitement lors d'une création avant l'affichage - Initialisation
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        FAAfactViewBean vBean = (FAAfactViewBean) viewBean;
        // Récupération de l'id entete (décompte) et du passage
        vBean.setIdEnteteFacture((String) session.getAttribute("saveIdEntete"));
        vBean.setIdPassage((String) session.getAttribute("saveIdPassage"));
        try {
            vBean.initDefaultValues();
            return vBean;
        } catch (Exception e) {
            return vBean;
        }
    }
}
