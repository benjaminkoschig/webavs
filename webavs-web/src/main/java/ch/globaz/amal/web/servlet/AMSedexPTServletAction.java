package ch.globaz.amal.web.servlet;

import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMActions;
import com.google.common.collect.Collections2;
import globaz.amal.process.annonce.AnnonceSedexPTGenererAnnonceProcess;
import globaz.amal.vb.sedexco.AMSedexcocomparaisonViewBean;
import globaz.amal.vb.sedexpt.AMSedexptListViewBean;
import globaz.amal.vb.sedexpt.AMSedexptcreationannonceViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pegasus.vb.decision.PCDecomptViewBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class AMSedexPTServletAction extends AMAbstractServletAction {

    public AMSedexPTServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean) {

        AMSedexptListViewBean listViewBean = (AMSedexptListViewBean) viewBean;
        if(listViewBean.getSearchModel().getInSDXMessageSubType() == null  || listViewBean.getSearchModel().getInSDXMessageSubType().isEmpty()) {
            ArrayList<String> messageSubType = new ArrayList<>(Arrays.asList(AMMessagesSubTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue(), AMMessagesSubTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue()));
            listViewBean.getSearchModel().setInSDXMessageSubType(messageSubType);
        }

        return listViewBean;
    }

    /**
     * Méthode permettant de lancer la génération des annonces PT par lot
     * Récupére les informations de la request pour les transmettre au viewBean
     * @param session La session
     * @param request La request
     * @param response La réponse
     * @param mainDispatcher Le dispacther
     * @param viewBean le viewBean
     * @return l'url de la prochaine destination.
     */
    public String launchGenererAnnoncePT(HttpSession session, HttpServletRequest request,
                                              HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        AMSedexptcreationannonceViewBean creerAnnoncePTViewBean = new AMSedexptcreationannonceViewBean();

        AnnonceSedexPTGenererAnnonceProcess process = new AnnonceSedexPTGenererAnnonceProcess();
        process.setSession(BSessionUtil.getSessionFromThreadContext());

        process.setEmail(request.getParameter("email"));
        process.setAnnee(request.getParameter("annee"));
        process.setDateReductionPrimeA(request.getParameter("dateReductionPrimeA"));
        process.setDateReductionPrimeDe(request.getParameter("dateReductionPrimeDe"));
        process.setIsSimulation(isSimulation(request));

        try {
            // -----------------------------------
            // Launch the process
            // -----------------------------------
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.error(this, "Error Launching Process AnnonceCMProcess : " + e.getMessage());
        }

        return  "/amal?userAction=" + IAMActions.ACTION_SEDEX_PT_CREATION_ANNONCE + ".afficher";
    }

    private boolean isSimulation(HttpServletRequest request) {
        try {
            return  "on".equals(request.getParameter("simulation"));
        } catch (Exception ex) {
            JadeLogger.error(this, "Error converting boolean isSimulation : " + ex.toString());
        }
        return Boolean.TRUE;
    }
}
