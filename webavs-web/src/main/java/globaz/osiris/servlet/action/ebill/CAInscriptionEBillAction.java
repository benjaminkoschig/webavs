package globaz.osiris.servlet.action.ebill;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ebill.CAInscriptionEBillViewBean;
import globaz.osiris.db.ebill.enums.CAStatutEBillEnum;
import globaz.osiris.servlet.action.CADefaultServletAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;

public class CAInscriptionEBillAction extends CADefaultServletAction {
    /**
     * Constructor for CADefaultServletAction.
     *
     * @param servlet
     */
    public CAInscriptionEBillAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     * @see FWDefaultServletAction#actionChercher(HttpSession, HttpServletRequest,
     * HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction monAction = FWAction.newInstance(request.getParameter("userAction"));

        String myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(monAction, mainDispatcher.getPrefix());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);

            myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            myDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");
        if (action.contains("atraiter")) {
            atraiter(session, request, response, dispatcher);
        } else if (action.contains("optionATraiter")) {
            optionATraiter(session, request, response, dispatcher);
        } else if (action.contains("avalider")) {
            avalider(session, request, response, dispatcher);
        } else if (action.contains("optionAValider")) {
            optionAValider(session, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void atraiter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                          FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            CAInscriptionEBillViewBean viewBean;

            if ((session.getAttribute(CADefaultServletAction.VB_ELEMENT) != null)
                    && (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CAInscriptionEBillViewBean)) {
                viewBean = (CAInscriptionEBillViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
            } else {
                viewBean = new CAInscriptionEBillViewBean();
            }

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setStatut(CAStatutEBillEnum.NUMERO_STATUT_A_TRAITER);
            viewBean.update();
            viewBean.updateStatutFichier();

            viewBean = (CAInscriptionEBillViewBean) mainDispatcher.dispatch(viewBean, FWAction.newInstance("osiris.ebill.inscriptionEBill.modifier"));
            if (viewBean.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void optionATraiter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                          FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CAInscriptionEBillViewBean viewBean;

            if ((session.getAttribute(CADefaultServletAction.VB_ELEMENT) != null)
                    && (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CAInscriptionEBillViewBean)) {
                viewBean = (CAInscriptionEBillViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
            } else {
                viewBean = new CAInscriptionEBillViewBean();
            }

            String selectedId = request.getParameter("selectedId");

            viewBean.setSession((BSession) mainDispatcher.getSession());
            viewBean.setId(selectedId);
            viewBean.retrieve();

            viewBean = (CAInscriptionEBillViewBean) mainDispatcher.dispatch(viewBean, FWAction.newInstance("osiris.ebill.inscriptionEBill.afficher"));

            viewBean.setStatut(CAStatutEBillEnum.NUMERO_STATUT_A_TRAITER);
            viewBean.update();
            viewBean.updateStatutFichier();
            viewBean = (CAInscriptionEBillViewBean) mainDispatcher.dispatch(viewBean, FWAction.newInstance("osiris.ebill.inscriptionEBill.modifier"));

            if (viewBean.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void avalider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                          FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            CAInscriptionEBillViewBean viewBean;

            if ((session.getAttribute(CADefaultServletAction.VB_ELEMENT) != null)
                    && (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CAInscriptionEBillViewBean)) {
                viewBean = (CAInscriptionEBillViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
            } else {
                viewBean = new CAInscriptionEBillViewBean();
            }

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setStatut(CAStatutEBillEnum.NUMERO_STATUT_TRAITE_MANUELLEMENT);
            viewBean.update();
            viewBean.updateStatutFichier();

            viewBean = (CAInscriptionEBillViewBean) mainDispatcher.dispatch(viewBean, FWAction.newInstance("osiris.ebill.inscriptionEBill.modifier"));

            if (viewBean.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * On arrive sur cette action depuis l'écran de contrôle des prestation suite à la création d'un droit.
     *
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @return
     * @throws Exception
     */
    public void optionAValider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                         FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CAInscriptionEBillViewBean viewBean;

            if ((session.getAttribute(CADefaultServletAction.VB_ELEMENT) != null)
                    && (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CAInscriptionEBillViewBean)) {
                viewBean = (CAInscriptionEBillViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
            } else {
                viewBean = new CAInscriptionEBillViewBean();
            }
            String selectedId = request.getParameter("selectedId");
            viewBean.setSession((BSession) mainDispatcher.getSession());
            viewBean.setId(selectedId);
            viewBean.retrieve();

            viewBean = (CAInscriptionEBillViewBean) mainDispatcher.dispatch(viewBean, FWAction.newInstance("osiris.ebill.inscriptionEBill.afficher"));

            viewBean.setStatut(CAStatutEBillEnum.NUMERO_STATUT_TRAITE_MANUELLEMENT);
            viewBean.update();
            viewBean.updateStatutFichier();
            viewBean = (CAInscriptionEBillViewBean) mainDispatcher.dispatch(viewBean, FWAction.newInstance("osiris.ebill.inscriptionEBill.modifier"));

            if (viewBean.hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

}
