package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CASectionViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des sections.
 * 
 * @author DDA
 */
public class CAApercuParSectionAction extends CAComptesAnnexesAction {
    /**
     * @param servlet
     */
    public CAApercuParSectionAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface element = (new CASectionAction((FWServlet) servlet)).getSection(session, request,
                    response, mainDispatcher);

            setSessionAttribute(session, VB_ELEMENT, element);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
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
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VB_ELEMENT);

            ((CASectionViewBean) viewBean).setIdMotifContentieux(null);
            ((CASectionViewBean) viewBean).setIdCompteAnnexeMotif(null);
            ((CASectionViewBean) viewBean).setIdSectionMotif(null);
            ((CASectionViewBean) viewBean).setIdMotifBlocage(null);
            ((CASectionViewBean) viewBean).setDateDebutMotif(null);
            ((CASectionViewBean) viewBean).setDateFinMotif(null);
            ((CASectionViewBean) viewBean).setCommentaire(null);

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VB_ELEMENT, viewBean);

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
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
