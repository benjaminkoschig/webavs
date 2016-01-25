package globaz.osiris.servlet.action.interets;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.osiris.db.interets.CAApercuInteretMoratoireListViewBean;
import globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des intérêts.
 * 
 * @author DDA
 */
public class CAInteretMoratoireAction extends CADefaultServletAction {

    /**
     * @param servlet
     */
    public CAInteretMoratoireAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&selectedId=" + ((CAInteretMoratoire) viewBean).getIdInteretMoratoire();
    }

    /**
     * Affiche une décision d'intérêt<br/>
     * Si la méthode est add (ajout d'une nouvelle décision) on va premièrement chercher une facture parmis le passage
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        BSession bSession = (BSession) mainDispatcher.getSession();
        if (bSession.getApplicationId().equals(FAApplication.DEFAULT_APPLICATION_MUSCA)) {
            bSession.setAttribute("musca_interet_moratoire", new Boolean(true));
        }

        String _myDestination = getRelativeURL(request, session) + "_de.jsp";
        try {
            FWViewBeanInterface element = getElement(request);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));

            if (element instanceof CAInteretMoratoireViewBean) {
                if (((CAInteretMoratoireViewBean) element).getCompteAnnexe() != null) {
                    request.getSession().setAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                            ((CAInteretMoratoireViewBean) element).getCompteAnnexe().getIdTiers());
                }
            }

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            _myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CAApercuInteretMoratoireViewBean element = getInteretMoratoire(session, request, response, mainDispatcher);

            CAApercuInteretMoratoireListViewBean manager = getInteretMoratoireManager(request);

            BSession osirisSession = (BSession) mainDispatcher.getSession();

            String forDomaine = request.getParameter("forDomaine");
            if ("FA".equals(forDomaine)) {
                BSession bSession = (BSession) session.getAttribute("objSession");
                bSession.setAttribute("musca_interet_moratoire", new Boolean(true));
                osirisSession.setAttribute("musca_interet_moratoire", new Boolean(true));
            }

            manager.setSession(osirisSession);
            JSPUtils.setBeanProperties(request, manager);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);
            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = FWDefaultServletAction.ERROR_PAGE;
            JadeLogger.error(this, e);
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CAApercuInteretMoratoireViewBean element = getInteretMoratoire(session, request, response, mainDispatcher);
            FWViewBeanInterface manager = getInteretMoratoireManager(request);

            if (JadeStringUtil.isNull(((CAApercuInteretMoratoireListViewBean) manager).getForIdJournalCalcul())) {
                ((CAApercuInteretMoratoireListViewBean) manager).setForIdJournalCalcul(element.getIdJournalCalcul());
            }

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    protected CAInteretMoratoireViewBean getElement(HttpServletRequest request) throws ServletException {
        CAInteretMoratoireViewBean element = (CAInteretMoratoireViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.interets.CAInteretMoratoireViewBean", "session");

        if (!JadeStringUtil.isNull(getId(request, "IdInteretMoratoire"))) {
            element.setIdInteretMoratoire(getId(request, "IdInteretMoratoire"));
        } else {
            element = (CAInteretMoratoireViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.interets.CAInteretMoratoireViewBean", "session", true);
        }

        if (request.getParameter("domaine") != null) {
            element.setDomaine(request.getParameter("domaine"));
        } else if (request.getParameter("forDomaine") != null) {
            element.setDomaine(request.getParameter("forDomaine"));
        }

        return element;
    }

    protected CAApercuInteretMoratoireViewBean getInteretMoratoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws Exception {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        action.changeActionPart(FWAction.ACTION_AFFICHER);

        FWViewBeanInterface element = (CAApercuInteretMoratoireViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean", "session", true);
        if ((request.getParameter("forDomaine") != null) && request.getParameter("forDomaine").equals("FA")) {
            ((CAApercuInteretMoratoireViewBean) element).setIdJournalFacturation(super.getId(request, "idPassage"));
            ((CAApercuInteretMoratoireViewBean) element).setDomaine("FA");

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);
        } else if (!JadeStringUtil.isNull(super.getId(request, "idJournal"))) {
            ((CAApercuInteretMoratoireViewBean) element).setIdJournalCalcul(super.getId(request, "idJournal"));
            ((CAApercuInteretMoratoireViewBean) element).setDomaine("CA");

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);
        }

        return (CAApercuInteretMoratoireViewBean) element;
    }

    protected CAApercuInteretMoratoireListViewBean getInteretMoratoireManager(HttpServletRequest request)
            throws Exception {
        CAApercuInteretMoratoireListViewBean manager = (CAApercuInteretMoratoireListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.interets.CAApercuInteretMoratoireListViewBean", "request");
        JSPUtils.setBeanProperties(request, manager);

        return manager;
    }
}
