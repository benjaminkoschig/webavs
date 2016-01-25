package globaz.lynx.servlet.escompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.escompte.LXEscompteViewBean;
import globaz.lynx.servlet.utils.LXVentilationActionUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXEscompteAction extends FWDefaultServletAction {

    private static final String USERACTION_CHERCHER_ESCOMPTE = "lynx.escompte.escompte.chercher";

    public LXEscompteAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(LXEscompteAction.USERACTION_CHERCHER_ESCOMPTE);
        return action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher&selectedId="
                + request.getParameter("idOrdreGroupe") + "&idSociete=" + request.getParameter("idSociete")
                + "&idOrganeExecution=" + request.getParameter("idOrganeExecution") + "&idOrdreGroupe="
                + request.getParameter("idOrdreGroupe");
    }

    @Override
    public void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = null;

            viewBean = new LXEscompteViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);
            ((LXEscompteViewBean) viewBean).setVentilations(LXVentilationActionUtils.getVentilationsFromRequest(
                    request, true, ((LXEscompteViewBean) viewBean).getMaxRows()));
            ((LXEscompteViewBean) viewBean).setShowRows(((LXEscompteViewBean) viewBean).getVentilations().size());

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, "viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirectWithoutParameters(destination, request, response);
    }

}
