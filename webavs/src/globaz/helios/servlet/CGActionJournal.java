package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGJournalAnnulerViewBean;
import globaz.helios.db.comptes.CGJournalComptabiliserViewBean;
import globaz.helios.db.comptes.CGJournalExComptabiliserViewBean;
import globaz.helios.db.comptes.CGJournalExtournerViewBean;
import globaz.helios.db.comptes.CGJournalImprimerEcrituresViewBean;
import globaz.helios.db.comptes.CGJournalListViewBean;
import globaz.helios.db.comptes.CGJournalViewBean;
import globaz.helios.tools.CGSessionDataContainerHelper;
import globaz.helios.translation.CodeSystem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour le journal.
 * 
 * @author: dda
 */
public class CGActionJournal extends CGActionNeedExerciceComptable {

    public static final String JOURNAL_PREFIX = "journal";
    private static final String JSP_ANNULER_DE = "Annuler_de.jsp";
    private static final String JSP_COMPTABILISER_DE = "Comptabiliser_de.jsp";
    private static final String JSP_EX_COMPTABILISER_DE = "ExComptabiliser_de.jsp";
    private static final String JSP_EXTOURNER_DE = "Extourner_de.jsp";
    private static final String JSP_IMPRIMER_ECRITURES_DE = "ImprimerEcritures_de.jsp";

    public CGActionJournal(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    private void actionAfficherAnnuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {
            CGJournalAnnulerViewBean viewBean = new CGJournalAnnulerViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + JSP_ANNULER_DE;
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherComptabiliser(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGJournalComptabiliserViewBean viewBean = new CGJournalComptabiliserViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + JSP_COMPTABILISER_DE;
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherExComptabiliser(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGJournalExComptabiliserViewBean viewBean = new CGJournalExComptabiliserViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + JSP_EX_COMPTABILISER_DE;
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherExtourner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {
            CGJournalExtournerViewBean viewBean = new CGJournalExtournerViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + JSP_EXTOURNER_DE;
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void actionAfficherImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {
            CGJournalImprimerEcrituresViewBean viewBean = new CGJournalImprimerEcrituresViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIdJournal(request.getParameter(SELECTED_ID));
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + JSP_IMPRIMER_ECRITURES_DE;
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getActionFullURL() + ".afficher";

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));
            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestEchec(session, request, response, viewBean);
            } else {
                destination = getActionFullURL() + ".afficher" + "&_method=&selectedId="
                        + ((CGJournalViewBean) viewBean).getIdJournal();
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("imprimer".equals(getAction().getActionPart())) {
            actionAfficherImprimer(session, request, response, mainDispatcher);
        } else if ("comptabiliser".equals(getAction().getActionPart())) {
            actionAfficherComptabiliser(session, request, response, mainDispatcher);
        } else if ("exComptabiliser".equals(getAction().getActionPart())) {
            actionAfficherExComptabiliser(session, request, response, mainDispatcher);
        } else if ("annuler".equals(getAction().getActionPart())) {
            actionAfficherAnnuler(session, request, response, mainDispatcher);
        } else if ("extourner".equals(getAction().getActionPart())) {
            actionAfficherExtourner(session, request, response, mainDispatcher);
        } else if ("afficherJournal".equals(getAction().getActionPart())) {
            afficherJournal(session, request, response, mainDispatcher);
        }

    }

    protected void afficherJournal(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String urlDestination = getActionFullURL() + ".afficher&selectedId=" + request.getParameter("idJournal");
        goSendRedirect(urlDestination, request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface) N'affiche que les 50 premiers journaux lors dans le rcListe.
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(50);

        String idPeriodeComptable = ((CGJournalListViewBean) viewBean).getForIdPeriodeComptable();

        CGSessionDataContainerHelper sessionDataContainer = new CGSessionDataContainerHelper();
        sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE,
                idPeriodeComptable.trim());

        return viewBean;
    }
}
