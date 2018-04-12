package ch.globaz.orion.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.orion.vb.adi.EBDemandesTransmisesViewBean;
import globaz.phenix.process.communications.CPProcessDemandePortailGenererDecision;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.businessimpl.services.adi.AdiServiceImpl;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;
import ch.globaz.xmlns.eb.adi.StatusDecisionAcompteIndEnum;

public class EBDemandesTransmisesAction extends EBAbstractServletAction {

    public EBDemandesTransmisesAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof EBDemandesTransmisesViewBean) {
            EBDemandesTransmisesViewBean vb = (EBDemandesTransmisesViewBean) viewBean;

            String doc = request.getParameter("doc");
            if (doc != null && !doc.isEmpty()) {
                vb.setDuplicataDoc(doc);
            }

            vb.setSelectedIds(request.getParameter("selectedIds"));
            viewBean = vb;
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (getAction().getActionPart().equals("validerBatch")) {
            String selectedIds = request.getParameter("selectedIds");
            String[] listIdDemande = selectedIds.split(",");

            callProcessDemandePortailGenererDecision(listIdDemande, session);

            String destination = "/" + getAction().getApplicationPart()
                    + "?userAction=orion.adi.demandesTransmises.chercher";

            goSendRedirect(destination, request, response);

        } else if (getAction().getActionPart().equals("quitterDemande")) {
            redirectToNextDemandeOrRC(request, response);

        } else if (getAction().getActionPart().equals("validerDemande")) {
            BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            String[] idDemande = { request.getParameter("id") };
            String remarqueCp = request.getParameter("remarqueCP");

            if (idDemande[0] != null && !JadeStringUtil.isEmpty(idDemande[0])) {
                // mise à jour de la remarque
                updateRemarqueCpWebAvs(bsession, idDemande[0], remarqueCp);
            }

            // Processus de validation de la demande
            callProcessDemandePortailGenererDecision(idDemande, session);

            redirectToNextDemandeOrRC(request, response);

        } else if (getAction().getActionPart().equals("refuserDemande")) {
            // Processus de refus de la demande
            BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            String idDemande = request.getParameter("id");
            String remarqueCp = request.getParameter("remarqueCP");

            // mise à jour des entités webavs et ebusiness
            updateStatutAndRemarqueCpWebAvs(bsession, idDemande, DemandeModifAcompteStatut.REFUSE, remarqueCp);
            updateStatutAndRemarqueCpEbusiness(bsession, idDemande, StatusDecisionAcompteIndEnum.REFUSEE, remarqueCp);

            redirectToNextDemandeOrRC(request, response);
        }
    }

    protected void updateStatutAndRemarqueCpWebAvs(BSession session, String idDemande,
            DemandeModifAcompteStatut newStatut, String remarqueCp) {
        EBDemandeModifAcompteEntity demande = new EBDemandeModifAcompteEntity();
        demande.setId(idDemande);
        try {
            demande.retrieve();

            if (newStatut != null) {
                // Persistance côté WebAVS
                demande.setCsStatut(newStatut.getValue());
            }
            if (!StringUtils.isEmpty(remarqueCp)) {
                demande.setRemarqueCp(remarqueCp);
            }

            demande.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateRemarqueCpWebAvs(BSession session, String idDemande, String remarqueCp) {
        updateStatutAndRemarqueCpWebAvs(session, idDemande, null, remarqueCp);
    }

    protected void updateStatutAndRemarqueCpEbusiness(BSession session, String idDemande,
            StatusDecisionAcompteIndEnum newStatus, String remarqueCp) {
        EBDemandeModifAcompteEntity demande = new EBDemandeModifAcompteEntity();
        demande.setId(idDemande);
        try {
            demande.retrieve();
            AdiServiceImpl.changeStatutDemande(session, Integer.valueOf(demande.getIdDemandePortail()), newStatus,
                    remarqueCp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void callProcessDemandePortailGenererDecision(String[] listIdDemande, HttpSession session) {
        try {
            BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();

            String idPassageFacturation = getIdPassageFacturation(bsession);

            CPProcessDemandePortailGenererDecision process = new CPProcessDemandePortailGenererDecision();
            process.setSession(bsession);
            process.setListIdDemande(listIdDemande);
            process.setIdPassage(idPassageFacturation);
            BProcessLauncher.start(process);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String getIdPassageFacturation(BSession session) {

        // Recherche du prochain passage de facturation
        IFAPassage passage = ServicesFacturation.getProchainPassageFacturation(session, null,
                FAModuleFacturation.CS_MODULE_COT_PERS_PORTAIL);

        if (passage == null || JadeStringUtil.isIntegerEmpty(passage.getIdPassage())) {
            throw new RuntimeException(session.getLabel("CP_MSG_0102"));
        } else {
            return passage.getIdPassage();
        }
    }

    private void redirectToNextDemandeOrRC(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String selectedIds = request.getParameter("selectedIds");
        List<String> listStrings = Arrays.asList(selectedIds.split(","));
        if (selectedIds.isEmpty()) {
            String destination = "/" + getAction().getApplicationPart()
                    + "?userAction=orion.adi.demandesTransmises.chercher";
            goSendRedirect(destination, request, response);
        } else {
            String id = listStrings.get(0);
            String destination = "/" + getAction().getApplicationPart()
                    + "?userAction=orion.adi.demandesTransmises.afficher&selectedId=" + id + "&selectedIds="
                    + listStrings.toString().replace("[", "").replace("]", "");
            goSendRedirect(destination, request, response);
        }
    }

}
