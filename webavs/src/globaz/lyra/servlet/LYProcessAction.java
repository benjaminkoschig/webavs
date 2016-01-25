package globaz.lyra.servlet;

import globaz.corvus.vb.echeances.REAnalyserEcheancesAjaxViewBean;
import globaz.corvus.vb.echeances.REDiminutionRenteEnfantAjaxViewBean;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.lyra.vb.process.LYProcessAjaxViewBean;
import globaz.perseus.process.echeance.PFEcheanceProcess;
import globaz.perseus.vb.echeance.PFEcheanceAjaxViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.corvus.process.echeances.diminution.REDiminutionRentePourEnfantProcess;
import ch.globaz.corvus.process.echeances.travauxaeffectuer.REListeTravauxAEffectuerProcess;
import com.google.gson.Gson;

public class LYProcessAction extends LYDefaultAction {

    public LYProcessAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionExecuterAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
        try {
            String userAction = request.getParameter("userAction");
            FWAction action = FWAction.newInstance(userAction);

            LYProcessAjaxViewBean ajaxViewBean;

            String processPath = request.getParameter("processPath");
            if (processPath.endsWith(REListeTravauxAEffectuerProcess.class.getName())) {
                ajaxViewBean = new Gson().fromJson(request.getParameter(processPath),
                        REAnalyserEcheancesAjaxViewBean.class);
            } else if (processPath.endsWith(REDiminutionRentePourEnfantProcess.class.getName())) {
                ajaxViewBean = new Gson().fromJson(request.getParameter(processPath),
                        REDiminutionRenteEnfantAjaxViewBean.class);
            } else if (processPath.endsWith(PFEcheanceProcess.class.getName())) {
                ajaxViewBean = new Gson().fromJson(request.getParameter(processPath), PFEcheanceAjaxViewBean.class);
            } else {
                ajaxViewBean = new LYProcessAjaxViewBean();
            }

            ajaxViewBean.setProcessPath(processPath);
            ajaxViewBean.setAdresseEmail(request.getParameter("adresseEmail"));
            ajaxViewBean.setMoisTraiement(request.getParameter("moisTraitement"));

            mainDispatcher.dispatch(ajaxViewBean, action);
            request.setAttribute(FWServlet.VIEWBEAN, ajaxViewBean);
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
