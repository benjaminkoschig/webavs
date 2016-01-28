package globaz.osiris.servlet.action.interets;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.interets.CAPlanCalculInteretViewBean;
import globaz.osiris.db.interets.CARubriqueSoumiseInteretViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage les rubriques soumises aux intérêts.
 * 
 * @author sch
 */
public class CARubriqueSoumiseInteretAction extends CADefaultServletAction {

    public CARubriqueSoumiseInteretAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession , HttpServletRequest,
     * HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CARubriqueSoumiseInteretViewBean viewBean = new CARubriqueSoumiseInteretViewBean();

        String selectedId = request.getParameter("selectedId");
        if (JadeStringUtil.isEmpty(selectedId)) {
            selectedId = request.getParameter("id");
        }
        viewBean.setIdRubrique(selectedId);
        viewBean.setIdPlanCalculInteret((String) session.getAttribute("idPlanCalculInteret"));

        // enregister les paramètres de la requête dans le bean
        try {
            // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        // TODO sch 23 août 2012 : revoir selon action parente comment faire au mieux
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CARubriqueSoumiseInteretViewBean) mainDispatcher.dispatch(viewBean, action);

        // sauve le bean dans la session en tant que bean foreign key (utilisé
        // plus tard également)
        session.setAttribute("viewBean", viewBean);

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                .forward(request, response);

        // super.actionAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CAPlanCalculInteretViewBean viewBean = new CAPlanCalculInteretViewBean();
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
        viewBean = (CAPlanCalculInteretViewBean) mainDispatcher.dispatch(viewBean, action);

        // sauve le bean dans la session en tant que bean foreign key (utilisé
        // plus tard également)
        session.setAttribute("viewBean", viewBean);

        // corrections pour chercher écritures depuis le journal

        // TODO Auto-generated method stub

        super.actionChercher(session, request, response, mainDispatcher);
    }

    protected CARubriqueSoumiseInteretViewBean getRubriqueSoumiseInteret(HttpServletRequest request)
            throws ServletException {
        CARubriqueSoumiseInteretViewBean element = (CARubriqueSoumiseInteretViewBean) JSPUtils.useBean(request,
                "element", "globaz.osiris.db.interets.CARubriqueSoumiseInteretViewBean", "session");
        if ((!JadeStringUtil.isBlank(getId(request, "idPlanCalculInteret")))
                && (!JadeStringUtil.isNull(getId(request, "idPlanCalculInteret")))) {
            element.setIdPlanCalculInteret(getId(request, "idPlanCalculInteret"));
        } else {
            element = (CARubriqueSoumiseInteretViewBean) JSPUtils.useBean(request, "element",
                    "globaz.osiris.db.interets.CARubriqueSoumiseInteretViewBean", "session", true);
        }

        return element;
    }

}
