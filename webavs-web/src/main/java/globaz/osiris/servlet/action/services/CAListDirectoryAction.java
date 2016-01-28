package globaz.osiris.servlet.action.services;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.services.CAListDirectoryManager;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour lister les fichiers présents sur le serveur.
 * 
 * @author DDA
 */
public class CAListDirectoryAction extends CADefaultServletAction {

    public CAListDirectoryAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CAListDirectoryManager manager = (CAListDirectoryManager) JSPUtils.useBean(request, "manager",
                    "globaz.osiris.db.services.CAListDirectoryManager", "request");

            JSPUtils.setBeanProperties(request, manager);

            if (!JadeStringUtil.isNull(request.getParameter("dir"))) {
                manager.setDirectory(request.getParameter("dir"));
            }

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            manager = (CAListDirectoryManager) beforeLister(session, request, response, manager);
            manager = (CAListDirectoryManager) mainDispatcher.dispatch(manager, action);

            setSessionAttribute(session, VBL_ELEMENT, manager);
            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

}