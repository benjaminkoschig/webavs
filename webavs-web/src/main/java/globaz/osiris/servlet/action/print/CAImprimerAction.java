package globaz.osiris.servlet.action.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.print.CABulletinsSoldesViewBean;
import globaz.osiris.db.print.CAImpressionEcheancierViewBean;
import globaz.osiris.db.print.CAImpressionPlanViewBean;
import globaz.osiris.db.print.CAImprimerOrdreGroupeViewBean;
import globaz.osiris.db.print.CAListComptesALettrerViewBean;
import globaz.osiris.db.print.CAListExportSoldeCACCViewBean;
import globaz.osiris.db.print.CAListExtraitCompteAnnexeViewBean;
import globaz.osiris.db.print.CAListImpressionsJournalViewBean;
import globaz.osiris.db.print.CAListSoldeCompteAnnexeViewBean;
import globaz.osiris.db.print.CAListSoldeSectionViewBean;
import globaz.osiris.db.print.CARappelPlanViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des imprimés.
 * 
 * @author DDA
 */
public class CAImprimerAction extends CADefaultServletAction {

    public CAImprimerAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            String actionSuite = getActionSuite(request);

            FWViewBeanInterface viewBean = this.getViewBean(request);
            viewBean.setISession(mainDispatcher.getSession());
            setProperties(request, actionSuite, viewBean);

            this.setSessionAttribute(session, request, actionSuite, viewBean);
            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void actionAfficherExtraitCAToPrint(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            String actionSuite = getActionSuite(request);

            CAListExtraitCompteAnnexeViewBean viewBean = (CAListExtraitCompteAnnexeViewBean) this.getViewBean(request);
            viewBean.setISession(mainDispatcher.getSession());

            CACompteAnnexeViewBean element = getCompteAnnexe(session, request, response, mainDispatcher);
            viewBean.setIdExterneRole(element.getIdExterneRole());
            viewBean.setDescriptionAffilie(element.getTitulaireEntete());
            this.setSessionAttribute(session, request, actionSuite, viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String actionSuite = getActionSuite(request);

        if ("listExtraitCompteAnnexe".equals(actionSuite)) {
            if ("afficherExtraitCA".equals(getAction().getActionPart())) {
                actionAfficherExtraitCAToPrint(session, request, response, dispatcher);
                return;
            }
        }
        super.actionCustom(session, request, response, dispatcher);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String actionSuite = getActionSuite(request);
        try {
            if (actionSuite.equals("listImpressionsJournal")) {
                CAListImpressionsJournalViewBean viewBean = (CAListImpressionsJournalViewBean) this
                        .getViewBean(request);
                viewBean.setISession(mainDispatcher.getSession());
                try {
                    JSPUtils.setBeanProperties(request, viewBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // executer l'action si valide
                String destination = FWDefaultServletAction.ERROR_PAGE;

                viewBean.setMessage("");
                viewBean.setMsgType(FWViewBeanInterface.OK);

                if (viewBean.validerPourExecution()) {
                    mainDispatcher.dispatch(viewBean, getAction());
                    destination = _getDestExecuterSucces(session, request, response, viewBean);
                } else {
                    destination = _getDestExecuterEchec(session, request, response, viewBean);
                }

                goSendRedirectWithoutParameters(destination, request, response);
            } else {
                super.actionExecuter(session, request, response, mainDispatcher);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private CACompteAnnexeViewBean getCompteAnnexe(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, Exception {
        FWViewBeanInterface element = (CACompteAnnexeViewBean) globaz.globall.http.JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CACompteAnnexeViewBean", "session", true);
        if ((mainDispatcher != null)) {
            if ((!JadeStringUtil.isBlank(getId(request, "idCompteAnnexe")))
                    && (!JadeStringUtil.isNull(super.getId(request, "idCompteAnnexe")))) {
                ((CACompteAnnexeViewBean) element).setIdCompteAnnexe(super.getId(request, "idCompteAnnexe"));
            }

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = beforeAfficher(session, request, response, element);
            element = mainDispatcher.dispatch(element, action);
        }

        return ((CACompteAnnexeViewBean) element);
    }

    protected void setProperties(HttpServletRequest request, String actionSuite, FWViewBeanInterface viewBean) {
        if (actionSuite.equals("imprimerOrdreGroupe")) {
            if (!JadeStringUtil.isNull(super.getId(request, "forIdGroup"))) {
                ((CAImprimerOrdreGroupeViewBean) viewBean).setIdOrdreGroupe(super.getId(request, "forIdGroup"));
            }
        } else if (actionSuite.equals("listImpressionsJournal")) {
            if (!JadeStringUtil.isNull(super.getId(request, "idJournal"))) {
                ((CAListImpressionsJournalViewBean) viewBean).setIdJournal(super.getId(request, "idJournal"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("showDetail"))) {
                ((CAListImpressionsJournalViewBean) viewBean).setShowDetail(JadeStringUtil.parseInt(
                        request.getParameter("showDetail"), 0));
            }
        } else if (actionSuite.equals("listSoldeCompteAnnexe")) {
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionRole"))) {
                ((CAListSoldeCompteAnnexeViewBean) viewBean).setForSelectionRole((String) request
                        .getAttribute("forSelectionRole"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionSigne"))) {
                ((CAListSoldeCompteAnnexeViewBean) viewBean).setForSelectionSigne((String) request
                        .getAttribute("forSelectionSigne"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionTri"))) {
                ((CAListSoldeCompteAnnexeViewBean) viewBean).setForSelectionTri((String) request
                        .getAttribute("forSelectionTri"));
            }
        } else if (actionSuite.equals("ListSoldeSection")) {
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionRole"))) {
                ((CAListSoldeSectionViewBean) viewBean).setForSelectionRole((String) request
                        .getAttribute("forSelectionRole"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionSigne"))) {
                ((CAListSoldeSectionViewBean) viewBean).setForSelectionSigne((String) request
                        .getAttribute("forSelectionSigne"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("idTypeSection"))) {
                ((CAListSoldeSectionViewBean) viewBean).setForIdTypeSection((String) request
                        .getAttribute("idTypeSection"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionTriCA"))) {
                ((CAListSoldeSectionViewBean) viewBean).setForSelectionTriCA((String) request
                        .getAttribute("forSelectionTriCA"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionTriSection"))) {
                ((CAListSoldeSectionViewBean) viewBean).setForSelectionTriSection((String) request
                        .getAttribute("forSelectionTriSection"));
            }
        } else if (actionSuite.equals("bulletinsSoldes")) {
            if (!JadeStringUtil.isNull(super.getId(request, "idJournal"))) {
                ((CABulletinsSoldesViewBean) viewBean).setIdSection(super.getId(request, "idSection"));
            }
        } else if (actionSuite.equals("listExportSoldeCACC")) {
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionRole"))) {
                ((CAListExportSoldeCACCViewBean) viewBean).setForSelectionRole((String) request
                        .getAttribute("forSelectionRole"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionSigne"))) {
                ((CAListExportSoldeCACCViewBean) viewBean).setForSelectionSigne((String) request
                        .getAttribute("forSelectionSigne"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionTri"))) {
                ((CAListExportSoldeCACCViewBean) viewBean).setForSelectionTri((String) request
                        .getAttribute("forSelectionTri"));
            }
        } else if (actionSuite.equals("listComptesALettrer")) {
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionRole"))) {
                ((CAListComptesALettrerViewBean) viewBean).setForSelectionRole((String) request
                        .getAttribute("forSelectionRole"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("idTypeSection"))) {
                ((CAListComptesALettrerViewBean) viewBean).setForIdTypeSection((String) request
                        .getAttribute("idTypeSection"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionTriCA"))) {
                ((CAListComptesALettrerViewBean) viewBean).setForSelectionTriCA((String) request
                        .getAttribute("forSelectionTriCA"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("forSelectionTriSection"))) {
                ((CAListComptesALettrerViewBean) viewBean).setForSelectionTriSection((String) request
                        .getAttribute("forSelectionTriSection"));
            }
            if (!JadeStringUtil.isNull((String) request.getAttribute("aLettrer"))) {
                ((CAListComptesALettrerViewBean) viewBean).setALettrer((String) request.getAttribute("aLettrer"));
            }
        } else if (actionSuite.equals("rappelPlan")) {
            ((CARappelPlanViewBean) viewBean).setIdPlanRecouvrement(super.getId(request, "idPlan"));
        } else if (actionSuite.equals("impressionEcheancier")) {
            ((CAImpressionEcheancierViewBean) viewBean).setIdPlanRecouvrement(super.getId(request, "idPlan"));
        } else if (actionSuite.equals("impressionPlan")) {
            ((CAImpressionPlanViewBean) viewBean).setIdPlanRecouvrement(super.getId(request, "idPlan"));
        }
    }

    protected void setSessionAttribute(HttpSession session, HttpServletRequest request, String actionSuite,
            FWViewBeanInterface viewBean) {
        if (actionSuite.equals("imprimerOrdreGroupe")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listImpressionsJournal")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listRole")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listRubrique")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listSequenceContentieux")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listSoldeCompteAnnexe")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listComptesAnnexesVerrouilles")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listComptesAnnexesMotifBlocageContentieux")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listExportSoldeCACC")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listResumeCompteAnnexe")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listSoldeSection")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listTypeOperation")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listTypeSection")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listExtraitCompteAnnexe")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("bulletinsSoldes")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listInteretMoratoire")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listComptesALettrer")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("listTaxesSommationEnSuspens")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("rappelPlan")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else if (actionSuite.equals("impressionPlan")) {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } else {
            this.setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        }
    }

}