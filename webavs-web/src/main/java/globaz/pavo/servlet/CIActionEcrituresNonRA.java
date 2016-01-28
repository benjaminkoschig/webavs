/*
 * Créé le 14 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.pavo.db.compte.CIEcritureViewBean;
import globaz.pavo.db.compte.CIEcrituresNonRAImprimerViewBean;
import globaz.pavo.db.compte.CIEcrituresNonRAViewBean;
import globaz.pavo.db.inscriptions.CIJournalViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action permettant le processus d'impression des inscriptions non connues au RA
 * 
 * @author sda
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIActionEcrituresNonRA extends FWDefaultServletAction {

    /**
     * Constructeur de la classe CIActionEcrituresNonRA
     * 
     * @param servlet
     */
    public CIActionEcrituresNonRA(FWServlet servlet) {

        super(servlet);
    }

    private void _actionImpression(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // nouvelle instance du bean utilisé dans l'en-tête de la recherche.
            CIEcrituresNonRAImprimerViewBean viewBean = new CIEcrituresNonRAImprimerViewBean(
                    new globaz.globall.db.BSession(globaz.pavo.application.CIApplication.DEFAULT_APPLICATION_PAVO));
            // viewBean.setFromIdJournal(request.getParameter("selectedId"));

            String idJournal = request.getParameter("fromIdJournal");

            CIJournalViewBean journal = new CIJournalViewBean();
            journal.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            journal.setIdJournal(idJournal);
            journal.retrieve();
            viewBean.setIdJournal(journal.getIdJournal());
            viewBean.setIdAffilie(journal.getNumeroAffilie());
            viewBean.setJournalDescription(journal.getDescription());
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            // appel du controlleur
            viewBean = (CIEcrituresNonRAImprimerViewBean) mainDispatcher.dispatch(viewBean, action);

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

    // Permet de mofifier l'écriture(change le viewBean "Ecriture" en viewBean
    // "Journal"
    protected void _actionModifierDepuisEcriture(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {

            FWAction _action = getAction();
            // _action.setRight(FWSecureConstants.UPDATE);
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);

            /*
             * recupération du bean depuis la sesison
             */
            CIEcrituresNonRAViewBean viewBean = new CIEcrituresNonRAViewBean();

            /*
             * set des properietes
             */

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            String idEcritureAAff = request.getParameter("selectedId");

            // viewBean.isCertificat();

            CIEcrituresNonRAViewBean ecr = new CIEcrituresNonRAViewBean();
            ecr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            ecr.setEcritureId(viewBean.getEcritureId());
            ecr.retrieve();
            ecr.setCertificat(viewBean.isCertificat());

            viewBean = (CIEcrituresNonRAViewBean) mainDispatcher.dispatch(ecr, _action);
            session.setAttribute("viewBean", ecr);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _getDestEchec(session, request, response, viewBean);
            } else {
                _destination = _getDestModifierSucces(session, request, response, viewBean);
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        goSendRedirect(_destination, request, response);
    }

    /* Change le viewBean "Ecriture" en viewBean "Journal" */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = null;
        FWAction action = getAction();
        action.setRight(FWSecureConstants.READ);
        String idJournal = request.getParameter("selectedId");
        CIEcritureViewBean ecr = new CIEcritureViewBean();
        ecr.setIdJournal(idJournal);
        ecr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
        try {
            ecr.retrieve();
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }
        CIJournalViewBean viewBean = new CIJournalViewBean();
        viewBean.setId(ecr.getIdJournal());
        viewBean.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
        try {
            viewBean.retrieve();
        } catch (Exception e1) {
            // TODO Bloc catch auto-généré
            e1.printStackTrace();
        }

        viewBean = (CIJournalViewBean) mainDispatcher.dispatch(viewBean, action);
        session.removeAttribute("viewBean");

        session.setAttribute("viewBean", viewBean);
        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (globaz.globall.util.JAUtil.isStringEmpty(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /* Change le viewBean "Ecriture" en "Journal" */
    /*
     * protected void actionModifier( HttpSession session, HttpServletRequest request, HttpServletResponse response,
     * FWDispatcher mainDispatcher) throws ServletException, IOException { String _destination = null; try{
     * 
     * 
     * //String action = request.getParameter ("userAction"); //FWAction _action = FWAction.newInstance(action);
     * FWAction _action = getAction(); _action.setRight(FWSecureConstants.UPDATE);
     * getAction().changeActionPart(FWAction.ACTION_MODIFIER);
     * 
     * /* recupération du bean depuis la sesison
     */
    // FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute
    // ("viewBean");

    /*
     * set des properietes
     */
    /*
     * globaz.globall.http.JSPUtils.setBeanProperties(request,viewBean);
     * 
     * String idEcritureAAff = request.getParameter("selectedId");
     * 
     * 
     * 
     * CIEcrituresNonRAViewBean ecr = new CIEcrituresNonRAViewBean(); ecr.setIdJournal(idEcritureAAff);
     * ecr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
     * //ecr.setEmployeurPartenaire(viewBean.) ecr.retrieve();
     * 
     * CIJournalViewBean viewBeanJournal = new CIJournalViewBean(); viewBeanJournal.setId(idEcritureAAff);
     * viewBeanJournal.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
     * viewBeanJournal.retrieve();
     * 
     * 
     * 
     * viewBean = mainDispatcher.dispatch(viewBean,_action); session.setAttribute ("viewBean", viewBeanJournal);
     * 
     * _destination = _getDestModifierSucces(session,request,response,viewBean);
     * 
     * 
     * 
     * } catch (Exception e) { _destination = ERROR_PAGE; }
     * 
     * /* redirection vers la destination
     */
    /*
     * servlet.getServletContext().getRequestDispatcher (_destination).forward (request, response); }
     */

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("modifierDepuisEcriture")) {
            // chercher le _caPage.jsp d'affichage des inscriptions
            _actionModifierDepuisEcriture(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimer")) {
            _actionImpression(session, request, response, dispatcher);
        }
    }

}
