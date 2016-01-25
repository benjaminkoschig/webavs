package globaz.osiris.servlet.action.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.contentieux.CACalculTaxeManagerListViewBean;
import globaz.osiris.db.contentieux.CACalculTaxeViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour le calcul des taxes.
 * 
 * @author DDA
 */
public class CACalculTaxeAction extends CADefaultServletAction {

    /**
     * Constructor for CACalculTaxe.
     * 
     * @param servlet
     */
    public CACalculTaxeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface element = getCalculTaxe(request);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VB_ELEMENT, element);

            if (element.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _myDestination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
            } else {
                _myDestination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CACalculTaxeViewBean element = getCalculTaxe(request);
            element.setSession((BSession) mainDispatcher.getSession());

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = (CACalculTaxeViewBean) beforeAfficher(session, request, response, element);
            element = (CACalculTaxeViewBean) mainDispatcher.dispatch(element, action);

            setSessionAttribute(session, VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            CACalculTaxeManagerListViewBean manager = getCalculTaxeManager(request);
            manager.setISession(mainDispatcher.getSession());

            JSPUtils.setBeanProperties(request, manager);

            manager = (CACalculTaxeManagerListViewBean) beforeLister(session, request, response, manager);
            manager = (CACalculTaxeManagerListViewBean) mainDispatcher.dispatch(manager,
                    FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, VBL_ELEMENT, manager);

            _myDestination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);

    }

    private CACalculTaxeViewBean getCalculTaxe(HttpServletRequest request) throws ServletException {
        CACalculTaxeViewBean element = (CACalculTaxeViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.contentieux.CACalculTaxeViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "forIdCalculTaxe")))
                && (!JadeStringUtil.isNull(super.getId(request, "forIdCalculTaxe")))) {
            element.setIdCalculTaxe(super.getId(request, "forIdCalculTaxe"));
        }

        return element;
    }

    private CACalculTaxeManagerListViewBean getCalculTaxeManager(HttpServletRequest request) throws ServletException {
        CACalculTaxeManagerListViewBean manager = (CACalculTaxeManagerListViewBean) JSPUtils.useBean(request,
                "manager", "globaz.osiris.db.contentieux.CACalculTaxeManagerListViewBean", "request");
        if (!JadeStringUtil.isNull(super.getId(request, "forIdCalculTaxe"))) {
            manager.setFromIdCalculTaxe(super.getId(request, "forIdCalculTaxe"));
        }

        return manager;
    }

}
