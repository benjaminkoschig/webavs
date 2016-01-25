package globaz.osiris.servlet.action;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public abstract class CADefaultServletAction extends FWDefaultServletAction {
    public final static String VB_ELEMENT = "viewBean";
    public final static String VBL_ELEMENT = "listViewBean";

    /**
     * Constructor for CADefaultServletAction.
     * 
     * @param servlet
     */
    public CADefaultServletAction(FWServlet servlet) {
        super(servlet);
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

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VB_ELEMENT, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else if (!JadeStringUtil.isNull(request.getParameter("_type"))
                    && request.getParameter("_type").equals("_sl")) {
                // On demande une redirection
                destination = "/" + CAApplication.DEFAULT_OSIRIS_NAME + "?userAction=";
                destination += request.getParameter("_destination");
                int start, end;
                start = JadeStringUtil.indexOf(request.getQueryString(), "&userAction=");
                end = JadeStringUtil.indexOf(request.getQueryString(), "&", start + 1);
                destination += "&" + JadeStringUtil.remove(request.getQueryString(), start, end - start);
            } else {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.10.2002 15:27:52)
     * 
     * @return java.lang.String
     * @param param
     *            javax.servlet.http.HttpServletRequest
     */
    protected final String getActionSuite(HttpServletRequest request) {
        return JadeStringUtil.word(request.getParameter("userAction"), 2, '.');
    }

    protected final String getId(HttpServletRequest request, String fieldName) {
        String id = "";
        if (!JadeStringUtil.isBlank(request.getParameter(fieldName))) {
            id = request.getParameter(fieldName);
        } else if (!JadeStringUtil.isBlank(request.getParameter("selectedId"))) {
            id = request.getParameter("selectedId");
        } else if (!JadeStringUtil.isBlank(request.getParameter("id"))) {
            id = request.getParameter("id");
        }

        return id;
    }

    protected FWListViewBeanInterface getListViewBean(HttpServletRequest request, String directory, String name,
            boolean isNew) throws ServletException {
        return (FWListViewBeanInterface) JSPUtils.useBean(request, "element", "globaz.osiris.db." + directory + ".CA"
                + JadeStringUtil.firstLetterToUpperCase(name) + "ManagerListViewBean", "session", isNew);
    }

    protected FWViewBeanInterface getViewBean(HttpServletRequest request) throws ServletException {
        return getViewBean(request, request.getParameter("userAction"), false);
    }

    protected FWViewBeanInterface getViewBean(HttpServletRequest request, String userAction, boolean isNew)
            throws ServletException {
        String directory = JadeStringUtil.word(userAction, 1, '.');
        String name = JadeStringUtil.word(userAction, 2, '.');

        return getViewBean(request, directory, name, isNew);
    }

    protected FWViewBeanInterface getViewBean(HttpServletRequest request, String directory, String name, boolean isNew)
            throws ServletException {
        return (FWViewBeanInterface) JSPUtils.useBean(request, "element", "globaz.osiris.db." + directory + ".CA"
                + JadeStringUtil.firstLetterToUpperCase(name) + "ViewBean", "session", isNew);
    }

    /**
     * Contrôle des droits spéciaux pour une action. <br/>
     * Si l'utilisateur n'a pas le(s) droit(s) nécesessaire un message d'erreur sera ajouté au viewBean.
     * 
     * @param viewBean
     * @param specialRight
     * @return
     * @throws Exception
     */
    protected boolean hasSpecialRight(FWViewBeanInterface viewBean, String specialRight) throws Exception {
        if (((BSession) viewBean.getISession()).hasRight(getAction().toString(), specialRight)) {
            return true;
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(FWMessageFormat.format("ERROR User [{0}] has no right [{1}] for the action [{2}]",
                    viewBean.getISession().getUserId(), specialRight, getAction()));

            return false;
        }
    }

}
