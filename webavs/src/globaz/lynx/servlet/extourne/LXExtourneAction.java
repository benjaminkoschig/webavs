package globaz.lynx.servlet.extourne;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.extourne.LXExtourneProcessViewBean;
import globaz.lynx.db.extourne.LXExtourneViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class des actions de l'extourne
 * 
 * @author SCO
 */
public class LXExtourneAction extends FWDefaultServletAction {

    private static final String USERACTION_EXTOURNE_PROCESS = "lynx.extourne.extourneProcess.extourner";

    /**
     * Constructeur
     */
    public LXExtourneAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterEchec(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(USERACTION_EXTOURNE_PROCESS);

        LXExtourneProcessViewBean viewBeanExtourne = (LXExtourneProcessViewBean) viewBean;
        setSessionAttribute(session, "viewBean", viewBeanExtourne);

        return action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".extourner&_valid=fail&selectedId="
                + request.getParameter("idJournal") + "&idSection=" + viewBeanExtourne.getIdSection() + "&idOperation="
                + viewBeanExtourne.getIdOperation();

    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(USERACTION_EXTOURNE_PROCESS);

        LXExtourneProcessViewBean viewBeanExtourne = (LXExtourneProcessViewBean) viewBean;

        return action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".extourner&process=launched&selectedId="
                + request.getParameter("idJournal") + "&idSection=" + viewBeanExtourne.getIdSection() + "&idOperation="
                + viewBeanExtourne.getIdOperation();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    public void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = null;

            viewBean = new LXExtourneViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("extourner".equals(getAction().getActionPart())) {
            actionExtourner(session, request, response, mainDispatcher);
        }
    }

    /**
     * Permet d'acceder a l'écran du traitment de l'extourne
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void actionExtourner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            if (viewBean == null || !(viewBean instanceof LXExtourneProcessViewBean)) {
                viewBean = new LXExtourneProcessViewBean();
                viewBean.setISession(mainDispatcher.getSession());
            }
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }
}
