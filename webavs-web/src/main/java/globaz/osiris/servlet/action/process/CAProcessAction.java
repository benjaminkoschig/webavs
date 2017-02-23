package globaz.osiris.servlet.action.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.filetransfer.FWFileUpload;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BProcess;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.process.APIProcessUpload;
import globaz.osiris.api.process.APIProcessUploadBVR;
import globaz.osiris.db.comptes.CAExtournerOperationViewBean;
import globaz.osiris.db.comptes.CAExtournerSectionViewBean;
import globaz.osiris.db.process.CAAnnulerJournalViewBean;
import globaz.osiris.db.process.CAAnnulerOrdreViewBean;
import globaz.osiris.db.process.CAAnnulerSoldeSectionViewBean;
import globaz.osiris.db.process.CAComptabiliserJournalViewBean;
import globaz.osiris.db.process.CAContentieuxAIJViewBean;
import globaz.osiris.db.process.CAContentieuxViewBean;
import globaz.osiris.db.process.CAExtournerJournalViewBean;
import globaz.osiris.db.process.CAInteretMoratoireManuelViewBean;
import globaz.osiris.db.process.CAPlanRecouvrementNonRespectesViewBean;
import globaz.osiris.db.process.CAPreparerOrdreViewBean;
import globaz.osiris.db.process.CARecouvrementDirectViewBean;
import globaz.osiris.db.process.CARouvrirJournalViewBean;
import globaz.osiris.db.process.CATransmettreOrdreGroupeViewBean;
import globaz.osiris.process.contentieux.CAProcessContentieuxAIJ;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour les processus et simuler un intérêt.
 * 
 * @author DDA
 * @revision SCO mars 2010
 */
public class CAProcessAction extends CADefaultServletAction {

    public CAProcessAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_de.jsp";
        String actionSuite = getActionSuite(request);

        try {
            FWViewBeanInterface viewBean = this.getViewBean(request, request.getParameter("userAction"), true);

            viewBean.setISession(mainDispatcher.getSession());
            setProperties(request, actionSuite, viewBean);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            myDestination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");

        if ((action != null) && (action.indexOf("osiris.process.interetMoratoireManuel.simuler") > -1)) {
            actionSimuler(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = this.getViewBean(request);

            if (viewBean instanceof CAAnnulerSoldeSectionViewBean) {
                viewBean = this.getViewBean(request, request.getParameter("userAction"), true);
            }

            String actionSuite = getActionSuite(request);
            setProperties(request, actionSuite, viewBean);

            if (viewBean instanceof APIProcessUpload
                    && JadeStringUtil.isEmpty(((APIProcessUploadBVR) viewBean).getIdYellowReportFile())) {
                FWFileUpload uploadBean = new FWFileUpload();
                uploadBean.setSavePath(Jade.getInstance().getHomeDir() + "work/");
                uploadBean.doUpload(request);
                if (uploadBean.getFilename() != null) {
                    ((APIProcessUpload) viewBean).setFileName(uploadBean.getSavePath() + uploadBean.getFilename());
                }
            }

            if (viewBean instanceof CAProcessContentieuxAIJ) {
                session.setAttribute("FWDocumentListener", ((CAProcessContentieuxAIJ) viewBean).getDocumentListener());
            }

            ((BProcess) viewBean).setControleTransaction(true);
            ((BProcess) viewBean).setSendCompletionMail(true);
            ((BProcess) viewBean).setSendMailOnError(true);

            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(request.getParameter("userAction")));

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            } else {
                if (viewBean instanceof CAProcessContentieuxAIJ) {
                    destination = getRelativeURL(request, session) + "_wait.jsp";
                    servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
                    return;
                } else if (viewBean instanceof CAPlanRecouvrementNonRespectesViewBean) {
                    destination = getRelativeURL(request, session) + "_de.jsp";
                    servlet.getServletContext().getRequestDispatcher(destination + "?process=launched")
                            .forward(request, response);
                    return;
                } else if (viewBean instanceof CARecouvrementDirectViewBean) {
                    destination = _getDestExecuterSucces(session, request, response, viewBean);
                    destination = destination.substring(0, destination.indexOf("recouvrementDirect"))
                            + "bvr"
                            + destination.substring(destination.indexOf("recouvrementDirect")
                                    + "recouvrementDirect".length());
                } else {
                    destination = _getDestExecuterSucces(session, request, response, viewBean);
                }
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        goSendRedirect(destination, request, response);
    }

    private void actionSimuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            CAInteretMoratoireManuelViewBean viewBean;

            if ((session.getAttribute(CADefaultServletAction.VB_ELEMENT) != null)
                    && (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CAInteretMoratoireManuelViewBean)) {
                viewBean = (CAInteretMoratoireManuelViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
            } else {
                viewBean = new CAInteretMoratoireManuelViewBean();
            }

            viewBean.setISession(dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setSimulationMode(new Boolean(true));

            FWAction action = FWAction.newInstance("osiris.interet.simulerInteret.simuler");
            action.setRight(FWSecureConstants.UPDATE);

            viewBean = (CAInteretMoratoireManuelViewBean) dispatcher.dispatch(viewBean, action);

            if (!viewBean.getMemoryLog().hasErrors()) {
                destination = destination.substring(0, destination.indexOf("_de.jsp")) + "Preview"
                        + destination.substring(destination.indexOf("_de.jsp"));
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(viewBean.getMemoryLog().getMessagesInString());
            }

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Set des properties du process.
     * 
     * @param request
     * @param actionSuite
     * @param viewBean
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected void setProperties(HttpServletRequest request, String actionSuite, FWViewBeanInterface viewBean)
            throws IllegalAccessException, InvocationTargetException {
        JSPUtils.setBeanProperties(request, viewBean);

        if (actionSuite.equals("bvr")) {
            // Do nothing
        } else if (actionSuite.equals("comptabiliserJournal")) {
            if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
                ((CAComptabiliserJournalViewBean) viewBean).setIdJournal(super.getId(request, "id"));
            }
        } else if (actionSuite.equals("annulerJournal")) {
            if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
                ((CAAnnulerJournalViewBean) viewBean).setIdJournal(super.getId(request, "id"));
            }
        } else if (actionSuite.equals("contentieux")) {
            ((CAContentieuxViewBean) viewBean).setIdSection(super.getId(request, "id"));
        } else if (actionSuite.equals("contentieuxAIJ")) {
            ((CAContentieuxAIJViewBean) viewBean).setIdSection(super.getId(request, "id"));
        } else if (actionSuite.equals("preparerOrdre")) {
            ((CAPreparerOrdreViewBean) viewBean).setIdOrdreGroupe(super.getId(request, "id"));
        } else if (actionSuite.equals("transmettreOrdreGroupe")) {
            ((CATransmettreOrdreGroupeViewBean) viewBean).setIdOrdreGroupe(super.getId(request, "id"));
        } else if (actionSuite.equals("extournerSection")) {
            ((CAExtournerSectionViewBean) viewBean).setIdSection(super.getId(request, "id"));
        } else if (actionSuite.equals("extournerOperation")) {
            ((CAExtournerOperationViewBean) viewBean).setIdOperation(super.getId(request, "id"));
        } else if (actionSuite.equals("extournerJournal")) {
            if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
                ((CAExtournerJournalViewBean) viewBean).setIdJournal(super.getId(request, "id"));
            }
        } else if (actionSuite.equals("rouvrirJournal")) {
            if (!JadeStringUtil.isNull(super.getId(request, "id"))) {
                ((CARouvrirJournalViewBean) viewBean).setIdJournal(super.getId(request, "id"));
            }
        } else if (actionSuite.equals("annulerOrdre")) {
            if (!JadeStringUtil.isNull(getId(request, "id"))) {
                ((CAAnnulerOrdreViewBean) viewBean).setIdOrdreGroupe(getId(request, "id"));
            }
        }
    }
}
