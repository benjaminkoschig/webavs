package globaz.tucana.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.db.bouclement.TUBouclementViewBean;
import globaz.tucana.db.bouclement.TUDetailViewBean;
import globaz.tucana.vb.administration.TUPassageSuppressionViewBean;
import globaz.tucana.vb.bouclement.TUDetailInsertionACMViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author FGo
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class TUActionBouclement extends TUActionTucanaDefault {

    /**
     * @param servlet
     */
    public TUActionBouclement(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUDetailInsertionACMViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterEchec (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUPassageSuppressionViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else {
            return super._getDestExecuterEchec(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestExecuterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof TUPassageSuppressionViewBean) {
            // Redirection, bouclement_rc
            return "/" + TUApplication.APPLICATION_NAME + "?userAction=tucana.bouclement.bouclement.chercher";
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("detailInsertionACM".equals(getAction().getClassPart())) {
            TUDetailInsertionACMViewBean vBean = new TUDetailInsertionACMViewBean();
            vBean.setIdBouclement(request.getParameter("idBouclement"));
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            String destination = "";
            try {
                action.changeActionPart(FWAction.ACTION_AFFICHER);
                vBean = (TUDetailInsertionACMViewBean) mainDispatcher.dispatch(vBean, action);
                session.setAttribute("viewBean", vBean);
                destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
            } catch (Exception e) {
                e.printStackTrace();
                destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    @Override
    public void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainController) throws javax.servlet.ServletException, java.io.IOException {
        if ("detail".equals(getAction().getClassPart())) {
            TUBouclementViewBean vBean = new TUBouclementViewBean();
            vBean.setIdBouclement(request.getParameter("idBouclement"));
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            String destination = "";
            try {
                action.changeActionPart(FWAction.ACTION_AFFICHER);
                vBean = (TUBouclementViewBean) mainController.dispatch(vBean, action);
                session.setAttribute("viewBean", vBean);
                destination = getRelativeURL(request, session) + "_rc.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } else {
            super.actionChercher(session, request, response, mainController);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof TUBouclementViewBean) && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
            ((TUBouclementViewBean) viewBean).setId(request.getParameter("idBouclement"));
        }
        if ((viewBean instanceof TUDetailViewBean) && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
            ((TUDetailViewBean) viewBean).setId(request.getParameter("idDetail"));
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

}