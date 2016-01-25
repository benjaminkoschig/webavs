package globaz.osiris.servlet.action.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.recouvrement.CACouvertureSectionViewBean;
import globaz.osiris.db.recouvrement.CAEcheancePlanViewBean;
import globaz.osiris.db.recouvrement.CAPlanRecouvrementViewBean;
import globaz.osiris.db.recouvrement.CASursisViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import globaz.osiris.vb.recouvrement.CASursisEcheanceViewBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des sursis aux paiement.
 */
public class CARecouvrementAction extends FWDefaultServletAction {

    public CARecouvrementAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);
        if ("echeancePlan".equals(getAction().getClassPart())) {
            if (JadeStringUtil.isEmpty(_destination)) {
                _destination = getActionFullURL() + ".chercher&id=" + request.getParameter("idPlanRecouvrement");
            }
            return _destination;
        } else if ("planRecouvrement".equals(getAction().getClassPart())) {
            if (JadeStringUtil.isEmpty(_destination)) {
                CAPlanRecouvrementViewBean bean = (CAPlanRecouvrementViewBean) viewBean;
                _destination = request.getServletPath()
                        + "?userAction=osiris.recouvrement.couvertureSection.chercher&id="
                        + bean.getIdPlanRecouvrement() + "&selectedId=" + bean.getIdPlanRecouvrement();
                session.setAttribute("idPlanRecouvrement", bean.getIdPlanRecouvrement());
                request.setAttribute(CADefaultServletAction.VB_ELEMENT, bean);
            }

            return _destination;
        } else if ("sursis".equals(getAction().getClassPart())) {
            if (JadeStringUtil.isEmpty(_destination)) {
                CASursisViewBean bean = (CASursisViewBean) viewBean;
                _destination = request.getServletPath() + "?userAction=osiris.recouvrement.echeancePlan.chercher&id="
                        + bean.getIdPlanRecouvrement() + "&selectedId=" + bean.getIdPlanRecouvrement();
                session.setAttribute("idPlanRecouvrement", bean.getIdPlanRecouvrement());
                request.setAttribute(CADefaultServletAction.VB_ELEMENT, bean);
            }

            return _destination;
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ("echeancePlan".equals(getAction().getClassPart())) {
            CAEcheancePlanViewBean bean = (CAEcheancePlanViewBean) viewBean;
            return super._getDestModifierSucces(session, request, response, bean) + "&id="
                    + bean.getIdPlanRecouvrement();
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ("echeancePlan".equals(getAction().getClassPart())) {
            return getActionFullURL() + ".chercher&id=" + request.getParameter("idPlanRecouvrement");
        } else if ("sursis".equals(getAction().getClassPart())) {
            return getActionFullURL().substring(0, getActionFullURL().length() - "sursis".length())
                    + "echeancePlan.chercher&id=" + request.getParameter("idPlanRecouvrement");
        } else {
            return super._getDestSupprimerSucces(session, request, response, viewBean);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String classPart = getAction().getClassPart();
        // Si on va afficher le détail d'un plan de recouvrement
        if ("planRecouvrement".equals(classPart)) {
            // On conserve son id en session
            String idPlanRecouvrement = request.getParameter("selectedId");
            if (idPlanRecouvrement != null) {
                // arrive si on est sur un écran nouveau
                session.setAttribute("idPlanRecouvrement", idPlanRecouvrement);
            }
        } else if ("sursis".equals(classPart)) {
            String selectedIds = request.getParameter("selectedIds");
            String[] pieces = selectedIds.split(",");
            List list = new ArrayList();
            // Suppression des -1
            for (int i = 0; i < pieces.length; i++) {
                if (!pieces[i].equals("-1")) {
                    String piece = pieces[i];
                    if (JadeStringUtil.contains(piece, "*")) {
                        /**
                         * Caractère "*" utilisé comme séparateur. Voir section_rcListe.jsp
                         */
                        list.add(pieces[i].substring(0, pieces[i].indexOf("*")));
                    } else {
                        list.add(pieces[i]);
                    }
                }
            }

            CASectionManager manager = new CASectionManager();
            manager.setSession((BSession) mainDispatcher.getSession());
            manager.setForIdSectionIn(list);
            BigDecimal solde = new BigDecimal("0");
            try {
                solde = manager.getSum(CASection.FIELD_SOLDE);
            } catch (Exception e) {
                throw new ServletException(e);
            }

            request.setAttribute("soldeSections", solde.toString());
        }
        super.actionAfficher(session, request, response, mainDispatcher);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String classPart = getAction().getClassPart();

        if ("couvertureSection".equals(classPart) || "echeancePlan".equals(classPart)) {
            // Si on va afficher les échéances ou les sections couvertes
            // On recherche le plan de recouvrement
            String idPlanRecouvrement = request.getParameter("id");
            if (JadeStringUtil.isEmpty(idPlanRecouvrement)
                    && !JadeStringUtil.isEmpty(request.getParameter("idPlanRecouvrement"))) {
                idPlanRecouvrement = request.getParameter("idPlanRecouvrement");
            }

            FWViewBeanInterface pr = new CAPlanRecouvrementViewBean();
            ((CAPlanRecouvrementViewBean) pr).setSession((BSession) mainDispatcher.getSession());
            ((CAPlanRecouvrementViewBean) pr).setIdPlanRecouvrement(idPlanRecouvrement);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            try {
                action.changeActionPart(FWAction.ACTION_AFFICHER);
            } catch (Exception e) {
                throw new ServletException(e);
            }

            pr = beforeAfficher(session, request, response, pr);
            pr = mainDispatcher.dispatch(pr, action);

            request.setAttribute(CADefaultServletAction.VB_ELEMENT, pr);
            session.setAttribute("idPlanRecouvrement", idPlanRecouvrement);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String userAction = request.getParameter("userAction");
        FWAction action = FWAction.newInstance(userAction);

        if ("echeancePlan".equals(action.getClassPart()) && "calculer".equals(action.getActionPart())) {
            // lancer le calcul des échéances
            dispatcher.dispatch(getViewBean(session, dispatcher), action);
            if ("preview".equals(request.getParameter("mode"))) {
                goSendRedirect("/osiris?userAction=osiris.recouvrement.echeancePlan.lister&mode=preview", request,
                        response);
            }
        } else if ("calculerSave".equals(action.getActionPart())) {
            CAPlanRecouvrementViewBean pr = getViewBean(session, dispatcher);
            // lancer le calcul des échéances
            pr = (CAPlanRecouvrementViewBean) dispatcher.dispatch(pr, action);
            goSendRedirect(
                    "/osiris?userAction=osiris.recouvrement.echeancePlan.chercher&id=" + pr.getIdPlanRecouvrement(),
                    request, response);
        } else if ("sursis".equals(action.getClassPart())) {
            CASursisEcheanceViewBean vb = new CASursisEcheanceViewBean();
            if (JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
                vb.setMessage(vb.getSession().getLabel("ERREUR_IDECHEANCE_VIDE"));
                vb.setMsgType(FWViewBeanInterface.ERROR);
            } else {
                vb.setId(request.getParameter("selectedId"));
            }
            /*
             * set automatique des proprietes
             */
            try {
                JSPUtils.setBeanProperties(request, vb);
                dispatcher.dispatch(vb, action);
            } catch (Exception e) {
                vb.setMessage(vb.getSession().getLabel("ERREUR_SURSIS_BEANPROPERTIES") + " - " + e.toString());
                vb.setMsgType(FWViewBeanInterface.ERROR);
            }

            String destination = "/osiris?userAction=osiris.recouvrement.echeancePlan.chercher&id="
                    + vb.getIdPlanRecouvrement();
            if (FWViewBeanInterface.ERROR == vb.getMsgType()) {
                destination += "&message=" + vb.getMessage();
            }
            goSendRedirect(destination, request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("preview".equals(request.getParameter("mode"))) {
            String userAction = request.getParameter("userAction");
            FWAction action = FWAction.newInstance(userAction);
            CAPlanRecouvrementViewBean pr = getViewBean(session, mainDispatcher);
            // lancer le calcul des échéances
            pr = (CAPlanRecouvrementViewBean) mainDispatcher.dispatch(pr, action);
            request.setAttribute(CADefaultServletAction.VB_ELEMENT, pr);

            String _destination = getRelativeURL(request, session) + "_rcListePreview.jsp";

            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else {
            super.actionLister(session, request, response, mainDispatcher);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("planRecouvrement".equals(getAction().getClassPart())) {
            String idPlanRecouvrement = request.getParameter("id");
            if (JadeStringUtil.isEmpty(idPlanRecouvrement)
                    && !JadeStringUtil.isEmpty(request.getParameter("idPlanRecouvrement"))) {
                idPlanRecouvrement = request.getParameter("idPlanRecouvrement");
            } else if (JadeStringUtil.isEmpty(idPlanRecouvrement)
                    && !JadeStringUtil.isEmpty(session.getAttribute("idPlanRecouvrement").toString())) {
                idPlanRecouvrement = session.getAttribute("idPlanRecouvrement").toString();
            }
            CAPlanRecouvrementViewBean pr = new CAPlanRecouvrementViewBean();
            pr.setSession((BSession) mainDispatcher.getSession());
            pr.setIdPlanRecouvrement(idPlanRecouvrement);
            try {
                pr.retrieve();
            } catch (Exception e) {
                throw new ServletException(e);
            }
            session.setAttribute(CADefaultServletAction.VB_ELEMENT, pr);
        }
        super.actionModifier(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionSupprimer(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("echeancePlan".equals(getAction().getClassPart())) {
            String idEcheance = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(idEcheance)) {
                return;
            }

            CAEcheancePlanViewBean pr = new CAEcheancePlanViewBean();
            pr.setSession((BSession) mainDispatcher.getSession());
            pr.setIdEcheancePlan(idEcheance);
            try {
                pr.retrieve();
            } catch (Exception e) {
                throw new ServletException(e);
            }
            session.setAttribute(CADefaultServletAction.VB_ELEMENT, pr);
        }
        super.actionSupprimer(session, request, response, mainDispatcher);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ("couvertureSection".equals(getAction().getClassPart())) {
            CACouvertureSectionViewBean vBean = (CACouvertureSectionViewBean) viewBean;
            vBean.setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
        }

        return viewBean;
    }

    /**
     * @param session
     * @param dispatcher
     * @return
     * @throws ServletException
     */
    private CAPlanRecouvrementViewBean getViewBean(HttpSession session, FWDispatcher dispatcher)
            throws ServletException {
        String idPlanRecouvrement = (String) session.getAttribute("idPlanRecouvrement");
        CAPlanRecouvrementViewBean pr = new CAPlanRecouvrementViewBean();
        pr.setSession((BSession) dispatcher.getSession());
        pr.setIdPlanRecouvrement(idPlanRecouvrement);
        try {
            pr.retrieve();
        } catch (Exception e) {
            throw new ServletException(e);
        }
        return pr;
    }
}
