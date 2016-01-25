package ch.globaz.al.web.servlet;

import globaz.al.vb.decision.ALDecisionListViewBean;
import globaz.al.vb.decision.ALDecisionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action liée à l'impression de décisions
 * 
 * @author jer
 * 
 */
public class ALActionDecision extends ALAbstractDefaultAction {

    /**
     * Constructeur
     * 
     * @param servlet
     */
    public ALActionDecision(FWServlet servlet) {
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

    // @Override
    // protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    //
    // return super.beforeNouveau(session, request, response, viewBean);
    //
    // }

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
                destination = "/" + getAction().getApplicationPart() + "?userAction="
                        + getAction().getApplicationPart() + ".decision.decision.afficher&selectedId="
                        + ((ALDecisionViewBean) viewBean).getIdDecompteAdi() + "&idDossier="
                        + ((ALDecisionViewBean) viewBean).getIdDossier() + "&fromDecompte="
                        + ((ALDecisionViewBean) viewBean).getFromDecompte() + "&id="
                        + ((ALDecisionViewBean) viewBean).getIdDecompteAdi();
            } else {

                destination = "/"
                        + getAction().getApplicationPart()
                        + "?userAction="
                        + getAction().getApplicationPart()
                        + ".decision.decision.afficher&selectedId="
                        + ((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel().getDossierModel()
                                .getIdDossier();
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

        FWViewBeanInterface viewBean = (FWViewBeanInterface) /* (ALDecisionViewBean) */request
                .getAttribute(FWServlet.VIEWBEAN);

        if (viewBean instanceof ALDecisionViewBean) {
            request.getSession().setAttribute(
                    globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel().getAllocataireComplexModel()
                            .getAllocataireModel().getIdTiersAllocataire());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.web.servlet.ALAbstractDefaultAction#beforeAfficher(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // on teste si idDossier est défini car uniquement le cas depuis écran décompte (menu imprimer)
        if (!JadeStringUtil.isEmpty(request.getParameter("idDossier"))) {
            ((ALDecisionViewBean) viewBean).setIdDossier(request.getParameter("idDossier"));
        }
        // on teste si fromDecompte est défini car uniquement le cas depuis écran décompte (menu imprimer)
        // fromDecompte indiquant la provenance (écran décompte) / sert à savoir ce qui est défini par viewBean#getId
        // (id du dossier ou id decompte)
        if (!JadeStringUtil.isEmpty(request.getParameter("fromDecompte"))) {
            ((ALDecisionViewBean) viewBean).setFromDecompte((request.getParameter("fromDecompte")));
            // représente id decompte adi si provenance de l'écran décompte
            if (!JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
                ((ALDecisionViewBean) viewBean).setIdDecompteAdi((request.getParameter("selectedId")));
            }
        }

        // TODO si fromDecompte = 1 => alors définir selectedId=dossierId
        // if ("1".equals(request.getParameter("fromDecompte"))) {
        //
        // ((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel().getDossierModel()
        // .setIdDossier(request.getParameter("idDossier"));
        //
        // }

        return super.beforeAfficher(session, request, response, viewBean);
    }
}
