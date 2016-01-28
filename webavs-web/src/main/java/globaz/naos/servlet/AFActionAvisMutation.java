package globaz.naos.servlet;

import globaz.draco.translation.CodeSystem;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.avisMutation.AFAvisMutationViewBean;
import globaz.naos.db.avisMutation.AFProcessAvisSedexWriterNew;
import globaz.naos.itext.AFAvisMutation_Doc;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (25.06.2003 15:56:54)
 * 
 * @author: ado
 */
public class AFActionAvisMutation extends FWDefaultServletAction {
    public final static String ACTION_IMPRIMER = "imprimer";
    public final static String ACTION_SELECTION_IMPRESSION = "selectionImpression";

    /**
     * Constructeur d'AFActionRentier.
     * 
     * @param servlet
     */
    public AFActionAvisMutation(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (AFActionAvisMutation.ACTION_IMPRIMER.equals(getAction().getActionPart())) {
            actionImprimer(session, request, response, dispatcher);
        } else {
            if (AFActionAvisMutation.ACTION_SELECTION_IMPRESSION.equals(getAction().getActionPart())) {
                actionSelectionImpression(session, request, response, dispatcher);
            } else {
                super.actionCustom(session, request, response, dispatcher);
                // ("/naosRoot/FR/avisMutation/avisMutationSelectionImpression_de.jsp");
            }
        }
    }

    protected void actionImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String selectionImpression = request.getParameter("selectionChangement");

        try {
            BISession bSession = CodeSystem.getSession(session);
            AFAvisMutationViewBean viewBean = (AFAvisMutationViewBean) session.getAttribute("viewBean");
            AFAvisMutation_Doc process = new AFAvisMutation_Doc((BSession) bSession, selectionImpression);
            process.setTiersId(viewBean.getTiersId());
            process.setIdAffiliation(viewBean.getAffiliationId());
            process.setNomChange(JadeStringUtil.parseBoolean(request.getParameter("nomChange"), false));
            process.setAvsChange(JadeStringUtil.parseBoolean(request.getParameter("avsChange"), false));
            process.setAdresseChange(JadeStringUtil.parseBoolean(request.getParameter("adresseChange"), false));
            process.setCantonChange(JadeStringUtil.parseBoolean(request.getParameter("cantonChange"), false));
            process.setPmChange(JadeStringUtil.parseBoolean(request.getParameter("pmChange"), false));
            process.setSiegeChange(JadeStringUtil.parseBoolean(request.getParameter("siegeChange"), false));
            process.setSelectionImpression(selectionImpression);
            process.setEMailAddress(request.getParameter("email"));
            process.setObservation(request.getParameter("observations"));
            process.setDateAvis(request.getParameter("dateAvis"));
            // déterminer si deux avis sont nécessaires
            if (process.isCantonChange()) {
                process.setMorePageNeeded(true);
            }
            process.start();
            if ("true".equals(request.getParameter("selectionSedex"))) {
                startSedexProcess(request, bSession, viewBean);
            }
            goSendRedirect("/naos?userAction=back", request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    protected void actionSelectionImpression(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            BISession bSession = CodeSystem.getSession(session);
            AFAvisMutationViewBean viewBean = new AFAvisMutationViewBean();
            viewBean.setISession(bSession);
            viewBean.setAffiliationId(request.getParameter("affiliationId"));
            viewBean.setTiersId((String) session.getAttribute("tiersPrincipale"));
            session.setAttribute("viewBean", viewBean);
            _destination = ("/naosRoot/" + bSession.getIdLangueISO().toUpperCase() + "/avisMutation/avisMutationSelectionImpression_de.jsp");
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void startSedexProcess(HttpServletRequest request, BISession bSession, AFAvisMutationViewBean viewBean) {
        String selectionImpression = request.getParameter("selectionChangement");

        AFProcessAvisSedexWriterNew process = new AFProcessAvisSedexWriterNew((BSession) bSession, selectionImpression);

        process.setIdTiers(viewBean.getTiersId());
        process.setIdAffiliation(viewBean.getAffiliationId());
        process.setNomChange(JadeStringUtil.parseBoolean(request.getParameter("nomChange"), false));
        process.setAvsChange(JadeStringUtil.parseBoolean(request.getParameter("avsChange"), false));
        process.setAdresseChange(JadeStringUtil.parseBoolean(request.getParameter("adresseChange"), false));
        process.setCantonChange(JadeStringUtil.parseBoolean(request.getParameter("cantonChange"), false));
        process.setPmChange(JadeStringUtil.parseBoolean(request.getParameter("pmChange"), false));
        process.setSiegeChange(JadeStringUtil.parseBoolean(request.getParameter("siegeChange"), false));
        process.setSelectionImpression(selectionImpression);
        process.setEMailAddress(request.getParameter("email"));
        process.setDateAvis(request.getParameter("dateAvis"));
        process.setObservations(request.getParameter("observations"));

        try {
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
