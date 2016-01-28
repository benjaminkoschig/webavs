package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.db.wizard.AFWizardViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AFActionIdeAnnonce extends FWDefaultServletAction {

    public static final String ACTION_PART_CUSTOM_PREPARE_AJOUT_ANNONCE_IDE_CREATION = "prepareAjoutAnnonceIdeCreation";

    public AFActionIdeAnnonce(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (ACTION_PART_CUSTOM_PREPARE_AJOUT_ANNONCE_IDE_CREATION.equalsIgnoreCase(getAction().getActionPart())) {
            actionPrepareAjoutAnnonceIdeCreation(session, request, response, dispatcher);
        } else {

            super.actionCustom(session, request, response, dispatcher);
        }

    }

    private void actionPrepareAjoutAnnonceIdeCreation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = "";

        try {

            FWViewBeanInterface vbSession = (FWViewBeanInterface) session.getAttribute("viewBean");

            if (vbSession instanceof AFAffiliationViewBean) {
                ((AFAffiliationViewBean) vbSession)._setIdeReadOnly(true);
                ((AFAffiliationViewBean) vbSession)._setAnnonceIdeCreationToAdd(true);
                ((AFAffiliationViewBean) vbSession)._setMessageAnnonceIdeCreationAjouteeToDisplay(true);
            } else {
                ((AFWizardViewBean) vbSession).getAffiliation()._setAnnonceIdeCreationToAdd(true);
                ((AFWizardViewBean) vbSession)._setIdeReadOnly(true);
                ((AFWizardViewBean) vbSession)._setMessageAnnonceIdeCreationAjouteeToDisplay(true);
            }

            session.setAttribute("viewBean", vbSession);

            destination = (String) session.getAttribute("redirectUrl");
            String destinationParams = (String) session
                    .getAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL);

            if (JadeStringUtil.isBlank(destination)) {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

            if ((destinationParams.toUpperCase().indexOf("_METHOD=ADD") == -1)
                    && (destinationParams.toUpperCase().indexOf("_METHOD=UPD") == -1)) {
                if (destinationParams.toUpperCase().indexOf("_METHOD=") == -1) {
                    destinationParams += "&_method=upd";
                } else {
                    int pos = destinationParams.toUpperCase().indexOf("_METHOD=");
                    pos += "_METHOD=".length();
                    String prev = destinationParams.substring(0, pos);
                    String next = destinationParams.substring(pos);
                    destinationParams = prev + "upd" + next;
                }
            }

            destination += "?_valid=fail&_back=sl&" + destinationParams;

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

}
