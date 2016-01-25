package globaz.osiris.servlet.action.bvrftp;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.bvrftp.CABvrFtpListViewBean;
import globaz.osiris.db.bvrftp.CABvrFtpViewBean;
import globaz.osiris.db.process.CABvrViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la lecture des BVRs.
 * 
 * @author DDA
 */
public class CABvrFtpAction extends CADefaultServletAction {

    public static final String BVR_FTP_PASSWORD = "bvrFtpPassword";
    public static final String DISPLAY_PASSWORD = FWAction.ACTION_CHERCHER + "Password";
    public static final String EXECUTE_PASSWORD_MODIFICATION = FWAction.ACTION_EXECUTER + "Password";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String NEW_PASSWORD_CONFIRMATION = "newPasswordConfirmation";

    public CABvrFtpAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            String selectedId = request.getParameter("selectedId");

            // Gestion des droits délèguée à méthode Lister.
            // Aucune possibilité d'action new dans cet écran.

            CABvrFtpListViewBean viewBeanList = (CABvrFtpListViewBean) session.getAttribute(VBL_ELEMENT);
            for (int i = 0; i < viewBeanList.getPvrFiles().size(); i++) {
                CABvrFtpViewBean tmp = viewBeanList.getPvrFile(i);
                if (tmp.getFileName().equals(selectedId)) {
                    setSessionAttribute(session, VB_ELEMENT, tmp);
                }
            }

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CABvrFtpListViewBean listViewBean = new CABvrFtpListViewBean();
            listViewBean.setISession(mainDispatcher.getSession());

            setSessionAttribute(session, VBL_ELEMENT, listViewBean);

            myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String destination = "/" + action.getApplicationPart() + "Root/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/process/bvr_de.jsp";

        CABvrViewBean viewBean = new CABvrViewBean();
        viewBean.setISession(mainDispatcher.getSession());

        try {
            String selectedFile = request.getParameter("selectedId");
            viewBean.setUserSelectedFileName(selectedFile);

            String distantDirectoryName = request.getParameter("distantDirectoryName");
            viewBean.setDistantDirectoryName(distantDirectoryName);

            viewBean = (CABvrViewBean) mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VB_ELEMENT, viewBean);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());

            JadeLogger.error(this, e);
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String destination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CABvrFtpListViewBean viewBean = (CABvrFtpListViewBean) FWListViewBeanActionFactory.newInstance(action,
                    mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (CABvrFtpListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (CABvrFtpListViewBean) mainDispatcher.dispatch(viewBean, action);

            request.setAttribute(VB_ELEMENT, viewBean);

            setSessionAttribute(session, VBL_ELEMENT, viewBean);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
