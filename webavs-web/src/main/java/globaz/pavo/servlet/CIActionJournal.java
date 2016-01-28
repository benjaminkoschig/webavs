package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.pavo.db.compte.CIEcritureViewBean;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalImprimerViewBean;
import globaz.pavo.db.inscriptions.CIJournalViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action concernant les journeaux. Date de création : (07.11.2002 17:43:11)
 * 
 * @author: ema
 */
public class CIActionJournal extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CIActionJournal.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionJournal(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    private void _actionAfficherDepuisEcriture(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            // action.changeActionPart(FWAction.ACTION_AFFICHER);

            String idEcritureAAff = request.getParameter("selectedId");
            CIEcritureViewBean ecr = new CIEcritureViewBean();
            ecr.setEcritureId(idEcritureAAff);
            ecr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            ecr.retrieve();
            CIJournalViewBean viewBean = new CIJournalViewBean();
            viewBean.setId(ecr.getIdJournal());
            viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            viewBean.retrieve();

            viewBean = (CIJournalViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionImpression(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CIJournalImprimerViewBean viewBean = new CIJournalImprimerViewBean(new globaz.globall.db.BSession(
                    globaz.pavo.application.CIApplication.DEFAULT_APPLICATION_PAVO));
            // viewBean.setFromIdJournal(request.getParameter("selectedId"));

            String idJournal = request.getParameter("fromIdJournal");

            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CIJournalImprimerViewBean) mainDispatcher.dispatch(viewBean, action);

            // Récupération de la date d'inscription du journal
            if (idJournal != null && idJournal.trim().length() > 0) {
                CIJournal journal = new CIJournal();
                journal.setSession((BSession) globaz.pavo.translation.CodeSystem.getSession(session));
                journal.setIdJournal(idJournal);
                journal.retrieve();
                if (journal != null && !journal.isNew()) {
                    viewBean.setFromDateInscription(journal.getDateInscription());
                }
            }
            // sauve le bean dans la session
            session.setAttribute("viewBean", viewBean);

            // redirection vers destination
            _destination = getRelativeURL(request, session) + "Imprimer_de.jsp";
            // _destination = getAction().getPackagePart() +
            // "/journalImprimer_de.jsp";
        } catch (Exception ex) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher";
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            /*
             * recuperation du bean depuis la session
             */
            CIJournalViewBean viewBean = (CIJournalViewBean) session.getAttribute("viewBean");
            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            // viewBean.setNumeroAffilie(viewBean.getIdAffiliation());
            viewBean = (CIJournalViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (CIJournalViewBean) mainDispatcher.dispatch(viewBean, getAction());
            viewBean.setNumeroAffilie(request.getParameter("idAffiliation"));
            viewBean.setForPremiereFoisSurPage("false");

            session.setAttribute("viewBean", viewBean);
            /*
             * chois de la destination _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja
             * rempli par l'utilisateur
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl&addNewEcriture=false";
            } else {
                _destination = "/" + getAction().getApplicationPart()
                        + "?userAction=pavo.compte.ecriture.chercherSurPage&idJournal="
                        + ((CIJournal) viewBean).getIdJournal() + "&addNewEcriture=true&forPremiereFoisSurPage=false";
                // _destination = request.getContextPath() +
                // request.getServletPath()
                // +"?userAction=pavo.compte.ecriture.chercherSurPage&idJournal="+((CIJournal)viewBean).getIdJournal();
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
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("imprimer")) {
            // chercher le panneau de lancement de l'impression
            _actionImpression(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("afficherDepuisEcriture")) {
            // afficher le dossier depuis l'écran du mandat
            _actionAfficherDepuisEcriture(session, request, response, dispatcher);
        }
    }

    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String action = request.getParameter("userAction");
        String _destination = "";
        CIJournalViewBean viewBean = (CIJournalViewBean) session.getAttribute("viewBean");
        FWAction _action = FWAction.newInstance(action);
        /*
         * 
         * /* recuperation du bean depuis la session
         */
        // FWViewBeanInterface viewBean = (FWViewBeanInterface)
        // session.getAttribute("viewBean");

        /*
         * appelle du dispatcher
         */
        // viewBean = beforeSupprimer(session,request,response,viewBean);
        viewBean = (CIJournalViewBean) mainDispatcher.dispatch(viewBean, _action);

        // journalSupp.setIdJournal(()viewBean)
        // _destination= getRelativeURL(request,session)+"_rc.jsp";

        /*
         * choix de la destination
         */

        /*
         * redirection vers la destination
         */
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            _destination = _getDestSupprimerSucces(session, request, response, viewBean);
        } else {
            _destination = _getDestEchec(session, request, response, viewBean);
        }

        goSendRedirect(_destination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(20);
        return viewBean;
    }
}
