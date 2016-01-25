package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.interets.CAApercuInteretMoratoireListViewBean;
import globaz.osiris.db.interets.CAApercuInteretMoratoireManager;
import globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SCO
 * 
 */
public class FAActionGestionInterets extends FWDefaultServletAction {

    public final static String VB_ELEMENT = "viewBean";
    public final static String VBL_ELEMENT = "listViewBean";

    /**
     * Constructeur de FAActionGestionInterets
     */
    public FAActionGestionInterets(FWServlet servlet) {
        super(servlet);
    }

    /*
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CAApercuInteretMoratoireViewBean element = getInteretMoratoire(session, request, response, mainDispatcher);
            CAApercuInteretMoratoireListViewBean manager = getInteretMoratoireManager(request);

            BSession bsession = (BSession) mainDispatcher.getSession();

            BSession bSession = (BSession) session.getAttribute("objSession");
            bSession.setAttribute("musca_interet_moratoire", new Boolean(true));
            bsession.setAttribute("musca_interet_moratoire", new Boolean(true));

            manager.setSession(bsession);
            JSPUtils.setBeanProperties(request, manager);

            setSessionAttribute(session, VB_ELEMENT, element);
            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
            JadeLogger.error(this, e);
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    /*
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CAApercuInteretMoratoireViewBean element = getInteretMoratoire(session, request, response, mainDispatcher);
            FWViewBeanInterface manager = getInteretMoratoireManager(request);

            if (JadeStringUtil.isNull(((CAApercuInteretMoratoireListViewBean) manager).getForIdJournalFacturation())) {
                ((CAApercuInteretMoratoireListViewBean) manager).setForIdJournalFacturation(element
                        .getIdJournalFacturation());
            }

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @return
     * @throws Exception
     */
    protected CAApercuInteretMoratoireViewBean getInteretMoratoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws Exception {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        action.changeActionPart(FWAction.ACTION_AFFICHER);

        FWViewBeanInterface element = (CAApercuInteretMoratoireViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean", "session", true);

        String id = "";
        if (!JadeStringUtil.isBlank(request.getParameter("idPassage"))) {
            id = request.getParameter("idPassage");
        }

        ((CAApercuInteretMoratoireViewBean) element).setIdJournalFacturation(id);
        ((CAApercuInteretMoratoireViewBean) element).setDomaine(CAApercuInteretMoratoireManager.DOMAINE_FA);

        element = beforeAfficher(session, request, response, element);
        element = mainDispatcher.dispatch(element, action);

        return (CAApercuInteretMoratoireViewBean) element;
    }

    /**
     * Permet de récupérer le manager sur les interets moratoires.
     * 
     * @param request
     * @return
     * @throws Exception
     */
    protected CAApercuInteretMoratoireListViewBean getInteretMoratoireManager(HttpServletRequest request)
            throws Exception {
        CAApercuInteretMoratoireListViewBean manager = (CAApercuInteretMoratoireListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.interets.CAApercuInteretMoratoireListViewBean", "request");
        JSPUtils.setBeanProperties(request, manager);

        // On doit spécifier le domaine car le domaine CA est setté par défaut
        manager.setForDomaine(CAApercuInteretMoratoireManager.DOMAINE_FA);

        return manager;
    }

}
