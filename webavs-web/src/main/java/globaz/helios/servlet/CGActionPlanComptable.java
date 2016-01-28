package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion du plan comptable.
 * 
 * @author DDA
 */
public class CGActionPlanComptable extends CGActionNeedExerciceComptable {

    public CGActionPlanComptable(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = ERROR_PAGE;
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);

            JSPUtils.setBeanProperties(request, viewBean);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.setRight(FWSecureConstants.ADD);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestEchec(session, request, response, viewBean);
            } else {
                // Pour les compte de genre CHARGE ou PRODUIT, il est possible
                // de leur définir
                // une analyse budgétaire -> retourne sur la page de détail qui
                // va afficher
                // le lien analyse budgétaire

                if (((CGPlanComptableViewBean) viewBean).getExerciceComptable().getMandat().isEstComptabiliteAVS()
                        .booleanValue()) {
                    if (CGCompte.CS_GENRE_CHARGE.equals(((CGPlanComptableViewBean) viewBean).getIdGenre())
                            || CGCompte.CS_GENRE_PRODUIT.equals(((CGPlanComptableViewBean) viewBean).getIdGenre())) {
                        destination = getActionFullURL() + ".reAfficher&_method=null&selectedId="
                                + ((CGPlanComptableViewBean) viewBean).getIdCompte();
                    } else {
                        destination = getActionFullURL() + ".chercher";
                    }
                } else {
                    if (!CGMandat.CS_PC_AVS.equals(((CGPlanComptableViewBean) viewBean).getExerciceComptable()
                            .getMandat().getIdTypePlanComptable())
                            && !CGMandat.CS_PC_USAM.equals(((CGPlanComptableViewBean) viewBean).getExerciceComptable()
                                    .getMandat().getIdTypePlanComptable())) {
                        destination = getActionFullURL() + ".reAfficher&_method=null&selectedId="
                                + ((CGPlanComptableViewBean) viewBean).getIdCompte();
                    } else {
                        destination = getActionFullURL() + ".chercher";
                    }
                }
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);

    }

}
