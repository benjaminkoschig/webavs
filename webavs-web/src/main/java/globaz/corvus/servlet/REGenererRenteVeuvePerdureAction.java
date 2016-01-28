package globaz.corvus.servlet;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.vb.process.REGenererRenteVeuvePerdureViewBean;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.utils.ged.PRGedUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REGenererRenteVeuvePerdureAction extends REDefaultProcessAction {

    public REGenererRenteVeuvePerdureAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        REGenererRenteVeuvePerdureViewBean viewBean = new REGenererRenteVeuvePerdureViewBean();
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            RERenteAccJoinTblTiersJoinDemRenteManager manager = new RERenteAccJoinTblTiersJoinDemRenteManager();
            manager.setSession((BSession) mainDispatcher.getSession());
            manager.setForIdTiersBeneficiaire(viewBean.getIdTiers());
            manager.setForGenrePrestation("13");
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                RERenteAccJoinTblTiersJoinDemandeRente renteAccordee = (RERenteAccJoinTblTiersJoinDemandeRente) manager
                        .get(i);

                if (JadeStringUtil.isBlank(renteAccordee.getDateFinDroit())) {
                    viewBean.setMontantRenteVeuve(renteAccordee.getMontantPrestation());
                    break;
                }
            }
            viewBean.setDateDebutRenteVieillesse(PRTiersHelper.getDateAgeVieillesseTiers(
                    (BSession) mainDispatcher.getSession(), viewBean.getIdTiers()));

            viewBean.setAnnexeParDefaut(Boolean.TRUE);

            if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.DECISION_DE_RENTES_VEUVE_PERDURE)) {
                viewBean.setShowGedCheckbox(true);
            }

            mainDispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionExecuter(session, request, response, mainDispatcher);
    }
}
