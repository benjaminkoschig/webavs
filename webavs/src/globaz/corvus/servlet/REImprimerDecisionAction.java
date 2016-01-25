package globaz.corvus.servlet;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.vb.process.REImprimerDecisionViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.prestation.utils.ged.PRGedUtils;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HPE
 */
public class REImprimerDecisionAction extends REDefaultProcessAction {

    public REImprimerDecisionAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {

            REImprimerDecisionViewBean viewBean = new REImprimerDecisionViewBean();

            String idDecision = request.getParameter("selectedId");
            viewBean.setIdDecision(idDecision);

            String idTierRequerant = request.getParameter("idTierRequerant");
            viewBean.setIdTiersRequerant(idTierRequerant);

            // Mise en GED
            // Inforom 529
            // Une décision peu avoir plusieurs numéros en fonction du type, il faut donc tous les tester
            ArrayList<String> docNumbers = new ArrayList<String>();
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_API);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_INVALIDITE);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_SURVIVANT);
            docNumbers.add(IRENoDocumentInfoRom.DECISION_DE_RENTES_VIEILLESSE);
            if (PRGedUtils.isDocumentsInGed(docNumbers, (BSession) mainDispatcher.getSession())) {
                viewBean.setDisplaySendToGed("1");
            } else {
                viewBean.setDisplaySendToGed("0");
            }

            viewBean.setISession(mainDispatcher.getSession());

            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
