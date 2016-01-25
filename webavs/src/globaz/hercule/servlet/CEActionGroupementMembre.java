package globaz.hercule.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.hercule.db.groupement.CEGroupeViewBean;
import globaz.hercule.db.groupement.CEMembreViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CEActionGroupementMembre extends FWDefaultServletAction {

    public CEActionGroupementMembre(FWServlet servlet) {
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
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CEGroupeViewBean viewBean = new CEGroupeViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CEGroupeViewBean) mainDispatcher.dispatch(viewBean, action);

        // sauve le bean dans la session en tant que bean foreig key (utilisé
        // plus
        // tard également)
        session.setAttribute("viewBeanFK", viewBean);

        // corrections pour chercher écritures depuis le journal
        CEMembreViewBean viewBean2 = new CEMembreViewBean();
        session.setAttribute("viewBean", viewBean2);

        // redirection vers destination
        String _destination = getRelativeURL(request, session) + "_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CEGroupeViewBean _bean = (CEGroupeViewBean) session.getAttribute("viewBeanFK");
        if (_bean != null) {
            ((CEMembreViewBean) viewBean).setIdGroupe(_bean.getIdGroupe());
        }
        return viewBean;
    }

}
