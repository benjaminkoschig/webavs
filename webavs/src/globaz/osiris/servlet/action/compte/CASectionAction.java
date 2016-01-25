package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAAuxiliaireManagerListViewBean;
import globaz.osiris.db.comptes.CAEcritureManagerListViewBean;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CASectionViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister les sections.
 * 
 * @author DDA
 */
public class CASectionAction extends CADefaultServletAction {

    private String _destination = "";

    /**
     * Constructor for CASectionAction.
     * 
     * @param servlet
     */
    public CASectionAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession , HttpServletRequest,
     * HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String myDestination = getRelativeURL(request, session) + "_rc.jsp";
        try {
            CASectionViewBean element = (CASectionViewBean) getSection(session, request, response, mainDispatcher);

            setSessionAttribute(session, VB_ELEMENT, element);

            if (element.getCompteAnnexe().isCompteAuxiliaire()) {
                myDestination = getRelativeURL(request, session) + "Aux_rc.jsp";
            } else {
                myDestination = getRelativeURL(request, session) + "_rc.jsp";
            }
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionFindNext(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionFindNext(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("listViewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            request.setAttribute("viewBean", viewBean);

            if (viewBean instanceof CAAuxiliaireManagerListViewBean) {
                _destination = getRelativeURL(request, session) + "Aux_rcListe.jsp";
            } else {
                _destination = getRelativeURL(request, session) + "_rcListe.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionFindPrevious (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionFindPrevious(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        JadeLogger.info(this, "actionFindPrevious");
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("listViewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            request.setAttribute("viewBean", viewBean);

            if (viewBean instanceof CAAuxiliaireManagerListViewBean) {
                _destination = getRelativeURL(request, session) + "Aux_rcListe.jsp";
            } else {
                _destination = getRelativeURL(request, session) + "_rcListe.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession , HttpServletRequest,
     * HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CASectionViewBean element = (CASectionViewBean) getSection(session, request, response, mainDispatcher);

            if (element.getCompteAnnexe().isCompteAuxiliaire()) {
                myDestination = listerAuxiliaire(session, request, response, mainDispatcher);
            } else {
                myDestination = listerEcriture(session, request, response, mainDispatcher);
            }
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    protected FWViewBeanInterface getSection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, Exception {
        FWViewBeanInterface element = (CASectionViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CASectionViewBean", "session");

        JSPUtils.setBeanProperties(request, element);

        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        action.changeActionPart(FWAction.ACTION_AFFICHER);

        element = beforeAfficher(session, request, response, element);
        element = mainDispatcher.dispatch(element, action);

        return element;
    }

    /**
     * Aperçu comptable de la section : Liste des opérations auxiliaires.
     * 
     * @param session
     * @param request
     * @param mainDispatcher
     * @return
     * @throws ServletException
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String listerAuxiliaire(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, Exception, InvocationTargetException,
            IllegalAccessException {
        FWViewBeanInterface manager = (CAAuxiliaireManagerListViewBean) globaz.globall.http.JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.comptes.CAAuxiliaireManagerListViewBean", "session", true);

        JSPUtils.setBeanProperties(request, manager);

        if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
            ((CAAuxiliaireManagerListViewBean) manager).setForIdSection(super.getId(request, "id"));
        } else {
            ((CAAuxiliaireManagerListViewBean) manager).setForIdSection("0");
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdTypeOperation"))) {
            ((CAAuxiliaireManagerListViewBean) manager).setForIdTypeOperation("1000");
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdTypeOperation"))) {
            if (!request.getParameter("forIdTypeOperation").equalsIgnoreCase("1000")) {
                ((CAAuxiliaireManagerListViewBean) manager).setForIdTypeOperation(request
                        .getParameter("forIdTypeOperation"));
            }
        }

        if (!JadeStringUtil.isNull(request.getParameter("forSelectionTri"))) {
            ((CAAuxiliaireManagerListViewBean) manager).setForSelectionTri(request.getParameter("forSelectionTri"));
        }

        ((CAAuxiliaireManagerListViewBean) manager).setLikeIdTypeOperation(APIOperation.CAAUXILIAIRE + "%");
        ((CAAuxiliaireManagerListViewBean) manager).setForEtat(APIOperation.ETAT_COMPTABILISE);
        ((CAAuxiliaireManagerListViewBean) manager).setOrderBy(CAOperationManager.ORDER_DATEOP);

        manager = beforeLister(session, request, response, manager);
        manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

        setSessionAttribute(session, VBL_ELEMENT, manager);

        return getRelativeURL(request, session) + "Aux_rcListe.jsp";
    }

    /**
     * Aperçu comptable de la section : Liste des écritures.
     * 
     * @param session
     * @param request
     * @param mainDispatcher
     * @return
     * @throws ServletException
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String listerEcriture(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, Exception, InvocationTargetException,
            IllegalAccessException {
        FWViewBeanInterface manager = (CAEcritureManagerListViewBean) globaz.globall.http.JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.comptes.CAEcritureManagerListViewBean", "session", true);

        JSPUtils.setBeanProperties(request, manager);

        if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
            ((CAEcritureManagerListViewBean) manager).setForIdSection(super.getId(request, "id"));
        } else {
            ((CAEcritureManagerListViewBean) manager).setForIdSection("0");
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdTypeOperation"))) {
            ((CAEcritureManagerListViewBean) manager).setForIdTypeOperation("1000");
        }

        if (!JadeStringUtil.isNull(request.getParameter("forIdTypeOperation"))) {
            if (!request.getParameter("forIdTypeOperation").equalsIgnoreCase("1000")) {
                ((CAEcritureManagerListViewBean) manager).setForIdTypeOperation(request
                        .getParameter("forIdTypeOperation"));
            }
        }

        if (!JadeStringUtil.isNull(request.getParameter("forSelectionTri"))) {
            ((CAEcritureManagerListViewBean) manager).setForSelectionTri(request.getParameter("forSelectionTri"));
        }

        if (!JadeStringUtil.isNull(request.getParameter("forCompteCourant"))) {
            ((CAEcritureManagerListViewBean) manager).setForCompteCourant(request.getParameter("forCompteCourant"));
        }

        ((CAEcritureManagerListViewBean) manager).setVueOperationRubCC("true");

        manager = beforeLister(session, request, response, manager);
        manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

        setSessionAttribute(session, VBL_ELEMENT, manager);

        return getRelativeURL(request, session) + "_rcListe.jsp";
    }

}
