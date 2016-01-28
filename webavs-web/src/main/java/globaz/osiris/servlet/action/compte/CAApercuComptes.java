package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAApercuComptesListViewBean;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion d'un compte annexe.
 * 
 * @author DDA
 */
public class CAApercuComptes extends CADefaultServletAction {
    public static final String SEARCH_CPT_ANNEXE_SAVED_MASK = "SEARCH_CPT_ANNEXE_SAVED_MASK";

    /**
     * Constructor for CAActionComptes.
     * 
     * @param servlet
     */
    public CAApercuComptes(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction monAction = FWAction.newInstance(request.getParameter("userAction"));

        String myDestination = getRelativeURL(request, session) + "_de.jsp";
        try {
            CACompteAnnexeViewBean element = (new CAComptesAnnexesAction((FWServlet) servlet)).getCompteAnnexe(session,
                    request, response, mainDispatcher);

            element = (CACompteAnnexeViewBean) mainDispatcher.dispatch(element, monAction);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, element);
            myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction monAction = FWAction.newInstance(request.getParameter("userAction"));

        String myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(monAction, mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction monAction = FWAction.newInstance(request.getParameter("userAction"));

        String myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(monAction,
                    mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);

            if (!JadeStringUtil.isNull(request.getParameter("forSelectionRole"))) {
                ((CAApercuComptesListViewBean) viewBean).setForSelectionRole(request.getParameter("forSelectionRole"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("fromNumNom"))) {
                ((CAApercuComptesListViewBean) viewBean).setFromNumNom(request.getParameter("fromNumNom"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("forSelectionTri"))) {
                ((CAApercuComptesListViewBean) viewBean).setForSelectionTri(request.getParameter("forSelectionTri"));
            } else {
                ((CAApercuComptesListViewBean) viewBean).setForSelectionTri("2");
            }
            if (!JadeStringUtil.isNull(request.getParameter("forSelectionCompte"))) {
                ((CAApercuComptesListViewBean) viewBean).setForSelectionCompte(request
                        .getParameter("forSelectionCompte"));
            } else {
                ((CAApercuComptesListViewBean) viewBean).setForSelectionCompte("1000");
            }

            if (!JadeStringUtil.isBlank(request.getParameter("likeNumNom"))) {
                session.setAttribute(CAApercuComptes.SEARCH_CPT_ANNEXE_SAVED_MASK, request.getParameter("likeNumNom"));
            }

            ((CAApercuComptesListViewBean) viewBean).changeManagerSize(50);

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VBL_ELEMENT, viewBean);

            myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getActionFullURL() + ".chercher";

        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session
                    .getAttribute(CADefaultServletAction.VB_ELEMENT);

            ((CACompteAnnexeViewBean) viewBean).setIdMotifContentieux(null);
            ((CACompteAnnexeViewBean) viewBean).setIdCompteAnnexeMotif(null);
            ((CACompteAnnexeViewBean) viewBean).setIdSectionMotif(null);
            ((CACompteAnnexeViewBean) viewBean).setIdMotifBlocage(null);
            ((CACompteAnnexeViewBean) viewBean).setDateDebutMotif(null);
            ((CACompteAnnexeViewBean) viewBean).setDateFinMotif(null);
            ((CACompteAnnexeViewBean) viewBean).setCommentaire(null);

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else if (!JadeStringUtil.isNull(request.getParameter("_type"))
                    && request.getParameter("_type").equals("_sl")) {
                // On demande une redirection spéciale
                destination = "/" + CAApplication.DEFAULT_OSIRIS_NAME + "?userAction=";
                destination += request.getParameter("_destination");
                int start, end;
                start = JadeStringUtil.indexOf(request.getQueryString(), "&userAction=");
                end = JadeStringUtil.indexOf(request.getQueryString(), "&", start + 1);
                destination += "&" + JadeStringUtil.remove(request.getQueryString(), start, end - start);
            } else {
                destination = getActionFullURL() + ".chercher";
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
