/*
 * Créé le 26 juin 07
 */

package globaz.corvus.servlet;

import globaz.corvus.itext.REListeRenteLiee;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HPE
 * 
 */

public class RERenteLieeJointRenteAccordeeAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RERenteLieeJointRenteAccordeeAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RERenteAccordeeJointDemandeRenteViewBean viewBean = new RERenteAccordeeJointDemandeRenteViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());

        String destination = null;

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            destination = FWScenarios.getInstance().getDestination(
                    (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                    new FWRequestActionAdapter().adapt(request), null);
            if (JadeStringUtil.isBlank(destination)) {
                destination = getRelativeURL(request, session) + "_rc.jsp";
            }
        } catch (InvocationTargetException e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        } catch (IllegalAccessException e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        this.saveViewBean(viewBean, request);

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public String imprimerListeRenteLiee(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        RERenteAccordeeJointDemandeRenteViewBean rlViewBean = new RERenteAccordeeJointDemandeRenteViewBean();
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, rlViewBean);

            REListeRenteLiee listeRenteLiee = new REListeRenteLiee();
            listeRenteLiee.setSession((BSession) mainDispatcher.getSession());
            listeRenteLiee.setForCsEtat(rlViewBean.getForCsEtat());
            listeRenteLiee.setForGenre(rlViewBean.getForGenre());
            listeRenteLiee.setForIdTiersLiant(rlViewBean.getForIdTiersLiant());
            listeRenteLiee.setForEnCours(rlViewBean.getIsRechercheRenteEnCours());
            listeRenteLiee.executeProcess();

            if ((listeRenteLiee.getAttachedDocuments() != null) && (listeRenteLiee.getAttachedDocuments().size() > 0)) {
                rlViewBean.setAttachedDocuments(listeRenteLiee.getAttachedDocuments());
            } else {
                rlViewBean.setAttachedDocuments(null);
            }
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        mainDispatcher.dispatch(rlViewBean, getAction());
        this.saveViewBean(rlViewBean, request);

        return getRelativeURL(request, session) + "_rc.jsp";
    }
}
