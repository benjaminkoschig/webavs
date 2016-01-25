package globaz.corvus.servlet;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATortManager;
import globaz.corvus.vb.process.REGenererDemandeCompensationViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.utils.ged.PRGedUtils;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REGenererDemandeCompensationAction extends REDefaultProcessAction {

    public REGenererDemandeCompensationAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            REGenererDemandeCompensationViewBean viewBean = (REGenererDemandeCompensationViewBean) FWViewBeanActionFactory
                    .newInstance(getAction(), mainDispatcher.getPrefix());

            // on lui donne les paramètres en requête au cas où
            JSPUtils.setBeanProperties(request, viewBean);

            if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.DEMANDE_DE_COMPENSATION,
                    (BSession) mainDispatcher.getSession())) {
                viewBean.setDisplaySendToGed("1");
            } else {
                viewBean.setDisplaySendToGed("0");
            }

            // on recherche s'il y a déjà eu des versement
            RERenteVerseeATortManager renteVerseeATortManager = new RERenteVerseeATortManager();

            renteVerseeATortManager.setForIdDemandeRente(Long.parseLong(viewBean.getIdDemandeRente()));
            renteVerseeATortManager.setSession((BSession) mainDispatcher.getSession());
            renteVerseeATortManager.find();

            BigDecimal montantTotalRenteVerseeATort = BigDecimal.ZERO;

            for (int i = 0; i < renteVerseeATortManager.size(); i++) {
                RERenteVerseeATort renteVerseeATort = (RERenteVerseeATort) renteVerseeATortManager.get(i);
                montantTotalRenteVerseeATort = montantTotalRenteVerseeATort.add(renteVerseeATort.getMontant());
            }

            viewBean.setMontant1(montantTotalRenteVerseeATort.toString());

            viewBean.setISession(mainDispatcher.getSession());
            saveViewBean(viewBean, session);
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
