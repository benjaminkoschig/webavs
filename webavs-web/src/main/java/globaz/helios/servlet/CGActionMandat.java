package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGMandatViewBean;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des mandats.
 * 
 * @author DDA
 */
public class CGActionMandat extends CGDefaultServletAction {

    public CGActionMandat(FWServlet servlet) {
        super(servlet);
    }

    protected void actionAjouterMandat(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, boolean isExtourner) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);

            JSPUtils.setBeanProperties(request, viewBean);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestEchec(session, request, response, viewBean);
            } else {
                destination = getActionFullURL() + ".chercher";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        if ("ajouterMandat".equals(getAction().getActionPart())) {
            actionAjouterMandat(session, request, response, mainDispatcher, false);
        }
    }

    /**
     * Si on modifie le mandat qui est le meme que celui qui se trouve en session ds l'exercice comptable, en retire
     * l'exerice comptable de la session car il n'est plus valable. il faudra le remettre en session manuellement.
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        CGMandatViewBean vBean = (CGMandatViewBean) viewBean;
        CGExerciceComptableViewBean exerciceSession = (CGExerciceComptableViewBean) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
        if ((exerciceSession != null) && (exerciceSession.getMandat() != null) && (viewBean != null)) {
            if (exerciceSession.getMandat().getIdMandat().equals(vBean.getIdMandat())) {
                session.removeAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            }
        }
        return viewBean;
    }

}
