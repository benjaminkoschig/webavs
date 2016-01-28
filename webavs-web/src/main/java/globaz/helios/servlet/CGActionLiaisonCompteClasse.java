package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.classifications.CGLiaisonCompteClasseViewBean;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la suppression de liaisons sur les comptes de type non avs (écran détail du compta via plan
 * comptable).
 * 
 * @author DDA
 * 
 */
public class CGActionLiaisonCompteClasse extends CGActionNeedExerciceComptable {

    public final static String ACTION_AJOUTER_LIAISON = "ajouterLink";
    public final static String ACTION_SUPPRIMER_LIAISON = "supprimerLink";

    public CGActionLiaisonCompteClasse(FWServlet servlet) {
        super(servlet);
    }

    protected void actionAjouterLiaison(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        CGPlanComptableViewBean pcViewBean = (CGPlanComptableViewBean) session.getAttribute(VIEWBEAN);

        String destination = "";
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            String selectedId = request.getParameter("selectedId");

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);
            ((CGLiaisonCompteClasseViewBean) viewBean).setIdCompte(selectedId);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            String langue = FWDefaultServletAction.getIdLangueIso(session);
            String tmp = "";
            if ((action != null) && (action.isWellFormed())) {
                tmp = "/" + action.getApplicationPart() + "Root/" + langue;
            }

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                pcViewBean.setMsgType(FWViewBeanInterface.ERROR);
                pcViewBean.setMessage(pcViewBean.getMessage());

                destination = tmp + "/comptes/planComptable_de.jsp?_valid=fail";
            } else {
                destination = tmp + "/comptes/planComptable_de.jsp?selectedId=" + selectedId;
            }

            setSessionAttribute(session, VIEWBEAN, pcViewBean);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (ACTION_AJOUTER_LIAISON.equals(getAction().getActionPart())) {
            actionAjouterLiaison(session, request, response, mainDispatcher);
        } else if (ACTION_SUPPRIMER_LIAISON.equals(getAction().getActionPart())) {
            actionSupprimerLiaison(session, request, response, mainDispatcher);
        }
    }

    protected void actionSupprimerLiaison(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        CGPlanComptableViewBean pcViewBean = (CGPlanComptableViewBean) session.getAttribute(VIEWBEAN);

        String destination = "";
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            String selectedId = request.getParameter("selectedId");

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);
            ((CGLiaisonCompteClasseViewBean) viewBean).setIdCompte(selectedId);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            String tmp = "";
            if ((action != null) && (action.isWellFormed())) {
                tmp = "/" + action.getApplicationPart() + "Root/" + FWDefaultServletAction.getIdLangueIso(session);
            }

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                pcViewBean.setMsgType(FWViewBeanInterface.ERROR);
                pcViewBean.setMessage(pcViewBean.getMessage());
                destination = tmp + "/comptes/planComptable_de.jsp?_valid=fail";
            } else {
                destination = tmp + "/comptes/planComptable_de.jsp?selectedId=" + selectedId;
            }

            setSessionAttribute(session, VIEWBEAN, pcViewBean);
        } catch (Exception e) {
            e.printStackTrace();
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
