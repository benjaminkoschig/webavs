package ch.globaz.al.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import globaz.al.vb.decision.ALDecisionListViewBean;
import globaz.al.vb.decision.ALDecisionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;

/**
 * Action liée à l'impression de décisions
 *
 * @author jer
 *
 */
public class ALActionDecisionFileAttente extends ALAbstractDefaultAction {

    /**
     * Constructeur
     *
     * @param servlet
     */
    public ALActionDecisionFileAttente(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALDecisionListViewBean) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        } else if (viewBean instanceof ALActionDecisionsMasse) {

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".dossier.dossier.chercher";
        } else {
            return _getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ((viewBean instanceof ALDecisionViewBean) && "supprimerCopie".equals(getAction().getActionPart())) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                    + ".decision.decision.afficher&selectedId=" + ((ALDecisionViewBean) viewBean).getId();
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = null;
        if (viewBean instanceof ALDecisionViewBean) {
            if (((ALDecisionViewBean) viewBean).getEditionDecompteAvecDecision()) {
                destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                        + ".decision.decision.afficher&selectedId=" + ((ALDecisionViewBean) viewBean).getIdDecompteAdi()
                        + "&idDossier=" + ((ALDecisionViewBean) viewBean).getIdDossier() + "&fromDecompte="
                        + ((ALDecisionViewBean) viewBean).getFromDecompte() + "&id="
                        + ((ALDecisionViewBean) viewBean).getIdDecompteAdi();
            } else {

                destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart()
                        + ".decision.decision.afficher&selectedId=" + ((ALDecisionViewBean) viewBean)
                                .getDossierDecisionComplexModel().getDossierModel().getIdDossier();
            }
        } else {
            destination = super._getDestModifierSucces(session, request, response, viewBean);
        }
        return destination;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);
    }

 
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super.beforeAfficher(session, request, response, viewBean);
    }
}
