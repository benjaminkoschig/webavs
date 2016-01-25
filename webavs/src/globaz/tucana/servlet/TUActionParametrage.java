package globaz.tucana.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.db.parametrage.TUCategorieRubriqueViewBean;
import globaz.tucana.db.parametrage.TUGroupeCategorieViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe définissant les actions du package parametrage
 * 
 * @author fgo date de création : 20.09.2006
 * @version : version 1.0
 * 
 */
public class TUActionParametrage extends TUActionTucanaDefault {

    /**
     * @param servlet
     */
    public TUActionParametrage(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    public void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainController) throws javax.servlet.ServletException, java.io.IOException {
        if ("categorieRubrique".equals(getAction().getClassPart())) {
            TUGroupeCategorieViewBean vBean = new TUGroupeCategorieViewBean();
            vBean.setIdGroupeCategorie(request.getParameter("idGroupeCategorie"));
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            String destination = "";
            try {
                action.changeActionPart(FWAction.ACTION_AFFICHER);
                vBean = (TUGroupeCategorieViewBean) mainController.dispatch(vBean, action);
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
        if ((viewBean instanceof TUGroupeCategorieViewBean)
                && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
            ((TUGroupeCategorieViewBean) viewBean).setId(request.getParameter("idGroupeCategorie"));
        }
        if ((viewBean instanceof TUCategorieRubriqueViewBean)
                && JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
            ((TUCategorieRubriqueViewBean) viewBean).setId(request.getParameter("idCategorieRubrique"));
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

}